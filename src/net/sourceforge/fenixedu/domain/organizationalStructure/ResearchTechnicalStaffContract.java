package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Person;

import org.joda.time.YearMonthDay;

public class ResearchTechnicalStaffContract extends ResearchTechnicalStaffContract_Base {
    
    public  ResearchTechnicalStaffContract(Person person, YearMonthDay beginDate, YearMonthDay endDate, ResearchUnit unit,
	    Boolean isExternalContract) {
	 super();
	 initResearchContract(person, beginDate, endDate, unit, isExternalContract);
    }
}
