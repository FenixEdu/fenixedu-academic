package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/degreeJurisdiction", module = "academicAdministration")
@Forwards({ @Forward(name = "manageJurisdictions", path = "/academicAdministration/degreeJurisdictions/manageJurisdictions.jsp") })
public class DegreeJurisdictionManagementDispacthAction extends FenixDispatchAction {
    public ActionForward manageJurisdictions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeJurisdictionManagementBean bean = new DegreeJurisdictionManagementBean();
        AdministrativeOffice alameda = FenixFramework.getDomainObject("2461016260610");
        AdministrativeOffice tagus = FenixFramework.getDomainObject("2461016260611");
        AdministrativeOffice posgrad = FenixFramework.getDomainObject("2461016260609");

        request.setAttribute("degrees", bean);
        request.setAttribute("alamedaOffice", alameda);
        request.setAttribute("tagusOffice", tagus);
        request.setAttribute("posgradOffice", posgrad);
        return mapping.findForward("manageJurisdictions");
    }

    public ActionForward changeDegreeJurisdiction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        String programOid = request.getParameter("programOid");
        String officeOid = request.getParameter("officeOid");
        String togglePreset = request.getParameter("togglePreset");
        if (togglePreset != null) {
            request.setAttribute("togglePreset", togglePreset);
        }

        changeDegreeJurisdiction(programOid, officeOid);

        return manageJurisdictions(mapping, actionForm, request, response);
    }

    @Atomic
    private void changeDegreeJurisdiction(String programOid, String officeOid) {
        AcademicProgram program = FenixFramework.getDomainObject(programOid);
        AdministrativeOffice office = FenixFramework.getDomainObject(officeOid);

        program.setAdministrativeOffice(office);
        if (program instanceof Degree) {
            Degree degree = (Degree) program;
            if (degree.getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA && degree.hasPhdProgram()) {
                degree.getPhdProgram().setAdministrativeOffice(office);
            }
        }
    }
}
