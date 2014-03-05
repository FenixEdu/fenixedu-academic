package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPhdApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AcademicAdminPhdApp.class, path = "manage-enrolment-periods",
        titleKey = "label.phd.manage.enrolment.periods", accessGroup = "academic(MANAGE_PHD_ENROLMENT_PERIODS)")
@Mapping(path = "/managePhdEnrolmentPeriods", module = "academicAdministration")
public class PhdProgramEnrolmentPeriodDA extends PhdIndividualProgramProcessDA {

    @Override
    @EntryPoint
    public ActionForward manageEnrolmentPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return super.manageEnrolmentPeriods(mapping, actionForm, request, response);
    }

}
