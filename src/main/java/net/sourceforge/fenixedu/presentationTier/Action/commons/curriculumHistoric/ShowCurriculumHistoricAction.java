/*
 * Created on Oct 11, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric.InfoCurriculumHistoricReport;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author nmgo
 * @author lmre
 */
@Mapping(path = "/showCurriculumHistoric", module = "academicAdministration")
@Forwards({ @Forward(name = "show-report", path = "/commons/curriculumHistoric/showCurriculumHistoricReport.jsp") })
public class ShowCurriculumHistoricAction extends FenixDispatchAction {

    public ActionForward showCurriculumHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final Integer curricularCourseOID = getIntegerFromRequest(request, "curricularCourseCode");
        final Integer semester = getIntegerFromRequest(request, "semester");
        final AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString(request.getParameter("academicInterval"));

        final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseOID);

        final AcademicInterval interval =
                curricularCourse.isAnual() ? academicInterval : academicInterval.getChildAcademicInterval(
                        AcademicPeriod.SEMESTER, semester);

        request.setAttribute("infoCurriculumHistoricReport", new InfoCurriculumHistoricReport(interval, curricularCourse));

        return mapping.findForward("show-report");
    }

}
