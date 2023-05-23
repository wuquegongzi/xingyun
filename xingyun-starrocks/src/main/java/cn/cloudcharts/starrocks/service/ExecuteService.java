package cn.cloudcharts.starrocks.service;

import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.common.utils.StringUtils;
import cn.cloudcharts.sqlparser.SqlPaser;
import cn.cloudcharts.sqlparser.starrocks.StarrocksParser;
import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.compress.utils.Lists;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuque
 * @title: executeMapper
 * @projectName xingyun
 * @description: 原生JDBC调用
 * @date 2023/5/820:29
 */
//@Service
@Slf4j
@Deprecated
public class ExecuteService {
    private static final String PREFIX_LOG = "【ExecuteMapper】";

//    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
//    @Autowired
//    private DfsService dfsService;


    @Value("${spring.datasource.olap.query.limit:1000}")
    private int maxVal;

    //TODO 分布式
    private ConcurrentHashMap<String,SqlSession> sessionMap = new ConcurrentHashMap();

    /**
     * 取消脚本执行
     * @param jobId
     * @return
     */
    public int cancle(String jobId){
        if(!sessionMap.contains(jobId)){
            return 1;
        }

        try {
            SqlSession session = sessionMap.get(jobId);
            session.rollback(true);
            closeSqlSession(session);
        } catch (Exception e) {
            return 0;
        }

        return 1;
    }

    /**
     * 执行多脚本
     * @param sql
     * @return
     */
    public List<String> execute(String sql){

        //        dataSource.setLogWriter();
        String jobId = UUID.fastUUID().toString();

        log.info(PREFIX_LOG+"execute script:"+sql);

        if(StringUtils.isEmpty(sql)){
            throw new ServiceException("Execution script cannot be empty");
        }

        List<String> list = new LinkedList<String>();
        PreparedStatement pst = null;

        SqlSession session = getSqlSession();
        sessionMap.put(jobId,session);

        try {
            Connection conn = session.getConnection();

            String sqls[] = sql.split(";");
            SqlPaser sqlPaser = new StarrocksParser();
            List<SqlNode> sqlNodes = sqlPaser.parser(sql);
            for (int i = 0; i < sqlNodes.size(); i++) {
                SqlNode sqlNode = sqlNodes.get(i);

                if(null != pst){
                    pst.clearParameters();
                }

                String tempSql = sqls[i];
                log.info("submit sql: {}" , tempSql);
                if(null != sqlNode && sqlNode.getKind() == SqlKind.SELECT){
                    tempSql = "select t.* from ("+tempSql+") as t limit "+ maxVal;
                }

                pst = conn.prepareStatement(tempSql);

                if(null != sqlNodes.get(i) && sqlNodes.get(i).getKind() == SqlKind.SELECT){
                    List< Map<String,Object>> rowDataList = Lists.newArrayList();
                    ResultSet result = pst.executeQuery();
                    //获得结果集结构信息,元数据
                    ResultSetMetaData md = result.getMetaData();
                    //获得列数
                    int columnCount = md.getColumnCount();
                    while (result.next()) {
                        Map<String,Object> rowData = new HashMap<String,Object>();
                        for (int j = 1; j <= columnCount; j++) {
                            rowData.put(md.getColumnName(j), result.getObject(j));
                        }
                        rowDataList.add(rowData);
                    }

                    String tId = UUID.fastUUID().toString();
                    printWriteData(jobId,tId,rowDataList);
                    list.add(jobId+":"+tId);

                }else{
                    pst.execute();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(pst!=null){
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            sessionMap.remove(jobId);
            closeSqlSession(session);
        }
        return list;
    }

    /**
     * 异步输出查询结果到CSV
     *
     * @param jobId
     * @param tId
     * @param rowDataList
     */
    private void printWriteData(String jobId, String tId,  List<Map<String, Object>> rowDataList) {

        rowDataList.forEach(map ->{
            System.out.println(map.values().toString());
        });

        //数据写入 OSS-service

    }



    /**
     * 获取sqlSession
     * @return
     */
    public SqlSession getSqlSession(){
        return SqlSessionUtils.getSqlSession(sqlSessionTemplate.getSqlSessionFactory(),
                sqlSessionTemplate.getExecutorType(), sqlSessionTemplate.getPersistenceExceptionTranslator());
    }

    /**
     * 关闭sqlSession
     * @param session
     */
    public void closeSqlSession(SqlSession session){
        SqlSessionUtils.closeSqlSession(session, sqlSessionTemplate.getSqlSessionFactory());
    }

}
