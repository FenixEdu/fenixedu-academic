/*
 * Created on Feb 23, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.projectsManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
<<<<<<< HEAD
=======
import net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement.ReadUserCostCenters;
import net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement.ReviewProjectAccess;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
>>>>>>> 3e4f245... Refactor: Replace ServiceManagerServiceFactory invocations with direct ones.
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
            throws FenixFilterException, FenixServiceException {
        return mapping.findForward("success");
    }
}
