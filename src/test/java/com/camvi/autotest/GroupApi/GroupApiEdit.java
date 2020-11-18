package com.camvi.autotest.GroupApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;

public class GroupApiEdit {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    private int groupId;

    @Test
    public void test() throws JSONException {
        tester.editGroup(Integer.toString(groupId),"my_very_own_unit_test_group_2");
        String result = tester.queryGroup(Integer.toString(groupId));
        Assert.assertEquals("my_very_own_unit_test_group_2",new JSONObject(result).get("name").toString());
    }


    @AfterClass
    public void tearDown() {
        tester.deleteGroup(Integer.toString(groupId));
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        String result = tester.createGroup("my_very_own_unit_test_group");
        groupId = SystemHelper.getGroupIdByJsonResult(result);
    }
}
