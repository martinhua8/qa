package com.camvi.autotest.AttributeApi;

import com.beust.jcommander.internal.Lists;
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
import java.util.List;

public class AttributeApiCreate {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<String> keyList = new ArrayList<>();

    @Test(dataProvider = "AttributeApiCreateTestData")
    public void testCreate(TestDataStruct data) throws JSONException {
    	
    	// Get existing attributes first. Then check if the new one is created after eliminating the existing ones
    	List<String> ktclist = new ArrayList<>();
        String listResult = tester.listAttribute();
        JSONArray jsonArray = new JSONArray(listResult);        
        for(int i=0;i<jsonArray.length();i++) {        	
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String ktc = jsonObject1.get("key").toString().trim() + "," + jsonObject1.get("type").toString().trim() + "," + jsonObject1.get("category").toString().trim();
            ktclist.add(ktc);
        }
        
        //System.out.println("ktclist=" + ktclist);

        String result= tester.createAttribute(data.get(0),data.get(1),data.get(2));
        Assert.assertEquals("ok",SystemHelper.getStatusByJsonResult(result));
        keyList.add(data.get(0));
        listResult = tester.listAttribute();
        jsonArray = new JSONArray(listResult);
        
        Assert.assertEquals(jsonArray.length(), ktclist.size() + 1);
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String ktc = jsonObject1.get("key").toString().trim() + "," + jsonObject1.get("type").toString().trim() + "," + jsonObject1.get("category").toString().trim();

            if(ktclist.contains(ktc)) {
            	continue;
            }
            Assert.assertEquals(data.get(0),jsonObject1.get("key").toString());
            Assert.assertEquals(data.get(1),jsonObject1.get("type").toString());
            Assert.assertEquals(data.get(2),jsonObject1.get("category").toString());

            break;
        }

        tester.deleteAttribute(data.get(0));
    }


    @DataProvider(name="AttributeApiCreateTestData")
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
