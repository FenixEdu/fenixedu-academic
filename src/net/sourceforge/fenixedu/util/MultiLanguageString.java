package net.sourceforge.fenixedu.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Language;

public class MultiLanguageString {
    
    private static InheritableThreadLocal<Locale> locale = new InheritableThreadLocal<Locale>();

    public static Locale getLocale()
    {
        return MultiLanguageString.locale.get();
    }

    public static void setLocale(Locale locale)
    {
        MultiLanguageString.locale.set(locale);
    }
    
    
    
	private Map<Language, String> contentsMap;

	public MultiLanguageString() {
		this.contentsMap = new HashMap<Language, String>();
	}
	
	public Collection<String> getAllContents() {
		return contentsMap.values();
	}

	public Collection<Language> getAllLanguages() {
		return contentsMap.keySet();
	}

    private Language getUserLanguage() {
        return getLocale() != null ? Language.valueOf(getLocale().getLanguage()) : null;
    }

    private Language getSystemLanguage() {
        return Language.getApplicationLanguage();
    }
    
    public String getContent() {
        final Language userLanguage = getUserLanguage();
        if (userLanguage != null && hasLanguage(userLanguage)) {
            return getContent(userLanguage);
        }
        final Language systemLanguage = getSystemLanguage();
        if (systemLanguage != null && hasLanguage(systemLanguage)) {
            return getContent(systemLanguage);
        }

        return contentsMap.isEmpty() ? null : contentsMap.values().iterator().next();
    }
	
    public void setContent(String text) {
        final Language userLanguage = getUserLanguage();
        if (userLanguage != null) {
            setContent(userLanguage, text);
        }
        final Language systemLanguage = getSystemLanguage();
        if (userLanguage != systemLanguage && !hasLanguage(systemLanguage)) {
            setContent(systemLanguage, text);
        }
    }

	public String getContent(Language language) {
		return contentsMap.get(language);
	}

	public void setContent(Language language, String content) {
		contentsMap.put(language, content);
	}

	public String removeContent(Language language) {
		return contentsMap.remove(language);
	}
	
	public boolean hasLanguage(Language language) {
        return contentsMap.containsKey(language);
	}

	public String exportAsString() {
		final StringBuilder result = new StringBuilder();
		for (final Language key : contentsMap.keySet()) {
            final String value = contentsMap.get(key);
            result.append(key);
            result.append(value.length());
            result.append(':');
            result.append(value);
		}
		return result.toString();
	}
}
