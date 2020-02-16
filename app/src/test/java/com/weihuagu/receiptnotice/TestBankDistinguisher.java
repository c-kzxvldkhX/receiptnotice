package com.weihuagu.receiptnotice;
import com.weihuagu.receiptnotice.pushclassification.pmentay.BankDistinguisher;

import junit.framework.TestCase;

import org.junit.Test;

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
                        String test4="不是银行正确的胡乱测试短信";
                        String test5="【北京银行】您尾号为****的京卡于19年9月16日10:49银联入账收入1.00元。活期余额435.45元。对方账号尾号:7207。对方户名:二维码快速收款码专用";
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

                        System.out.println(onedistinguisher.distinguishByMessageContent(test5));








                }
                @Test
                public void testExtractMoney() throws Exception{
                        String test1= "您尾号0345卡8月15日23:46工商银行收入(他行汇入)1,000元，余额1,000.00元。【工商银行】";
                        String test2= "您尾号7865卡人民币活期07:45存入10,00.00[银联入账:（特约）京东]，可用余额1000.00。【浦发银行】";

                        String test3="【网商银行】您尾号8225的账户，于08月22日 09:32通过支付宝账户（183******13）成功转入20,000.00元。";
                        String test4="不是银行正确的胡乱测试短信";
                        System.out.println(onedistinguisher.extractMoney(test1));
                }
                
                @Test
                public void testExtractCardNum() throws Exception{
                         String test1= "您尾号0345卡8月15日23:46工商银行收入(他行汇入)1,000元，余额1,000.00元。【工商银行】";
                        String test3="【网商银行】您尾号8225的账户，于08月22日 09:32通过支付宝账户（183******13）成功转入20,000.00元。";
                        System.out.println(onedistinguisher.extractCardNum(test1));
                }

                @Test
                public void testExtractTime() throws Exception{
                        String test1= "您尾号0345卡8月15日23:46工商银行收入(他>行汇入)1,000元，余额1,000.00元。【工商银行】";
                        String time= "2019-09-16 22:37:37";
                        System.out.println(onedistinguisher.extractTime(test1,time));









                }



        }
