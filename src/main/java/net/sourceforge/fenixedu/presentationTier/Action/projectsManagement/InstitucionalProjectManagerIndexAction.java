/*
 * Created on Feb 23, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Susana Fernandes
 * 
 */
public class InstitucionalProjectManagerIndexAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws  FenixServiceException {
        return mapping.findForward("success");
    }
}
