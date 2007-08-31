package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentSubstitutionsDA extends StudentDismissalsDA {
    
    @Override
    public ActionForward chooseEquivalents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
	DismissalBean dismissalBean = (DismissalBean) getRenderedObject("dismissalBean");
	if(dismissalBean.getSelectedEnrolments().isEmpty()) {
	    addActionMessage(request, "error.createSubstitution.origin.cannot.be.empty");
	    return prepare(mapping, form, request, response);
	}
	
        return super.chooseEquivalents(mapping, form, request, response);
    }
    
    @Override
    protected String getServiceName() {
        return "CreateNewSubstitutionDismissal";
    }

}
