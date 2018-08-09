package com.camvi.autotest;
import org.junit.After;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CamviTestNGPlatform {
    public CamviTestNGPlatform() {
    }

    @Test
    public void test(){

    }

    @BeforeSuite
    public void setUp() throws Exception {
        System.out.println("----------beforeClass----------");
        //SystemHelper.runShellScript("src/test/testFile/testShelllScript/clearDB.sh");
        System.out.println("``````````````setServerBase`````````````````");
        BaseQuery.setServerBase("http://localhost:8080/service/api");
        if (!BaseQuery.login()) {
            return;
        }

        boolean setUpLibrary = false;
        if (setUpLibrary) {
            SystemHelper.setUpStandardPicLibrary("src/test/testFile/testData/standardPicLibrary.xlsx");
        }

    }

    @AfterClass
    public void afterClass() throws Exception {
        System.out.println("CamviTestNGPlatform test afterclass");

    }
    @BeforeClass
    public void beforeClass() throws Exception {
        System.out.println("CamviTestNGPlatform test beforeClass");
    }
    @AfterMethod
    public void afterMethod(){
        System.out.println("CamviTestNGPlatform test AfterMethod");
    }

    @BeforeMethod
    public void beforeMethod(){
        System.out.println("CamviTestNGPlatform test BeforeMethod");
    }

    @AfterTest
    public void AfterTest() throws Exception {
        System.out.println("CamviTestNGPlatform test AfterTest");
    }

    @BeforeTest
    public void BeforeTest() throws Exception {
        System.out.println("CamviTestNGPlatform test BeforeTest");
    }


    @AfterSuite
    public void AfterSuite() throws Exception {
        System.out.println("CamviTestNGPlatform test AfterSuite");
    }

    @BeforeSuite
    public void BeforeSuite() throws Exception {
        System.out.println("CamviTestNGPlatform test BeforeSuite");
    }
}
