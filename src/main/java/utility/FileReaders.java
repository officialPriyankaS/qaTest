package utility;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class FileReaders {
    public static final String getDataFromPropertiesFile(final String fileName, final String key){
        try{
            Properties prop= new Properties();
            String propFile = fileName +".properties";
            InputStream is= FileReaders.class.getClassLoader().getResourceAsStream(propFile);
            prop.load(is);
            String data=prop.getProperty(key);
            is.close();
            return data;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    public static void getDataFromExcel(String filePath,String fileName,String sheetName){
        try{
            FileInputStream excelDataFile = new FileInputStream(new File(filePath+"\\"+fileName));
            XSSFWorkbook wb =null;
            String fileExtension = fileName.substring((fileName.indexOf(".")));
            if(fileExtension.equals(".xlsx")){
                wb = new XSSFWorkbook(excelDataFile);
            }else if(fileExtension.equals(".xls")){
                wb=new XSSFWorkbook(excelDataFile);
            }
            Sheet sh= wb.getSheet(sheetName);
            int rowCount=sh.getLastRowNum()-sh.getFirstRowNum();
            for(int i=0;i<rowCount;i++){
                Row row= sh.getRow(i);
                for(int j=0;j<row.getLastCellNum();j++){
                    System.out.println(row.getCell(j).getStringCellValue());
                }
                excelDataFile.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
