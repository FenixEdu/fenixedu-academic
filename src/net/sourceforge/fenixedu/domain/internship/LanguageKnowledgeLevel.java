package net.sourceforge.fenixedu.domain.internship;

public enum LanguageKnowledgeLevel {
	NONE, FAIR, GOOD, EXCELENT;

	public String getQualifiedKey() {
		return "LanguageKnowledgeLevel." + name();
	}
}
