package net.sourceforge.fenixedu.util;

import java.util.Collection;
import java.util.Map;

public class MultiLanguageString {
	public class ExistingLanguageException extends Exception {
	}

	private Map<String, String> contentsMap;

	public Collection<String> getAllContents() {
		return contentsMap.values();
	}

	public Collection<String> getAllLanguages() {
		return contentsMap.keySet();
	}

	public String getContent(String language) {
		return contentsMap.get(language);
	}

	public String addContent(String language, String content) throws ExistingLanguageException {
		if (!contentsMap.containsKey(language)) {
			return contentsMap.put(language, content);
		} else {
			throw new ExistingLanguageException();
		}
	}

	public String editContent(String language, String content) {
		return contentsMap.put(language, content);
	}

	public String removeContent(String language) {
		return contentsMap.remove(language);
	}
	
	public boolean hasLanguage(String language) {
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
		
		for (String key : contentsMap.keySet()) {
			result.concat(key + "\001" + contentsMap.get(key) + "\001");
		}
		return null;
	}
}
