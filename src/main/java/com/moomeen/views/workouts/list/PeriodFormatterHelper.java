package com.moomeen.views.workouts.list;

import java.util.Locale;
import java.util.ResourceBundle;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class PeriodFormatterHelper {

	private static final String BUNDLE_NAME = "org.joda.time.format.messages";
	
	public static PeriodFormatter getForLocale(Locale l){
        ResourceBundle b = ResourceBundle.getBundle(BUNDLE_NAME, l);
        PeriodFormatter pf = new PeriodFormatterBuilder()
            .appendDays()
            .appendSuffix(firstLetter(b.getString("PeriodFormat.day")))
            .appendSeparator(":")
            .appendHours()
            .appendSuffix(firstLetter(b.getString("PeriodFormat.hour")))
            .appendSeparator(":")
            .appendMinutes()
            .appendSuffix(firstLetter(b.getString("PeriodFormat.minute")))
            .appendSeparator(":")
            .appendSeconds()
            .appendSuffix(firstLetter(b.getString("PeriodFormat.second")))
            .toFormatter();
        
        return pf;
	}
	
	private static String firstLetter(String s){
		return s.substring(1, 2);
	}
	
}
