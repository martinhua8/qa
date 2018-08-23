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

public class PersonApiRemoveDetail {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();

    @Test
    public void test() throws JSONException {
        System.out.println("testing remove detail");
        tester.addDetailPersonManagement(personIdList.get(0).toString(),"Test_Key","Test_Value");
        String result = tester.query(personIdList.get(0));
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("details");
        Assert.assertEquals("Test_Key",jsonArray.getJSONObject(0).getString("key"));
        Assert.assertEquals("Test_Value",jsonArray.getJSONObject(0).getString("value"));
        //------------------
        result = tester.removeDetail(personIdList.get(0).toString(),"Test_Key");
        Assert.assertEquals("Test_Key",new JSONObject(result).getString("key removed"));
        result = tester.query(personIdList.get(0));
        jsonObject = new JSONObject(result);
        jsonArray =jsonObject.getJSONArray("details");
        Assert.assertEquals("[]",jsonArray.toString());

    }




    @AfterClass
    public void tearDown() {
        SystemHelper.cleanUp(personIdList, tester);
        tester.deleteAttribute("Test_Key");
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
        tester.createAttribute("Test_Key","CharType","PersonCat");
    }
}
