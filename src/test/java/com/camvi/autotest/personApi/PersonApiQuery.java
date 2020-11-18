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

public class PersonApiQuery {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    int count =0;

    @Test(dataProvider = "TestData")
    public void testQuery(TestDataStruct data) throws JSONException {
        System.out.println("testing query");
        String result = tester.query(personIdList.get(count++));
        Assert.assertEquals(data.get(0),SystemHelper.getPersonNameByJsonResult(result));

    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        //return SystemHelper.getTestData("src/test/testFile/testData/standardPicLibrary.xlsx");
    	return SystemHelper.getTestData("src/test/testFile/testData/standardPicLibrary.csv");
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
