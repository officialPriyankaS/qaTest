package database;

import javax.xml.bind.DatatypeConverter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static utility.FileReaders.getDataFromPropertiesFile;

public class database {
    public void Extract(String squery) {
        try {
            String databaseConnectionURL = getDataFromPropertiesFile("databaseConfig","database");
            String userid = getDataFromPropertiesFile("databaseConfig","userid");
            String pwd = getDataFromPropertiesFile("databaseConfig","pwd");
            String switchEnv = getDataFromPropertiesFile("databaseConfig","env");
            System.out.println(
                    " database -- "+databaseConnectionURL+
                            " user -- "+userid +
                            " pwd -- "+pwd+
                            " env -- "+switchEnv);
            byte[] decodeByte= DatatypeConverter.parseBase64Binary(userid);
            userid = new String(decodeByte);
            decodeByte= DatatypeConverter.parseBase64Binary(pwd);
            pwd = new String(decodeByte);

            String url=databaseConnectionURL;
            try{
                Class.forName("com.ibm.db2.jcc.DB2Driver");
                Connection con= DriverManager.getConnection(databaseConnectionURL,userid,pwd);
                Statement stmt =con.createStatement();
//                String squery="SELECT * from table";
                ResultSet rs=stmt.executeQuery(squery);
                String response ="";
                while(rs.next()){
                    response= rs.getString(1);
                }
                System.out.println(response);
                rs.close();
                stmt.close();
                con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
