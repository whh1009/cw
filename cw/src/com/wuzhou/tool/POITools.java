package com.wuzhou.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * POI工具
 * 
 * @author wanghonghui
 * 
 */
public class POITools {

	/**
	 * 获取excel
	 * 
	 * @param excelPath
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Workbook getWorkbook(String excelPath) throws EncryptedDocumentException, InvalidFormatException, IOException {
		return WorkbookFactory.create(new File(excelPath));
	}

	/**
	 * excel另存为
	 * 
	 * @param wb
	 * @param path
	 */
	public static void saveAsWorkbook(Workbook wb, String path) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建工作簿
	 * 
	 * @param wb
	 * @param sheetName
	 * @return
	 */
	public static Sheet createSheet(Workbook wb, String sheetName) {
		if ("".equals(sheetName)) {
			return wb.createSheet();
		} else {
			return wb.createSheet(sheetName);
		}
	}

	/**
	 * 合并单元格
	 * 
	 * @param sheet
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public static void mergeCell(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	}

	/**
	 * 设置行高 16px代表一个行高
	 * 
	 * @param row
	 * @param height
	 *            16*3 代表3个行高
	 */
	public static void setRowHeight(Row row, int heightCount) {
		if (heightCount == 0)
			heightCount = 1;
		row.setHeightInPoints(15.625f * heightCount);
		// row.setHeight(height);
	}

	/**
	 * 设置单元格宽度 35px代表一列
	 * 
	 * @param sheet
	 * @param colNumber
	 *            第几列
	 * @param width
	 *            宽度 35*3代表3个列宽
	 */
	public static void setCellWidth(Sheet sheet, int colNumber, int widthCount) {
		if (widthCount == 0)
			widthCount = 1;
		sheet.setColumnWidth(colNumber, (2000 * widthCount));
	}

	/**
	 * 设置单元格边框<br />
	 * 1 BORDER_THIN 细实线<br />
	 * 2 BORDER_MEDIUM 中等厚实线 3 BORDER_DASHED 长虚线<br />
	 * 4 BORDER_DOTTED 虚线 * <br />
	 * 5 BORDER_THICK 厚边框<br />
	 * 
	 * 9 BORDER_DASH_DOT <br />
	 * 11 BORDER_DASH_DOT_DOT 破折号和点<br />
	 * 
	 * @param wb
	 * @param border
	 * @return
	 */
	public static void setCellBorderDash(CellStyle cs, short border) {
		cs.setBorderTop(border);
		cs.setBorderBottom(border);
		cs.setBorderLeft(border);
		cs.setBorderRight(border);
	}

	/**
	 * 设置文本对齐方式<br />
	 * 1:左对齐<br />
	 * 2：居中显示<br />
	 * 3:右对齐<br />
	 * 等等。。。
	 * 
	 * @param cs
	 * @param align
	 * @return
	 */
	public static void setCellAlign(CellStyle cs, short align, short valign) {
		cs.setAlignment(align);
		cs.setVerticalAlignment(valign);
	}

	/**
	 * 设置单元格字体
	 * 
	 * @param cs
	 * @param wb
	 * @param fontName
	 * @param fontSize
	 */
	public static void setCellFont(CellStyle cs, Workbook wb, String fontName, short fontColor, short fontSize, boolean isBlod, boolean isItalic) {
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setColor(fontColor);
		font.setBold(isBlod);
		font.setItalic(isItalic);
		font.setFontHeightInPoints(fontSize);
		cs.setFont(font);
	}

	/**
	 * 设置单元格背景色
	 * 
	 * @param cs
	 * @param backgroundColor
	 *            背景色
	 */
	public static void setCellColor(CellStyle cs, short backgroundColor) {
		cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cs.setFillForegroundColor(backgroundColor);
	}

	/**
	 * 设置单元格文本自动换行
	 * 
	 * @param cs
	 */
	public static void setCellWrapText(CellStyle cs) {
		cs.setWrapText(true);
	}

	/**
	 * 设置单元格的值
	 * 
	 * @param cell
	 * @param cellValue
	 * @param cs
	 */
	public static void setCellValue(Cell cell, Object cellValue, CellStyle cs) {
		if (cellValue == null) {
			cell.setCellValue("");
			return;
		}
		if (cellValue instanceof String) {
			cell.setCellValue((String) cellValue);
		} else if (cellValue instanceof Boolean) {
			cell.setCellValue((Boolean) cellValue);
		} else if (cellValue instanceof Calendar) {
			cell.setCellValue((Calendar) cellValue);
		} else if (cellValue instanceof Double) {
			cell.setCellValue((Double) cellValue);
		} else if (cellValue instanceof Integer || cellValue instanceof Long || cellValue instanceof Short || cellValue instanceof Float) {
			cell.setCellValue((Double.parseDouble(cellValue.toString())));
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (cellValue instanceof HSSFRichTextString) {
			cell.setCellValue((HSSFRichTextString) cellValue);
		} else {
			cell.setCellValue(cellValue.toString());
		}
		if (cs != null) {
			cell.setCellStyle(cs);
		}
	}

	/**
	 * 创建行，行从0开始
	 * 
	 * @param sheet
	 * @return
	 */
	public static Row createRow(Sheet sheet, short rowNumber) {
		return sheet.createRow(rowNumber);
	}

	/**
	 * 创建带有超链接的单元格
	 * 
	 * @param wb
	 * @param cell
	 * @param url
	 * @param content
	 */
	public static void createHyperCell(Workbook wb, Cell cell, String url, String content) {
		CreationHelper createHelper = wb.getCreationHelper();
		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);
		Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
		link.setAddress(url);
		cell.setHyperlink(link);
		cell.setCellStyle(hlink_style);
		cell.setCellValue(content);
	}

	/**
	 * 根据sheet下标获取sheet
	 * 
	 * @param index
	 * @param wb
	 * @return
	 */
	public static Sheet getSheet(int index, Workbook wb) {
		return wb.getSheetAt(index);
	}

	/**
	 * 根据sheet名获取sheet
	 * 
	 * @param sheetName
	 * @param wb
	 * @return
	 */
	public static Sheet getSheet(String sheetName, Workbook wb) {
		return wb.getSheet(sheetName);
	}

	/**
	 * 根据行号获取row
	 * @param sheet
	 * @param rowNumber
	 * @return
	 */
	public static Row getRow(Sheet sheet, int rowNumber) {
		return sheet.getRow(rowNumber);
	}
	
	/**
	 * 获取单元格值
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		DecimalFormat df = new DecimalFormat("0.00");
		String value = "";
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_FORMULA:
//			value = cell.getCellFormula();
			try {
				value = String.valueOf(cell.getNumericCellValue());
			}catch(Exception ex) {
				value = String.valueOf(cell.getRichStringCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
			} else {
				value = df.format(cell.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			value = "";
			break;
		case HSSFCell.CELL_TYPE_ERROR: // 故障     
            value="";
            break;
		default:
			value = "";
			break;
		}
		return value.trim();
	}
	
	/**
	 * 获取excel总行数
	 * @param sheet
	 * @return
	 */
	public static int getRowCount(Sheet sheet) {
		return sheet.getPhysicalNumberOfRows();
	}
	
	/**
	 * 获取行的总列数
	 * @param row
	 * @return
	 */
	public static int getColumnCount(Row row) {
		return row.getPhysicalNumberOfCells();
	}
	
	
}
