package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu._development.PropertiesManager;

public enum Language {
	pt, 
	en,
	de,
	fr,
	es;
	
	public static Language getDefaultLanguage(){
		return Language.valueOf(PropertiesManager.getProperty("language"));
	}
}
