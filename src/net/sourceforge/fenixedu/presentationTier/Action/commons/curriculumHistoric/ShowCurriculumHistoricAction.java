/*
 * Created on Oct 11, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric.InfoCurriculumHistoricReport;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author nmgo
 * @author lmre
 */
public class ShowCurriculumHistoricAction extends FenixDispatchAction {

    public ActionForward showCurriculumHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	final Integer curricularCourseOID = getIntegerFromRequest(request, "curricularCourseCode");
	final Integer semester = getIntegerFromRequest(request, "semester");
	final Integer year = getIntegerFromRequest(request, "year");
	AcademicInterval academicInterval = AcademicInterval.getAcademicIntervalFromResumedString(request
		.getParameter("academicInterval"));

	CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseOID);

	request.setAttribute("infoCurriculumHistoricReport", new InfoCurriculumHistoricReport(academicInterval
		.getChildAcademicInterval(AcademicPeriod.SEMESTER, semester), curricularCourse));

	return mapping.findForward("show-report");
    }

}
