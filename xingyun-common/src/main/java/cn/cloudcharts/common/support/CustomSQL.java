/**
 * Copyright (c) 2011, SuZhou USTC Star Information Technology CO.LTD
 * All Rights Reserved.
 */

package cn.cloudcharts.common.support;

import cn.cloudcharts.common.jmx.SQLManager;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在开发的时，设置虚拟机参数-DreloadSQLFiles=true. 可以重新加载sql语句配置文件，
 * 避免修改配置文件，需要重新启动服务器。
 *
 */
public class CustomSQL implements ApplicationContextAware {
	private static Logger logger = LoggerFactory.getLogger(CustomSQL.class);

	private Map<String, SQLBean> _sqlPool = new ConcurrentHashMap<String, SQLBean>();

	private static final String STRING_SPACE = " ";

	private final SAXReader saxReader = new SAXReader();
	private Configuration configuration = null;
	private StringTemplateLoader stringTemplateLoader = null;

	private Map<String, Long> configMap = new HashMap<String, Long>();
	private boolean reloadSQLFiles = false;

	private static CustomSQL instance = null;

	private CustomSQL() {
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		instance = context.getBean(CustomSQL.class);
		instance.init();
	}

	public static CustomSQL getInstance() {
		if(instance == null) {
			synchronized (CustomSQL.class) {
				if(instance == null) {
					instance = new CustomSQL();
					instance.init();
				}
			}
		}

		return instance;
	}

	public void init() {
		reloadSQLFiles = Boolean.valueOf(System.getProperty("reloadSQLFiles"));

		try {
			configuration = new Configuration();
			stringTemplateLoader = new StringTemplateLoader();
	        configuration.setDefaultEncoding("UTF-8");
	        configuration.setNumberFormat("#");

	        Resource[] configs = loadConfigs();
			for (Resource _config : configs) {
				logger.info("Loading " + _config.getURL().getPath());
				configMap.put(_config.getURL().getPath(), _config.lastModified());
				read(_config.getInputStream());
			}

	        configuration.setTemplateLoader(stringTemplateLoader);
		}
		catch (Exception e) {
			logger.error("", e);
		}

		SQLManager manager = new SQLManager();
		register("cn.cloudcharts.common.bee:type=SQLStat", manager);
	}

	private ObjectName register(String name, Object mbean) {
        try {
            ObjectName objectName = new ObjectName(name);

            MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

            try {
                mbeanServer.registerMBean(mbean, objectName);
            } catch (InstanceAlreadyExistsException ex) {
                mbeanServer.unregisterMBean(objectName);
                mbeanServer.registerMBean(mbean, objectName);
            }

            return objectName;
        } catch (JMException e) {
            throw new IllegalArgumentException(name, e);
        }
    }

	protected Resource[] loadConfigs() throws IOException {
		PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		return patternResolver.getResources("classpath*:custom-sql/**/*.xml");
	}

	public String get(String id) {
		if(reloadSQLFiles) {
			try {
				reloadConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		SQLBean bean = _sqlPool.get(id);

		if(bean == null) {
			throw new IllegalStateException("sql id 不存在：" + id);
		}

		if("simple".equals(bean.getTempateType())) {
			return _sqlPool.get(id).getContent();
		} else {
			throw new RuntimeException("SQL 模板类型不正确，只可以是simple类型");
		}
	}

	public String get(String id, Map<String, Object> models) {
		if(reloadSQLFiles) {
			try {
				reloadConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			Template template = configuration.getTemplate(id);

			StringWriter writer = new StringWriter();
			template.process(models, writer);
			logger.info("生成的SQL:{}",writer.toString());
			return writer.toString();
        } catch (TemplateException e) {
            throw new RuntimeException("Parse sql failed", e);
        } catch (FileNotFoundException e) {
        	throw new IllegalStateException("sql id 不存在：" + id);
        } catch (IOException e) {
            throw new RuntimeException("Parse sql failed", e);
        }
	}

	/**
	 * 更新文件修改时间刷新。
	 */
	private void reloadConfig() throws IOException {
		Resource[] newConfigs = this.loadConfigs();
		for(Resource newConfig : newConfigs) {
			boolean flag = true;
			for(Entry<String, Long> entry : configMap.entrySet()) {
				if(newConfig.getURL().getPath().equals(entry.getKey())) {
					flag = false;

					if (newConfig.getFile().lastModified() != entry.getValue().longValue()) {
						configMap.put(entry.getKey(), newConfig.getFile().lastModified());
						read(newConfig.getInputStream());
						logger.info("Reloading " + entry.getKey());

						break;
					}
				}
			}

			if(flag) {
				configMap.put(newConfig.getURL().getPath(), newConfig.getFile().lastModified());
				read(newConfig.getInputStream());
				logger.info("Reloading " + newConfig.getURL().getPath());
			}
		}
	}

	protected void read(InputStream is) {

		if (is == null){ return;}

		Document document;
		try {
			document = saxReader.read(is);
		} catch (DocumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		Element rootElement = document.getRootElement();

		for (Object sqlObj : rootElement.elements("sql")) {
			Element sqlElement = (Element)sqlObj;

			String id = sqlElement.attributeValue("id");
			String sqlType = sqlElement.attributeValue("sqlType");
			String tempateType = sqlElement.attributeValue("tempateType");

			if("simple".equals(tempateType) || "freeMarker".equals(tempateType)) {
				String content = transform(sqlElement.getText());

				SQLBean bean = new SQLBean();
				bean.setTempateType(tempateType);
				bean.setSqlType(sqlType);
				bean.setContent(content);

				if("freeMarker".equals(tempateType)) {
					stringTemplateLoader.putTemplate(id, content);
				}

				_sqlPool.put(id, bean);
			} else {
				logger.warn("{} 对应 tempateType 值 {} 不正确，可选值为：simple和freeMarker", id, sqlType);
			}
		}
	}

	protected String transform(String sql) {
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader bufferedReader =
				new BufferedReader(new StringReader(sql));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line.trim());
				sb.append(STRING_SPACE);
			}

			bufferedReader.close();
		}
		catch (IOException ioe) {
			return sql;
		}

		return sb.toString();
	}

	public Map<String, SQLBean> getAllSQL() {
		return _sqlPool;
	}

	public StringTemplateLoader getStringTemplateLoader() {
		return stringTemplateLoader;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public static class SQLBean {
		/**
		 * 两种可选类型：simple和freeMarker
		 */
		private String tempateType = "simple";
		/**
		 * 两种可选类型：SQL和HQL
		 */
		private String sqlType = "SQL";
		private String content = "";

		public String getTempateType() {
			return tempateType;
		}
		public void setTempateType(String tempateType) {
			this.tempateType = tempateType;
		}
		public String getSqlType() {
			return sqlType;
		}
		public void setSqlType(String sqlType) {
			this.sqlType = sqlType;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}

	}

}
