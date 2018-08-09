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

public class FaceDetectionApiList {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<String> imageIdList = new ArrayList<>();


    @Test(dataProvider = "TestData")
    public void test(TestDataStruct data) throws JSONException {
        String result = tester.detectFacesLocalImage(data.get(0),data.get(2));
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("faces");
        JSONObject faceInfo = jsonArray.getJSONObject(0);
        String imageId = faceInfo.getString("image-id");
        imageIdList.add(imageId);
        String listResult = tester.listFaceDetection(data.get(0));
        Assert.assertEquals("["+imageId+"]",listResult);
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/standardPicLibrary.xlsx");
    }


    @AfterClass
    public void tearDown() {
        for (String imageId:imageIdList
                ) {
            tester.deleteFaceDetection(Integer.parseInt(imageId));
        }
    }


}
