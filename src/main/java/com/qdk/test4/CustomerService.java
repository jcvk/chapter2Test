package com.qdk.test4;

import com.qdk.helper.DatabaseHelper;
import com.qdk.test2.Customer;
import com.qdk.util.PropsUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Created by lenovo on 2017/6/7.
 */
public class CustomerService {
//    private static final String DRIVER;
//    private static final String URL;
//    private static final String USERNAME;
//    private static final String PASSWORD;
//    private static final Logger LOGGER = Logger.getLogger(CustomerService.class);

    /**
     * 使用PropsUtil读取config.properties配置文件,获取与JDBC相关的配置项
     */
//    static {
//        Properties conf = PropsUtil.loadProps("config.properties");
//        DRIVER = conf.getProperty("jdbc.driver");
//        URL = conf.getProperty("jdbc.url");
//        USERNAME = conf.getProperty("jdbc.username");
//        PASSWORD = conf.getProperty("jdbc.password");
//        try {
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            LOGGER.error("can not load jdbc driver", e);
//        }
//    }

    /**
     * 数据库的链接都写在方法里面
     * @param keyword
     * @return
     */
    public List<Customer> getCustomerList(String keyword) {
//        Connection conn = null;
//        try {
//            List<Customer> customerList = new ArrayList<Customer>();
            String sql = "SELECT *  FROM customer";
            //conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//            conn= DatabaseHelper.getConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                Customer customer = new Customer();
//                customer.setId(rs.getLong("id"));
//                customer.setName(rs.getString("name"));
//                customer.setContact(rs.getString("contact"));
//                customer.setTelephone(rs.getString("telephone"));
//                customer.setEmail(rs.getString("email"));
//                customer.setRemark(rs.getString("remark"));
//                customerList.add(customer);
//            }
//            return customerList;
//            return DatabaseHelper.queryEntityList(Customer.class,conn,sql);
            return DatabaseHelper.queryEntityList(Customer.class,sql);
//        } catch (SQLException e) {
//            LOGGER.error("execute sql failure", e);
//        } finally {
////            if (conn != null) {
////                try {
////                    conn.close();
////                } catch (SQLException e) {
////                    LOGGER.error("close connection failure", e);
////                }
////            }
//            DatabaseHelper.closeConnection(conn);
//        }
    }


    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class,fieldMap);
    }

    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class,id,fieldMap);
    }

    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class,id);
    }
}
