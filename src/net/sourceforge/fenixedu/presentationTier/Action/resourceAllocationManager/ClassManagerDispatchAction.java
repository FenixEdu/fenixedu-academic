/**
 * Project Sop 
 * 
 * Package presentationTier.Action.sop
 * 
 * Created on 4/Dez/2002
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author jpvl
 * 
 * 
 */
public class ClassManagerDispatchAction extends FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction {
    private static final String CLASS_NAME_PARAM = "className";

    public ActionForward createClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.removeAttribute(SessionConstants.EXECUTION_COURSE_KEY);
	request.removeAttribute(SessionConstants.LESSON_LIST_ATT);
	String className = getClassName(form);

	IUserView userView = UserView.getUser();

	if (className != null && !className.equals("")) {

	    InfoClass classView = getInfoTurma(userView, className, request);

	    if (classView == null) {
		InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request
			.getAttribute(SessionConstants.CURRICULAR_YEAR);
		Integer curricularYear = infoCurricularYear.getYear();
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
			.getAttribute(SessionConstants.EXECUTION_DEGREE);
		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
			.getAttribute(SessionConstants.EXECUTION_PERIOD);

		InfoClass infoClass = null;

		Object argsCriarTurma[] = { className, curricularYear, infoExecutionDegree, infoExecutionPeriod };

		try {
		    infoClass = (InfoClass) ServiceUtils.executeService("CriarTurma", argsCriarTurma);
		    request.setAttribute(SessionConstants.CLASS_VIEW, infoClass);
		} catch (ExistingServiceException e) {
		    throw new ExistingActionException("A SchoolClass", e);
		}
		return viewClass(mapping, form, request, response);
	    }

	    addErrorMessage(request, "existingClass", "errors.existClass", className);
	    return mapping.getInputForward();

	}
	return mapping.getInputForward();

    }

    public ActionForward editClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String className = getClassName(form);

	IUserView userView = UserView.getUser();
	boolean change = request.getParameter("change") != null;

	if (change) {

	    InfoClass oldClassView = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

	    if (oldClassView == null) {
		addErrorMessage(request, "errors.unknownClass", "errors.unknownClass", className);
		return mapping.getInputForward();

	    }
	    InfoClass newClassView = null;
	    Object[] argsEditarTurma = { oldClassView, newClassView };
	    try {

		newClassView = (InfoClass) ServiceUtils.executeService("EditarTurma", argsEditarTurma);
	    } catch (ExistingServiceException ex) {
		throw new ExistingActionException("A SchoolClass", ex);
	    } catch (NotAuthorizedException e) {
		throw e;
	    } catch (FenixServiceException e) {
		addErrorMessage(request, "existingClass", "errors.existClass", className);
		return mapping.getInputForward();
	    }

	    request.setAttribute(SessionConstants.CLASS_VIEW, newClassView);

	} else {
	    /** starting editing */
	    request.setAttribute(SessionConstants.CLASS_VIEW, getInfoTurma(userView, className, request));
	}

	setLessonListToSession(request, userView, className);

	return mapping.getInputForward();
    }

    public ActionForward viewClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// String className = getClassName(form);
	IUserView userView = UserView.getUser();
	// InfoClass classView = getInfoTurma(userView, className, request);
	InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);
	setLessonListToSession(request, userView, infoClass.getNome());
	// request.setAttribute(SessionConstants.CLASS_VIEW, classView);

	return mapping.getInputForward();
    }

    public ActionForward deleteClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

	Object argsApagarTurma[] = { infoClass };
	ServiceUtils.executeService("ApagarTurma", argsApagarTurma);

	request.removeAttribute(SessionConstants.CLASS_VIEW);

	return mapping.findForward("listClasses");
    }

    private String getClassName(ActionForm form) {
	DynaValidatorForm classForm = (DynaValidatorForm) form;

	return (String) classForm.get(CLASS_NAME_PARAM);

    }

    private InfoClass getInfoTurma(IUserView userView, String className, HttpServletRequest request) throws Exception {
	/* :FIXME: put this 2 variables into parameters */
	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
	InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute(SessionConstants.EXECUTION_DEGREE);

	Object argsLerTurma[] = { className, infoExecutionDegree, infoExecutionPeriod };
	InfoClass classView = (InfoClass) ServiceUtils.executeService("LerTurma", argsLerTurma);
	return classView;
    }

    private void setLessonListToSession(HttpServletRequest request, IUserView userView, String className) throws Exception {

	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
	InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute(SessionConstants.EXECUTION_DEGREE);

	InfoClass infoClass = null;
	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
	for (final SchoolClass schoolClass : executionDegree.getSchoolClassesSet()) {
	    if (schoolClass.getExecutionPeriod().getIdInternal().equals(infoExecutionPeriod.getIdInternal())
		    && schoolClass.getNome().equals(className)) {
		infoClass = InfoClass.newInfoFromDomain(schoolClass);
		break;
	    }
	}

	Object argsApagarTurma[] = { infoClass };

	List lessonList = (ArrayList) ServiceUtils.executeService("LerAulasDeTurma", argsApagarTurma);

	request.setAttribute(SessionConstants.LESSON_LIST_ATT, lessonList);

    }

}