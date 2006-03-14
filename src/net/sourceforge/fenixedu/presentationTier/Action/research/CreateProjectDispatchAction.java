package net.sourceforge.fenixedu.presentationTier.Action.research;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateProjectDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
//        
//        List<Project> projects = new ArrayList<Project>();
//
//        for(ProjectParticipation participation : userView.getPerson().getProjectParticipations()) {
//            projects.add(participation.getProject());
//        }
//        request.setAttribute("projects", projects);
        return mapping.findForward("Success");  
    }

}