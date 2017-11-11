package com.ituple.apps.pdf.comparator.filecopy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.ituple.apps.pdf.comparator.Constant;

public class JSCHLogger extends Formatter {
	static String timeStmp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	public static Logger JSCHlogger = Logger.getLogger("AllPdfLocLog");
	static FileHandler fhAllPdfLocLog;
	static {
		try {
			System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
			fhAllPdfLocLog = new FileHandler(Constant.JSCH_LOG_LOCATION + System.nanoTime() + ".log");
			JSCHlogger.addHandler(fhAllPdfLocLog);
			SimpleFormatter formatter1 = new SimpleFormatter();
			fhAllPdfLocLog.setFormatter(formatter1);

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String format(LogRecord record) {
		return null;
	}

}
