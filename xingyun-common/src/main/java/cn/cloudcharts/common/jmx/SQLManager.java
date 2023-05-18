package cn.cloudcharts.common.jmx;

import cn.cloudcharts.common.support.CustomSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.JMException;
import javax.management.openmbean.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class SQLManager implements SQLManagerMBean {

	private static final Logger _logger = LoggerFactory.getLogger(SQLManager.class);

	private ConcurrentMap<String, CompositeData> concurrentMap = new ConcurrentHashMap<String, CompositeData>();

	@Override
	public TabularData getUriList() throws JMException {
		return data;
	}

	@Override
	public String findSQL(String id) {
		CustomSQL.SQLBean bean = CustomSQL.getInstance().getAllSQL().get(id);
		if(bean == null) {
			return "sql id不存在";
		}
		else {
			return bean.getContent();
		}
	}

	@Override
	public String resetSql(String id, String type, String sql) {
		CustomSQL.SQLBean bean = CustomSQL.getInstance().getAllSQL().get(id);
		if(bean == null) {
			return "sql id不存在";
		} else  {
			if("simple".equals(bean.getTempateType()) || "freeMarker".equals(bean.getTempateType())) {
				bean.setContent(sql);
				if("freeMarker".equals(bean.getTempateType())) {
					CustomSQL.getInstance().getStringTemplateLoader().putTemplate(id, sql);
					CustomSQL.getInstance().getConfiguration().clearTemplateCache();
				}
				return "设置成功";
			} else {
				return "类型值不正确，只能是simple或freeMarker";
			}
		}
	}

	@Override
	public long getSQLCount() {
		return data.size();
	}

	private TabularData data = null;
	public SQLManager() {
		try {
			CompositeType rowType = getCompositeType();
	        String[] indexNames = rowType.keySet().toArray(new String[rowType.keySet().size()]);

	        TabularType tabularType = new TabularType("SQLStatistic", "SQLStatistic", rowType, indexNames);
	        data = new TabularDataSupport(tabularType);

	        for(Map.Entry<String, CustomSQL.SQLBean> entry : CustomSQL.getInstance().getAllSQL().entrySet()) {
	        	 Map<String,String> map = new HashMap<String, String>();
	             map.put("id", entry.getKey());
	             map.put("tempateType", entry.getValue().getTempateType());
	             map.put("sqlType", entry.getValue().getSqlType());
	             map.put("SQL", entry.getValue().getContent());

	             CompositeData compositeData = new CompositeDataSupport(getCompositeType(), map);
	             data.put(compositeData);
	             concurrentMap.put(entry.getKey(), compositeData);
	        }
		} catch (JMException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private CompositeType COMPOSITE_TYPE = null;
	private CompositeType getCompositeType() throws JMException {

        if (COMPOSITE_TYPE != null) {
            return COMPOSITE_TYPE;
        }

        OpenType<?>[] indexTypes = new OpenType<?>[] { SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING};

        String[] indexNames = {"id", "tempateType", "sqlType", "SQL"};
        String[] indexDescriptions = indexNames;
        COMPOSITE_TYPE = new CompositeType("SQLStatistic", "SQL Statistic", indexNames, indexDescriptions, indexTypes);

        return COMPOSITE_TYPE;
    }
}
