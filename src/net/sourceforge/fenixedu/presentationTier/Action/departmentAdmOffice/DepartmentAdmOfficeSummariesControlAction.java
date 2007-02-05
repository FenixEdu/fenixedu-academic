package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.SummariesControlAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.util.LabelValueBean;

public class DepartmentAdmOfficeSummariesControlAction extends SummariesControlAction {

    @Override
    protected void readAndSaveAllDepartments(HttpServletRequest request) throws FenixFilterException,
	    FenixServiceException {

	List<LabelValueBean> departments = new ArrayList<LabelValueBean>();
	Person person = SessionUtils.getUserView(request).getPerson();
	if (person.getEmployee() != null) {
	    Department department = person.getEmployee().getCurrentDepartmentWorkingPlace();
	    if (department != null) {
		LabelValueBean bean = new LabelValueBean();
		bean.setLabel(department.getRealName());
		bean.setValue(department.getIdInternal().toString());
		departments.add(bean);
	    }	    
	}
	request.setAttribute("departments", departments);
    }

}
