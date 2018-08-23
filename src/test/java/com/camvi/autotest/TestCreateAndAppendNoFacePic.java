package com.camvi.autotest;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;

public class TestCreateAndAppendNoFacePic {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void test(TestDataStruct data) throws JSONException {
        String createPersonResult = tester.createPerson(data.get(0), data.get(1), data.get(2));
        int personId = SystemHelper.getPersonIdByJsonResult(createPersonResult);
        personIdList.add(personId);
        String queryResult = tester.query(personId);
        String personName = SystemHelper.getPersonNameByJsonResult(queryResult);
        Assert.assertEquals(data.get(0),personName);


        String appendFaceResult = tester.appendFace(personId,"src/test/testFile/standardPic/car.jpg");
    }

    @DataProvider(name="TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/singlePerson.xlsx");


    }

    @AfterClass
    public void afterClass() throws Exception {
        System.out.println("PersonApiCreate test afterclass");
        //SystemHelper.cleanUp(personIdList,tester);
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        System.out.println("PersonApiCreate test beforeClass");
    }
}
