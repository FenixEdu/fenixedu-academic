package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class SeperateExecutionCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTransfer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	IUserView userView = UserView.getUser();

	Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));

	InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) ServiceManagerServiceFactory.executeService(
		"ReadExecutionCourseWithShiftsAndCurricularCoursesByOID", new Object[] { executionCourseId });
	request.setAttribute("infoExecutionCourse", infoExecutionCourse);

	List executionDegrees = (List) ServiceManagerServiceFactory.executeService("ReadExecutionDegreesByExecutionPeriodId",
		new Object[] { infoExecutionCourse.getInfoExecutionPeriod().getIdInternal() });
	transformExecutionDegreesIntoLabelValueBean(executionDegrees);
	request.setAttribute("executionDegrees", executionDegrees);

	List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
	request.setAttribute(SessionConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);

	return mapping.findForward("showSeperationPage");
    }

    private void transformExecutionDegreesIntoLabelValueBean(List executionDegreeList) {
	CollectionUtils.transform(executionDegreeList, new Transformer() {

	    public Object transform(Object arg0) {
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
		StringBuilder label = new StringBuilder(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
			.getTipoCurso().toString());
		label.append(" em ");
		label.append(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());

		return new LabelValueBean(label.toString(), infoExecutionDegree.getIdInternal().toString());
	    }

	});

	Collections.sort(executionDegreeList, new BeanComparator("label"));
	executionDegreeList.add(0, new LabelValueBean("escolher", ""));
    }

    public ActionForward changeDestinationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	prepareTransfer(mapping, form, request, response);

	DynaActionForm dynaActionForm = (DynaActionForm) form;

	String destinationExecutionDegreeId = (String) dynaActionForm.get("destinationExecutionDegreeId");
	String destinationCurricularYear = (String) dynaActionForm.get("destinationCurricularYear");

	if (isSet(destinationExecutionDegreeId) && isSet(destinationCurricularYear)) {
	    IUserView userView = UserView.getUser();

	    InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) request.getAttribute("infoExecutionCourse");

	    Object args[] = { new Integer(destinationExecutionDegreeId),
		    infoExecutionCourse.getInfoExecutionPeriod().getIdInternal(), new Integer(destinationCurricularYear) };
	    List executionCourses = (List) ServiceManagerServiceFactory.executeService(
		    "ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear", args);
	    Collections.sort(executionCourses, new BeanComparator("nome"));
	    request.setAttribute("executionCourses", executionCourses);
	}

	return mapping.findForward("showSeperationPage");
    }

    public ActionForward transfer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixServiceException, FenixFilterException {

	DynaActionForm dynaActionForm = (DynaActionForm) form;

	Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
	String destinationExecutionCourseIDString = (String) dynaActionForm.get("destinationExecutionCourseID");
	String[] shiftIdsToTransfer = (String[]) dynaActionForm.get("shiftIdsToTransfer");
	String[] curricularCourseIdsToTransfer = (String[]) dynaActionForm.get("curricularCourseIdsToTransfer");

	IUserView userView = UserView.getUser();

	Integer destinationExecutionCourseID = null;
	if (destinationExecutionCourseIDString != null && destinationExecutionCourseIDString.length() > 0
		&& StringUtils.isNumeric(destinationExecutionCourseIDString)) {
	    destinationExecutionCourseID = new Integer(destinationExecutionCourseIDString);
	}

	ServiceManagerServiceFactory.executeService("SeperateExecutionCourse", new Object[] { executionCourseId,
		destinationExecutionCourseID, makeIntegerArray(shiftIdsToTransfer),
		makeIntegerArray(curricularCourseIdsToTransfer) });

	return mapping.findForward("returnFromTransfer");
    }

    private Integer[] makeIntegerArray(String[] stringArray) {
	Integer[] integerArray = new Integer[stringArray.length];

	for (int i = 0; i < stringArray.length; i++) {
	    integerArray[i] = new Integer(stringArray[i]);
	}

	return integerArray;
    }

    private boolean isSet(String parameter) {
	return parameter != null && parameter.length() > 0 && StringUtils.isNumeric(parameter);
    }

}