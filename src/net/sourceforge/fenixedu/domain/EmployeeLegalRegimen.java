package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LegalRegimenType;
import net.sourceforge.fenixedu.util.RegimenType;

import org.joda.time.YearMonthDay;

public class EmployeeLegalRegimen extends EmployeeLegalRegimen_Base {
    
    public EmployeeLegalRegimen(Employee employee, YearMonthDay beginDate, YearMonthDay endDate, LegalRegimenType legalRegimenType, RegimenType regimenType) {
        super();
        super.init(beginDate, endDate, legalRegimenType, regimenType);
        setEmployee(employee);
    }
    
    @Override
    public void delete() {
	setEmployee(null);
	super.delete();
    }
    
    @Override
    public void setEmployee(Employee employee) {
	if(employee == null) {
	    throw new DomainException("error.EmployeeLegalRegimen.empty.employee");
	}
	super.setEmployee(employee);
    }
}
