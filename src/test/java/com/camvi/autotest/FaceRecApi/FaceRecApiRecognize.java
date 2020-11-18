package com.camvi.autotest.FaceRecApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;

public class FaceRecApiRecognize {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void test(TestDataStruct data) throws JSONException {
        String result = tester.recognizeFaceRec("1",data.get(0));
        Assert.assertEquals("Unit_Test_001",new JSONObject(result).getString("name"));
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        //return SystemHelper.getTestData("src/test/testFile/testData/FaceRecognitionAPI/testRecognize.xlsx");
    	return SystemHelper.getTestData("src/test/testFile/testData/FaceRecognitionAPI/testRecognize.csv");
    }


    @AfterClass
    public void tearDown() {
        SystemHelper.cleanUp(personIdList, tester);
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        //SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
    	SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.csv", tester, personIdList);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
