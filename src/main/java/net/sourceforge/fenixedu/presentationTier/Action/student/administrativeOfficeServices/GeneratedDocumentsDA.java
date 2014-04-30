package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = StudentAcademicOfficeServices.class, path = "annual-irs-documents",
        titleKey = "label.documents.anualIRS")
@Mapping(path = "/generatedDocuments", module = "student")
@Forwards(@Forward(name = "showAnnualIRSDocuments",
        path = "/student/administrativeOfficeServices/generatedDocuments/showAnnualIRSDocuments.jsp"))
public class GeneratedDocumentsDA extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {

        request.setAttribute("person", AccessControl.getPerson());

        request.setAttribute("annualIRSDocuments", AccessControl.getPerson().getAnnualIRSDocuments());

        return mapping.findForward("showAnnualIRSDocuments");
    }

}
