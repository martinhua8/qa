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

public class GroupApiList {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    private int groupId;

    @Test
    public void test() throws JSONException {
        String result = tester.listGroup();

    }


    @AfterClass
    public void tearDown() {
        tester.deleteGroup(Integer.toString(groupId));
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
        String result = tester.createGroup("Test_Group");
        groupId = SystemHelper.getGroupIdByJsonResult(result);

    }
}
