package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.alumni.CreateFormation;
import net.sourceforge.fenixedu.applicationTier.Servico.alumni.EditFormation;
import net.sourceforge.fenixedu.applicationTier.Servico.person.qualification.DeleteQualification;
import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormationBean;
import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "alumni", path = "/formation", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "alumniCreateFormation", path = "/alumni/alumniManageFormation.jsp", tileProperties = @Tile(
                title = "private.alumni.trainingandeducation.training")),
        @Forward(name = "alumniEditFormation", path = "/alumni/alumniManageFormation.jsp", tileProperties = @Tile(
                title = "private.alumni.trainingandeducation.training")),
        @Forward(name = "viewAlumniQualifications", path = "/alumni/viewAlumniQualifications.jsp", tileProperties = @Tile(
                title = "private.alumni.trainingandeducation.training")) })
public class AlumniFormationManagementDA extends AlumniEntityManagementDA {

    public ActionForward initFormationManagement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alumniFormationBean", new AlumniFormationBean(getAlumniFromLoggedPerson(request)));
        return mapping.findForward("viewAlumniQualifications");
    }

    public ActionForward innerFormationManagement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("educationFormationList", getAlumniFromLoggedPerson(request).getFormations());
        return mapping.findForward("viewAlumniQualifications");
    }

    public ActionForward initFormationCreation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alumniFormation", new AlumniFormation());
        request.setAttribute("formationUpdate", "true");
        return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward updateAlumniFormationTypePostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("alumniFormation", getObjectFromViewState("alumniFormation"));
        AlumniFormation formationInfo = (AlumniFormation) getObjectFromViewState("alumniFormationDegree");
        formationInfo.updateTypeSchema();
        RenderUtils.invalidateViewState("alumniFormationDegree");
        request.setAttribute("alumniFormationDegree", formationInfo);
        request.setAttribute("formationEducationArea", getFromRequest(request, "formationEducationArea"));
        request.setAttribute("formationUpdate", "true");
        return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward updateAlumniFormationInfoPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setAttribute("alumniFormation", getObjectFromViewState("alumniFormation"));
        AlumniFormation formationInfo = (AlumniFormation) getObjectFromViewState("alumniFormationInstitution");
        formationInfo.updateInstitutionSchema();
        RenderUtils.invalidateViewState("alumniFormationInstitution");
        request.setAttribute("alumniFormationInstitution", formationInfo);
        request.setAttribute("formationEducationArea", getFromRequest(request, "formationEducationArea"));
        request.setAttribute("formationUpdate", "true");
        return mapping.findForward("alumniCreateFormation");
    }

    public ActionForward manageAlumniQualification(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniFormation formationInfo = (AlumniFormation) getObjectFromViewState("alumniFormation");
        formationInfo.setEducationArea(getIntegerFromRequest(request, "formationEducationArea"));

        try {
            if (formationInfo.hasAssociatedFormation()) {
                EditFormation.runEditFormation(formationInfo);
            } else {
                CreateFormation.runCreateFormation(getAlumniFromLoggedPerson(request), formationInfo);
            }
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            request.setAttribute("alumniFormation", getObjectFromViewState("alumniFormation"));
            request.setAttribute("alumniFormationDegree", getObjectFromViewState("alumniFormationDegree"));
            request.setAttribute("alumniFormationInstitution", getObjectFromViewState("alumniFormationInstitution"));
            request.setAttribute("formationEducationArea", getFromRequest(request, "formationEducationArea"));
            request.setAttribute("formationUpdate", "true");
            return mapping.findForward("alumniCreateFormation");
        }

        return innerFormationManagement(mapping, actionForm, request, response);
    }

    public ActionForward prepareFormationEdit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("formationUpdate", "true");
        return getFormation(mapping, request);
    }

    private ActionForward getFormation(ActionMapping mapping, HttpServletRequest request) {
        final Integer formationId = getIntegerFromRequest(request, "formationId");
        final Qualification qualification = RootDomainObject.getInstance().readQualificationByOID(formationId);
        final AlumniFormation formation = AlumniFormation.buildFrom((Formation) qualification);
        request.setAttribute("formationEducationArea", formation.getEducationArea().getExternalId());
        request.setAttribute("alumniFormation", formation);
        return mapping.findForward("alumniEditFormation");
    }

    public ActionForward deleteFormation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (getFromRequest(request, "cancel") == null) {
            try {
                DeleteQualification.runDeleteQualification(getIntegerFromRequest(request, "formationId"));
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            }
        }

        return innerFormationManagement(mapping, actionForm, request, response);
    }
}