package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.util.RegimeType;

import org.joda.time.YearMonthDay;

public class EmployeeProfessionalSituation extends EmployeeProfessionalSituation_Base {

    public EmployeeProfessionalSituation(Employee employee, Category category, YearMonthDay beginDate, YearMonthDay endDate,
	    Integer lessonHoursNumber, ProfessionalSituationType legalRegimenType, RegimeType regimenType) {

	super();
	super.init(beginDate, endDate, legalRegimenType, regimenType, employee, category);
    }

    @Override
    public boolean isEmployeeProfessionalSituation() {
	return true;
    }
}
