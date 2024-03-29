package com.dncrm.util;

import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导入到EXCEL
 * 类名称：ObjectExcelView.java
 * 类描述：
 * 创建人：Simon
 * 作者单位：
 * 联系方式：
 *
 * @version 1.0
 */
public class ObjectExcelView extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      HSSFWorkbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        Date date = new Date();
        String filename = Tools.date2Str(date, "yyyyMMddHHmmss");
        HSSFSheet sheet;
        HSSFCell cell = null;
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
        sheet = workbook.createSheet("sheet1");

        List<String> titles = (List<String>) model.get("titles");
        int len = titles.size();
        HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont headerFont = workbook.createFont();    //标题字体
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 11);
        headerStyle.setFont(headerFont);
        short width = 20, height = 25 * 20;
        sheet.setDefaultColumnWidth(width);
        for (int i = 0; i < len; i++) { //设置标题
            String title = titles.get(i);
            cell = getCell(sheet, 0, i);
            cell.setCellStyle(headerStyle);
            setText(cell, title);
        }
        sheet.getRow(0).setHeight(height);

        HSSFCellStyle contentStyle = workbook.createCellStyle(); //内容样式
        contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        HSSFDataFormat format = workbook.createDataFormat();
        
        List<PageData> varList = (List<PageData>) model.get("varList");
        int varCount = varList.size();
        for (int i = 0; i < varCount; i++) {
            PageData vpd = varList.get(i);
            for (int j = 0; j < len; j++) {
            	Object obj = vpd.get("var" + (j + 1));
            	String varstr = "";
        		cell = getCell(sheet, i + 1, j);
                cell.setCellStyle(contentStyle);
            	if(obj instanceof Long) {
        			//varstr = String.valueOf((Long)obj);
                    cell.setCellValue(Double.valueOf((Long)obj));
            	} else if(obj instanceof Integer) {
            		//varstr = String.valueOf((Long)obj);
                    cell.setCellValue(Double.valueOf((Integer)obj));
            	} else if(obj instanceof Double) {
            		//varstr = String.valueOf((Long)obj);
                    cell.setCellValue(Double.valueOf((Double)obj));
            	} else {
            		if(obj != null) {
            			varstr = (String)obj;
            		}
            		if(varstr.indexOf("=SUM(") == 0) {
                		cell.setCellFormula(varstr.substring(1));
            		} else {
                		//String varstr = vpd.getString("var" + (j + 1)) != null ? vpd.getString("var" + (j + 1)) : "";
                        setText(cell, varstr);
            		}
            	}
                
            }

        }
        
        //输出
        /*FileOutputStream out = new FileOutputStream("E:\\"+filename+".xls");
        workbook.write(out);
        out.close();*/
    }

}
