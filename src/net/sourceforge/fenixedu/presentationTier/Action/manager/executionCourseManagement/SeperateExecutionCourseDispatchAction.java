package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCourseWithShiftsAndCurricularCoursesByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
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
	Integer executionDegreeID = getIntegerFromRequest(request, "originExecutionDegreeID");
	Integer curricularYearID = getIntegerFromRequest(request, "curricularYearId");

	InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseWithShiftsAndCurricularCoursesByOID.run(executionCourseId);
	request.setAttribute("infoExecutionCourse", infoExecutionCourse);

	List executionDegrees = ReadExecutionDegreesByExecutionPeriodId.run(infoExecutionCourse.getInfoExecutionPeriod()
		.getIdInternal());
	transformExecutionDegreesIntoLabelValueBean(executionDegrees);
	request.setAttribute("executionDegrees", executionDegrees);

	List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
	request.setAttribute(PresentationConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);
	request.setAttribute("originExecutionDegreeID", executionDegreeID);
	request.setAttribute("curricularYearId", curricularYearID);
	return mapping.findForward("showSeperationPage");
    }

    private void transformExecutionDegreesIntoLabelValueBean(List executionDegreeList) {
	CollectionUtils.transform(executionDegreeList, new Transformer() {

	    @Override
	    public Object transform(Object arg0) {
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;
		StringBuilder label = new StringBuilder(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
			.getDegreeType().toString());
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

	    List executionCourses = (List) ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear.run(new Integer(
		    destinationExecutionDegreeId), infoExecutionCourse.getInfoExecutionPeriod().getIdInternal(), new Integer(
		    destinationCurricularYear));
	    Collections.sort(executionCourses, new BeanComparator("nome"));
	    request.setAttribute("executionCourses", executionCourses);
	}

	return mapping.findForward("showSeperationPage");
    }

    public ActionForward transfer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixServiceException, FenixFilterException {
	/**/
	ExecutionCourseBean bean = new ExecutionCourseBean();

	/**/
	DynaActionForm dynaActionForm = (DynaActionForm) form;

	Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
	String destinationExecutionCourseIDString = (String) dynaActionForm.get("destinationExecutionCourseID");
	Integer originExecutionDegreeID = (Integer) dynaActionForm.get("originExecutionDegreeID");
	Integer curricularYearID = (Integer) dynaActionForm.get("curricularYearId");
	String[] shiftIdsToTransfer = (String[]) dynaActionForm.get("shiftIdsToTransfer");
	String[] curricularCourseIdsToTransfer = (String[]) dynaActionForm.get("curricularCourseIdsToTransfer");

	IUserView userView = UserView.getUser();

	Integer destinationExecutionCourseID = null;
	if (destinationExecutionCourseIDString != null && destinationExecutionCourseIDString.length() > 0
		&& StringUtils.isNumeric(destinationExecutionCourseIDString)) {
	    destinationExecutionCourseID = new Integer(destinationExecutionCourseIDString);
	}

	// SeperateExecutionCourse.run(executionCourseId,
	// destinationExecutionCourseID, makeIntegerArray(shiftIdsToTransfer),
	// makeIntegerArray(curricularCourseIdsToTransfer));

	ExecutionDegree originExecutionDegree = rootDomainObject.readExecutionDegreeByOID(originExecutionDegreeID);
	final ExecutionCourse originExecutionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	final CurricularYear curricularYear = rootDomainObject.readCurricularYearByOID(curricularYearID);

	bean.setExecutionDegree(originExecutionDegree);
	bean.setChooseNotLinked(false);
	bean.setSourceExecutionCourse(originExecutionCourse);
	bean.setExecutionSemester(originExecutionCourse.getExecutionPeriod());
	bean.setCurricularYear(curricularYear);
	request.setAttribute("originExecutionDegree", originExecutionDegree);

	Boolean transferSucess = true;
	request.setAttribute("transferSucess", transferSucess);
	request.setAttribute("sessionBean", bean);

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