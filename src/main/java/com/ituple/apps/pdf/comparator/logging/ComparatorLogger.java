package com.ituple.apps.pdf.comparator.logging;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.ituple.apps.pdf.comparator.Constant;

public class ComparatorLogger extends Formatter {
	static String timeStmp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	public static Logger comparatorLogger = Logger.getLogger("AllPdfLocLog");
	static FileHandler fhcomparatorLogger;
	static {
		try {
			System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
			fhcomparatorLogger = new FileHandler(Constant.COMPARATOR_LOG_LOCATION + System.nanoTime() + ".log");
			comparatorLogger.addHandler(fhcomparatorLogger);
			SimpleFormatter formatter1 = new SimpleFormatter();
			fhcomparatorLogger.setFormatter(formatter1);

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
