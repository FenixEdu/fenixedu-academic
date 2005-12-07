package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Function extends Function_Base {

    public boolean isInherentFunction() {
        if (this.getParentInherentFunction() != null) {
            return true;
        }
        return false;
    }

    public void edit(String functionName, Date beginDate, Date endDate, FunctionType type, IUnit unit,
            IFunction parentInherentFunction) {       
        this.setName(functionName);
        this.setBeginDate(beginDate);
        if(endDate != null && endDate.before(beginDate)){
            throw new DomainException("error.endDateBeforeBeginDate");
        }
        this.setEndDate(endDate);
        this.setType(type);
        this.setUnit(unit);
        this.setParentInherentFunction(parentInherentFunction);
    }

    public boolean isActive(Date currentDate) {
        if (this.getEndDate() == null
                || (this.getEndDate() != null && (this.getEndDate().after(currentDate) || this
                        .getEndDate().equals(currentDate)))) {
            return true;
        }
        return false;
    }

    public void delete() {
        if (this.getPersonFunctions().isEmpty() && this.getInherentFunctions().isEmpty()) {
            this.setParentInherentFunction(null);
            this.setUnit(null);
            super.deleteDomainObject();
        } else {
            throw new DomainException("error.delete.function");
        }
    }
}
