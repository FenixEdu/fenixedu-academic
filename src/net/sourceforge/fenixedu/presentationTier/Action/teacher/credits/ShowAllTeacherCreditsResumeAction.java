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
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.CreditLine;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowAllTeacherCreditsResumeAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	IUserView userView = UserView.getUser();

	Teacher teacher = userView.getPerson().getTeacher();
	request.setAttribute("teacher", teacher);

	Object[] args = new Object[] { teacher.getIdInternal() };
	List<CreditLine> creditsLines = (List) ServiceUtils.executeService("ReadAllTeacherCredits", args);
	request.setAttribute("creditsLinesSize", creditsLines.size());

	BeanComparator dateComparator = new BeanComparator("executionPeriod.beginDate");
	Iterator orderedCreditsLines = new OrderedIterator(creditsLines.iterator(), dateComparator);

	request.setAttribute("creditsLines", orderedCreditsLines);
	return mapping.findForward("show-all-credits-resume");
    }
}
