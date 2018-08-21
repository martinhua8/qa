package com.camvi.autotest.personApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

public class PersonApiFindByName {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    int count = 0;

    @Test(dataProvider = "TestData")
    public void test1(TestDataStruct data) throws JSONException {
        String result = tester.findByName(data.get(0));
        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        Assert.assertEquals(personIdList.get(count++), jsonObject.get("person"));

    }

    @Test
    public void testDuplicateName() throws JSONException {
        String result = tester.findByName("Unit_Test");
        JSONArray jsonArray = new JSONArray(result);
        ArrayList<Integer> personIds = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            personIds.add(Integer.parseInt(jsonObject.get("person").toString()));
        }
        Assert.assertEquals(personIds, personIdList);
    }


    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/standardPicLibrary.xlsx");
    }


    @AfterClass
    public void tearDown() {
        SystemHelper.cleanUp(personIdList, tester);
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
    }
}