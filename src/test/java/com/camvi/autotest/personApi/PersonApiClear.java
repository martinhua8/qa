package com.camvi.autotest.personApi;

import com.camvi.autotest.AutoTest;
import com.camvi.autotest.SystemHelper;
import com.camvi.autotest.TestDataStruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;

public class PersonApiClear {
    private AutoTest tester = new AutoTest(null, null);
    private ArrayList<Integer> personIdList = new ArrayList<>();
    private ArrayList<Integer> faceIdList = new ArrayList<>();

    @Test(dataProvider = "TestData")
    public void appendFace(TestDataStruct data) throws JSONException {
        String result = tester.appendFace(personIdList.get(0), data.get(0));
        JSONObject jsonObject = new JSONObject(result);
        String faceId = jsonObject.get("faceId").toString();
        faceIdList.add(Integer.parseInt(faceId));
    }

    @Test(dependsOnMethods = "appendFace")
    public void test() throws JSONException {
        System.out.println("testing clear");
        String result = tester.query(personIdList.get(0));
        JSONObject jsonObject = new JSONObject(result);
        String faceId = jsonObject.get("faces").toString();
        String[] faceIdArray = faceId.substring(1,faceId.length()-1).split(",");
        int size = faceIdArray.length;
        String clearResult = tester.clear(personIdList.get(0));
        Assert.assertEquals(size,SystemHelper.getRemovedByJsonResult(clearResult));
        String queryresult =tester.query(personIdList.get(0));
        JSONObject jsonObject2 = new JSONObject(queryresult);
        String faceId2 = jsonObject2.get("faces").toString();
        Assert.assertEquals("[]",faceId2);


//        String[] array = new String[faceIdList.size()];
//        for(int i = 0;i<array.length;i++){
//            array[i] = faceIdList.get(i).toString();
//        }
//        Assert.assertEquals(array,faceIdArray);
    }

    @DataProvider(name = "TestData")
    public static Object[][] objectTestData() throws IOException, InvalidFormatException {
        return SystemHelper.getTestData("src/test/testFile/testData/PersonFaceManagementAPI/testAppendFace.xlsx");
    }


    @AfterClass
    public void tearDown() {
        SystemHelper.cleanUp(personIdList,tester);
    }

    @BeforeClass
    public void setUp() throws JSONException, InvalidFormatException, IOException {
        SystemHelper.setUp("src/test/testFile/testData/standardPicLibrary.xlsx", tester, personIdList);
        String result = tester.query(personIdList.get(0));
        JSONObject jsonObject = new JSONObject(result);
        String faceId = jsonObject.get("faces").toString();
        String[] faceIdArray = faceId.substring(1,faceId.length()-1).split("'");
        for (int i =0;i<faceIdArray.length;i++){
            faceIdList.add(Integer.parseInt(faceIdArray[i]));
        }
    }
}
