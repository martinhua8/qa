package com.camvi.autotest.GroupApi;

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
import java.lang.reflect.Method;
import java.util.ArrayList;

public class GroupApiGetInfo {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    private int groupId;

    @Test
    public void test() throws JSONException {
        String result = tester.getGroupInfo(Integer.toString(groupId));
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertEquals("my_very_own_unit_test_group",jsonObject.get("name"));

    }


    @AfterClass
    public void tearDown() {
        SystemHelper.cleanUp(personIdList, tester);
        tester.deleteGroup(Integer.toString(groupId));
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        //SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
    	SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.csv", tester, personIdList);
        String result = tester.createGroup("my_very_own_unit_test_group");
        groupId = SystemHelper.getGroupIdByJsonResult(result);
        for (int personId:personIdList
                ) {
            tester.appendPersonToGroup(Integer.toString(groupId),Integer.toString(personId));
        }
    }
    
}
