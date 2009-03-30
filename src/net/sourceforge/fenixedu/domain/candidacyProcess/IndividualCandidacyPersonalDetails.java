package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

public abstract class IndividualCandidacyPersonalDetails extends IndividualCandidacyPersonalDetails_Base {
    static final public Comparator<IndividualCandidacyPersonalDetails> COMPARATOR_BY_NAME = new Comparator<IndividualCandidacyPersonalDetails>() {
	@Override
	public int compare(IndividualCandidacyPersonalDetails o1, IndividualCandidacyPersonalDetails o2) {
	    return o1.getName().compareTo(o2.getName());
	}
    };

    static final public Comparator<IndividualCandidacyPersonalDetails> COMPARATOR_BY_NAME_AND_ID = new Comparator<IndividualCandidacyPersonalDetails>() {
	public int compare(final IndividualCandidacyPersonalDetails o1, final IndividualCandidacyPersonalDetails o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(COMPARATOR_BY_NAME);
	    comparatorChain.addComparator(COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    public IndividualCandidacyPersonalDetails() {
	super();
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getCandidacy().getRootDomainObject();
    }

    public abstract boolean isInternal();

    public abstract void edit(PersonBean personBean);

    public abstract void ensurePersonInternalization();

    public static void createDetails(IndividualCandidacy candidacy, IndividualCandidacyProcessBean bean) {
	if (bean.getInternalPersonCandidacy()) {
	    new IndividualCandidacyInternalPersonDetails(candidacy, bean.getOrCreatePersonFromBean());
	} else {
	    new IndividualCandidacyExternalPersonDetails(candidacy, bean);
	}
    }

    public boolean hasStudent() {
	return getStudent() != null;
    }

    public abstract Student getStudent();

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getDocumentIdNumber();

    public abstract void setDocumentIdNumber(String documentIdNumber);

    public abstract IDDocumentType getIdDocumentType();

    public abstract void setIdDocumentType(IDDocumentType type);

    public abstract YearMonthDay getEmissionDateOfDocumentIdYearMonthDay();

    public abstract void setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay date);

    public abstract YearMonthDay getExpirationDateOfDocumentIdYearMonthDay();

    public abstract void setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay date);

    public abstract String getEmissionLocationOfDocumentId();

    public abstract void setEmissionLocationOfDocumentId(String location);

    public abstract YearMonthDay getDateOfBirthYearMonthDay();

    public abstract void setDateOfBirthYearMonthDay(YearMonthDay birthday);

    public abstract Gender getGender();

    public abstract void setGender(Gender gender);

    public abstract MaritalStatus getMaritalStatus();

    public abstract void setMaritalStatus(MaritalStatus status);

    public abstract Country getCountry();

    public abstract void setCountry(Country country);

    public abstract String getSocialSecurityNumber();

    public abstract void setSocialSecurityNumber(String number);

    public abstract PhysicalAddress getDefaultPhysicalAddress();
}
