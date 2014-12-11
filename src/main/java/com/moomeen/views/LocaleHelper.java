package com.moomeen.views;

import java.util.Locale;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;

import com.moomeen.views.workouts.list.PeriodFormatterHelper;
import com.vaadin.server.Page;

public class LocaleHelper {
	
	public static String getDateFormat(){
		Locale browserLocale = getBrowserLocale();
		return DateTimeFormat.patternForStyle("MM", browserLocale);
	}
	
	public static PeriodFormatter getPeriodFormatter(){
		Locale browserLocale = getBrowserLocale();
		return PeriodFormatterHelper.getForLocale(browserLocale);
	}

	private static Locale getBrowserLocale() {
		return Page.getCurrent().getWebBrowser().getLocale();
	}
	
}
