package com.camvi.autotest.AttributeApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;

public class AttributeApiDelete {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<String> keyList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void test(TestDataStruct data) throws JSONException {
        String result= tester.createAttribute(data.get(0),data.get(1),data.get(2));
        Assert.assertEquals("ok",SystemHelper.getStatusByJsonResult(result));
        keyList.add(data.get(0));
        String deleteResult = tester.deleteAttribute(data.get(0));
        Assert.assertEquals("ok",SystemHelper.getStatusByJsonResult(deleteResult));
        String listResult = tester.listAttribute();
        Assert.assertEquals("[]",listResult);
    }

    @DataProvider(name = "TestData")
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
