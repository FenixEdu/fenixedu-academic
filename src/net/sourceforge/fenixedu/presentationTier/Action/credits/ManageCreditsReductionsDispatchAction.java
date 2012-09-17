package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ReductionService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ManageCreditsReductionsDispatchAction extends FenixDispatchAction {

    public ActionForward showCreditsReductionsDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
	ExecutionYear executionYear = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionYearOid"));
	Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherOid"));

	List<ReductionService> creditsReductions = new ArrayList<ReductionService>();
	for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
	    TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	    if (teacherService != null) {
		ReductionService reductionService = teacherService.getReductionService();
		if (reductionService != null) {
		    creditsReductions.add(reductionService);
		}
	    }
	}
	Collections.sort(creditsReductions, new BeanComparator("teacherService.executionPeriod"));

	request.setAttribute("creditsReductions", creditsReductions);
	return mapping.findForward("showCreditsReductions");
    }

    public ActionForward editCreditsReduction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId((String) getFromRequest(request,
		"executionPeriodOID"));
	Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherOID"));
	TeacherService teacherService = getTeacherService(teacher, executionSemester);
	ReductionService reductionService = teacherService.getReductionService();
	if (reductionService != null) {
	    request.setAttribute("reductionService", reductionService);
	} else {
	    request.setAttribute("teacherService", teacherService);
	}
	return mapping.findForward("editReductionService");
    }

    @Service
    private TeacherService getTeacherService(Teacher teacher, ExecutionSemester executionSemester) {
	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	if (teacherService == null) {
	    teacherService = new TeacherService(teacher, executionSemester);
	}
	return teacherService;
    }
}
