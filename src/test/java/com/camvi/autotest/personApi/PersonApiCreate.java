package com.camvi.autotest.personApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.BaseQuery;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.ArrayList;


public class PersonApiCreate {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();

    @Test(dataProvider = "PersonApiCreateTestData")
    public void test(TestDataStruct data) throws JSONException {
        System.out.println("PersonApiCreate testing !!!!");
        String createPersonResult = tester.createPerson(data.get(0), data.get(1), data.get(2));
        int personId = SystemHelper.getPersonIdByJsonResult(createPersonResult);
        personIdList.add(personId);
        String queryResult = tester.query(personId);
        String personName = SystemHelper.getPersonNameByJsonResult(queryResult);
        Assert.assertEquals(data.get(0),personName);
    }

    @DataProvider(name="PersonApiCreateTestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/standardPicLibrary.xlsx");

//
//        Object[][] aa =  new Object[][] {
//                { new TestDataStruct("Unit_Test_001", "1", "src/test/testFile/standardPic/DonaldTrump001.jpg") },
//                { new TestDataStruct("Unit_Test_002", "1", "src/test/testFile/standardPic/HarryKane001.jpg") },
//                { new TestDataStruct("Unit_Test_003", "1", "src/test/testFile/standardPic/JackyCheung001.jpeg") }
//        };


    }

    @AfterClass
    public void afterClass() throws Exception {
        System.out.println("PersonApiCreate test afterclass");
        SystemHelper.cleanUp(personIdList,tester);
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        System.out.println("PersonApiCreate test beforeClass");
    }
    @AfterMethod
    public void afterMethod(){
        System.out.println("PersonApiCreate test AfterMethod");
    }

    @BeforeMethod
    public void beforeMethod(){
        System.out.println("PersonApiCreate test BeforeMethod");
    }


    @BeforeSuite
    public void beforeSuit(){
//        System.out.println("----------beforeClass----------");
//        //SystemHelper.runShellScript("src/test/testFile/testShelllScript/clearDB.sh");
//        System.out.println("``````````````setServerBase`````````````````");
//        BaseQuery.setServerBase("http://localhost:8080/service/api");
//        if (!BaseQuery.login()) {
//            return;
//        }


    }
}
