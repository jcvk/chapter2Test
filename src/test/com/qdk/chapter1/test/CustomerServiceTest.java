package com.qdk.chapter1.test;

import com.qdk.helper.DatabaseHelper;
import com.qdk.test2.Customer;
import com.qdk.test4.CustomerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/6/12.
 */
public class CustomerServiceTest {
    private final CustomerService customerService;

    public CustomerServiceTest() {
        this.customerService = new CustomerService();
    }

    @Before
    /**
     * 从当前线程中获取上下文中的ClassLoader,通过classpath下的sql/customer_init.sql
     * 获取一个InputStream对象，通过该输入流来创建BufferedReader对象，循环读取其中的每一行
     * 并调用executeUpdate方法来执行每条SQL语句
     */
    public void init() throws Exception {
        DatabaseHelper.executeSqlFile("sql/customer_init");
    }

    @Test
    /**
     * assertEquals断言是判断预期值和实际值是否相等，如果相等则通过测试否则就没有通过
     */
    public void getCustomerListTest() throws Exception {
        List<Customer> customerList = customerService.getCustomerList("123");
        Assert.assertEquals(0, customerList.size());

    }


    @Test
    public void createCustomerTest() throws Exception {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name", "customer100");
        fieldMap.put("contact", "John");
        fieldMap.put("telephone", "13245798");
        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void updateCustomerTest() throws Exception {
        long id = 1;
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("contact", "Eric");
        boolean result = customerService.updateCustomer(id, fieldMap);
        Assert.assertFalse(result);
    }

    public void deleteCustomerTest() throws Exception {
        long id = 1;
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }


}
