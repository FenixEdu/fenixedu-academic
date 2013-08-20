/**
 * Dec 13, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.credits.ReadAllTeacherCredits;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowAllTeacherCreditsResumeAction extends FenixDispatchAction {

    protected void readAllTeacherCredits(HttpServletRequest request, Teacher teacher) throws FenixActionException {

        request.setAttribute("teacher", teacher);
        Department department = teacher.getCurrentWorkingDepartment();
        if (department != null) {
            request.setAttribute("department", department.getRealName());
        }

        List<CreditLineDTO> creditsLines;
        try {
            creditsLines = ReadAllTeacherCredits.run(teacher.getExternalId());
        } catch (ParseException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("creditsLinesSize", creditsLines.size());

        BeanComparator dateComparator = new BeanComparator("executionPeriod.beginDate");
        Iterator orderedCreditsLines = new OrderedIterator(creditsLines.iterator(), dateComparator);

        request.setAttribute("creditsLines", orderedCreditsLines);
    }
}