package com.camvi.autotest.FaceRecApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;

public class FaceRecApiVerify {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void test(TestDataStruct data) throws JSONException {
        String result = tester.verifyFaceRec(personIdList.get(0).toString(),data.get(0));
        Assert.assertTrue(SystemHelper.getConfidenceFromJsonResult(result)>0.60);
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        //return SystemHelper.getTestData("src/test/testFile/testData/PersonFaceManagementAPI/testAppendFace.xlsx");
    	return SystemHelper.getTestData("src/test/testFile/testData/PersonFaceManagementAPI/testAppendFace.csv");
    }


    @AfterClass
    public void tearDown() {
        SystemHelper.cleanUp(personIdList, tester);
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        //SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
    	SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.csv", tester, personIdList);
    }
}
