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

public class PersonApiFaceInfo {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    private ArrayList<PersonIdFaceIdData> personIdFaceIdList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void createFaces(TestDataStruct data) throws JSONException {

        String createFacesResult = tester.createPerson(data.get(0), data.get(1), data.get(2));
        personIdList.add(SystemHelper.getPersonIdByJsonResult(createFacesResult));
        PersonIdFaceIdData PersonData = new PersonIdFaceIdData();
        PersonData.setPersonId(SystemHelper.getPersonIdByJsonResult(createFacesResult));
        PersonData.setFaceId(SystemHelper.getFaceIdByJsonResult(createFacesResult));
        personIdFaceIdList.add(PersonData);
    }

    @Test(dependsOnMethods = "createFaces")
    public void testFaceInfo() throws JSONException {
        System.out.println("testing face info");
        for (int i=0;i< personIdFaceIdList.size();i++){
            String result = tester.faceInfo(personIdFaceIdList.get(i).getFaceId());
            Assert.assertEquals(personIdFaceIdList.get(i).getPersonId(),SystemHelper.getPersonIdFromFaceInfo(result));
        }
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

    }
}
