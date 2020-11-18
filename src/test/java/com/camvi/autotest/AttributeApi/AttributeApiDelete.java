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
import java.util.Collections;
import java.util.List;

public class AttributeApiDelete {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<String> keyList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void test(TestDataStruct data) throws JSONException {
    	
    	// Get existing attributes first. Then check if the new one is created after eliminating the existing ones
    	List<String> ktclist = new ArrayList<>();
        String listResult = tester.listAttribute();
        JSONArray jsonArray = new JSONArray(listResult);        
        for(int i=0;i<jsonArray.length();i++) {        	
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String ktc = jsonObject1.get("key").toString().trim() + "," + jsonObject1.get("type").toString().trim() + "," + jsonObject1.get("category").toString().trim();
            ktclist.add(ktc);
        }

        String result= tester.createAttribute(data.get(0),data.get(1),data.get(2));
        Assert.assertEquals("ok",SystemHelper.getStatusByJsonResult(result));
        keyList.add(data.get(0));
        String deleteResult = tester.deleteAttribute(data.get(0));
        Assert.assertEquals("ok",SystemHelper.getStatusByJsonResult(deleteResult));
        listResult = tester.listAttribute();
        jsonArray = new JSONArray(listResult);

        Assert.assertEquals(jsonArray.length(), ktclist.size()); // The original size and new size should be the same and the new key should not be there
        
        List<String> ktclist1 = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {        	
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String ktc = jsonObject1.get("key").toString().trim() + "," + jsonObject1.get("type").toString().trim() + "," + jsonObject1.get("category").toString().trim();
            ktclist1.add(ktc);
        }
        
        Collections.sort(ktclist);
        Collections.sort(ktclist1);
        
        Assert.assertEquals(ktclist, ktclist1);
        
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        //return SystemHelper.getTestData("src/test/testFile/testData/AttributeManagementAPI/testCreateAttribute.xlsx");
    	return SystemHelper.getTestData("src/test/testFile/testData/AttributeManagementAPI/testCreateAttribute.csv");

    }

    @AfterClass
    public void afterClass() {
        for (String key:keyList) {
            tester.deleteAttribute(key);
        }
    }
}
