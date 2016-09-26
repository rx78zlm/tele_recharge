import com.google.common.collect.Tables;
import com.tele.service.RechargeServiceAccount;
import com.tele.utils.XLSUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhangleimin
 * @package PACKAGE_NAME
 * @date 16-9-26
 */
public class MethodTest {

    @Test
    public void testReadExcel_old() {
        InputStream fio = null;
        try {
            fio = new FileInputStream("e:/jctwcs.xls");
            Workbook book = new HSSFWorkbook(fio);
            Sheet sheet = book.getSheetAt(0);
            for(Row row : sheet) {
                for (Cell cell : row) {
                    System.out.println(cell.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fio != null) {
                try {
                    fio.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testReadExcel_2007() {
        InputStream fio = null;
        try {
            fio = new FileInputStream("d:/tmp/test.xlsx");
            Workbook book = new XSSFWorkbook(fio);
            Sheet sheet = book.getSheetAt(0);
            for(Row row : sheet) {
                for (Cell cell : row) {
                    System.out.println(cell.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fio != null) {
                try {
                    fio.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testReadExcel() {
        InputStream fio = null;
        try {
//            fio = new FileInputStream("e:/jctwcs.xls");
            fio = new FileInputStream("d:/tmp/test.xlsx");
            Workbook book = WorkbookFactory.create(fio);
            Sheet sheet = book.getSheetAt(0);
            for(Row row : sheet) {
                for (Cell cell : row) {
                    System.out.println(cell.toString());
                }
            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (fio != null) {
                try {
                    fio.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void readExcelToTable() {
        XLSUtil xlsUtil = new XLSUtil();
        xlsUtil.read("d:/tmp/test.xlsx");
    }

    @Test
    public void testRunCharge() {
        String srcFile = "d:/tmp/test.xlsx";
        String msgFile = "d:/tmp/test_result.xlsx";
        RechargeServiceAccount rechargeService = new RechargeServiceAccount();
        rechargeService.recharge(srcFile, msgFile);
    }
}
