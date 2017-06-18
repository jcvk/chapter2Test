package com.qdk.helper;


import com.qdk.util.CollectionUtil;
import com.qdk.util.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lenovo on 2017/6/16.
 */
public final class DatabaseHelper {
    private static final Logger LOGGER= Logger.getLogger(DatabaseHelper.class);

    private static final QueryRunner QUERY_RUNNER=new QueryRunner();
    private static final ThreadLocal<Connection> CONNECTION_HOLDER=new ThreadLocal<Connection>();
    private static final BasicDataSource DATA_SOURCE;

    static {
        Properties conf = PropsUtil.loadProps("config");
        String driver = conf.getProperty("jdbc.driver");
        String url = conf.getProperty("jdbc.url");
        String username = conf.getProperty("jdbc.username");
        String password = conf.getProperty("jdbc.password");

        DATA_SOURCE=new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    /**
     * 开启数据库连接,每次获取Connection时，首先在ThreadLocal中寻找，
     * 若不存在，则创建一个性的Connection,并将其放入ThreadLocal中，当
     * 使用Connection完毕后，需要移除ThreadLocal中持有的Connection
     * @return
     */
    public static Connection getConnection(){
        Connection conn=CONNECTION_HOLDER.get();
        if(conn==null) {
            try {
                //conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                conn=DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     */
    public static void closeConnection(){
        Connection conn=CONNECTION_HOLDER.get();
        if(conn!=null){
            try {
                conn.close();
            }catch (SQLException e){
                LOGGER.error("close connection failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * object... 表示的是数组 同 object[] args
     * DbUtils提供的QueryRunner对象可以通过面向实体(Entity)进行查询
     * 实际上，DbUtils首先执行SQL语句并返回一个ResultSet，随后通过反射
     * 出创建并初始化实体对象，由于需要返回的是List，因此用BeanLIstHandler.
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass,String sql,Object... params){
        List<T> entityList;
        try {
            Connection conn=getConnection();
            entityList=QUERY_RUNNER.query(conn,sql,new BeanListHandler<T> (entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }finally {
            closeConnection();
        }
        return entityList;
    }

    /**
     * 返回的rows是受影响的行数,QUERY_RUNNER.update 可以执行很多语句，不只是插入
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql,Object... params){
        int rows=0;
        try{
            Connection conn=getConnection();
            rows=QUERY_RUNNER.update(conn,sql,params);
        }catch (SQLException e){
            LOGGER.error("execute update failure",e);
            throw new RuntimeException(e);
        }finally {
            closeConnection();
        }
        return rows;
    }

    public static <T> boolean insertEntity(Class<T> entityClass, Map<String,Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }

        String sql="INSERT INTO "+ getTableName(entityClass);
        StrBuilder columns=new StrBuilder("(");
        StrBuilder values=new StrBuilder("(");
        for (String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "),columns.length(),")");
        values.replace(values.lastIndexOf(", "),values.length(),")");
        sql+=columns+" VALUES "+values;

        Object[] params=fieldMap.values().toArray();
        return executeUpdate(sql,params)==1;
    }

    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }

    public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity:fieldMap id empty");
            return false;
        }

        String sql=" UPDATE "+getTableName(entityClass)+" SET ";
        StrBuilder columns=new StrBuilder();
        for(String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql+=columns.substring(0,columns.lastIndexOf(", "))+"WHERE id=?";

        List<Object> paramList=new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params=paramList.toArray();

        return executeUpdate(sql,params)==1;
    }

    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql="DELETE FROM "+ getTableName(entityClass)+"WHERE id=?";
        return executeUpdate(sql,id)==1;
    }

    public static void executeSqlFile(String filePath){
        InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql=reader.readLine())!=null){
                executeUpdate(sql);
            }
        }catch (Exception e){
            LOGGER.error("execute sql file failure",e);
            throw new RuntimeException(e);
        }
    }



}
