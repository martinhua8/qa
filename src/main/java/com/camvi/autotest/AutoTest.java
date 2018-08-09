package com.camvi.autotest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class AutoTest extends BaseQuery implements Runnable {

    private static AtomicInteger s_numOfQueries = new AtomicInteger();
    private static AtomicInteger s_numTop1Match = new AtomicInteger();
    private static AtomicInteger s_numTop10Match = new AtomicInteger();
    private static AtomicInteger s_numFailures = new AtomicInteger();
    private static AtomicInteger s_responseTimeTotal = new AtomicInteger();
    private static ArrayList<Integer> s_missedNsfwIds = new ArrayList<Integer>();
    private static AtomicInteger s_outstandingNsfwTasks = new AtomicInteger();
    private static AtomicInteger s_totalNsfwRemoved = new AtomicInteger();
    private static TestMode mode = TestMode.None;
    private static int numThreads = 16;
    private static int s_totalQueries = 200;
    private String m_searchName;
    private int m_nsfwId;
    private File m_importFile;

    public AutoTest(String searchImage, File importFile) {
        m_importFile = importFile;
    }

    public static void main(String[] args) {
        // FaceDetector.test();

        if (args.length < 2 && mode == TestMode.ImportFile) {
            System.out.println("Argument: <server API root> <import directory>");
            System.out.println("Example: http://192.168.0.8:8080/service/api /path/to/import");
            return;
        }

        if (args.length < 4 && mode == TestMode.DirSearch) {
            System.out.println("usage: <server address> <search directory> <num threads> <num searches>");
            return;
        }

        if (args.length < 3 && mode == TestMode.Compare) {
            System.out.println("Usage: <server address> <image path 1> <image path 2>");
            return;
        }

        if (args.length == 0) {
            BaseQuery.setServerBase("http://localhost:8080/service/api");
        } else {
            BaseQuery.setServerBase(args[0]);
        }

        if (!BaseQuery.login()) {
            return;
        }

        AutoTest tester = new AutoTest(null, null);

        if (mode != TestMode.None) {
            long startTime = System.currentTimeMillis();
            if (mode == TestMode.Import || mode == TestMode.ImportFile) {
                importPersons(args[1]);
            } else if (mode == TestMode.LFWSearch) {
                lfwSearchPersons("/Users/stevenkan/development/lfw");
            } else if (mode == TestMode.DirSearch) {
                numThreads = Integer.parseInt(args[2]);
                s_totalQueries = Integer.parseInt(args[3]);
                dirSearchPersons(args[1]);
            } else if (mode == TestMode.RemoveNsfw) {
                // removeNsfwAll();
            } else if (mode == TestMode.Top5Match) {
                tester.listTop5Matches(args[0], args[1]);
            } else if (mode == TestMode.Compare) {
                tester.compare(args[1], args[2]);
            } else {
                for (int iThread = 0; iThread < numThreads; iThread++) {
                    Thread t = new Thread(new AutoTest("test" + String.format("%02d", iThread % 10 + 1) + ".jpg", null));
                    // Thread t = new Thread(new BasicTest("test01.jpg", null));
                    t.start();
                }
            }
            long stopTime = System.currentTimeMillis();
            System.out.println("\nElapsed time was " + (stopTime - startTime) + " miliseconds.");
            return;
        }

        // misc testing


        //tester.registerPerson("TestSW3", 1, "/Users/stevenkan/development/centos_share/S033-05-t10_01.ppm");
        // tester.testCompare("/Users/stevenkan/Downloads/FaceImages/f1.png", "/Users/stevenkan/Downloads/FaceImages/f2.png");
        // tester.addUrl("Michael Jordan", 1, "https://www.biography.com/.image/t_share/MTE5NTU2MzE2MjY3OTA2NTcx/michael-jordan-9358066-1-402.jpg");
        tester.addUrl("Multiple Faces", 1, "https://www.uni-regensburg.de/Fakultaeten/phil_Fak_II/Psychologie/Psy_II/beautycheck/english/durchschnittsgesichter/m(01-32).jpg");

        tester.addUrl("Multiple Faces", 1, "https://media.istockphoto.com/photos/friendship-picture-id532969250?k=6&m=532969250&s=612x612&w=0&h=Vlf2_iNPkEjbCNozIbZlScGfRx4fDSpGphGM9P1XGFQ=");

        tester.addUrl("Found from google001", 1, "https://media.glamour.com/photos/5a425fd3b6bcee68da9f86f8/master/w_644,c_limit/best-face-oil.png");


        //tester.testDeletePerson();

        //tester.checkForQuality("http://i.dawn.com/medium/2016/12/58597887d8503.jpg", "40");

        // tester.detectFacesUrl("http://www.filhamparkvets.co.uk/Images/DC%20Cyprus%201.jpg");

        /*
        byte[] imgData = BasicTest.getImageFromURL("http://www.filhamparkvets.co.uk/Images/DC%20Cyprus%201.jpg");
        String imgStr = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imgData);
        for (int i=0; i<1; i++) {
            tester.detectFaces(imgStr);
        }
        */
        {  // just for this test, use root as API base
            //BaseQuery.setServerBase("http://192.168.56.101:8080/");
            //tester.setNotificationUrl("http://localhost/madeup");
        }
        // tester.addPersonDetail3("2");
        //tester.removePersonDetail("42855");
        // tester.addAttribute();
        //tester.deleteAttribute();

        //tester.getFaceFeature(2);

        //BasicTest tester = new BasicTest(null, null);

        //tester.checkForQuality("http://i.dawn.com/medium/2016/12/58597887d8503.jpg", "50", "3.4");
        // tester.checkForQuality("http://i.dawn.com/medium/2016/12/58597887d8503.jpg", "80");
        //tester.checkForDupe("http://i.dawn.com/medium/2016/12/58597887d8503.jpg");
        //tester.checkForDupe("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuOArh8Qtwkpul92zoIIbmMhl1Ta47g-4-bI6C_Sz5nvTd4PBl");

        // tester.addUrl("TestSW3", 7, "http://www.theimaginativeconservative.org/wp-content/uploads/2015/04/robert-downey-jr.png");
        // tester.testRemoveNsfw(2098840);
        // tester.testRemovePerson(42856);
        //tester.registerPerson("testCrop", 1, "/Users/stevenkan/Downloads/FaceImages/1620985.jpeg");
        // TODO: fix this! dog face mistaken as a valid face
        // http://idata.over-blog.com/2/48/64/04/Nice-2009/P1050387.jpg

        // very blurry face accepted
        // tester.addUrl("blurry", 1, "http://www.angelfire.com/nj3/rowanwic/wickedv1i1p1.jpg");

        // cartoon face
        // http://slotobaza.com/wp-content/uploads/2016/10/tablicy-vyigryshej-v-slote-dzhek-i-dzhill.jpg

        // not exactly a face
        // http://www.rizwanashraf.com/wp-content/uploads/2010/10/GreenPois0n.png

        // tester.addUrl("showUpOnAll15", 1, "http://i.telegraph.co.uk/multimedia/archive/03585/Emmeline_Pankhurst_3585787c.jpg");
        //for (int iRepeat=0; iRepeat<1; iRepeat++) {
        //    tester.addUrl("testPornFilter", 1, "http://yourxporn.net/wp-content/uploads/2017/02/398711-250x354.jpg");
        //}
        //tester.addUrl("testNotAPerson", 1, "https://www.petfinder.com/wp-content/uploads/2012/11/100691619-what-is-cat-fostering-632x475.jpg");
        // tester.addUrl("testUrl21", 1, "http://www.djfl.de/filmnews/wp-content/uploads/2012/06/Diva-Award_fuer_Maria_Furtw%C3%A4ngler-270x170.jpg999");
        // tester.checkForDupe("http://i.dawn.com/medium/2016/12/58597887d8503.jpg");
        //tester.addUrl("testUrl2", 1, "http://www.independent.co.uk/sites/all/themes/ines_themes/independent_theme/img/ms-icon-310x310.png");
        //tester.addUrl("testUrl3", 1, "https://pbs.twimg.com/profile_images/1160034742/steven_2_reasonably_small_copy_400x400.png");
        // tester.addUrl("testUrl55", 1, "https://www.organspende-info.de/sites/all/files/styles/flipbox/public/image/grafik_gewebetransplantation.jpg");
        // tester.addUrl("testUrl4", 1, "http://cache4.asset-cache.net/xt/645184320.jpg?v=1&g=fs2|0|editorial482|84|320&s=1");
        // tester.addUrl("testUrl6", 1,"http://www.apimages.com/Images/beatlesheader.jpg");
        // tester.addUrl("testUrl8", 1, "http://i.dawn.com/thumbnail/2017/01/588b301bb8925.jpg");
        //tester.addUrl("testUrl15", 1, "http://i.dawn.com/medium/2016/12/58597887d8503.jpg");
        //tester.addUrl("testUrl0928", 1, "https://media2.s-nbcnews.com/i/newscms/2016_10/1003286/jennifer-garner-today-160307-tease_2da3043dfc52438cd482c618b36a22a3.jpg");
        //tester.testAddGroup("Black List");
        //tester.testAddDetailToGroup(4);
        //tester.getGroupInfo(1);
        //tester.testRemoveAttrGroup(4);
        tester.getGroupInfo2(1);
        // tester.getGroupInfo(4);

        // tester.testAddPersonsToGroup(2);
        // tester.testAppendPerson();
        //tester.testAppendFace(42870, "/Users/stevenkan/Downloads/FaceImages/testAppendAPI.jpg");
        //
        // tester.registerPerson("Damien", 1, "/Users/stevenkan/Downloads/test8.jpg");
        // tester.refreshPerson(100);

        //tester.getPersonInfo("2");
        //tester.setLogDetail();
        //tester.addAttribute();
        //tester.getAttributeList();

        //tester.getFaceInfo("37165");
        //tester.getListNextPerson(200, 20);
        //tester.getPersonCount(4);
    }

    public static void importPersons(String directory) {
        System.out.println("Importing from : " + directory);
        ExecutorService threadPool = java.util.concurrent.Executors.newFixedThreadPool(numThreads);
        File[] files = new File(directory).listFiles();
        int numImported = 0;
        for (File file : files) {
            // if (file.isDirectory())
            {
                // System.out.println("Importing person : " + file.getName());
                //BasicTest importer = new BasicTest(null, file);
                //importer.importFaces();
                threadPool.execute(new AutoTest(null, file));
            }
            numImported++;
            if (s_totalQueries > 0 && numImported >= s_totalQueries) {
                break;
            }
        }
        threadPool.shutdown();
    }

    public static void removeNsfwAll_allFinished() {

        ExecutorService threadPool = java.util.concurrent.Executors.newFixedThreadPool(30);
        int featId = 131000;
        //for (int featId=101501; featId<351117; featId++) {
        while (featId < 351117) {
            if (s_outstandingNsfwTasks.get() < 30) {
                s_outstandingNsfwTasks.incrementAndGet();
                AutoTest task = new AutoTest(null, null);
                task.m_nsfwId = featId;
                featId++;
                threadPool.execute(task);
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }

        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(100, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        threadPool = java.util.concurrent.Executors.newFixedThreadPool(30);
        int missedIdIndex = 0;
        int initialNumMissed = AutoTest.s_missedNsfwIds.size();
        while (missedIdIndex < initialNumMissed) {
            if (s_outstandingNsfwTasks.get() < 20) {
                s_outstandingNsfwTasks.incrementAndGet();
                AutoTest task = new AutoTest(null, null);
                task.m_nsfwId = s_missedNsfwIds.get(missedIdIndex).intValue();
                missedIdIndex++;
                threadPool.execute(task);
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
            // this.registerPerson(m_importFile.getName(), 1, file.getAbsolutePath());
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(100, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        System.out.println("all NSFW check and retry finished");
        for (int ind = missedIdIndex; ind < s_missedNsfwIds.size(); ind++) {
            System.out.println("possibly invalid ID, still failing " + s_missedNsfwIds.get(ind).intValue());
        }
        System.out.println("Total NSFW removed " + AutoTest.s_totalNsfwRemoved.get());
    }

    public static void lfwSearchPersons(String directory) {
        ExecutorService threadPool = java.util.concurrent.Executors.newFixedThreadPool(numThreads);
        File[] personDirs = new File(directory).listFiles();
        int numSearched = 0;
        long startTime = System.currentTimeMillis();
        for (File personFile : personDirs) {
            if (personFile.isDirectory()) {
                // System.out.println("Importing person : " + file.getName());
                //BasicTest importer = new BasicTest(null, file);
                //importer.importFaces();
                File[] files = personFile.listFiles();
                int fileIndex = 0;
                for (File file : files) {
                    if (!file.isDirectory()) {
                        fileIndex++;
                        if (fileIndex == 3) {
                            numSearched++;
                            System.out.println("Searching face : " + file.getName());
                            AutoTest task = new AutoTest(null, file);
                            task.m_searchName = personFile.getName();
                            threadPool.execute(task);
                            // this.registerPerson(m_importFile.getName(), 1, file.getAbsolutePath());
                            break;  // only search 2nd face of a person for now
                        }
                    }
                }
            }

            if (numSearched >= s_totalQueries) {
                break;
            }
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(100, TimeUnit.SECONDS);
            long stopTime = System.currentTimeMillis();
            System.out.println("\nElapsed time was " + (stopTime - startTime) + " miliseconds.");
            System.out.println("Top 10 matches: " + s_numTop10Match.get());
            System.out.println("Top 1 matches: " + s_numTop1Match.get());
            System.out.println("Failures: " + s_numFailures.get());
            System.out.println("average response time: " + s_responseTimeTotal.get() / s_totalQueries);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void dirSearchPersons(String directory) {
        ExecutorService threadPool = java.util.concurrent.Executors.newFixedThreadPool(numThreads);
        File[] files = new File(directory).listFiles();
        int numSearched = 0;
        long startTime = System.currentTimeMillis();
        while (true) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    if (file.getName().indexOf(".jpg") == -1
                            && file.getName().indexOf(".jpeg") == -1
                            && file.getName().indexOf(".png") == -1) {
                        // not a graphic file, ignore
                        continue;
                    }
                    numSearched++;
                    System.out.println("Searching face : " + file.getName());
                    AutoTest task = new AutoTest(null, file);
                    task.m_searchName = file.getName();
                    threadPool.execute(task);
                    if (numSearched >= s_totalQueries)
                        break;
                }
            }

            if (numSearched >= s_totalQueries) {
                break;
            }
        }

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(100, TimeUnit.SECONDS);
            long stopTime = System.currentTimeMillis();
            System.out.println("\nElapsed time was " + (stopTime - startTime) + " miliseconds.");
            System.out.println("Top 10 matches: " + s_numTop10Match.get());
            System.out.println("Top 1 matches: " + s_numTop1Match.get());
            System.out.println("Failures: " + s_numFailures.get());
            System.out.println("average response time: " + s_responseTimeTotal.get() / s_totalQueries);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        if (mode == TestMode.Import) {
            importFaces();
        } else if (mode == TestMode.ImportFile) {
            importFile();
        } else if (mode == TestMode.LFWSearch) {
            lfwSearchFace();
        } else if (mode == TestMode.DirSearch) {
            dirSearchFace();
        } else if (mode == TestMode.Search || mode == TestMode.DetectFaces) {
            runSearchOrDetect();
        } else if (mode == TestMode.Verify) {
            runVerify();
        } else if (mode == TestMode.AddUrl) {
            runAddUrl();
        } else if (mode == TestMode.RemoveNsfw) {
            testRemoveNsfw(m_nsfwId);
        }
    }

    public void addLocalImage(String personName, int groupId, String localImageAddress) {
        try {
            BufferedImage img1 = ImageIO.read(new File(localImageAddress));
            Map<String, String> params = new HashMap<String, String>();
            params.put("person-name", personName);
            params.put("group-ids", Integer.toString(groupId));
            params.put("image-data", toBase64Image(img1));
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

    public String createPerson(String personName, String groupId, String localImageAddress) {
        String resp2 = "";
        try {
            BufferedImage img1 = ImageIO.read(new File(localImageAddress));
            Map<String, String> params = new HashMap<String, String>();
            params.put("person-name", personName);
            params.put("group-ids", groupId);
            params.put("image-data", toBase64Image(img1));
            byte[] resp = doPost("/person/create", params);
            System.out.println("RESULT: " + new String(resp, "UTF-8"));
            resp2 = new String(resp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp2;
    }

    public String edit(String personId, String personName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);
        params.put("person-name", personName);
        String resp = "";

        try {
            resp = new String(doPost("/person/edit", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    // Given two face images, compare them and return the similarity.
    public String compareFaceRec(String photo1, String photo2) {
        String resp2 = "";
        BufferedImage img1;
        BufferedImage img2;
        String imgStr1 = "";
        String imgStr2 = "";
        try {
            img1 = ImageIO.read(new File(photo1));
            img2 = ImageIO.read(new File(photo2));
            imgStr1 = toBase64Image(img1);
            imgStr2 = toBase64Image(img2);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("image-data-1", imgStr1);
        params.put("image-data-2", imgStr2);
        try {
            byte[] resp = doPost("/compare", params);
            System.out.println(new String(resp, "UTF-8"));
            resp2 = new String(resp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resp2;


    }

    // Given an image of a face, check if the quality of the face image is good for registration.
    public String checkQualityFaceRec(String photo) {
        String resp2 = "";
        BufferedImage img1;
        String imgStr = "";
        try {
            img1 = ImageIO.read(new File(photo));
            imgStr = toBase64Image(img1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        //String imgStr = toBase64Image(img1);


        Map<String, String> params = new HashMap<String, String>();
        params.put("image-data", imgStr);
        try {
            byte[] resp = doPost("/checkquality", params);
            System.out.println(new String(resp, "UTF-8"));
            resp2 = new String(resp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resp2;


    }

    // Given a face and a group, find similar faces in the group.
    public String searchFaceRec(String groupId, String photo) {
        String resp2 = "";
        BufferedImage img1;
        String imgStr = "";
        try {
            img1 = ImageIO.read(new File(photo));
            imgStr = toBase64Image(img1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        //String imgStr = toBase64Image(img1);


        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        params.put("image-data", imgStr);
        try {
            byte[] resp = doPost("/search", params);
            System.out.println("search response: " + new String(resp, "UTF-8"));
            resp2 = new String(resp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resp2;


    }

    // Given a face and a group, find the most similar person within the group.
    public String recognizeFaceRec(String groupId, String photo) {
        String resp2 = "";
        BufferedImage img1;
        String imgStr = "";
        try {
            img1 = ImageIO.read(new File(photo));
            imgStr = toBase64Image(img1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        //String imgStr = toBase64Image(img1);


        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        params.put("image-data", imgStr);
        try {
            byte[] resp = doPost("/recognize", params);
            System.out.println("search response: " + new String(resp, "UTF-8"));
            resp2 = new String(resp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resp2;


    }

    // Given a person and a face, decide if the face belongs to the person
    public String verifyFaceRec(String personId, String photo) {
        String resp2 = "";
        BufferedImage img1;
        String imgStr = "";
        try {
            img1 = ImageIO.read(new File(photo));
            imgStr = toBase64Image(img1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        //String imgStr = toBase64Image(img1);


        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);
        params.put("image-data", imgStr);
        try {
            byte[] resp = doPost("/verify", params);
            System.out.println("search response: " + new String(resp, "UTF-8"));
            resp2 = new String(resp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resp2;


    }

    //Face list API  List all detected face image tagged with ‘tag’.  return array of images' ID
    public String listFaceDetection(String tag) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tag", tag);
        String resp = "";
        try {
            resp = new String(doPost("/image/list", params));
            System.out.println("RESULT: " + resp);
            return resp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return resp;
        }
    }

    //Face detection API   deletePerson a list of images or tagged images
    public String deleteFaceDetection(int personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("image-ids", Integer.toString(personId));
        String resp = "";
        try {
            resp = new String(doPost("/image/delete", params));
            System.out.println("RESULT: " + resp);
            return resp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return resp;
        }
    }

    public String detectFacesLocalImage(String tag, String photo) {

        String resp2 = "";
        try {
            BufferedImage img1 = ImageIO.read(new File(photo));
            Map<String, String> params = new HashMap<String, String>();
            params.put("tag", tag);
            params.put("image-data", toBase64Image(img1));
            byte[] resp = doPost("/image/detect", params);
            System.out.println("search response: " + new String(resp, "UTF-8"));
            resp2 = new String(resp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resp2;
    }

    //query the properties of a face image
    public String faceInfo(int faceId) {
        String resp = "";
        try {
            resp = new String(doGet("/person/face-info?face-id=" + faceId));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("RESULT: " + resp);
        return resp;
    }
    //query the properties of a face image
    public String faceInfoWithoutTryCatch(int faceId) throws Exception {
        String resp = "";
            resp = new String(doGet("/person/face-info?face-id=" + faceId));
        System.out.println("RESULT: " + resp);
        return resp;
    }

    //query the properties of a person
    public String query(int personId) {
        String resp = "";
        try {
            resp = new String(doGet("/person/query?person-id=" + personId));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("RESULT: " + resp);
        return resp;
    }
    public String queryWithoutTryCatch(int personId) throws Exception {
        String resp = "";
        resp = new String(doGet("/person/query?person-id=" + personId));
        System.out.println("RESULT: " + resp);
        return resp;
    }

    public String appendFace(int personId, String photo) {
        String resp2 = "";
        try {
            BufferedImage img1 = ImageIO.read(new File(photo));
            Map<String, String> params = new HashMap<String, String>();
            params.put("person-id", Integer.toString(personId));
            params.put("image-data", toBase64Image(img1));
            byte[] resp = doPost("/person/append-face", params);
            System.out.println("got response: " + new String(resp, "UTF-8"));
            resp2 = new String(resp, "UTF-8");
            return resp2;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resp2;
        }
    }

    //Find Person Info by name
    public String findByName(String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        String resp = "";
        try {
            resp = new String(doPost("/person/find-by-name", params));
            System.out.println("RESULT: " + resp);
            return resp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resp;

    }

    //remove all faces of a person
    public String clear(int personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", Integer.toString(personId));
        String resp = "";
        try {
            resp = new String(doPost("/person/clear", params));
            System.out.println("RESULT: " + resp);
            return resp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return resp;
        }
    }

    public String removeFace(int personId, int faceId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", Integer.toString(personId));
        params.put("face-ids", Integer.toString(faceId));
        String resp = "";
        try {
            resp = new String(doPost("/person/remove-face", params));
            System.out.println("RESULT: " + resp);
            return resp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return resp;
        }
    }

    public String deletePerson(int personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-ids", Integer.toString(personId));
        String resp = "";
        try {
            resp = new String(doPost("/person/delete", params));
            //String resp = new String(doGet("/person/create?person-name=testadd&server-image-file="+URLEncoder.encode(imageFile)));
            System.out.println("RESULT: " + resp);
            return resp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return resp;
        }
    }

    public String addUrl(String personName, int groupId, String imageUrl) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-name", personName);
        params.put("group-ids", Integer.toString(groupId));
        params.put("image-url", imageUrl);
        params.put("checkdupe", "true");
        params.put("checkporn", "true");
        String resp = "";

        try {
            resp = new String(doPost("/person/create", params));
            //String resp = new String(doGet("/person/create?person-name=testadd&server-image-file="+URLEncoder.encode(imageFile)));
            System.out.println("RESULT: " + resp);
            return resp;
        } catch (Exception e) {
            System.out.println("Error registerPerson: " + e.getMessage());
            e.printStackTrace();
        } finally {
            return resp;
        }
    }

    public void getFaceInfo(String faceId) {
        String resp = "";
        try {
            resp = new String(doGet("/person/face-info?face-id=" + faceId));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("RESULT: " + resp);
    }

    public void getAttributeList() {
        String resp = "";
        try {
            resp = new String(doGet("/attribute/list"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("RESULT: " + resp);
    }

    public void setLogDetail() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("log-id", "20");
        params.put("key", "access");
        params.put("value", "1");

        try {
            String resp;
            resp = new String(doPost("/log/set-attr", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getPersonInfo(String personId) {
        String resp = "";
        try {
            resp = new String(doGet("/person/query?person-id=" + personId));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("RESULT: " + resp);
    }

    public void getListNextPerson(long personId, int count) {
        String req = "/person/list-next?person-id=" + personId + "&count=" + count;
        submitGetRequest(req, "getListNextPerson");
    }

    public void getPersonCount(long groupId) {
        String req = "/person/person-count?group-id=" + groupId;
        submitGetRequest(req, "getPersonCount");
    }

    public void getGroupInfo(int groupId) {
        String resp = "";
        try {
            resp = new String(doGet("/group/query?group-id=" + groupId));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("RESULT: " + resp);
    }

    public void getGroupInfo2(int groupId) {
        String resp = "";
        try {
            resp = new String(doGet("/group/get-info?group-id=" + groupId));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("RESULT: " + resp);
    }

    public void setNotificationUrl(String url) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("url", url);

        byte[] resp;
        try {
            resp = doPost("/manage/set/subscriber", params);
            System.out.println("search response: " + new String(resp, "UTF-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String addDetailPersonManagement(String personId, String key, String value) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);
        params.put("key", key);
        params.put("value", value);
        String resp = "";

        try {
            resp = new String(doPost("/person/set-attr", params));
            System.out.println("RESULT: " + resp);

//            resp = new String(doGet("/person/query?person-id=" + personId));
//            System.out.println("RESULT: " + resp);


        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public void addPersonDetail(String personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);
        params.put("key", "schedule");
        params.put("value", "5-8 1,5-7 *");

        try {
            String resp;
            resp = new String(doPost("/person/set-attr", params));
            System.out.println("RESULT: " + resp);

            params = new HashMap<String, String>();
            params.put("person-id", personId);
            params.put("key", "age");
            params.put("value", "40");
            resp = new String(doPost("/person/set-attr", params));
            System.out.println("RESULT: " + resp);

            resp = new String(doGet("/person/query?person-id=" + personId));
            System.out.println("RESULT: " + resp);

            params = new HashMap<String, String>();
            params.put("person-id", personId);
            params.put("key", "age");
            params.put("value", "45");
            resp = new String(doPost("/person/set-attr", params));
            System.out.println("RESULT: " + resp);

            resp = new String(doGet("/person/query?person-id=" + personId));
            System.out.println("RESULT: " + resp);

            /*params = new HashMap<String, String>();
            params.put("person-id", personId);
            params.put("key", "schedule");

            resp = new String(doPost("/person/remove-attr", params));
            System.out.println("RESULT: " + resp);*/

            resp = new String(doGet("/person/query?person-id=" + personId));
            System.out.println("RESULT: " + resp);

        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addPersonDetail2(String personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);

        try {
            String resp;
            resp = new String(doPost("/person/enable", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addPersonDetail3(String personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);
        params.put("key", "schedule");
        // time day-of-month month day-of-week (optional)year
        params.put("value", "16:30-20 1-8 * 0,3-6 *");

        try {
            String resp;
            resp = new String(doPost("/person/set-attr", params));
            System.out.println("RESULT: " + resp);

            params = new HashMap<String, String>();
            params.put("person-id", personId);
            params.put("key", "expiration");
            params.put("value", "2018-04-30");
            resp = new String(doPost("/person/set-attr", params));
            System.out.println("RESULT: " + resp);

            /*params = new HashMap<String, String>();
            params.put("person-id", personId);
            resp = new String(doPost("/person/disable", params));
            System.out.println("RESULT: " + resp);*/

            resp = new String(doGet("/person/query?person-id=" + personId));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String disablePerson(String personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);
        String resp = "";
        try {

            resp = new String(doPost("/person/disable", params));
            System.out.println("RESULT: " + resp);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String enablePerson(String personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);
        String resp = "";
        try {

            resp = new String(doPost("/person/enable", params));
            System.out.println("RESULT: " + resp);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String removeDetail(String personId, String key) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);
        params.put("key", key);
        String resp = "";

        try {

            resp = new String(doPost("/person/remove-attr", params));
            System.out.println("RESULT: " + resp);


        } catch (Exception e) {
            System.out.println("Error remove attribute: " + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public void removePersonDetail(String personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", personId);
        params.put("key", "expiration");

        try {
            String resp;
            resp = new String(doPost("/person/remove-attr", params));
            System.out.println("RESULT: " + resp);

            resp = new String(doGet("/person/query?person-id=" + personId));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error remove attribute: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String createAttribute(String key, String type, String category) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", key);
        params.put("type", type);
        params.put("category", category);
        String resp = "";

        try {
            resp = new String(doPost("/attribute/create", params));
            System.out.println("RESULT: " + resp);


        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public void addAttribute() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", "DOB");
        params.put("type", "TimeType");
        params.put("category", "PersonCat");

        try {
            String resp = new String(doPost("/attribute/create", params));
            System.out.println("RESULT: " + resp);

            params = new HashMap<String, String>();
            params.put("key", "Country");
            params.put("type", "CharType");
            params.put("category", "PersonCat");

            resp = new String(doPost("/attribute/create", params));
            System.out.println("RESULT: " + resp);

            params = new HashMap<String, String>();
            params.put("key", "Height");
            params.put("type", "CharType");
            params.put("category", "PersonCat");

            resp = new String(doPost("/attribute/create", params));
            System.out.println("RESULT: " + resp);

            params = new HashMap<String, String>();
            params.put("key", "Note");
            params.put("type", "CharType");
            params.put("category", "FaceCat");

            resp = new String(doPost("/attribute/create", params));
            System.out.println("RESULT: " + resp);

            params = new HashMap<String, String>();
            params.put("key", "Event");
            params.put("type", "CharType");
            params.put("category", "FaceCat");

            resp = new String(doPost("/attribute/create", params));
            System.out.println("RESULT: " + resp);

            params = new HashMap<String, String>();
            params.put("key", "Region");
            params.put("type", "CharType");
            params.put("category", "GroupCat");

            resp = new String(doPost("/attribute/create", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String deleteAttribute(String key) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", key);
        String resp = "";

        try {
            resp = new String(doPost("/attribute/delete", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error deleting attribute: " + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public void deleteAttribute() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", "location");

        try {
            String resp = new String(doPost("/attribute/delete", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error deleting attribute: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public String listAttribute() {
        Map<String, String> params = new HashMap<String, String>();
        //params.put("key", "location");
        String resp = "";

        try {
            resp = new String(doGet("/attribute/list"));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String listPersonWithinGroup(String groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        String resp = "";

        try {
            resp = new String(doPost("/person/list", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String getCount(String groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        String resp = "";

        try {
            resp = new String(doPost("/person/person-count", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public void getFaceFeature(int id) {
        try {
            byte[] buffer = doGet("/person/face-feature/" + id + "");
            float[] fArr = new float[buffer.length / 4];  // 4 bytes per float
            System.out.println("feature float:");
            for (int i = 0; i < fArr.length; i++) {
                fArr[i] = ByteBuffer.wrap(buffer, i * 4, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                System.out.println(fArr[i]);
            }
            // System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error getFaceFeature: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //region Group API
    public String createGroup(String groupName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-name", groupName);
        String resp = "";

        try {
            resp = new String(doPost("/group/create", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String deleteGroup(String groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-ids", groupId);
        String resp = "";

        try {
            resp = new String(doPost("/group/delete", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String appendPersonToGroup(String groupId, String personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        params.put("person-ids", personId);
        String resp = "";

        try {
            resp = new String(doPost("/group/append-person", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String removePersonFromGroup(String groupId, String personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        params.put("person-ids", personId);
        String resp = "";

        try {
            resp = new String(doPost("/group/remove-person", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String clearPeopleFromGroup(String groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        String resp = "";

        try {
            resp = new String(doPost("/group/clear", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String editGroup(String groupId, String groupName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        params.put("group-name", groupName);
        String resp = "";

        try {
            resp = new String(doPost("/group/edit", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String queryGroup(String groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        String resp = "";

        try {
            resp = new String(doPost("/group/query", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String queryGroupWithoutTryCatch(String groupId) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        String resp = "";
        resp = new String(doPost("/group/query", params));
        System.out.println("RESULT: " + resp);
        return resp;
    }

    public String getGroupInfo(String groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        String resp = "";

        try {
            resp = new String(doPost("/group/get-info", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String listGroup() {
        Map<String, String> params = new HashMap<String, String>();

        String resp = "";

        try {
            resp = new String(doGet("/group/list"));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String addDetailGroup(String groupId, String key, String value) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        params.put("key", key);
        params.put("value", value);
        String resp = "";

        try {
            resp = new String(doPost("/group/set-attr", params));
            System.out.println("RESULT: " + resp);


        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String removeDetailGroup(String groupId, String key) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", groupId);
        params.put("key", key);
        String resp = "";

        try {
            resp = new String(doPost("/group/remove-attr", params));
            System.out.println("RESULT: " + resp);


        } catch (Exception e) {
            System.out.println("Error remove attribute: " + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }
    //endregion

    public String retrieveLog() {
        String resp = "";
        try {
            resp = new String(doGet("/log/retrieve"));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }

    public String setAttributeLog(String logId, String key, String value) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("log-id", logId);
        params.put("key", key);
        params.put("value", value);
        String resp = "";

        try {
            resp = new String(doPost("/log/set-attr", params));
            System.out.println("RESULT: " + resp);


        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
        }
        return resp;
    }


    public void listTop5Matches(String directory, String outputResult) {
        System.out.println("Matching from : " + directory);
        File[] files = new File(directory).listFiles();
        this.setServerBase("localhost:8080");
        FileWriter writer = null;
        BufferedWriter bw = null;
        try {
            writer = new FileWriter(outputResult);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("unable to write to " + outputResult);
        }
        bw = new BufferedWriter(writer);

        for (File file : files) {
            BufferedImage img;
            try {
                bw.write(file.getName());
                bw.write(", ");
                img = ImageIO.read(file);
                if (img == null) {
                    continue;
                }
                String imgStr = toBase64Image(img);
                JSONArray result = search(1, imgStr, 5, 0.5f, 1);
                for (int iResp = 0; result != null && iResp < result.length(); iResp++) {
                    JSONObject candidate = result.getJSONObject(iResp);
                    bw.write(candidate.getString("name") + ", ");
                }
                bw.write("\n");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                continue;
            }
        }
    }

    public void importFile() {
        System.out.println("Importing : " + m_importFile.getName());
        if (!m_importFile.isDirectory()) {
            System.out.println("Importing face : " + m_importFile.getName());
            this.registerPerson(m_importFile.getName(), 1, m_importFile.getAbsolutePath());
        }
    }

    public void importFaces() {
        System.out.println("Importing : " + m_importFile.getName());
        File[] files = m_importFile.listFiles();
        int faceIndex = 0;
        for (File file : files) {
            if (!file.isDirectory()) {
                faceIndex++;
                if (faceIndex == 1) {
                    System.out.println("Importing face : " + file.getName());
                    this.registerPerson(m_importFile.getName(), 1, file.getAbsolutePath());
                    break;  // only import 1 face per person for now
                }
            }
        }
    }

    public void lfwSearchFace() {
        BufferedImage img;
        try {
            img = ImageIO.read(m_importFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        String imgStr = toBase64Image(img);
        long startTime = System.currentTimeMillis();
        JSONArray result = search(1, imgStr, 10, 0.35f, 1);
        s_responseTimeTotal.addAndGet((int) (System.currentTimeMillis() - startTime));
        for (int iResp = 0; result != null && iResp < result.length(); iResp++) {
            JSONObject candidate = null;
            try {
                candidate = result.getJSONObject(iResp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (candidate.getString("name").equals(m_searchName)) {
                    s_numTop10Match.incrementAndGet();
                    if (iResp == 0) {
                        s_numTop1Match.incrementAndGet();
                    }
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void dirSearchFace() {
        BufferedImage img;
        try {
            img = ImageIO.read(m_importFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        String imgStr = toBase64Image(img);
        long startTime = System.currentTimeMillis();
        JSONArray result = search(1, imgStr, 10, 0.35f, 1);
        s_responseTimeTotal.addAndGet((int) (System.currentTimeMillis() - startTime));
    }

    protected void runSearchOrDetect() {
        String fileName = "/Users/stevenkan/development/debug/photo4.jpg";
        BufferedImage img1;
        try {
            // img1 = ImageIO.read(getClass().getResourceAsStream("/" + m_searchImage));
            img1 = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

        System.out.println("Searching for " + fileName);
        long startTime = System.currentTimeMillis();
        String imgStr = toBase64Image(img1);
        int localSearches = 0;
        while (s_numOfQueries.addAndGet(1) <= s_totalQueries) {
            localSearches++;
            if (mode == TestMode.Search) {
                search(1, imgStr, 10, 0.5f, 1);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Searched " + localSearches + "/" + s_totalQueries + " in " + (endTime - startTime) + " milliseconds");
    }

    protected void runAddUrl() {
        while (s_numOfQueries.addAndGet(1) <= s_totalQueries) {
            addUrl("testPornFilter", 1, "http://yourxporn.net/wp-content/uploads/2017/02/398711-250x354.jpg");
        }

    }

    protected void runVerify() {
        BufferedImage img1;
        try {
            img1 = ImageIO.read(new File("/Users/stevenkan/development/temp/photo4.jpg"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        String imgStr = toBase64Image(img1);

        while (s_numOfQueries.addAndGet(1) <= s_totalQueries) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("person-id", "414");
            params.put("image-data", imgStr);
            try {
                byte[] resp = doPost("/verify", params);
                System.out.println("search response: " + new String(resp, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void compare(String file1, String file2) {
        BufferedImage img1;
        BufferedImage img2;
        try {
            img1 = ImageIO.read(new File(file1));
            img2 = ImageIO.read(new File(file2));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        String imgStr1 = toBase64Image(img1);
        String imgStr2 = toBase64Image(img2);
        Map<String, String> params = new HashMap<String, String>();
        params.put("image-data-1", imgStr1);
        params.put("image-data-2", imgStr2);

        try {
            String resp = new String(doPost("/compare", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error compare: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void checkForQuality(String imageUrl, String faceSize, String quality) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("image-url", imageUrl);
        params.put("min-face-size", faceSize);
        params.put("min-quality", quality);

        try {
            String resp = new String(doPost("/checkquality", params));
            //String resp = new String(doGet("/person/create?person-name=testadd&server-image-file="+URLEncoder.encode(imageFile)));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error checkForDup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void checkForDupe(String imageUrl) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("image-url", imageUrl);

        try {
            String resp = new String(doPost("/checkdupe", params));
            //String resp = new String(doGet("/person/create?person-name=testadd&server-image-file="+URLEncoder.encode(imageFile)));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error checkForDupe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void testRemoveNsfw(int faceId) {
        Map<String, String> params = new HashMap<String, String>();

        try {
            String resp = new String(doPost("/person/remove-if-nsfw/" + faceId, params));
            //String resp = new String(doGet("/person/create?person-name=testadd&server-image-file="+URLEncoder.encode(imageFile)));
            System.out.println("RESULT: " + resp);
            if (resp.contains("NSFW")) {
                AutoTest.s_totalNsfwRemoved.incrementAndGet();
            }
        } catch (Exception e) {
            System.out.println("Error testRemoveNsfw: ID " + this.m_nsfwId);
            System.out.println(e.getMessage());
            // e.printStackTrace();
            AutoTest.s_missedNsfwIds.add(new Integer(this.m_nsfwId));  // to retry
        } finally {
            AutoTest.s_outstandingNsfwTasks.decrementAndGet();
        }
    }

    public void testAddGroup(String groupName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-name", groupName);
        try {
            String resp = new String(doPost("/group/create", params));
            //String resp = new String(doGet("/person/create?person-name=testadd&server-image-file="+URLEncoder.encode(imageFile)));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error testAddGroup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void testAddPersonsToGroup(int groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("group-id", Integer.toString(groupId));
        params.put("person-ids", "10,501,1023,2100,3012,3640,4021,4036,4653,5021,5198");
        try {
            String resp = new String(doPost("/group/append-person", params));
            //String resp = new String(doGet("/person/create?person-name=testadd&server-image-file="+URLEncoder.encode(imageFile)));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error testAddGroup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void testAddDetailToGroup(int groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", "Region");
        params.put("group-id", Integer.toString(groupId));
        params.put("value", "NorthEast");
        try {
            String resp = new String(doPost("/group/set-attr", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error testAddDetailToGroup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void testRemoveAttrGroup(int groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", "Region");
        params.put("group-id", Integer.toString(groupId));
        try {
            String resp = new String(doPost("/group/remove-attr", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error testRemoveAttrGroup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void testCompare(String imageFile1, String imageFile2) {
        Map<String, String> params = new HashMap<String, String>();
        BufferedImage img1, img2;
        try {
            img1 = ImageIO.read(new File(imageFile1));
            img2 = ImageIO.read(new File(imageFile2));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        params.put("image-data-1", toBase64Image(img1));
        params.put("image-data-2", toBase64Image(img2));

        try {
            String resp = new String(doPost("/compare", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error compare: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void registerPerson(String userName, int groupId, String imageFile) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-name", userName);
        params.put("group-ids", Integer.toString(groupId));
        if (getServerBase().startsWith("localhost")) {
            // file is local
            params.put("server-image-file", imageFile);
        } else {
            // file is remote
            BufferedImage img;
            try {
                img = ImageIO.read(new File(imageFile));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
            if (img == null) {
                System.out.println("Unable to read image from " + imageFile);
                return;
            }
            params.put("image-data", toBase64Image(img));
        }

        try {
            String resp = new String(doPost("/person/create", params));
            //String resp = new String(doGet("/person/create?person-name=testadd&server-image-file="+URLEncoder.encode(imageFile)));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error registerPerson: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refreshPerson(int personId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("person-id", Integer.toString(personId));
        try {
            String resp = new String(doPost("/person/refresh", params));
            System.out.println("RESULT: " + resp);
        } catch (Exception e) {
            System.out.println("Error registerPerson: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void detectFacesUrl(String imgUrl) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tag", "detectTest");
        params.put("image-url", imgUrl);
        try {
            byte[] resp = doPost("/image/detect", params);
            System.out.println("search response: " + new String(resp, "UTF-8"));
            //ObjectMapper mapper = new ObjectMapper();
            //List<SearchingResultItem> result = mapper.readValue(resp, new TypeReference<List<SearchingResultItem>>() {
            //});
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void submitGetRequest(String req, String method) {
        try {
            System.out.println("1. Method/Params: " + method);
            System.out.println("2. Request URL: " + req);
            String resp = new String(doGet(req));
            System.out.println("3. RESULT: " + resp);
        } catch (Exception e) {
            System.err.println("Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    enum TestMode {
        None,   // for misc tests
        Import, // if the file is local to the server, send the name to server;
        // if the file is remote, send the content to server.
        ImportFile,  // flat directory structure import
        LFWSearch,
        DirSearch,  // search for image files in directory
        Search,
        DetectFaces,
        Verify,
        AddUrl,
        RemoveNsfw,
        Compare,
        Top5Match
    }


}