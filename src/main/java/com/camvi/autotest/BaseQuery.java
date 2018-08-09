package com.camvi.autotest;

import org.json.JSONArray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class BaseQuery {
    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';
    private static String s_base;
    private static String s_authToken;

    public static String getServerBase() {
        return s_base;
    }

    public static void setServerBase(String base) {
        s_base = base;
    }

    public static boolean login() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "admin");
        params.put("password", Base64.getEncoder().encodeToString(new String("admin").getBytes()));

        try {
            String resp;
            resp = new String(doPost("/user/login", params));
            System.out.println("RESULT: " + resp);
            s_authToken = resp;
        } catch (Exception e) {
            System.out.println("Error create attribute: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static protected byte[] doPost(String path, Map<String, String> parameters) throws Exception {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        String serverBase = getServerBase();
        if (serverBase == null) {
            throw new Exception("Server's URL base is not set!");
        }

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(serverBase + path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Authorization", s_authToken);
            String postData = createQueryString(parameters);
            urlConnection.setFixedLengthStreamingMode(postData.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postData);
            out.close();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                byte[] data = getResponseData(urlConnection, false);
                System.out.println(new String(data, "UTF-8"));
                throw new Exception("Access failure");
            }
            return getResponseData(urlConnection, false);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception("Bad URL: " + serverBase + path);
        } catch (IOException e) {
            // e.printStackTrace();
            // throw new Exception("Access remote service error: " + e.getMessage());
            System.out.println("server returned status code " + urlConnection.getResponseCode());
            return getResponseData(urlConnection, true);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    static private byte[] getResponseData(HttpURLConnection conn, boolean errorStream) throws IOException {
        InputStream inputStream = null;
        if (errorStream) {
            inputStream = new BufferedInputStream(conn.getErrorStream());
        } else {
            inputStream = new BufferedInputStream(conn.getInputStream());
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];
        int n;
        while ((n = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, n);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    static private String createQueryString(Map<String, String> parameters) {
        StringBuilder queryString = new StringBuilder();
        if (parameters != null) {
            boolean first = true;
            for (String name : parameters.keySet()) {
                if (!first) {
                    queryString.append(PARAMETER_DELIMITER);
                }
                try {
                    queryString.append(name).append(PARAMETER_EQUALS_CHAR).append(URLEncoder.encode(parameters.get(name), "UTF-8"));
                    if (first) {
                        first = false;
                    }
                } catch (UnsupportedEncodingException e) {
                    System.out.println("Encode http request param [" + name + "] error: " + e.getMessage());
                }
            }
        }
        return queryString.toString();
    }

    static byte[] getImageFromURL(String imageURL) {
        byte[] image = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        if (imageURL != null && imageURL.length() > 0) {
            try {
                URL url = new URL(imageURL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(1000);
                conn.setReadTimeout(2000);
                is = (new URL(imageURL)).openStream();
                image = readFully(is);
                // image = ImageIO.read(new URL(imageURL));
            } catch (SocketTimeoutException ex) {
                return null;
            } catch (IOException e) {
                try {
                    // read everything off the error stream to prevent leaking CLOSE_WAIT sockets
                    InputStream es = ((HttpURLConnection) conn).getErrorStream();
                    byte[] errorBuf = new byte[128];
                    // read the response body
                    if (es != null) {
                        while (es.read(errorBuf) > 0) {
                        }
                        // close the errorstream
                        es.close();
                    }
                } catch (IOException ex2) {
                }
                return null;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        return image;
    }

    public static byte[] readFully(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    protected byte[] doGet(String path) throws Exception {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        String serverBase = getServerBase();
        if (serverBase == null) {
            throw new Exception("Server's URL base is not set!");
        }

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(serverBase + path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", s_authToken);
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Search error: " + statusCode);
            }
            return getResponseData(urlConnection, false);
        } catch (MalformedURLException e) {
            System.out.println("Bad URL [" + serverBase + path + "]: " + e.getMessage());
            throw new Exception("Bad URL: " + serverBase + path);
        } catch (IOException e) {
            System.out.println("Access remote service error: " + e.getMessage());
            throw new Exception("Access remote service error: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    protected String toBase64Image(BufferedImage image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(out.toByteArray());
    }

    protected JSONArray search(int groupId, String imgStr, int tops, float threshold, int level) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app-id", "test");
        params.put("app-key", "test");
        params.put("group-id", Integer.toString(groupId));
        params.put("image-data", imgStr);
        params.put("tops", Integer.toString(tops));
        params.put("threshold", Float.toString(threshold));
        params.put("level", Integer.toString(level));
        params.put("fast", "false");
        try {
            byte[] resp = doPost("/search", params);
            String respStr = new String(resp, "UTF-8");
            System.out.println("search response: " + respStr);
            JSONArray mapper = new JSONArray();
            // System.out.println(mapper.toString());
            //List<SearchingResultItem> result = mapper.readValue(resp, new TypeReference<List<SearchingResultItem>>() {
            //});
            return mapper;
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static class ServerException extends Throwable {
        private static final long serialVersionUID = 4017805196493169996L;

        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}