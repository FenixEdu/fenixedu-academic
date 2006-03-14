package net.sourceforge.fenixedu.util;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Language;

public class MultiLanguageString {
	public class ExistingLanguageException extends Exception {
	}

	private Map<Language, String> contentsMap;

	public Collection<String> getAllContents() {
		return contentsMap.values();
	}

	public Collection<Language> getAllLanguages() {
		return contentsMap.keySet();
	}
    
    public String getContent() {
        return getContent(Language.getDefaultLanguage());
    }

	public String getContent(Language language) {
		return contentsMap.get(language.toString());
	}

	public String addContent(Language language, String content) throws ExistingLanguageException {
		if (!contentsMap.containsKey(language)) {
			return contentsMap.put(language, content);
		} else {
			throw new ExistingLanguageException();
		}
	}

	public String editContent(Language language, String content) {
		return contentsMap.put(language, content);
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

	@Override
	public String toString() {
		String result = new String();
		
		for (Language key : contentsMap.keySet()) {
			result.concat(key + "\001" + contentsMap.get(key) + "\001");
		}
		return null;
	}
}
