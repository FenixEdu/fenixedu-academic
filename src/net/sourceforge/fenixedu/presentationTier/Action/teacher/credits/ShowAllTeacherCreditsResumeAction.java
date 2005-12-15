/**
* Dec 13, 2005
*/
package net.sourceforge.fenixedu.presentationTier.Action.teacher.credits;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.credits.CreditLine;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ShowAllTeacherCreditsResumeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        Object args[] = { userView.getUtilizador() };
        ITeacher teacher = (ITeacher) ServiceUtils.executeService(userView,
                "ReadDomainTeacherByUsername", args);
        request.setAttribute("teacher", teacher);

        args[0] = teacher.getIdInternal();
        List<CreditLine> creditsLines = (List) ServiceUtils.executeService(userView, "ReadAllTeacherCredits", args);        

        BeanComparator dateComparator = new BeanComparator("executionPeriod.beginDate");
        Iterator orderedCreditsLines = new OrderedIterator(creditsLines.iterator(),dateComparator);
        
        request.setAttribute("creditsLines", orderedCreditsLines);
        return mapping.findForward("show-all-credits-resume");
    }
}


