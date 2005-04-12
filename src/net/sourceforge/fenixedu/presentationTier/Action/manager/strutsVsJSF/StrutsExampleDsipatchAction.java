package net.sourceforge.fenixedu.presentationTier.Action.manager.strutsVsJSF;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StrutsExampleDsipatchAction extends FenixDispatchAction {
    
    public ActionForward showFirstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        
        return mapping.findForward("showFirstPage");

        
    }
    
    public ActionForward showExecutionPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
                
        final Object[] args = {};
        Collection infoExecutionPeriods = null;
        try {
            infoExecutionPeriods = (Collection) ServiceUtils
                    .executeService(null, "ReadExecutionPeriods", args);
        } catch (FenixFilterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FenixServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        request.setAttribute("executionPeriods", infoExecutionPeriods);
        
        return mapping.findForward("showListExecutionPeriods");

        
    }

}
