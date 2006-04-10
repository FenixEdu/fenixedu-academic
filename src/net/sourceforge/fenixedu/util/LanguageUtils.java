package net.sourceforge.fenixedu.util;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.Language;



public class LanguageUtils extends FenixUtil {

    private static InheritableThreadLocal<Locale> locale = new InheritableThreadLocal<Locale>();
    
    public static Locale getLocale(){
        return LanguageUtils.locale.get();
    }

    public static void setLocale(Locale locale){
        LanguageUtils.locale.set(locale);
    }
    
    public static Language getUserLanguage() {
        Locale locale = getLocale();
        
        return locale != null ? Language.valueOf(locale.getLanguage()) : null;
    }

    public static Language getSystemLanguage() {
        return Language.getApplicationLanguage();
    }
    
    public static Language getLanguage(){
        return locale != null ? LanguageUtils.getUserLanguage() : LanguageUtils.getSystemLanguage();
    }
}
