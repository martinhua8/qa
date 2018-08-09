package com.camvi.autotest.FaceDetectionApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;

public class FaceDetectionApiDelete {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<String> imageIdList = new ArrayList<>();
    int count =0;

    @Test(dataProvider = "TestData")
    public void test(TestDataStruct data) throws JSONException {
        String result = tester.detectFacesLocalImage(data.get(0),data.get(2));
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("faces");
        JSONObject faceInfo = jsonArray.getJSONObject(0);
        imageIdList.add(faceInfo.getString("image-id"));

    }

    @Test(dataProvider = "TestData",dependsOnMethods = "test")
    public void testDelete(TestDataStruct data){
        tester.deleteFaceDetection(Integer.parseInt(imageIdList.get(count++)));
        String result =tester.listFaceDetection(data.get(0));
        Assert.assertEquals("[]",result);
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/standardPicLibrary.xlsx");
    }


//    @AfterClass
//    public void tearDown() {
//        SystemHelper.cleanUp(personIdList, tester);
//    }

}
