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
/**
 * 
 * Project sop 
 * Package presentationTier.Action.publico 
 * Created on 1/Fev/2003
 */
package org.fenixedu.academic.ui.struts.action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.dto.InfoDegreeCurricularPlan;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.dto.InfoLessonInstanceAggregation;
import org.fenixedu.academic.dto.InfoSiteTimetable;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.publico.ReadExecutionDegreesByExecutionYearAndDegreeInitials;
import org.fenixedu.academic.ui.struts.action.base.FenixAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants;
import org.fenixedu.academic.ui.struts.action.utils.ContextUtils;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Jo�o Mota
 * 
 */
@Mapping(module = "publico", path = "/viewClassTimeTableNew", input = "viewClassTimeTable", formBean = "chooseSearchContextForm",
        scope = "request", validate = false)
@Forwards(value = { @Forward(name = "Sucess", path = "viewClassTimeTable") })
public class ViewClassTimeTableActionNew extends FenixAction {

    private static final Logger logger = LoggerFactory.getLogger(ViewClassTimeTableActionNew.class);

    /**
     * Constructor for ViewClassTimeTableAction.
     */

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        try {
            ContextUtils.setExecutionPeriodContext(request);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        DynaActionForm escolherContextoForm = (DynaActionForm) form;
        String className = request.getParameter("className");
        String indice = (String) escolherContextoForm.get("indice");
        escolherContextoForm.set("indice", indice);
        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getExternalId().toString());

        String classIdString = request.getParameter("classId");
        request.setAttribute("classId", classIdString);
        String degreeInitials = request.getParameter("degreeInitials");
        request.setAttribute("degreeInitials", degreeInitials);
        String nameDegreeCurricularPlan = request.getParameter("nameDegreeCurricularPlan");
        request.setAttribute("nameDegreeCurricularPlan", nameDegreeCurricularPlan);
        String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        String degreeId = request.getParameter("degreeID");
        request.setAttribute("degreeID", degreeId);

        if (classIdString == null) {
            return mapping.getInputForward();
        }

        final SchoolClass schoolClass = FenixFramework.getDomainObject(classIdString);
        InfoExecutionDegree infoExecutionDegree =
                ReadExecutionDegreesByExecutionYearAndDegreeInitials.getInfoExecutionDegree(schoolClass.getExecutionDegree());
        // try {
        // infoExecutionDegree = (InfoExecutionDegree)
        // ReadExecutionDegreesByExecutionYearAndDegreeInitials
        // .run(infoExecutionPeriod
        // .getInfoExecutionYear(), degreeInitials, nameDegreeCurricularPlan);
        // } catch (FenixServiceException e1) {
        // throw new FenixActionException(e1);
        // }
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan();
        request.setAttribute(PresentationConstants.INFO_DEGREE_CURRICULAR_PLAN, infoDegreeCurricularPlan);

        InfoSiteTimetable component = getInfoSiteTimetable(schoolClass);

        request.setAttribute("component", component);
        request.setAttribute("className", className);
        request.setAttribute("schoolClass", schoolClass);
        return mapping.findForward("Sucess");
    }

    private static InfoSiteTimetable getInfoSiteTimetable(SchoolClass domainClass) {
        InfoSiteTimetable component = new InfoSiteTimetable();
        List infoLessonList = null;

        Collection<Shift> shiftList = domainClass.getAssociatedShiftsSet();
        infoLessonList = new ArrayList();

        ExecutionSemester executionSemester = domainClass.getExecutionPeriod();
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionSemester);

        for (Object element : shiftList) {
            Shift shift = (Shift) element;
            infoLessonList.addAll(InfoLessonInstanceAggregation.getAggregations(shift));
        }
        component.setInfoExecutionPeriod(infoExecutionPeriod);

        component.setLessons(infoLessonList);

        return component;
    }

}