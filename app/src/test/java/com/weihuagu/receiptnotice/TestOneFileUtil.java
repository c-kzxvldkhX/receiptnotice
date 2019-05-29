package com.weihuagu.receiptnotice;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.io.File;
public class TestOneFileUtil extends TestCase{
    OneFileUtil onefile=null;
    protected void setUp() throws Exception {
        super.setUp();
       File testlog=new File(this.getClass().getResource("/testlog").getFile());
       onefile =new OneFileUtil(testlog);
    }

    @Test
    public void testGetFileList() throws Exception{
        ArrayList filelist=onefile.getFileList();
        System.out.println(filelist.toString());


    }




}
