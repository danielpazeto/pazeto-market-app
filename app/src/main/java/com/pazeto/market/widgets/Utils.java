package com.pazeto.market.widgets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.util.Log;

public class Utils {

	public static int componentTimeToTimestamp(Calendar c) {

		// c.setTimeZone(TimeZone.getTimeZone("America/Brasilia"));
		// c.set(year, month, day);
		c.set(c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH), 0, 0, 0);

		// c.set(Calendar.YEAR, year);
		// c.set(Calendar.MONTH, month);
		// c.set(Calendar.DAY_OF_MONTH, day);
		// c.set(Calendar.HOUR, -14);
		// c.set(Calendar.MINUTE, 00);
		// c.set(Calendar.SECOND, 00);
		// c.set(Calendar.MILLISECOND, 0);
		Log.d("c calendar", ":" + c.getTime().toString());
		return (int) (c.getTimeInMillis() / 1000L);

	}

	public static int dateToUnixTimeStamp(int year, int month, int day) {
		SimpleDateFormat dfm = new SimpleDateFormat("yyyyMMddHHmmss");
		dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = String.valueOf(year);
		date = date.concat(String.valueOf(month));
		date = date.concat(String.valueOf(day));
		Log.d("", date);

		try {
			java.util.Date ddate = new SimpleDateFormat("yyyyMMdd").parse(date);
			Log.d(" data realemnte", ddate.toString());
			ddate.getTime();
			return (int) ddate.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
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
