package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract.ResearchContractType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.PersonName;

import org.joda.time.YearMonthDay;
/**
 * @author jaime
 *
 */
public class PersonBean implements Serializable {

    private String username;

    public PersonBean() {
	setUsername(null);
    }
    
    public void setUsername(String username) {
	this.username = username;
    }

    public String getUsername() {
	return username;
    }
    
}
