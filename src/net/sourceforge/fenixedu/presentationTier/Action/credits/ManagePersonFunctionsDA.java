package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Forwards(value = { @Forward(name = "addPersonFunctionShared", path = "/credits/personFunction/addPersonFunctionShared.jsp"),
	@Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits") })
public class ManagePersonFunctionsDA extends FenixDispatchAction {

    public ActionForward prepareToAddPersonFunctionShared(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	PersonFunctionBean personFunctionBean = getRenderedObject();
	if (personFunctionBean == null) {
	    Teacher teacher = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "teacherOid"));
	    ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId((String) getFromRequest(request,
		    "executionPeriodOid"));
	    personFunctionBean = new PersonFunctionBean(teacher, executionSemester);
	}
	request.setAttribute("personFunctionBean", personFunctionBean);
	return mapping.findForward("addPersonFunctionShared");
    }

    public ActionForward prepareToEditPersonFunctionShared(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	PersonFunctionBean personFunctionBean = getRenderedObject();
	if (personFunctionBean == null) {
	    PersonFunction personFunction = AbstractDomainObject.fromExternalId((String) getFromRequest(request,
		    "personFunctionOid"));
	    personFunctionBean = new PersonFunctionBean(personFunction);
	}
	request.setAttribute("personFunctionBean", personFunctionBean);
	return mapping.findForward("addPersonFunctionShared");
    }

    public ActionForward editPersonFunctionShared(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	PersonFunctionBean personFunctionBean = getRenderedObject();
	try {
	    personFunctionBean.createOrEditPersonFunction();
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	}
	request.setAttribute("personFunctionBean", personFunctionBean);
	return mapping.findForward("addPersonFunctionShared");
    }

    public ActionForward deletePersonFunctionShared(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	PersonFunction personFunction = AbstractDomainObject
		.fromExternalId((String) getFromRequest(request, "personFunctionOid"));
	PersonFunctionBean personFunctionBean = new PersonFunctionBean(personFunction);
	request.setAttribute("teacherOid", personFunction.getPerson().getTeacher().getExternalId());
	request.setAttribute("executionYearOid", getFromRequest(request, "executionYearOid"));
	if (personFunction != null) {
	    personFunctionBean.deletePersonFunction();
	}
	return mapping.findForward("viewAnnualTeachingCredits");
    }

}
