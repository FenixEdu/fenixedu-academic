package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadDegreesClassesLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewAllClassesSchedulesDA extends FenixContextDispatchAction {

	public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AcademicInterval academicInterval =
				AcademicInterval.getAcademicIntervalFromResumedString((String) request
						.getAttribute(PresentationConstants.ACADEMIC_INTERVAL));
		// setExecutionContext(request);
		/* Cria o form bean com as licenciaturas em execucao. */

		List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
		for (ExecutionDegree executionDegree : ExecutionDegree.filterByAcademicInterval(academicInterval)) {
			final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
			infoExecutionDegreeList.add(infoExecutionDegree);
		}

		Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());
		MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
		infoExecutionDegreeList = InfoExecutionDegree.buildLabelValueBeansForList(infoExecutionDegreeList, messageResources);

		request.setAttribute(PresentationConstants.INFO_EXECUTION_DEGREE_LIST, infoExecutionDegreeList);

		return mapping.findForward("choose");
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm chooseViewAllClassesSchedulesContextForm = (DynaActionForm) form;

		AcademicInterval academicInterval =
				AcademicInterval.getAcademicIntervalFromResumedString((String) request
						.getAttribute(PresentationConstants.ACADEMIC_INTERVAL));
		// setExecutionContext(request);

		List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
		for (ExecutionDegree executionDegree : ExecutionDegree.filterByAcademicInterval(academicInterval)) {
			final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
			infoExecutionDegreeList.add(infoExecutionDegree);
		}

		Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

		Boolean selectAllDegrees = (Boolean) chooseViewAllClassesSchedulesContextForm.get("selectAllDegrees");
		List selectedInfoExecutionDegrees = null;
		if (selectAllDegrees != null && selectAllDegrees.booleanValue()) {
			selectedInfoExecutionDegrees = infoExecutionDegreeList;
		} else {
			String[] selectedDegreesIndexes = (String[]) chooseViewAllClassesSchedulesContextForm.get("selectedDegrees");
			selectedInfoExecutionDegrees = new ArrayList();

			for (String selectedDegreesIndexe : selectedDegreesIndexes) {
				Integer index = new Integer("" + selectedDegreesIndexe);
				InfoExecutionDegree infoEd = infoExecutionDegreeList.get(index.intValue());

				selectedInfoExecutionDegrees.add(infoEd);
			}
		}

		List infoViewClassScheduleList = ReadDegreesClassesLessons.run(selectedInfoExecutionDegrees, academicInterval);

		if (infoViewClassScheduleList != null && infoViewClassScheduleList.isEmpty()) {
			request.removeAttribute(PresentationConstants.ALL_INFO_VIEW_CLASS_SCHEDULE);
		} else {
			Collections.sort(infoViewClassScheduleList, new BeanComparator("infoClass.nome"));
			request.setAttribute(PresentationConstants.ALL_INFO_VIEW_CLASS_SCHEDULE, infoViewClassScheduleList);
		}

		return mapping.findForward("list");
	}
}