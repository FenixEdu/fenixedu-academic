package net.sourceforge.fenixedu.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Language;

public class MultiLanguageString {
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
    
    public String getContent() {
        //In a future version this should return the content in the LOCALE language
        //only if that language has no content the Application Language should be used
        return getContent(Language.getApplicationLanguage());
    }
	
    public void setContent(String text) {
        //In a future version the content should be set to the LOCALE language and the
        //Application languange at the same time.
        setContent(Language.getApplicationLanguage(), text);
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
		String result = contentsMap.get(language);
		if (result == null) {
			return false;
		}
		else {
			return true;
		}
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
