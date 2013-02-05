package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/generatedDocuments", module = "student", formBeanClass = FenixActionForm.class)
@Forwards({

@Forward(name = "showAnnualIRSDocuments", path = "df.page.documents.showAnnualIrsDocuments")

})
public class GeneratedDocumentsDA extends FenixDispatchAction {

    public ActionForward showAnnualIRSDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        request.setAttribute("person", AccessControl.getPerson());

        request.setAttribute("annualIRSDocuments", AccessControl.getPerson().getAnnualIRSDocuments());

        return mapping.findForward("showAnnualIRSDocuments");
    }

}
