package com.tele.utils;

import com.google.common.collect.Lists;
import com.tele.model.ChargeRequest;
import com.tele.model.ChargeResponse;
import lombok.extern.log4j.Log4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

/**
 * @author zhangleimin
 * @package com.tele.utils
 * @date 16-9-26
 */
@Log4j
public class XLSUtil {

    /**
     * 读取EXCEL内容
     * @param fileName excel
     * @return  充值请求信息
     */
    public List<ChargeRequest> read(String fileName) {
        List<ChargeRequest> cardInfoList = Lists.newArrayList();
        InputStream fio = null;
        try {
            fio = new FileInputStream(fileName);
            Workbook book = WorkbookFactory.create(fio);
            Sheet sheet = book.getSheetAt(0);
            for(int r = sheet.getFirstRowNum()+1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                ChargeRequest info = new ChargeRequest();
                info.setAccountNo(row.getCell(0).getStringCellValue());
                info.setCardNo(row.getCell(1).getStringCellValue());
                info.setCardPwd(row.getCell(2).getStringCellValue());
                cardInfoList.add(info);
            }
        } catch (IOException | InvalidFormatException e) {
            log.error(e);
        } finally {
            StreamUtil.close(fio);
        }
        return cardInfoList;
    }

    /**
     * 写EXCEL内容
     * @param responses 响应消息
     * @param fileName excel
     * @return  充值请求信息
     */
    public boolean write(List<ChargeResponse> responses, String fileName) {
        FileOutputStream fileOut = null;
        boolean result = false;
        try {
            fileOut = new FileOutputStream(fileName);
            Workbook book = new XSSFWorkbook();
            Sheet sheet = book.createSheet("充值结果");
            int rowNum = 0;
            Row titleRow = sheet.createRow(rowNum++);
            Cell accountCell = titleRow.createCell(0);
            Cell cardNoCell = titleRow.createCell(1);
            Cell cardPwdCell = titleRow.createCell(2);
            Cell msgCell = titleRow.createCell(3);
            accountCell.setCellValue("分账序号");
            cardNoCell.setCellValue("卡号");
            cardPwdCell.setCellValue("卡密");
            msgCell.setCellValue("充值结果");
            for (ChargeResponse response : responses) {
                Row row = sheet.createRow(rowNum++);
                accountCell = row.createCell(0);
                cardNoCell = row.createCell(1);
                cardPwdCell = row.createCell(2);
                msgCell = row.createCell(3);
                accountCell.setCellValue(response.getAccountNo());
                cardNoCell.setCellValue(response.getCardNo());
                cardPwdCell.setCellValue(response.getCardPwd());
                msgCell.setCellValue(response.getMessage());
            }
            book.write(fileOut);
            result = true;
        } catch (IOException e) {
            result = false;
            log.error(e);
        } finally {
            StreamUtil.close(fileOut);
        }
        return result;
    }
}
