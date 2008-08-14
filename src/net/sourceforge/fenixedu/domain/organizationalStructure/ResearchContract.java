package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public abstract class ResearchContract extends ResearchContract_Base {

    public void initResearchContract(Person person, YearMonthDay beginDate, YearMonthDay endDate, ResearchUnit unit,
	    Boolean isExternalContract) {

	for (Accountability accountability : person.getParentAccountabilities(AccountabilityTypeEnum.RESEARCH_CONTRACT)) {
	    ResearchContract contract = (ResearchContract) accountability;
	    if (contract.getUnit().equals(unit) && beginDate.equals(contract.getBeginDate())) {
		throw new DomainException("error.contract.already.exists");
	    }
	}

	super.init(person, beginDate, endDate, unit);
	setExternalContract(isExternalContract);
	setAccountabilityType(AccountabilityType.readAccountabilityTypeByType(AccountabilityTypeEnum.RESEARCH_CONTRACT));
    }

    public static ResearchContract createResearchContract(ResearchContractType contractType, Person person,
	    YearMonthDay beginDate, YearMonthDay endDate, ResearchUnit unit, Boolean isExternalContract) {

	switch (contractType) {
	case RESEARCHER_CONTRACT:
	    return new ResearcherContract(person, beginDate, endDate, unit, isExternalContract);
	case TECHNICAL_STAFF_CONTRACT:
	    return new ResearchTechnicalStaffContract(person, beginDate, endDate, unit, isExternalContract);
	case SCHOLARSHIP_CONTRACT:
	    return new ResearchScholarshipContract(person, beginDate, endDate, unit, isExternalContract);
	case INTERNSHIP_CONTRACT:
	    return new ResearchInternshipContract(person, beginDate, endDate, unit, isExternalContract);
	default:
	    return null;
	}

    }

    public enum ResearchContractType {
	RESEARCHER_CONTRACT, TECHNICAL_STAFF_CONTRACT, SCHOLARSHIP_CONTRACT, INTERNSHIP_CONTRACT;

	public String getName() {
	    return name();
	}
    }
}
