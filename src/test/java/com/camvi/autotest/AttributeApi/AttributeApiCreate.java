package com.camvi.autotest.AttributeApi;

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

public class AttributeApiCreate {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<String> keyList = new ArrayList<>();

    @Test(dataProvider = "AttributeApiCreateTestData")
    public void testCreate(TestDataStruct data) throws JSONException {
        String result= tester.createAttribute(data.get(0),data.get(1),data.get(2));
        Assert.assertEquals("ok",SystemHelper.getStatusByJsonResult(result));
        keyList.add(data.get(0));
        String listResult = tester.listAttribute();
        JSONArray jsonArray = new JSONArray(listResult);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        Assert.assertEquals(data.get(0),jsonObject.get("key").toString());
        Assert.assertEquals(data.get(1),jsonObject.get("type").toString());
        Assert.assertEquals(data.get(2),jsonObject.get("category").toString());
        tester.deleteAttribute(data.get(0));
    }


    @DataProvider(name="AttributeApiCreateTestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/AttributeManagementAPI/testCreateAttribute.xlsx");

    }

    @AfterClass
    public void afterClass() {
        for (String key:keyList) {
            tester.deleteAttribute(key);
        }
    }
}
