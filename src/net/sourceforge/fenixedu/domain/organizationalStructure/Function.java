package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;

public class Function extends Function_Base {

    public boolean isInherentFunction() {
        return (this.getParentInherentFunction() != null);
    }

    public void edit(String functionName, Date beginDate, Date endDate, FunctionType type, Unit unit,
            Function parentInherentFunction) {

        if(functionName == null && functionName.equals("")){
            throw new DomainException("error.no.function.name");
        }
        this.setName(functionName);        
        this.setBeginDate(beginDate);
        if (endDate != null && endDate.before(beginDate)) {
            throw new DomainException("error.endDateBeforeBeginDate");
        }
        this.setEndDate(endDate);
        this.setFunctionType(type);
        this.setUnit(unit);
        this.setParentInherentFunction(parentInherentFunction);
        this.setType(AccountabilityTypeEnum.MANAGEMENT_FUNCTION);
    }

    public boolean isActive(Date currentDate) {
        return (this.getEndDate() == null || (DateFormatUtil.equalDates("yyyyMMdd", this.getEndDate(),
                currentDate) || this.getEndDate().after(currentDate)));
    }

    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        return (!this.getBeginDate().after(endDate) && (this.getEndDate() == null || !this.getEndDate()
                .before(beginDate)));
    }

    public void delete() {
        if (!hasAnyAccountabilities() && !hasAnyInherentFunctions()) {
            removeParentInherentFunction();
            removeUnit();
            removeRootDomainObject();
            deleteDomainObject();
        } else {
            throw new DomainException("error.delete.function");
        }
    }

    public List<PersonFunction> getPersonFunctions() {
        List<PersonFunction> personFunctions = new ArrayList();
        for (Accountability accountability : getAccountabilities()) {
            personFunctions.add((PersonFunction) accountability);
        }
        return personFunctions;
    }
}
