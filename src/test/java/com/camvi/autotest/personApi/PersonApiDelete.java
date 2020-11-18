package com.camvi.autotest.personApi;
import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
public class PersonApiDelete {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    int count =0;

    @Test(dataProvider = "TestData")
    public void testCreate(TestDataStruct data) throws JSONException {
        System.out.println("PersonApiDelete testing !!!!");
        String queryResult = tester.query(personIdList.get(count++));
        String personName = SystemHelper.getPersonNameByJsonResult(queryResult);
        Assert.assertEquals(data.get(0),personName);
    }
    @DataProvider(name="TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        //return SystemHelper.getTestData("src/test/testFile/testData/standardPicLibrary.xlsx");
    	return SystemHelper.getTestData("src/test/testFile/testData/standardPicLibrary.csv");
    }


    @Test(dependsOnMethods = {"testCreate"})
    public void testDelete(){
        System.out.println("testing delete");
        for (int i = 0; i < personIdList.size(); i++) {
            tester.deletePerson(personIdList.get(i));
            try {
                tester.queryWithoutTryCatch(personIdList.get(i));
            } catch (Exception e) {
                // can't find entry

                Assert.assertEquals("Search error: 500",e.getMessage());
            }

        }
    }

    @AfterClass
    public void tearDown() {

    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        //SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx",tester,personIdList);
    	SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.csv",tester,personIdList);
    }
}
