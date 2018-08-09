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
import java.util.List;

public class PersonApiList {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();

    @Test
    public void testList() {
        String result = tester.listPersonWithinGroup("1");
        String[] faceIdArray = result.substring(1,result.length()-1).split(",");
        ArrayList<Integer> faceIdList = new ArrayList<>();
        for (String faceId:faceIdArray
             ) {faceIdList.add(Integer.parseInt(faceId));
        }
        Assert.assertEquals(faceIdList,personIdList);
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
