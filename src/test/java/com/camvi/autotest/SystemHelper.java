package com.camvi.autotest;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.camvi.autotest.BaseQuery.doPost;

public class SystemHelper {
    public static void runShellScript(String file) {
        String s;
        Process p;
        try {
            System.out.println("before excute file");
            System.out.println(file);
            p = Runtime.getRuntime().exec(file);

            System.out.println("after excute file");
            //p.waitFor();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
        }
    }

    public static void inputStandardPicture(String personName, int groupId, String localImageAddress) {
        try {
            BufferedImage img1 = ImageIO.read(new File(localImageAddress));
            Map<String, String> params = new HashMap<String, String>();
            BaseQuery bq = new BaseQuery();
            params.put("person-name", personName);
            params.put("group-ids", Integer.toString(groupId));
            params.put("image-data", bq.toBase64Image(img1));
            params.put("checkdupe", "true");
            params.put("checkporn", "true");
            byte[] resp = doPost("/person/create", params);
            System.out.println("RESULT: " + new String(resp, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setUp(String file, AutoTest tester,ArrayList<Integer> personIdList) throws IOException, InvalidFormatException, JSONException {
        Object[][] testData = getTestData(file);
        for (int i =0;i<testData.length;i++){
            TestDataStruct data = (TestDataStruct)testData[i][0];
            String createPersonResult =tester.createPerson(data.get(0), data.get(1), data.get(2));
            int personId = SystemHelper.getPersonIdByJsonResult(createPersonResult);
            personIdList.add(personId);
        }

    }

//    System.out.println("JunitPersonApiCreate testing !!!!");
//    String createPersonResult = tester.createPerson(data.get(0), data.get(1), data.get(2));
//    int personId = SystemHelper.getPersonIdByJsonResult(createPersonResult);
//        personIdList.add(personId);
//    String queryResult = tester.query(personId);
//    String personName = SystemHelper.getPersonNameByJsonResult(queryResult);
//        Assert.assertEquals(data.get(0),personName);


    public static void setUpStandardPicLibrary(String file) throws IOException, InvalidFormatException {
        LinkedList<AddLocalImageDataStruct> list = ReadCSVData.getCSVInfoWithoutFistRow(file); //GetExccelInfo.getExcelInfoWithoutFistRow(file);

        for (AddLocalImageDataStruct data : list
                ) {
            inputStandardPicture(data.getPersonName(), data.getGroupId(), data.getLocalImageAddress());
        }
        System.out.println("finish  setUpStandardPicLibrary");
    }

    public static ArrayList<Integer> getPersonIdByName(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            list.add(Integer.parseInt(jsonObject.get("person").toString()));

        }
        return list;
    }
    public static double getConfidenceFromJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return  Double.parseDouble(jsonObject.get("confidence").toString());
    }
    public static double getSimilarityFromJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return  Double.parseDouble(jsonObject.get("similarity").toString());
    }

    public static int getPersonIdFromFaceInfo(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return Integer.parseInt(jsonObject.get("person").toString());
    }
    public static int getRemovedByJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return Integer.parseInt(jsonObject.get("removed").toString());
    }
    public static int getPersonIdByJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return Integer.parseInt(jsonObject.get("personId").toString());
    }
    public static int getFaceIdByJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return Integer.parseInt(jsonObject.get("faceId").toString());
    }
    public static int getGroupIdByJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return Integer.parseInt(jsonObject.get("groupId").toString());
    }

    public static String getStatusByJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return (jsonObject.get("status").toString());
    }
    public static String getPersonNameByJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return (jsonObject.get("name").toString());
    }
    public static String getNameByJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return (jsonObject.get("name").toString());
    }
    public static String getCodeByJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return (jsonObject.get("code").toString());
    }
    public static String getEnabledByJsonResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        return (jsonObject.get("enabled").toString());
    }

    public static boolean isEmptyJsonArray(String str){
        String string = str.substring(1,str.length()-1);
        return string.isEmpty();
    }

    public static void Login() {

    }
    public static Object[][] getTestData(String file) throws IOException, InvalidFormatException {
        LinkedList<TestDataStruct> list;
        //list = GetExccelInfo.getStringListFromExcelWithoutFistRow(file);
        list = ReadCSVData.getStringListFromCSVWithoutFistRow(file);
        int size = list.size();
        Object[][] objects = new Object[size][1];
        for(int i = 0;i<size;i++){
            objects[i][0] = list.toArray()[i];
        }
        return objects;
    }

    public static void cleanUp(ArrayList<Integer> personIdList, AutoTest tester){
        for (int personId:personIdList) {
            tester.deletePerson(personId);
        }
        System.out.println("Finish clean up !!");
    }
/*
    public static void cleanUp(String filePath) throws JSONException, IOException, InvalidFormatException {
        AutoTest tester2 = new AutoTest(null, null);
        System.out.println("clean up begin");
        LinkedList<String> listOfNames = GetExccelInfo.getNameFromExcel(filePath);
        for (int i = 0; i < listOfNames.size(); i++) {
            String result = tester2.findByName(listOfNames.get(i));
            ArrayList<Integer> listOfId = SystemHelper.getPersonIdByName(result);
            for (int j = 0; j < listOfId.size(); j++) {
                System.out.println("begin to delete person " + listOfId.get(j));
                tester2.deletePerson(listOfId.get(j));
            }
        }
        System.out.println("Finish clean up !!");
    }
    */

}
