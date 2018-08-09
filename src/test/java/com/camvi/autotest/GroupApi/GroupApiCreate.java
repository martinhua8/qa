package com.camvi.autotest.GroupApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;

public class GroupApiCreate {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> groupIdList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void testCreate(TestDataStruct data) throws JSONException {
        String result = tester.createGroup(data.get(0));
        groupIdList.add(SystemHelper.getGroupIdByJsonResult(result));
        String queryResult = tester.queryGroup(Integer.toString(SystemHelper.getGroupIdByJsonResult(result)));
        Assert.assertEquals(data.get(0),SystemHelper.getNameByJsonResult(queryResult));
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/GroupManagementAPI/testCreateGroup.xlsx");
    }


    @AfterClass
    public void tearDown() {
        for(int i = 0;i<groupIdList.size();i++){
            tester.deleteGroup(groupIdList.get(i).toString());
        }
    }

}
