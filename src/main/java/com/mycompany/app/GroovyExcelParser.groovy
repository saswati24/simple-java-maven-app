package com.mycompany.app


import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.util.CellReference
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GroovyExcelParser {
    //http://poi.apache.org/spreadsheet/quick-guide.html#Iterator

    def finalOutput(String path){
        // Reading file from local directory
        FileInputStream file = new FileInputStream(
                new File(path));

        // Create Workbook instance holding reference to
        // .xlsx file
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        // Get first/desired sheet from the workbook
        HSSFSheet sheet = workbook.getSheetAt(0);

        // Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        Row row = rowIterator.next()
        def headers = getRowData(row)

        def rows = []
        while (rowIterator.hasNext()) {
            row = rowIterator.next()
            rows << getRowData(row)
        }
        file.close()
        [headers, rows]
    }


//    def parse(path) {
//        InputStream inp = new FileInputStream(path)
//        Workbook wb = WorkbookFactory.create(inp);
//        Sheet sheet = wb.getSheetAt(0);
//
//        Iterator<Row> rowIt = sheet.rowIterator()
//        Row row = rowIt.next()
//        def headers = getRowData(row)
//
//        def rows = []
//        while(rowIt.hasNext()) {
//            row = rowIt.next()
//            rows << getRowData(row)
//        }
//        [headers, rows]
//    }

    def getRowData(Row row) {
        def data = []
        for (Cell cell : row) {
            getValue(row, cell, data)
        }
        data
    }

    def getRowReference(Row row, Cell cell) {
        def rowIndex = row.getRowNum()
        def colIndex = cell.getColumnIndex()
        CellReference ref = new CellReference(rowIndex, colIndex)
        ref.getRichStringCellValue().getString()
    }

    def getValue(Row row, Cell cell, List data) {
        def rowIndex = row.getRowNum()
        def colIndex = cell.getColumnIndex()
        def value = ""
        switch (cell.getCellType()) {
            case CellType.STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case CellType.NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case CellType.BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case CellType.FORMULA:
                value = cell.getCellFormula();
                break;
            default:
                value = ""
        }
        data[colIndex] = value
        data
    }

    def toXml(header, row) {
        def obj = "<object>\n"
        row.eachWithIndex { datum, i ->
            def headerName = header[i]
            obj += "\t<$headerName>$datum</$headerName>\n"
        }
        obj += "</object>"
    }

    public static void main(String[]args) {

//        File file = new File(".");
//        for(String fileNames : file.list()) System.out.println(fileNames);

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("EEE dd/MM/yyyy");
        String strDate = dateFormat.format(date);
        System.out.println("Current Date: " + strDate);

        def col_index = null

        def filename = 'temp.xls'
        def envname = 'CT0A'

        GroovyExcelParser parser = new GroovyExcelParser()
//        def (headers, rows) = parser.parse(filename)
        def (headers, rows) = parser.finalOutput(filename)
//        println 'Headers'
//        println '------------------'
        headers.eachWithIndex { header, index ->
            if (header == strDate) {
                println strDate + " found at Column Index " + index
                col_index = index
            }
        }
        if (col_index != null) {
//        println '\nRows'
//        println '------------------'
            def ld_dates = null
            def ld_no = null
            for (int i = 0; i < rows.size(); i++) {
                def row = rows[i]
//            println parser.toXml(headers, row)
                if (row[0] != null && row[0] == envname) {
                    ld_dates = rows[i - 1][col_index]
                    println 'LD Dates found : ' + ld_dates

                    ld_no = rows[i - 2][col_index]
                    println 'LD Number found : ' + ld_no
                    break
                }
            }

            def ld_no_final = null
            String[] values = ld_no.split('/')
            for (String value : values) {
                if (value.matches('.*B$')) {
                    ld_no_final = 'LD' + (value =~ "[0-9]+")[0] + '_Batch'
                }
            }
            println 'Final LD No: ' + ld_no_final

            def ld_dates_final_list = []
            def ld_dates_final = null
            for (String date_data : ld_dates.split(',')) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy", Locale.ENGLISH);
                ld_dates_final_list << LocalDate.parse(date_data.trim(), formatter);
            }
            ld_dates_final = ld_dates_final_list[0]
            if (ld_dates_final_list.size() > 1 && ld_dates_final_list[0].isAfter(ld_dates_final_list[1])) {
                ld_dates_final = ld_dates_final_list[1]
            }
            println 'Final LD Date: ' + ld_dates_final
        }else {
            println "For current date there is no entry"
        }
    }
}