package net.sourceforge.fenixedu.dataTransferObject.accounting.administrativeOffice;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;

public abstract class GratuityLetterDTO implements Serializable {

    private String entityCode;

    private Person person;

    private ExecutionYear executionYear;

    protected GratuityLetterDTO(final Person person, final ExecutionYear executionYear,
	    final String entityCode) {
	super();
	setPerson(person);
	setExecutionYear(executionYear);
	setEntityCode(entityCode);
    }

    public String getEntityCode() {
	return entityCode;
    }

    public void setEntityCode(String entityCode) {
	this.entityCode = entityCode;
    }

    public ExecutionYear getExecutionYear() {
	return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

}
