package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu._development.PropertiesManager;

public enum Language {
    ar,
    bg,
    cs,
    da,
    de,
    el,
    en,
    eo,
    es,
    et,
    fi,
    fr,
    hr,
    hu,
    id,
    is,
    it,
    ja,
    ko,
    lt,
    lv,
    nl,
    no,
    pl,
    pt,
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
