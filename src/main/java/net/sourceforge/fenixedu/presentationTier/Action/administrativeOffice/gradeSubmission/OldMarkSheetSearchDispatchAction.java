package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminMarksheetApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AcademicAdminMarksheetApp.class, path = "old-marksheets", titleKey = "link.old.markSheet.management")
@Mapping(path = "/oldMarkSheetManagement", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/gradeSubmission/oldMarkSheets/markSheetManagement.jsp")
@Forwards({
        @Forward(name = "searchMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/markSheetManagement.jsp"),
        @Forward(name = "viewMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/viewMarkSheet.jsp"),
        @Forward(name = "removeMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/removeMarkSheet.jsp"),
        @Forward(name = "searchMarkSheetFilled",
                path = "/academicAdministration/oldMarkSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "confirmMarkSheet", path = "/academicAdministration/gradeSubmission/oldMarkSheets/confirmMarkSheet.jsp"),
        @Forward(name = "choosePrinter", path = "/academicAdministration/printMarkSheet.do?method=choosePrinterMarkSheet") })
public class OldMarkSheetSearchDispatchAction extends MarkSheetSearchDispatchAction {

    @Override
    @EntryPoint
    public ActionForward prepareSearchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementSearchBean markSheetManagementSearchBean = new MarkSheetManagementSearchBean();
        request.setAttribute("edit", markSheetManagementSearchBean);

        return mapping.findForward("searchMarkSheet");
    }
}
