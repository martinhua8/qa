package com.camvi.autotest;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//import org.powermock.modules.junit4.PowerMockRunnerDelegate;

/**
 * Unit test
 */
@RunWith(JUnitParamsRunner.class)
public class AppTest {
    /**
     * Rigorous Test :-)
     */


    private AutoTest tester = new AutoTest(null, null);


    @BeforeClass
    public static void beforeClass() throws IOException, InvalidFormatException {

        System.out.println("----------beforeClass----------");
        //SystemHelper.runShellScript("src/test/testFile/testShelllScript/clearDB.sh");
        System.out.println("``````````````setServerBase`````````````````");
        BaseQuery.setServerBase("http://localhost:8080/service/api");
        if (!BaseQuery.login()) {
            return;
        }

        boolean setUpLibrary = false;
        if (setUpLibrary) {
            SystemHelper.setUpStandardPicLibrary("src/test/testFile/testData/standardPicLibrary.xlsx");
        }

    }


    @AfterClass
    public static void afterClass() {
        System.out.println("========after class=============");
    }

    @Before
    public void before() {
        //MockitoAnnotations.initMocks(this);
        System.out.println("========before =============");
    }

    @After
    public void after() {
        System.out.println("========after =============");

    }

    //region Person/Face Management API

    @Test
    @FileParameters("/home/qiang/Desktop/FBI.csv")
    public void testFBIPerson(String personName, String groupId, String photo) throws JSONException, IOException, InvalidFormatException {

            tester.createPerson(personName, groupId, photo);

            assertTrue(true);



    }



    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testCreate.csv")
    public void testCreatePerson(String personName, String groupId, String photo) throws JSONException, IOException, InvalidFormatException {
        try {
            tester.createPerson(personName, groupId, photo);
            String result = tester.findByName(personName);
            ArrayList list = SystemHelper.getPersonIdByName(result);
            System.out.println(list.size());
            //assertTrue(true);
            assertFalse(list.isEmpty());
        } finally {
            System.out.println("~~~~~~~~~~");
        }

    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/testAddUrl.csv")
    public void testAddUrl(String personName, int groupId, String imageUrl, String code) throws JSONException, IOException, InvalidFormatException {
        String result = tester.addUrl(personName, groupId, imageUrl);
        JSONObject jsonObject = new JSONObject(result);
        assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testRemovePersons.csv")
    public void testDeletePerson(int personId) throws JSONException {
        String result = tester.deletePerson(personId);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testAppendFace.csv")
    public void testAppendFace(int personId, String photo) throws JSONException {
        String result = tester.appendFace(personId, photo);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testRemoveFace.csv")
    public void testRemoveFace(int personId, int faceId) throws JSONException {
        String result = tester.removeFace(personId, faceId);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testClear.csv")
    public void testClear(int personId) throws JSONException {
        String result = tester.clear(personId);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }


    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testAddDetail.csv")
    public void testAddDetail(String personId, String key, String value) throws JSONException {
        String result = tester.addDetailPersonManagement(personId, key, value);
        JSONObject jsonObject = new JSONObject(result);
        //assertTrue(true);
        assertEquals("Country", jsonObject.get("key set").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testRemoveDetail.csv")
    public void testRemoveDetail(String personId, String key) throws JSONException {
        String result = tester.removeDetail(personId, key);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals("Country", jsonObject.get("key removed").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testDisablePerson.csv")
    public void testDisablePerson(String personId) throws JSONException {
        String result = tester.disablePerson(personId);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals("Country", jsonObject.get("key removed").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testEnablePerson.csv")
    public void testEnablePerson(String personId) throws JSONException {
        String result = tester.enablePerson(personId);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals("Country", jsonObject.get("key removed").toString());
    }


    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testQuery.csv")
    public void testQuery(int personId) throws JSONException {
        String result = tester.query(personId);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testFaceInfo.csv")
    public void testFaceInfo(int personId) throws JSONException {
        String result = tester.faceInfo(personId);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testList.csv")
    public void testList(String groupId) throws JSONException {
        String result = tester.listPersonWithinGroup(groupId);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testGetCount.csv")
    public void testGetCount(String groupId) throws JSONException {
        String result = tester.getCount(groupId);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }


    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/PersonFaceManagementAPI/testFindByName.csv")
    public void testFindByName(String name) throws JSONException {
        String result = tester.findByName(name);
//        JSONArray jsonArray = new JSONArray(result);
//
//        //System.out.println(jsonArray);
//        ArrayList list = new ArrayList();
//        for(int i = 0;i<jsonArray.length();i++){
//            JSONObject jsonObject = jsonArray.getJSONObject(i);
//            System.out.println(jsonObject.get("person").toString());
//            list.add(Integer.parseInt(jsonObject.get("person").toString()));
//        }

        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }
    //endregion


    //region Attribute Management API
    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/AttributeManagementAPI/testCreateAttribute.csv")
    public void testCreateAttribute(String key, String type, String category) throws JSONException {
        String result = tester.createAttribute(key, type, category);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/AttributeManagementAPI/testDeleteAttribute.csv")
    public void testDeleteAttribute(String key) throws JSONException {
        String result = tester.deleteAttribute(key);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
    }

    @Ignore
    @Test
    public void testListAttribute() throws JSONException {
        String result = tester.listAttribute();
        assertTrue(true);
    }
    //endregion

    //region Group Management API

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testCreateGroup.csv")
    public void testCreateGroup(String groupName) throws JSONException {
        String result = tester.createGroup(groupName);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testDeleteGroup.csv")
    public void testDeleteGroup(String groupId) throws JSONException {
        String result = tester.deleteGroup(groupId);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testAppendPerson.csv")
    public void testAppendPerson(String groupId, String personId) throws JSONException {
        String result = tester.appendPersonToGroup(groupId, personId);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testRemovePerson.csv")
    public void testRemovePerson(String groupId, String personId) throws JSONException {
        String result = tester.removePersonFromGroup(groupId, personId);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testClear.csv")
    public void testClearGroup(String groupId) throws JSONException {
        String result = tester.clearPeopleFromGroup(groupId);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testEdit.csv")
    public void testEdit(String groupId, String groupName) throws JSONException {
        String result = tester.editGroup(groupId, groupName);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testQuery.csv")
    public void testQuery(String groupId) throws JSONException {
        String result = tester.queryGroup(groupId);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }


    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testGetInfo.csv")
    public void testGetGroupInfo(String groupId) throws JSONException {
        String result = tester.getGroupInfo(groupId);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    public void testListGroup() throws JSONException {
        String result = tester.listGroup();
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testAddDetail.csv")
    public void testAddDetailGroup(String groupId, String key, String value) throws JSONException {
        String result = tester.addDetailGroup(groupId, key, value);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }


    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/GroupManagementAPI/testRemoveDetail.csv")
    public void testDeleteDetailGroup(String groupId, String key) throws JSONException {
        String result = tester.removeDetailGroup(groupId, key);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }


    //endregion

    //region Face Detection API
    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/FaceDetectionAPI/testDetectFacesLocalImage.csv")
    public void testDetectFaceLocalImage(String tag, String photo) throws JSONException {

        String result = tester.detectFacesLocalImage(tag, photo);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/FaceDetectionAPI/testListFaceDetection.csv")
    public void testListFaceDetection(String tag) throws JSONException {

        String result = tester.listFaceDetection(tag);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/FaceDetectionAPI/testDeleteFaceDetection.csv")
    public void testDeleteFaceDetection(int imageId) throws JSONException {

        String result = tester.deleteFaceDetection(imageId);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }
    //endregion

    //region Face Recognition API
    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/FaceRecognitionAPI/testVerifyFaceRec.csv")
    public void testVerifyFaceRec(String personId, String photo) throws JSONException {

        String result = tester.verifyFaceRec(personId, photo);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/FaceRecognitionAPI/testRecognizeFaceRec.csv")
    public void testRecognizeFaceRec(String groupId, String photo) throws JSONException {

        String result = tester.recognizeFaceRec(groupId, photo);
        JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/FaceRecognitionAPI/testSearchFaceRec.csv")
    public void testSearchFaceRec(String groupId, String photo) throws JSONException {

        String result = tester.searchFaceRec(groupId, photo);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/FaceRecognitionAPI/testCheckQualityFaceDetection.csv")
    public void testCheckQualityFaceRec(String photo) throws JSONException {

        String result = tester.checkQualityFaceRec(photo);
        JSONObject jsonObject = new JSONObject(result);
        assertEquals("pass", jsonObject.get("result").toString());
    }

    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/FaceRecognitionAPI/testCompareFaceRec.csv")
    public void testCompareFaceRec(String photo1, String photo2) throws JSONException {

        String result = tester.compareFaceRec(photo1, photo2);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }
    //endregion

    //region Log API
    @Ignore
    @Test
    public void testRetrieve() throws JSONException {
        String result = tester.retrieveLog();
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }


    @Ignore
    @Test
    @FileParameters("src/test/testFile/testData/LogAPI/testAddDetail.csv")
    public void testAddAttributeLog(String groupId, String key, String value) throws JSONException {
        String result = tester.setAttributeLog(groupId, key, value);
        //JSONObject jsonObject = new JSONObject(result);
        assertTrue(true);
        //assertEquals(code, jsonObject.get("code").toString());
    }


    //endregion

    @Test
    public void test1() {
        assertTrue(true);
        System.out.println("simple test1 finished");
    }


}
