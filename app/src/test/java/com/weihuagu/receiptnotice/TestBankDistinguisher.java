package com.weihuagu.receiptnotice;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
public class TestBankDistinguisher extends TestCase{
    BankDistinguisher onedistinguisher=null;
    protected void setUp() throws Exception {
        super.setUp();
        onedistinguisher = new BankDistinguisher();
    }

    @Test
    public void testDistinguishByMessageContent() throws Exception{
        String test1= "您尾号0345卡8月15日23:46工商银行收入(他行汇入)1,000元，余额1,000.00元。【工商银行】";
        String test2= "您尾号7865卡人民币活期07:45存入10,00.00[银联入账:（特约）京东]，可用余额1000.00。【浦发银行】";
        String test3="【网商银行】您尾号8225的账户，于08月22日 09:32通过支付宝账户（183******13）成功转入20,000.00元。";
        String test4="没有行的胡乱测试短信";
        System.out.println("test1 工商");
        if(onedistinguisher.distinguishByMessageContent(test1)!=null)
                System.out.println(onedistinguisher.distinguishByMessageContent(test1));
        else 
                System.out.println("非银行短信");
        ////////////////////////////////////////
        System.out.println("test2 浦发");
        if(onedistinguisher.distinguishByMessageContent(test2)!=null)
                System.out.println(onedistinguisher.distinguishByMessageContent(test2));
        else
                System.out.println("非银行短信");
        ////////////////////////////////////////
        System.out.println("test3 网商");
        if(onedistinguisher.distinguishByMessageContent(test3)!=null)
                System.out.println(onedistinguisher.distinguishByMessageContent(test3));
        else
                System.out.println("非银行短信");
        /////////////////////////////////
         if(onedistinguisher.distinguishByMessageContent(test4)!=null)
                System.out.println(onedistinguisher.distinguishByMessageContent(test4));
        else
                System.out.println("非银行短信");







    }




}
