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

public class PersonApiRemoveFace {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    private ArrayList<Integer> faceIdList = new ArrayList<>();
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
    public void testRemoveFace(){
        System.out.println("testing remove face");
        for (int i=0;i< personIdFaceIdList.size();i++){
            tester.removeFace(personIdFaceIdList.get(i).getPersonId(),personIdFaceIdList.get(i).getFaceId());
            try {
                tester.faceInfoWithoutTryCatch(personIdFaceIdList.get(i).getFaceId());
            } catch (Exception e) {
                Assert.assertEquals("Search error: 500", e.getMessage());
            }
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

class PersonIdFaceIdData{
    private int personId;
    private int faceId;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }
}
