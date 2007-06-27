package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class DeleteCompetenceCoursesAction extends FenixDispatchAction {
	
    public ActionForward deleteCompetenceCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException{
       	IUserView userView = SessionUtils.getUserView(request);
       	DynaActionForm actionForm = (DynaActionForm) form;
       	
       	Integer[] competenceCoursesIDs = (Integer[]) actionForm.get("competenceCoursesIds");
       	Object[] args = {competenceCoursesIDs};
       	
       	try {
            ServiceUtils.executeService(userView, "DeleteCompetenceCourses", args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
       	return mapping.findForward("showAllCompetenceCourses");
    }
}
