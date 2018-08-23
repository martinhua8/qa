package com.camvi.autotest.personApi;

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
import java.util.List;

public class PersonApiAppendFace {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    private ArrayList<Integer> faceIdList = new ArrayList<>();


    @Test(dataProvider = "TestData")
    public void testAppendFace(TestDataStruct data) throws JSONException {
        String result = tester.appendFace(personIdList.get(0), data.get(0));
        JSONObject jsonObject = new JSONObject(result);
        String faceId = jsonObject.get("faceId").toString();
        faceIdList.add(Integer.parseInt(faceId));
    }

    @Test(dependsOnMethods = "testAppendFace")
    public void test() throws JSONException {
        System.out.println("testing append face");
        String result = tester.query(personIdList.get(0));
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("faces");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(Integer.parseInt(jsonArray.getString(i)));
        }
        Assert.assertEquals(list,faceIdList);
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/PersonFaceManagementAPI/testAppendFace.xlsx");
    }


    @AfterClass
    public void tearDown() {
        SystemHelper.cleanUp(personIdList,tester);
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
        String result = tester.query(personIdList.get(0));
        JSONObject jsonObject = new JSONObject(result);
        String faceId = jsonObject.get("faces").toString();
        String[] faceIdArray = faceId.substring(1,faceId.length()-1).split(",");
        for (int i =0;i<faceIdArray.length;i++){
            faceIdList.add(Integer.parseInt(faceIdArray[i]));
        }
    }
}
