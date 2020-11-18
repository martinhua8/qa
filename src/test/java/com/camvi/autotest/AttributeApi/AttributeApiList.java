package com.camvi.autotest.AttributeApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeApiList {
    private AutoTest tester = new AutoTest(null, null);
    private List<String> keyList = new ArrayList<>();
    private List<String> ktcList = new ArrayList<>();
    private List<String> beforeList = new ArrayList<>();

    @Test
    public void initList() throws JSONException {
    	System.out.println("Calling initList.......");
    	
    	String listResult = tester.listAttribute();
        JSONArray jsonArray = new JSONArray(listResult);        
        for(int i=0;i<jsonArray.length();i++) {        	
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String ktc = jsonObject1.get("key").toString().trim() + "," + jsonObject1.get("type").toString().trim() + "," + jsonObject1.get("category").toString().trim();
            beforeList.add(ktc);
        }
    }
    
    @Test(dataProvider = "TestData", dependsOnMethods = {"initList"})
    public void create(TestDataStruct data) throws JSONException {
        String result= tester.createAttribute(data.get(0),data.get(1),data.get(2));
        Assert.assertEquals("ok",SystemHelper.getStatusByJsonResult(result));
        ktcList.add(data.get(0) + "," + data.get(1) + "," + data.get(2));
        keyList.add(data.get(0));

    }

    @Test(dependsOnMethods = "create")
    public void test() throws JSONException {
        String result = tester.listAttribute();
        JSONArray jsonArray = new JSONArray(result);
        Assert.assertEquals(jsonArray.length(),beforeList.size() + keyList.size());
        List<String> newList = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String ktc = jsonObject1.get("key").toString().trim() + "," + jsonObject1.get("type").toString().trim() + "," + jsonObject1.get("category").toString().trim();
            newList.add(ktc);
        }
        
        beforeList.addAll(ktcList);
        Collections.sort(beforeList);
        Collections.sort(newList);
        
        Assert.assertEquals(beforeList, newList);
    }

    @AfterClass
    public void afterClass() {
        for (String key:keyList) {
            tester.deleteAttribute(key);
        }
    }

    @DataProvider(name="TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        //return SystemHelper.getTestData("src/test/testFile/testData/AttributeManagementAPI/testCreateAttribute.xlsx");
    	return SystemHelper.getTestData("src/test/testFile/testData/AttributeManagementAPI/testCreateAttribute.csv");

    }
}
