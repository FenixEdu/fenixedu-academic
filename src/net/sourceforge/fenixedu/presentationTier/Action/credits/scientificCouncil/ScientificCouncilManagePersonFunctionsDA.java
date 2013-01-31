package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManagePersonFunctionsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/managePersonFunctionsShared", module = "scientificCouncil", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "addPersonFunction", path = "/credits/personFunction/addPersonFunction.jsp"),
		@Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits") })
public class ScientificCouncilManagePersonFunctionsDA extends ManagePersonFunctionsDA {

	public ActionForward prepareToAddPersonFunction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
		PersonFunctionBean personFunctionBean = getRenderedObject();
		if (personFunctionBean == null) {
			Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherOid"));
			ExecutionSemester executionSemester =
					AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionPeriodOid"));
			personFunctionBean = new PersonFunctionBean(teacher, executionSemester);
		}
		request.setAttribute("personFunctionBean", personFunctionBean);
		return mapping.findForward("addPersonFunction");
	}

	public ActionForward prepareToEditPersonFunction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
		PersonFunction personFunction =
				AbstractDomainObject.fromExternalId((String) getFromRequest(request, "personFunctionOid"));
		ExecutionSemester executionSemester =
				AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionPeriodOid"));
		request.setAttribute("personFunctionBean", new PersonFunctionBean(personFunction, executionSemester));
		return mapping.findForward("addPersonFunction");
	}

	public ActionForward editPersonFunction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
		PersonFunctionBean personFunctionBean = getRenderedObject();
		try {
			personFunctionBean.createOrEditPersonFunction();
		} catch (DomainException e) {
			addActionMessage(request, e.getMessage());
			request.setAttribute("personFunctionBean", personFunctionBean);
			return mapping.findForward("addPersonFunction");
		}
		request.setAttribute("teacherOid", personFunctionBean.getTeacher().getExternalId());
		request.setAttribute("executionYearOid", personFunctionBean.getExecutionSemester().getExecutionYear().getExternalId());
		return mapping.findForward("viewAnnualTeachingCredits");
	}
}
