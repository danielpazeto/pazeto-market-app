package com.pazeto.comercio.widgets;

import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.pazeto.comercio.utils.Constants.DATE_FORMAT_STRING;

public class Utils {

    public static SimpleDateFormat ISO_8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");

    public static Calendar getCalendarDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    public static Calendar getCalendarDate(Calendar c) {
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    public static String formatDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault());
        return sdf.format(date);
    }

    public static void setNumberValueView(TextView et, double value) {
        DecimalFormat df = new DecimalFormat();
        String formattedValue = df.format(value);
        et.setText(formattedValue);
    }

    public static void setCurrencyValueView(TextView et, double value) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        String formattedValue = format.format(value);
        et.setText(formattedValue);
    }

    public static Date addDateOneDay(Date currentDate) {
        Calendar date = Calendar.getInstance();
        date.setTime(currentDate);
        date.add(Calendar.DATE, 1);
        return date.getTime();
    }

    // public static void createPDF()
    // {
    // System.out.println("OI");
    // Document doc = new Document();
    //
    //
    // try {
    // String path = Environment.getExternalStorageDirectory().getAbsolutePath()
    // + "/droidText";
    //
    // File dir = new File(path);
    // if(!dir.exists())
    // dir.mkdirs();
    //
    // Log.d("PDFCreator", "PDF Path: " + path);
    //
    //
    // File file = new File(dir, "sample.pdf");
    // FileOutputStream fOut = new FileOutputStream(file);
    //
    // PdfWriter.getInstance(doc, fOut);
    //
    // //open the document
    // doc.open();
    //
    //
    // Paragraph p1 = new
    // Paragraph("Hi! I am generating my first PDF using DroidText");
    // Font paraFont= new Font(Font.COURIER);
    // p1.setAlignment(Paragraph.ALIGN_CENTER);
    // p1.setFont(paraFont);
    //
    // //add paragraph to document
    // doc.add(p1);
    //
    // Paragraph p2 = new Paragraph("This is an example of a simple paragraph");
    // Font paraFont2= new Font(Font.COURIER,14.0f,Color.GREEN);
    // p2.setAlignment(Paragraph.ALIGN_CENTER);
    // p2.setFont(paraFont2);
    //
    // doc.add(p2);
    //
    //
    //
    // //set footer
    // Phrase footerText = new Phrase("This is an example of a footer");
    // HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
    // doc.setFooter(pdfFooter);
    //
    //
    //
    // } catch (DocumentException de) {
    // Log.e("PDFCreator", "DocumentException:" + de);
    // } catch (IOException e) {
    // Log.e("PDFCreator", "ioException:" + e);
    // }
    // finally
    // {
    // doc.close();
    // }
    //
    // }
}
