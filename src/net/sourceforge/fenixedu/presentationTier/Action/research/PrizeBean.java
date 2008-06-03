package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PrizeBean implements Serializable {

	private Integer year;
	private MultiLanguageString name;
	private MultiLanguageString description;
	private DomainReference<Person> person;
	
	public PrizeBean(Person person) {
		setPerson(person);
	}
	
	public PrizeBean() {
		setPerson(null);
	}
	
	public Person getPerson() {
		return this.person.getObject();
	}
	
	public void setPerson(Person person) {
		this.person = new DomainReference<Person>(person);	
	}
	
	public MultiLanguageString getDescription() {
		return description;
	}
	public void setDescription(MultiLanguageString description) {
		this.description = description;
	}
	public MultiLanguageString getName() {
		return name;
	}
	public void setName(MultiLanguageString name) {
		this.name = name;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	
	
}
