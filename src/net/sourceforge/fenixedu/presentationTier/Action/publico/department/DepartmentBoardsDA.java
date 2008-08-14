package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteBoardsDA;

public abstract class DepartmentBoardsDA extends UnitSiteBoardsDA {

    @Override
    protected void setUnitContext(HttpServletRequest request) {
	Department department = getDepartment(request);
	if (department != null) {
	    request.setAttribute("department", department);
	}
    }

    public Department getDepartment(HttpServletRequest request) {
	Unit unit = getUnit(request);
	if (unit == null) {
	    return null;
	} else {
	    return unit.getDepartment();
	}
    }

    @Override
    public String getContextParamName() {
	return "selectedDepartmentUnitID";
    }

}
