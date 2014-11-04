/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Dec 13, 2005
 */
package org.fenixedu.academic.ui.struts.action.credits;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.service.services.credits.ReadAllTeacherCredits;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import org.fenixedu.academic.dto.credits.CreditLineDTO;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShowAllTeacherCreditsResumeAction extends FenixDispatchAction {

    protected void readAllTeacherCredits(HttpServletRequest request, Teacher teacher) throws FenixActionException {

        request.setAttribute("teacher", teacher);
        Department department = teacher.getDepartment();
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