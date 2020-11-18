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

public class GroupApiDelete {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> groupIdList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void create(TestDataStruct data) throws JSONException {
        String result = tester.createGroup(data.get(0));
        groupIdList.add(SystemHelper.getGroupIdByJsonResult(result));
        System.out.println("in create - result=" + result);
    }

    @Test(dependsOnMethods = "create")
    public void test() throws Exception {
        for(int i = 0;i<groupIdList.size();i++){
            tester.deleteGroup(groupIdList.get(i).toString());
            String result =  tester.queryGroup(groupIdList.get(i).toString());
            // return code 1002 Invalid group Id
            Assert.assertEquals("1002",SystemHelper.getCodeByJsonResult(result));
        }
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        //return SystemHelper.getTestData("src/test/testFile/testData/GroupManagementAPI/testCreateGroup.xlsx");
    	return SystemHelper.getTestData("src/test/testFile/testData/GroupManagementAPI/testCreateGroup.csv");
    }


    @AfterClass
    public void tearDown() {
        //for(int i = 0;i<groupIdList.size();i++){
        //    tester.deleteGroup(groupIdList.get(i).toString());
        //}
    }


}
