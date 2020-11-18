package com.camvi.autotest.personApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;

public class PersonApiEnable {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();

    @Test
    public void Disable() throws JSONException {
        for (int i = 0;i<personIdList.size();i++) {
            tester.disablePerson(personIdList.get(i).toString());
            String result = tester.query(personIdList.get((i)));
            Assert.assertEquals("false",SystemHelper.getEnabledByJsonResult(result));
        }
    }
    @Test(dependsOnMethods = "Disable")
    public void testEnable() throws JSONException {
        System.out.println("testing enable");
        for (int i = 0;i<personIdList.size();i++) {
            tester.enablePerson(personIdList.get(i).toString());
            String result = tester.query(personIdList.get((i)));
            Assert.assertEquals("true",SystemHelper.getEnabledByJsonResult(result));
        }
    }


    @AfterClass
    public void tearDown() {
        SystemHelper.cleanUp(personIdList, tester);
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        //SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
    	SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.csv", tester, personIdList);
    }
}
