package test.dao;

import io.CauHinhData;
import io.ItemData;
import model.CauHinh;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Item;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CauHinhDataTest {
    void insert(String data, String timestart, int type){
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    List<Item> items = new ArrayList<>();
                    String[] dat = data.substring(2, data.length() - 2).split("\\},\\{");
                    for (String d:dat){
                        String[] tmp = d.split(",");
                        items.add(
                                new Item(
                                        Integer.parseInt(tmp[1].split(":")[1]),
                                        Float.parseFloat(tmp[0].split(":")[1]))
                        );
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date parsedDate = dateFormat.parse(timestart);
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    CauHinh cauHinh = new CauHinh(items.toArray(new Item[items.size()]), timestamp, 0, type);
                    CauHinhData.insert(cauHinh);
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        };
        thread.start();
        try {
            thread.join(1000);
            thread.interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    void doTestCheckInsertSuccessfully(String data, String timestart, int type){
        String query = "select * from cauhinh where `data` = '%s' and `timestart` = '%s' and `type` = %s;";
        query = String.format(query, data, timestart, "" + type);
        try {
            assertTrue(DAO.doQuery(query).next());
        } catch (SQLException e) {
            assertTrue(false, e.toString());
        }
    }
    @BeforeAll
    static void setUp() {
//        System.out.close();
        DAO.getConnection();
        DAO.runStatement("set autocommit=0;");
        DAO.runStatement("delete from cauhinh;");
    }

    @AfterAll
    static void tearDown() {
        DAO.rollback();
        DAO.runStatement("set autocommit=1;");
    }
    @Test
    @Order(1)
    void testTableExists() {
        ResultSet rs = DAO.doQuery("SELECT TABLE_NAME FROM information_schema.TABLES " +
                "WHERE TABLE_SCHEMA = 'sqa' and TABLE_NAME = 'cauhinh';");
        try {
            assertTrue(rs.next());
        } catch (SQLException e) {
            assertTrue(false, e.toString());
        }
    }
    @Test
    @Order(2)
    void testInsertConfigKhoiSinhHoatSuccessfully(){
        insert(
                "[{\"price\":1.0,\"id\":0},{\"price\":2.0,\"id\":1},{\"price\":3.0,\"id\":2},{\"price\":4.0,\"id\":3},{\"price\":5.0,\"id\":4},{\"price\":6.0,\"id\":5},{\"price\":7.0,\"id\":6}]",
                "2025-09-22 17:00:00",
                CauHinhData.TYPE_KHOI_SINH_HOAT
        );
        doTestCheckInsertSuccessfully(
                "[{\"price\":1.0,\"id\":0},{\"price\":2.0,\"id\":1},{\"price\":3.0,\"id\":2},{\"price\":4.0,\"id\":3},{\"price\":5.0,\"id\":4},{\"price\":6.0,\"id\":5},{\"price\":7.0,\"id\":6}]",
                "2025-09-22 17:00:00",
                CauHinhData.TYPE_KHOI_SINH_HOAT
        );
    }
    @Test
    @Order(3)
    void testInsertConfigKhoiHanhChinhSuccessfully(){
        insert(
                "[{\"price\":1.0,\"id\":0},{\"price\":2.0,\"id\":1},{\"price\":3.0,\"id\":2},{\"price\":4.0,\"id\":3}]",
                "2025-09-22 17:00:00",
                CauHinhData.TYPE_KHOI_HANH_CHINH
        );
        doTestCheckInsertSuccessfully(
                "[{\"price\":1.0,\"id\":0},{\"price\":2.0,\"id\":1},{\"price\":3.0,\"id\":2},{\"price\":4.0,\"id\":3}]",
                "2025-09-22 17:00:00",
                CauHinhData.TYPE_KHOI_HANH_CHINH
        );
    }
    @Test
    @Order(4)
    void testInsertConfigKhoiKinhDoanhSuccessfully(){
        insert(
                "[{\"price\":1.0,\"id\":0},{\"price\":2.0,\"id\":1},{\"price\":3.0,\"id\":2},{\"price\":4.0,\"id\":3},{\"price\":5.0,\"id\":4},{\"price\":6.0,\"id\":5},{\"price\":7.0,\"id\":6},{\"price\":8.0,\"id\":7},{\"price\":9.0,\"id\":8}]",
                "2025-09-22 17:00:00",
                CauHinhData.TYPE_KHOI_KINH_DOANH
        );
        doTestCheckInsertSuccessfully(
                "[{\"price\":1.0,\"id\":0},{\"price\":2.0,\"id\":1},{\"price\":3.0,\"id\":2},{\"price\":4.0,\"id\":3},{\"price\":5.0,\"id\":4},{\"price\":6.0,\"id\":5},{\"price\":7.0,\"id\":6},{\"price\":8.0,\"id\":7},{\"price\":9.0,\"id\":8}]",
                "2025-09-22 17:00:00",
                CauHinhData.TYPE_KHOI_KINH_DOANH
        );
    }
    @Test
    @Order(5)
    void testInsertConfigKhoiSanXuatSuccessfully(){
        insert(
                "[{\"price\":1.0,\"id\":0},{\"price\":2.0,\"id\":1},{\"price\":3.0,\"id\":2},{\"price\":4.0,\"id\":3},{\"price\":5.0,\"id\":4},{\"price\":6.0,\"id\":5},{\"price\":7.0,\"id\":6},{\"price\":8.0,\"id\":7},{\"price\":9.0,\"id\":8},{\"price\":10.0,\"id\":9},{\"price\":11.0,\"id\":10},{\"price\":12.0,\"id\":11}]",
                "2025-09-22 17:00:00",
                CauHinhData.TYPE_KHOI_SAN_XUAT
        );
        doTestCheckInsertSuccessfully(
                "[{\"price\":1.0,\"id\":0},{\"price\":2.0,\"id\":1},{\"price\":3.0,\"id\":2},{\"price\":4.0,\"id\":3},{\"price\":5.0,\"id\":4},{\"price\":6.0,\"id\":5},{\"price\":7.0,\"id\":6},{\"price\":8.0,\"id\":7},{\"price\":9.0,\"id\":8},{\"price\":10.0,\"id\":9},{\"price\":11.0,\"id\":10},{\"price\":12.0,\"id\":11}]",
                "2025-09-22 17:00:00",
                CauHinhData.TYPE_KHOI_SAN_XUAT
        );
    }
    @Test
    @Order(6)
    void testGetValidConfig() {
        DAO.runStatement("INSERT INTO `cauhinh` (`data`, `timestart`, `status`, `type`) VALUES (\"test\", \"2001-09-23 17:00:00\", 0, 1);");
        System.out.println(1);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        DAO.runStatement(String.format("INSERT INTO `cauhinh` (`data`, `timestart`, `status`, `type`) VALUES (\"test\", \"%s\", 0, 1);", now.toString()));
        System.out.println(2);
        List<CauHinh> list = CauHinhData.withsStatus(0);
        System.out.println(3);
        assertTrue(list.size() == 4, "" + list.size());
    }
}