package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu._development.PropertiesManager;

public enum Language {
    pt,
    en,
    es,
    de,    
    fr,
    it,    
   
  
    ar,
    bg,
    cs,
    da,

    el,

    eo,

    et,
    fi,

    hr,
    hu,
    id,
    is,

    ja,
    ko,
    lt,
    lv,
    nl,
    no,
    pl,

    ro,
    ru,
    sk,
    sl,
    sr,
    sv,
    th,
    tr,
    uk,
    zh;
	
	public static Language getApplicationLanguage(){
		return Language.valueOf(PropertiesManager.getProperty("language"));
	}
}
