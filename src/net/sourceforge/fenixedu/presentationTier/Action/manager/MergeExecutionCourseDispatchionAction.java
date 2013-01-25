/*
 * Created on 3/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 3/Dez/2003
 * @author Fernanda Quitério 17/Dez/2003
 * 
 */
public class MergeExecutionCourseDispatchionAction extends FenixDispatchAction {

    private Boolean previousOrEqualSemester = false;

    public ActionForward chooseDegreesAndExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	DegreesMergeBean degreeBean = getRenderedObject("degreeBean");
	request.setAttribute("degreeBean", degreeBean);
	RenderUtils.invalidateViewState();

	AcademicInterval choosedSemester = degreeBean.getAcademicInterval();
	AcademicInterval actualSemester = ExecutionSemester.readActualExecutionSemester().getAcademicInterval();

	previousOrEqualSemester = choosedSemester.isBefore(actualSemester) || choosedSemester.isEqualOrEquivalent(actualSemester);

	request.setAttribute("previousOrEqualSemester", previousOrEqualSemester);

	if (degreeBean.getDestinationDegree().getExecutionCourses(degreeBean.getAcademicInterval()).isEmpty()
		&& degreeBean.getSourceDegree().getExecutionCourses(degreeBean.getAcademicInterval()).isEmpty()) {
	    addActionMessage("error", request, "message.merge.execution.courses.degreesHasNoCourses");
	    return mapping.findForward("chooseDegreesAndExecutionPeriod");
	} else {
	    if (degreeBean.getDestinationDegree().getExecutionCourses(degreeBean.getAcademicInterval()).isEmpty()) {
		addActionMessage("error", request, "message.merge.execution.courses.destinationDegreeHasNoCourses");
		return mapping.findForward("chooseDegreesAndExecutionPeriod");
	    } else {
		if (degreeBean.getSourceDegree().getExecutionCourses(degreeBean.getAcademicInterval()).isEmpty()) {
		    addActionMessage("error", request, "message.merge.execution.courses.sourceDegreeHasNoCourses");
		    return mapping.findForward("chooseDegreesAndExecutionPeriod");
		}
	    }
	}
	return mapping.findForward("chooseExecutionCourses");
    }

    public ActionForward prepareChooseDegreesAndExecutionPeriod(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException {
	DegreesMergeBean degreeBean = new DegreesMergeBean();
	request.setAttribute("degreeBean", degreeBean);
	return mapping.findForward("chooseDegreesAndExecutionPeriod");
    }

    public ActionForward mergeExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	DegreesMergeBean degreeBean = getRenderedObject("degreeBean");
	RenderUtils.invalidateViewState();

	ExecutionCourse sourceExecutionCourse = degreeBean.getSourceExecutionCourse();
	ExecutionCourse destinationExecutionCourse = degreeBean.getDestinationExecutionCourse();

	Integer sourceExecutionCourseId = sourceExecutionCourse.getIdInternal();

	Integer destinationExecutionCourseId = destinationExecutionCourse.getIdInternal();

	Object[] args = { destinationExecutionCourseId, sourceExecutionCourseId };

	Boolean error = false;

	try {
	    ServiceUtils.executeService("MergeExecutionCourses", args);
	} catch (FenixServiceException fse) {
	    error = true;
	    addActionMessageLiteral("errorFenixException", request, fse.getMessage());
	}

	if (!error) {
	    addActionMessage("success", request, "message.merge.execution.courses.success");
	}
	return mapping.findForward("sucess");
    }

    public static class DegreesMergeBean implements Serializable {

	private static final long serialVersionUID = -5030417665530169855L;

	private Degree sourceDegree;

	private Degree destinationDegree;

	private ExecutionCourse sourceExecutionCourse;

	private ExecutionCourse destinationExecutionCourse;

	private AcademicInterval academicInterval;

	public ExecutionCourse getSourceExecutionCourse() {
	    return sourceExecutionCourse;
	}

	public void setSourceExecutionCourse(ExecutionCourse sourceExecutionCourse) {
	    this.sourceExecutionCourse = sourceExecutionCourse;
	}

	public ExecutionCourse getDestinationExecutionCourse() {
	    return destinationExecutionCourse;
	}

	public void setDestinationExecutionCourse(ExecutionCourse destinationExecutionCourse) {
	    this.destinationExecutionCourse = destinationExecutionCourse;
	}

	public AcademicInterval getAcademicInterval() {
	    return academicInterval;
	}

	public void setAcademicInterval(AcademicInterval academicInterval) {
	    this.academicInterval = academicInterval;
	}

	public Degree getSourceDegree() {
	    return sourceDegree;
	}

	public void setSourceDegree(Degree sourceDegree) {
	    this.sourceDegree = sourceDegree;
	}

	public Degree getDestinationDegree() {
	    return destinationDegree;
	}

	public void setDestinationDegree(Degree destinationDegree) {
	    this.destinationDegree = destinationDegree;
	}
    }

}