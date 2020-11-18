package com.camvi.autotest.GroupApi;

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

public class GroupApiAddDetail {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    String groupId;

    @Test
    public void test() throws JSONException {
        tester.addDetailGroup(groupId,"Test_Key","Test_Value");
        String result = tester.getGroupInfo(groupId);
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("details");
        Assert.assertEquals("Test_Key",jsonArray.getJSONObject(0).get("key"));
        Assert.assertEquals("Test_Value",jsonArray.getJSONObject(0).get("value"));
        System.out.println("-------------------------");
        //tester.deleteAttribute("Test_Key"); -- Fails because the attribute is in use
        //System.out.println("-------------------------");
    }

    @AfterClass
    public void tearDown() {

        tester.deleteGroup(groupId);
        System.out.println("----------");
        tester.deleteAttribute("Test_Key");
        System.out.println("----------");
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        tester.deleteAttribute("Test_Key");
        tester.createAttribute("Test_Key","CharType","GroupCat");

        String result = tester.createGroup("my_very_own_unit_test_group");
        groupId = Integer.toString(SystemHelper.getGroupIdByJsonResult(result)) ;
    }
}
