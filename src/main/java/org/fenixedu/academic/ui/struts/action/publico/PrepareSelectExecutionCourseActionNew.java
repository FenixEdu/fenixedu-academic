/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.ui.struts.action.base.FenixContextAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Mota
 */
public class PrepareSelectExecutionCourseActionNew extends FenixContextAction {

    private static final Logger logger = LoggerFactory.getLogger(PrepareSelectExecutionCourseActionNew.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e1) {
            logger.error(e1.getMessage(), e1);
        }

        InfoExecutionPeriod infoExecutionPeriod = RequestUtils.getExecutionPeriodFromRequest(request);

        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

        Integer curricularYear = (Integer) request.getAttribute("curYear");

        Object argsSelectExecutionCourse[] = { infoExecutionDegree, infoExecutionPeriod, curricularYear };

//        List infoExecutionCourses;
//        try {
//            infoExecutionCourses =
//                    (List) ServiceManagerServiceFactory.executeService("SelectExecutionCourseNew", argsSelectExecutionCourse);
//        } catch (FenixServiceException e) {
//            throw new FenixActionException(e);
//        }
//        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
//        request.setAttribute("exeCourseList", infoExecutionCourses);
//        return mapping.findForward("sucess");
        throw new UnsupportedOperationException("Service SelectExecutionCourseNew does not exist!");
    }

}