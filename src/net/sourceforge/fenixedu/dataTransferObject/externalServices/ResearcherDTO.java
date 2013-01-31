package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.Researcher;

public class ResearcherDTO {
	private String username;
	private String name;
	private String[] keywordsPortuguese;
	private String[] keywordsEnglish;
	private Contact[] contacts;
	private String[] units;

	public ResearcherDTO(Researcher researcher) {
		this.username = researcher.getPerson().getUsername();
		this.name = researcher.getPerson().getName();
		this.setKeywordsPortuguese(researcher.getKeywordsPt() != null ? researcher.getKeywordsPt() : "");
		this.setKeywordsEnglish(researcher.getKeywordsEn() != null ? researcher.getKeywordsEn() : "");
		this.units = getAssociatedUnitNames(researcher.getPerson().getAssociatedResearchOrDepartmentUnits());

		if (researcher.getAllowsContactByMedia()) {
			this.contacts = getContacts(researcher);
		}
	}

	private Contact[] getContacts(Researcher researcher) {
		java.util.List<Contact> contacts = new java.util.ArrayList<Contact>();
		for (PartyContact contact : researcher.getAvailableContacts()) {
			contacts.add(new Contact(contact));
		}

		return contacts.toArray(new Contact[0]);
	}

	public static class Contact {
		String typeContactPortuguese;
		String typeContactEnglish;
		String value;

		public Contact() {

		}

		public Contact(PartyContact contact) {
			ResourceBundle bundlePortuguese = ResourceBundle.getBundle("resources/ResearcherResources", new Locale("pt", "PT"));
			ResourceBundle bundleEnglish = ResourceBundle.getBundle("resources/ResearcherResources", new Locale("en", "EN"));
			this.typeContactPortuguese = bundlePortuguese.getString("label." + contact.getClass().getName());
			this.typeContactEnglish = bundleEnglish.getString("label." + contact.getClass().getName());
			this.value = contact.getPresentationValue();
		}

		public String getTypeContactPortuguese() {
			return typeContactPortuguese;
		}

		public void setTypeContactPortuguese(String typeContactPortuguese) {
			this.typeContactPortuguese = typeContactPortuguese;
		}

		public String getTypeContactEnglish() {
			return typeContactEnglish;
		}

		public void setTypeContactEnglish(String typeContactEnglish) {
			this.typeContactEnglish = typeContactEnglish;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	// FIXME Anil : there should be a method in Person that gives the Unit Names
	// in an array
	private String[] getAssociatedUnitNames(Set<Unit> associatedResearchOrDepartmentUnits) {
		String[] names = new String[associatedResearchOrDepartmentUnits.size()];
		int i = 0;
		for (Unit unit : associatedResearchOrDepartmentUnits) {
			names[i++] = unit.getName();
		}

		return names;
	}

	private void setKeywordsEnglish(String keywordsEn) {
		this.keywordsEnglish = getKeywordsSplitted(keywordsEn);

	}

	private String[] getKeywordsSplitted(String keywords) {
		String[] keywordsArray = keywords.split(",");

		for (int i = 0; i < keywordsArray.length; i++) {
			keywordsArray[i] = keywordsArray[i].trim();
		}

		return keywordsArray;
	}

	private void setKeywordsPortuguese(String keywordsPt) {
		this.keywordsPortuguese = getKeywordsSplitted(keywordsPt);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getKeywordsPortuguese() {
		return keywordsPortuguese;
	}

	public void setKeywordsPortuguese(String[] keywordsPortuguese) {
		this.keywordsPortuguese = keywordsPortuguese;
	}

	public String[] getKeywordsEnglish() {
		return keywordsEnglish;
	}

	public void setKeywordsEnglish(String[] keywordsEnglish) {
		this.keywordsEnglish = keywordsEnglish;
	}

	public String[] getUnits() {
		return units;
	}

	public void setUnits(String[] units) {
		this.units = units;
	}

	public Contact[] getContacts() {
		return contacts;
	}

	public void setContacts(Contact[] contacts) {
		this.contacts = contacts;
	}

}
