/*
 * Created on 08/Set/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author asnr and scpo
 *  
 */
public class ViewShiftsAndGroupsAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

        IUserView userView = getUserView(request);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        Integer groupPropertiesCode = Integer.valueOf(groupPropertiesCodeString);
        
        String username = userView.getUtilizador();

        List<InfoExportGrouping> infoExportGroupings = (List<InfoExportGrouping>) ServiceUtils.
                executeService(userView, "ReadExportGroupingsByGrouping", new Object[]{ groupPropertiesCode });
        request.setAttribute("infoExportGroupings", infoExportGroupings);

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups;
        Object[] args = { groupPropertiesCode, username};
        try {
            infoSiteShiftsAndGroups = (InfoSiteShiftsAndGroups) ServiceUtils.executeService(userView,
                    "ReadShiftsAndGroups", args);

        } catch (InvalidSituationServiceException e)
        {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error2 = null;
            error2 = new ActionError("error.noProject");
            actionErrors2.add("error.noProject", error2);
            saveErrors(request, actionErrors2);
            return mapping.findForward("viewExecutionCourseProjects");
        }catch (NotAuthorizedException e) {
			ActionErrors actionErrors2 = new ActionErrors();
			ActionError error2 = null;
			error2 = new ActionError("errors.noStudentInAttendsSet");
			actionErrors2.add("errors.noStudentInAttendsSet", error2);
			saveErrors(request, actionErrors2);
			return mapping.findForward("insucess");
		}catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoSiteShiftsAndGroups", infoSiteShiftsAndGroups);

        return mapping.findForward("sucess");
    }
}