
package pt.ornrocha.tslabeler.core.io.write;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pt.ornrocha.tslabeler.core.utils.files.FileUtils;

public class ExcelWriterUtils {

	public static void WriteHeader(XSSFSheet sheet, String header, String delimiter) {

		String[] elems = header.split(delimiter);
		if (elems.length > 0) {
			Row row = sheet.createRow(0);
			for (int i = 0; i < elems.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(elems[i]);
			}

		}

	}

	private static void WriteDataToExcelSheet(Sheet sht, ArrayList<ArrayList<Object>> excelrows) {

		for (int i = 0; i < excelrows.size(); i++) {

			Row row = sht.createRow(i);
			ArrayList<Object> data = excelrows.get(i);

			for (int j = 0; j < data.size(); j++) {

				Object obj = data.get(j);
				Cell cell = row.createCell(j);

				if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
				else if (obj instanceof Double)
					cell.setCellValue((Double) obj);
				else if (obj instanceof Boolean)
					cell.setCellValue((Boolean) obj);
				else if (obj instanceof Date)
					cell.setCellValue((Date) obj);
				else if (obj instanceof String)
					cell.setCellValue((String) obj);
				else
					cell.setCellValue(String.valueOf(obj));
			}

		}

	}

	public static void WriteDataToNewExcelFile(String filepath, String extension,
			ArrayList<ArrayList<Object>> excelrows, String sheetname) throws IOException {

		Workbook wb = null;
		Sheet sht = null;

		if (extension.toLowerCase().equals("xls"))
			wb = new HSSFWorkbook();
		else
			wb = new XSSFWorkbook();

		if (sheetname != null)
			sht = wb.createSheet(sheetname);
		else
			sht = wb.createSheet();

		String filepathout = FileUtils.createFilePathWithExtension(filepath, extension);
		File f = new File(filepathout);
		if (!f.exists())
			f.createNewFile();

		WriteDataToExcelSheet(sht, excelrows);

		FileOutputStream fos = new FileOutputStream(filepathout);
		wb.write(fos);
		fos.close();
		wb.close();

	}

}
