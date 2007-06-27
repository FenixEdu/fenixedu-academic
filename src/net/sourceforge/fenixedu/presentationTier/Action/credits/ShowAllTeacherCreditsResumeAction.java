/**
 * Dec 13, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.CreditLine;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowAllTeacherCreditsResumeAction extends FenixDispatchAction {  
         
    protected void readAllTeacherCredits(HttpServletRequest request, Teacher teacher)
            throws FenixServiceException, FenixFilterException {

        request.setAttribute("teacher", teacher);
        Department department = teacher.getCurrentWorkingDepartment();
        if (department != null) {
            request.setAttribute("department", department.getRealName());
        }

        Object[] args = new Object[] { teacher.getIdInternal() };
        List<CreditLine> creditsLines = (List) ServiceUtils.executeService(SessionUtils.getUserView(request), "ReadAllTeacherCredits", args);

        request.setAttribute("creditsLinesSize", creditsLines.size());

        BeanComparator dateComparator = new BeanComparator("executionPeriod.beginDate");
        Iterator orderedCreditsLines = new OrderedIterator(creditsLines.iterator(), dateComparator);

        request.setAttribute("creditsLines", orderedCreditsLines);
    }
}
