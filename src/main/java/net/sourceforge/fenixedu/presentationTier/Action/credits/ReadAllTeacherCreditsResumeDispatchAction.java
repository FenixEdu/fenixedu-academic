/**
 * Jan 16, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.credits.ReadAllTeacherCredits;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.CreditLine;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadAllTeacherCreditsResumeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException,  FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showTeacherCreditsResume");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherCreditsResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        IUserView userView = UserView.getUser();

        DynaActionForm dynaForm = (DynaActionForm) form;
        String teacherId = dynaForm.getString("teacherId");

        Teacher teacher = Teacher.readByIstId(teacherId);
        dynaForm.set("teacherId", teacher.getIdInternal());
        request.setAttribute("teacher", teacher);

        List<CreditLine> creditsLines = (List) ReadAllTeacherCredits.run(teacher.getIdInternal());
        request.setAttribute("creditsLinesSize", creditsLines.size());

        BeanComparator dateComparator = new BeanComparator("executionPeriod.beginDate");
        Iterator orderedCreditsLines = new OrderedIterator(creditsLines.iterator(), dateComparator);

        request.setAttribute("creditsLines", orderedCreditsLines);
        return mapping.findForward("show-all-credits-resume");
    }

}