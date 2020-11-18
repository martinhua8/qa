package com.camvi.autotest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class GetExccelInfo {
	
    static void getExcelInfo_XXXX(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        // Iterating over all the sheets in the workbook

        workbook.forEach(sheet -> {
            System.out.println("=> " + sheet.getSheetName());
        });

        /*
           ==================================================================
           Iterating over all the rows and columns in a Sheet (Multiple ways)
           ==================================================================
        */

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        // 1. You can obtain a rowIterator and columnIterator and iterate over them
        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            }
            System.out.println();
        }

        // Closing the workbook
        workbook.close();
    }


    public static LinkedList<String> getNameFromExcel_XXXX(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        LinkedList list = new LinkedList<String>();


        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            Cell cell = row.getCell(0);
            list.add(dataFormatter.formatCellValue(cell));
        }

        // Closing the workbook
        workbook.close();

        return list;
    }

    static LinkedList<AddLocalImageDataStruct> getExcelInfoWithoutFistRow_XXXX(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        // Iterating over all the sheets in the workbook
        System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
        workbook.forEach(sheet -> {
            System.out.println("=> " + sheet.getSheetName());
        });


        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        LinkedList list = new LinkedList<AddLocalImageDataStruct>();

        //ignore the first row with titles
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            AddLocalImageDataStruct dataStruct = new AddLocalImageDataStruct();
            for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
                Cell cell = row.getCell(cellnum);
                if (cellnum == 0) {
                    dataStruct.setPersonName(dataFormatter.formatCellValue(cell));

                }
                if (cellnum == 1) {
                    dataStruct.setGroupId(Integer.parseInt(dataFormatter.formatCellValue(cell)));

                }
                if (cellnum == 2) {
                    dataStruct.setLocalImageAddress(dataFormatter.formatCellValue(cell));
                }

            }
            list.add(dataStruct);


        }

        // Closing the workbook
        workbook.close();

        return list;
    }


    public static LinkedList<TestDataStruct> getStringListFromExcelWithoutFistRow_XXXX(String filePath) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        LinkedList<TestDataStruct> list = new LinkedList<>();

        //ignore the first row with titles
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            LinkedList<String> listRow = new  LinkedList<>();

            for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
                Cell cell = row.getCell(cellnum);
                listRow.add(dataFormatter.formatCellValue(cell));
            }
            Object[] objectArray = listRow.toArray();
            String[] stringArray = Arrays.copyOf(objectArray, objectArray.length, String[].class);
            list.add(new TestDataStruct(stringArray));
        }

        // Closing the workbook
        workbook.close();

        return list;
    }
}


class AddLocalImageDataStruct {
    private String personName;
    private int groupId;
    private String localImageAddress;

    public AddLocalImageDataStruct() {

    }

    public AddLocalImageDataStruct(String personName, int groupId, String localImageAddress) {
        this.personName = personName;
        this.groupId = groupId;
        this.localImageAddress = localImageAddress;
    }


    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getLocalImageAddress() {
        return localImageAddress;
    }

    public void setLocalImageAddress(String localImageAddress) {
        this.localImageAddress = localImageAddress;
    }
}

class NameDataStruct {
    private String name;

    public NameDataStruct() {
    }

    public NameDataStruct(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
