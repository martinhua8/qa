package com.camvi.autotest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;


public class ReadCSVData {
    
	private static String COMMA_DELIMITER = ",";
	private static String COMMENT_CHAR = "#";
	
    static LinkedList<AddLocalImageDataStruct> getCSVInfoWithoutFistRow(String filePath) throws IOException, InvalidFormatException {
    	
    	//List<List<String>> records = new ArrayList<>();
        LinkedList<AddLocalImageDataStruct> list = new LinkedList<>();

    	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
    	    String line;
    	    while ((line = br.readLine()) != null) {
    	    	String line1 = line.trim();
    	    	if(line1.isEmpty()) { // Ignore blank lines
    	    		//System.out.println("Found blank line:");
    	    		continue;
    	    	}
    	    	if(line1.startsWith(COMMENT_CHAR)) { // Ignore comments
    	    		//System.out.println("Found comment line:" + line1);
    	    		continue;
    	    	}
    	        String[] values = line.split(COMMA_DELIMITER);
    	        if(values == null) {
    	        	continue;
    	        }
                AddLocalImageDataStruct dataStruct = new AddLocalImageDataStruct();
                dataStruct.setPersonName(values[0]);
                dataStruct.setGroupId(Integer.parseInt(values[1]));
                dataStruct.setLocalImageAddress(values[2]);

                
                list.add(dataStruct);
    	    }
    	}
    	
    	return list;
    	
    }

    public static LinkedList<TestDataStruct> getStringListFromCSVWithoutFistRow(String filePath) throws IOException, InvalidFormatException {
        LinkedList<TestDataStruct> list = new LinkedList<>();
        
    	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
    	    String line;
    	    while ((line = br.readLine()) != null) {
    	    	String line1 = line.trim();
    	    	if(line1.isEmpty()) { // Ignore blank lines
    	    		//System.out.println("Found blank line:");
    	    		continue;
    	    	}
    	    	if(line1.startsWith(COMMENT_CHAR)) { // Ignore comments    	    		
    	    		//System.out.println("Found comment line:" + line1);
    	    		continue;
    	    	}
    	        String[] values = line.split(COMMA_DELIMITER);
    	        if(values == null) {
    	        	continue;
    	        }
    	        
                
                list.add(new TestDataStruct(values));
    	    }
    	}
    	
    	return list;

    }

}
