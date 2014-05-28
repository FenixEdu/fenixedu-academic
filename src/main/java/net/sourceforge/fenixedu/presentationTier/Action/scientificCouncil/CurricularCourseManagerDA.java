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
/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.EditCurriculum;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.InsertCurriculum;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.ReadCurricularCourseByOIdService;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.ReadCurriculumByOIdService;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.ScientificCouncilComponentService;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.ScientificCouncilCurricularCourseCurriculumComponentService;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.SetBasicCurricularCoursesService;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBasicCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteDegreeCurricularPlans;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSCDegrees;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head presentationTier.Action.scientificCouncil
 * 
 */
public class CurricularCourseManagerDA extends FenixDispatchAction {

    public ActionForward prepareSelectDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);
        ISiteComponent component = new InfoSiteSCDegrees();
        readSiteView(request, userView, null, null, null, component);
        return mapping.findForward("selectDegree");

    }

    public ActionForward showDegreeCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        String degreeIdString = request.getParameter("index");

        ISiteComponent component = new InfoSiteDegreeCurricularPlans();
        readSiteView(request, userView, degreeIdString, null, null, component);
        return mapping.findForward("showDegreeCurricularPlans");
    }

    public ActionForward showCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        String degreeCurricularPlanIdString = request.getParameter("index");

        ISiteComponent component = new InfoSiteCurricularCourses();
        readSiteView(request, userView, null, null, degreeCurricularPlanIdString, component);
        return mapping.findForward("showCurricularCourses");
    }

    public ActionForward showBasicCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        String degreeCurricularPlanIdString = request.getParameter("index");

        ISiteComponent component = new InfoSiteBasicCurricularCourses();
        SiteView siteView = readSiteView(request, userView, null, null, degreeCurricularPlanIdString, component);

        DynaActionForm coursesForm = (DynaActionForm) form;
        List curricularCoursesIds = ((InfoSiteBasicCurricularCourses) siteView.getComponent()).getBasicCurricularCoursesIds();
        List nonBasicCurricularCourses =
                ((InfoSiteBasicCurricularCourses) siteView.getComponent()).getNonBasicCurricularCourses();

        String[] formValues = new String[curricularCoursesIds.size() + nonBasicCurricularCourses.size()];
        int i = 0;
        for (Iterator iter = curricularCoursesIds.iterator(); iter.hasNext();) {
            Integer courseId = (Integer) iter.next();
            formValues[i] = courseId.toString();
            i++;
        }

        coursesForm.set("basicCurricularCourses", formValues);
        return mapping.findForward("showCurricularCourses");
    }

    public ActionForward setBasicCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        DynaActionForm basicCoursesList = (DynaActionForm) form;
        String[] coursesIdsString = (String[]) basicCoursesList.get("basicCurricularCourses");
        List<String> coursesIds = new ArrayList<String>();
        for (String element : coursesIdsString) {
            coursesIds.add(element);
        }

        String curricularPlanId = request.getParameter("curricularIndex");

        try {
            SetBasicCurricularCoursesService.run(coursesIds, curricularPlanId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("firstPage");
    }

    public ActionForward viewCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        String curricularCourseIdString = request.getParameter("index");

        SiteView siteView = null;
        try {
            siteView =
                    ScientificCouncilCurricularCourseCurriculumComponentService.run(new InfoSiteCurriculum(),
                            curricularCourseIdString, null);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        return mapping.findForward("viewCurriculum");
    }

    public ActionForward prepareEditCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        String curriculumIdString = request.getParameter("index");

        SiteView siteView = null;
        try {
            siteView = ReadCurriculumByOIdService.run(curriculumIdString);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        return mapping.findForward("editCurriculum");
    }

    public ActionForward editCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        String program = request.getParameter("program");
        String programEn = request.getParameter("programEn");
        String operacionalObjectives = request.getParameter("operacionalObjectives");
        String operacionalObjectivesEn = request.getParameter("operacionalObjectivesEn");
        String generalObjectives = request.getParameter("generalObjectives");
        String generalObjectivesEn = request.getParameter("generalObjectivesEn");
        String curriculumIdString = request.getParameter("curriculumId");

        Boolean result;
        try {
            result =
                    EditCurriculum.run(curriculumIdString, program, programEn, operacionalObjectives, operacionalObjectivesEn,
                            generalObjectives, generalObjectivesEn, new Boolean(true));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (result.booleanValue()) {
            return showCurricularCourses(mapping, form, request, response);
        }
        return null;
    }

    public ActionForward prepareInsertCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        String curricularCourseIdString = request.getParameter("index");

        SiteView siteView = null;
        try {
            siteView = ReadCurricularCourseByOIdService.run(curricularCourseIdString);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        return mapping.findForward("insertCurriculum");
    }

    public ActionForward insertCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        String program = request.getParameter("program");
        String programEn = request.getParameter("programEn");
        String operacionalObjectives = request.getParameter("operacionalObjectives");
        String operacionalObjectivesEn = request.getParameter("operacionalObjectivesEn");
        String generalObjectives = request.getParameter("generalObjectives");
        String generalObjectivesEn = request.getParameter("generalObjectivesEn");
        String curricularCourseIdString = request.getParameter("curricularCourseId");

        Boolean result;
        try {
            result =
                    InsertCurriculum.run(curricularCourseIdString, program, programEn, operacionalObjectives,
                            operacionalObjectivesEn, generalObjectives, generalObjectivesEn, new DateTime(), Boolean.TRUE);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (result.booleanValue()) {
            return showCurricularCourses(mapping, form, request, response);
        }
        return null;
    }

    private SiteView readSiteView(HttpServletRequest request, User userView, String degreeId, Integer coursesIds,
            String degreeCurricularPlanId, ISiteComponent component) throws FenixActionException {

        SiteView siteView = null;
        try {
            siteView = ScientificCouncilComponentService.run(component, degreeId, coursesIds, degreeCurricularPlanId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        return siteView;
    }

}