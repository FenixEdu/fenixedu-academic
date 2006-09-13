package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.collections.Table;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowExecutionCourseSitesDispatchAction extends FenixContextDispatchAction {

    public ActionForward listSites(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	// degreeID
	final Integer degreeOID = getFromRequest("degreeOID", request);
	request.setAttribute("degreeID", degreeOID);

	final Degree degree = getDegreeAndSetInfoDegree(request, degreeOID);
	final List<ExecutionCourseView> executionCourseViews = getExecutionCourseViews(request, degree);
	final InfoExecutionPeriod infoExecutionPeriod = getPreviousExecutionPeriod(request);

	organizeExecutionCourseViews(request, executionCourseViews, infoExecutionPeriod);

	return mapping.findForward("show-exeution-course-site-list");
    }

    private Degree getDegreeAndSetInfoDegree(HttpServletRequest request, Integer degreeOID) {
	final Degree degree = rootDomainObject.readDegreeByOID(degreeOID);

	final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
	infoDegree.prepareEnglishPresentation(getLocale(request));
	request.setAttribute("infoDegree", infoDegree);

	return degree;
    }

    private InfoExecutionPeriod getPreviousExecutionPeriod(HttpServletRequest request) {
	final InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);
	final InfoExecutionPeriod previousInfoExecutionPeriod = infoExecutionPeriod.getPreviousInfoExecutionPeriod();

	request.setAttribute("previousInfoExecutionPeriod", previousInfoExecutionPeriod);
	return previousInfoExecutionPeriod;
    }

    private List<ExecutionCourseView> getExecutionCourseViews(HttpServletRequest request, Degree degree)
	    throws FenixServiceException, FenixFilterException {
	final Object[] args = { degree };
	List<ExecutionCourseView> result = (List) ServiceManagerServiceFactory.executeService(null, "ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree", args);

	BeanComparator beanComparator = new BeanComparator("executionCourseName", Collator.getInstance());
	Collections.sort(result, beanComparator);

	return result;
    }

    private void organizeExecutionCourseViews(HttpServletRequest request, List<ExecutionCourseView> executionCourseViews, InfoExecutionPeriod previousExecutionPeriod) {
	Table executionCourseViewsTableCurrent1_2 = new Table(2);
	Table executionCourseViewsTableCurrent3_4 = new Table(2);
	Table executionCourseViewsTableCurrent5 = new Table(1);
	Table executionCourseViewsTablePrevious1_2 = new Table(2);
	Table executionCourseViewsTablePrevious3_4 = new Table(2);
	Table executionCourseViewsTablePrevious5 = new Table(1);

	for (ExecutionCourseView executionCourseView : executionCourseViews) {
	    int curricularYear = executionCourseView.getCurricularYear();
	    boolean previousExecPeriod = previousExecutionPeriod != null && executionCourseView.getExecutionPeriodOID().equals(previousExecutionPeriod.getIdInternal());

	    switch (curricularYear) {
	    case 1:
	    case 2:
		if (previousExecPeriod) {
		    executionCourseViewsTablePrevious1_2.appendToColumn(curricularYear - 1, executionCourseView);
		} else {
		    executionCourseViewsTableCurrent1_2.appendToColumn(curricularYear - 1, executionCourseView);
		}
		break;
	    case 3:
	    case 4:
		if (previousExecPeriod) {
		    executionCourseViewsTablePrevious3_4.appendToColumn(curricularYear - 3, executionCourseView);
		} else {
		    executionCourseViewsTableCurrent3_4.appendToColumn(curricularYear - 3, executionCourseView);
		}
		break;
	    case 5:
		if (previousExecPeriod) {
		    executionCourseViewsTablePrevious5.appendToColumn(curricularYear - 5, executionCourseView);
		} else {
		    executionCourseViewsTableCurrent5.appendToColumn(curricularYear - 5, executionCourseView);
		}
		break;
	    }
	}

	request.setAttribute("executionCourseViewsTableCurrent1_2", executionCourseViewsTableCurrent1_2);
	request.setAttribute("executionCourseViewsTableCurrent3_4", executionCourseViewsTableCurrent3_4);
	request.setAttribute("executionCourseViewsTableCurrent5", executionCourseViewsTableCurrent5);

	request.setAttribute("executionCourseViewsTablePrevious1_2", executionCourseViewsTablePrevious1_2);
	request.setAttribute("executionCourseViewsTablePrevious3_4", executionCourseViewsTablePrevious3_4);
	request.setAttribute("executionCourseViewsTablePrevious5", executionCourseViewsTablePrevious5);
    }

}
