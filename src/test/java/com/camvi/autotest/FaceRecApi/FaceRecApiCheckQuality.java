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

public class FaceRecApiCheckQuality {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void test(TestDataStruct data) throws JSONException {
        String result = tester.checkQualityFaceRec(data.get(0));
        Assert.assertEquals("pass",new JSONObject(result).getString("result"));
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/PersonFaceManagementAPI/testAppendFace.xlsx");
    }
}
