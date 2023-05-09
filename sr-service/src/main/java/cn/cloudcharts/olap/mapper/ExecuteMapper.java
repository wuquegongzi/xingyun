package cn.cloudcharts.olap.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

/**
 * @author wuque
 * @title: executeMapper
 * @projectName xingyun
 * @description: 原生JDBC调用
 * @date 2023/5/820:29
 */
@Component
@Slf4j
public class ExecuteMapper {
    private static final String PREFIX_LOG = "【ExecuteMapper】";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public List<String> execute(String sql){
        log.info(PREFIX_LOG+"执行sql脚本:"+sql);

        //TODO 切割SQL

        //SQL解析，判断SQL类型

        List<String> list = new LinkedList<String>();

        PreparedStatement pst = null;
        SqlSession session = getSqlSession();
        ResultSet result = null;

        try {
            Connection connection = session.getConnection();
            pst = connection.prepareStatement(sql);
            result = pst.executeQuery();
            //获得结果集结构信息,元数据
            ResultSetMetaData md = result.getMetaData();
            int columnCount = md.getColumnCount();   //获得列数
            while (result.next()) {
                Map<String,Object> rowData = new HashMap<String,Object>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), result.getObject(i));
                }
//                list.add(); rowData
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
            closeSqlSession(session);
        }
        return list;
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