package com.weihuagu.receiptnotice;
import com.weihuagu.receiptnotice.util.ExternalInfoUtil;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

public class TestExternalInfoUtil extends TestCase{
        protected void setUp() throws Exception {
                super.setUp();
        }

        @Test
        public void testGetCustomPostOption() throws Exception{

                String test="test1-test1value;test2:test2vvalue;test3:test3value";
                Map map1 = new HashMap();      //定义Map集合对象
                map1.put("apple", "新鲜的苹果");     //向集合中添加对象
                map1.put("computer", "配置优良的计算机");
                map1.put("book", "堆积成山的图书");
                Map haha= ExternalInfoUtil.getCustomPostOption(test);
                map1.putAll(haha);
                System.out.println(map1.toString());
        }



}
