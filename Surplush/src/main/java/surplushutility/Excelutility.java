package surplushutility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Excelutility {

    static FileInputStream fis;
    FileOutputStream fos;

    public String readDataFromExcel(String sheetName, int rowNum, int cellNum) throws Exception {
        fis = new FileInputStream("./src/test/resources/Surplush_TestData.xlsx");
        Workbook wb = WorkbookFactory.create(fis);
        String data = wb.getSheet(sheetName).getRow(rowNum).getCell(cellNum).toString();
        wb.close();
        return data;
    }

    public int getRowCount(String sheetName) throws Exception {
        fis = new FileInputStream("./src/test/resources/Surplush_TestData.xlsx");
        Workbook wb = WorkbookFactory.create(fis);
        int count = wb.getSheet(sheetName).getPhysicalNumberOfRows();
        wb.close();
        return count;
    }

    public int getCellCount(String sheetName) throws Exception {
        fis = new FileInputStream("./src/test/resources/Surplush_TestData.xlsx");
        Workbook wb = WorkbookFactory.create(fis);
        int count = wb.getSheet(sheetName).getRow(0).getPhysicalNumberOfCells();
        wb.close();
        return count;
    }

    public void writeDataInExistingCell(String sheetName, int rowNum, int cellNum, String value) throws Exception {
        fis = new FileInputStream("./src/test/resources/Surplush_TestData.xlsx");
        Workbook wb = WorkbookFactory.create(fis);
        wb.getSheet(sheetName).getRow(rowNum).getCell(cellNum).setCellValue(value);
        fos = new FileOutputStream("./src/test/resources/Surplush_TestData.xlsx");
        wb.write(fos);
        wb.close();
    }

    public void writeDataInNewCell(String sheetName, int rowNum, int cellNum, String value) throws Exception {
        fis = new FileInputStream("./src/test/resources/Surplush_TestData.xlsx");
        Workbook wb = WorkbookFactory.create(fis);
        wb.getSheet(sheetName).getRow(rowNum).createCell(cellNum).setCellValue(value);
        fos = new FileOutputStream("./src/test/resources/Surplush_TestData.xlsx");
        wb.write(fos);
        wb.close();
    }

    // FIXED for Java 1.8 - No var
    public static String getDataByTC(String tcID, String columnName) throws Exception {
        fis = new FileInputStream("./src/test/resources/Surplush_TestData.xlsx");
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sh = wb.getSheet("Test Data");
        
        int colIndex = -1;
        Row header = sh.getRow(0);
        for(int i=0; i<header.getPhysicalNumberOfCells(); i++) {
            if(header.getCell(i).getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                colIndex = i;
                break;
            }
        }
        
        for(int r=1; r<sh.getPhysicalNumberOfRows(); r++) {
            Row row = sh.getRow(r);
            if(row == null) continue;
            Cell firstCell = row.getCell(0);
            if(firstCell == null) continue;
            if(firstCell.getStringCellValue().trim().equalsIgnoreCase(tcID)) {
                Cell dataCell = row.getCell(colIndex);
                String data = "";
                if(dataCell != null) {
                    data = dataCell.getStringCellValue();
                }
                wb.close();
                return data;
            }
        }
        wb.close();
        return "";
    }
}