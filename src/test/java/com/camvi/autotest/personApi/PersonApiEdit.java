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

public class PersonApiEdit {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();


    @Test
    public void testEdit() throws JSONException {
        System.out.println("testing edit");
        tester.edit(personIdList.get(0).toString(),"camvi");
        String result = tester.query(personIdList.get((0)));
        Assert.assertEquals("camvi",SystemHelper.getPersonNameByJsonResult(result));
    }




    @AfterClass
    public void tearDown() {
        SystemHelper.cleanUp(personIdList,tester);
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        //SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
    	SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.csv", tester, personIdList);
    }
}
