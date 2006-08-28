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
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBasicCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteDegreeCurricularPlans;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSCDegrees;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head presentationTier.Action.scientificCouncil
 *  
 */
public class CurricularCourseManagerDA extends FenixDispatchAction {

    public ActionForward prepareSelectDegree(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);
            ISiteComponent component = new InfoSiteSCDegrees();
            readSiteView(request, userView, null, null, null, component);
            return mapping.findForward("selectDegree");
        }
        throw new FenixActionException();

    }

    public ActionForward showDegreeCurricularPlans(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);

            String degreeIdString = request.getParameter("index");

            Integer degreeId = new Integer(degreeIdString);
            ISiteComponent component = new InfoSiteDegreeCurricularPlans();
            readSiteView(request, userView, degreeId, null, null, component);
            return mapping.findForward("showDegreeCurricularPlans");
        }
        throw new FenixActionException();

    }

    public ActionForward showCurricularCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);

            String degreeCurricularPlanIdString = request.getParameter("index");
            Integer degreeCurricularPlanId = new Integer(degreeCurricularPlanIdString);

            ISiteComponent component = new InfoSiteCurricularCourses();
            readSiteView(request, userView, null, null, degreeCurricularPlanId, component);
            return mapping.findForward("showCurricularCourses");
        }
        throw new FenixActionException();

    }

    public ActionForward showBasicCurricularCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);

            String degreeCurricularPlanIdString = request.getParameter("index");
            Integer degreeCurricularPlanId = new Integer(degreeCurricularPlanIdString);

            ISiteComponent component = new InfoSiteBasicCurricularCourses();
            SiteView siteView = readSiteView(request, userView, null, null, degreeCurricularPlanId,
                    component);

            DynaActionForm coursesForm = (DynaActionForm) form;
            List curricularCoursesIds = ((InfoSiteBasicCurricularCourses) siteView.getComponent())
                    .getBasicCurricularCoursesIds();
            List nonBasicCurricularCourses = ((InfoSiteBasicCurricularCourses) siteView.getComponent())
                    .getNonBasicCurricularCourses();

            String[] formValues = new String[curricularCoursesIds.size()
                    + nonBasicCurricularCourses.size()];
            int i = 0;
            for (Iterator iter = curricularCoursesIds.iterator(); iter.hasNext();) {
                Integer courseId = (Integer) iter.next();
                formValues[i] = courseId.toString();
                i++;
            }

            coursesForm.set("basicCurricularCourses", formValues);
            return mapping.findForward("showCurricularCourses");
        }
        throw new FenixActionException();

    }

    public ActionForward setBasicCurricularCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);

            DynaActionForm basicCoursesList = (DynaActionForm) form;
            String[] coursesIdsString = (String[]) basicCoursesList.get("basicCurricularCourses");
            List coursesIds = new ArrayList();
            for (int i = 0; i < coursesIdsString.length; i++) {
                coursesIds.add(new Integer(coursesIdsString[i]));
            }

            String curricularPlanId = request.getParameter("curricularIndex");

            Object[] args = { coursesIds, new Integer(curricularPlanId) };

            try {
                ServiceUtils.executeService(userView, "setBasicCurricularCourses", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            return mapping.findForward("firstPage");
        }
        throw new FenixActionException();

    }

    public ActionForward viewCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);

            String curricularCourseIdString = request.getParameter("index");

            Object[] args = { new InfoSiteCurriculum(), new Integer(curricularCourseIdString), null };
            SiteView siteView = null;
            try {
                siteView = (SiteView) ServiceUtils.executeService(userView,
                        "ScientificCouncilCurricularCourseCurriculumComponentService", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            request.setAttribute("siteView", siteView);
            return mapping.findForward("viewCurriculum");
        }
        throw new FenixActionException();

    }

    public ActionForward prepareEditCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);

            String curriculumIdString = request.getParameter("index");

            Object[] args = { new Integer(curriculumIdString) };
            SiteView siteView = null;
            try {
                siteView = (SiteView) ServiceUtils.executeService(userView,
                        "ReadCurriculumByOIdService", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            request.setAttribute("siteView", siteView);
            return mapping.findForward("editCurriculum");
        }
        throw new FenixActionException();

    }

    public ActionForward editCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);

            String program = request.getParameter("program");
            String programEn = request.getParameter("programEn");
            String operacionalObjectives = request.getParameter("operacionalObjectives");
            String operacionalObjectivesEn = request.getParameter("operacionalObjectivesEn");
            String generalObjectives = request.getParameter("generalObjectives");
            String generalObjectivesEn = request.getParameter("generalObjectivesEn");
            String curriculumIdString = request.getParameter("curriculumId");

            Object[] args = { new Integer(curriculumIdString), program, programEn,
                    operacionalObjectives, operacionalObjectivesEn, generalObjectives,
                    generalObjectivesEn, new Boolean(true) };

            Boolean result;
            try {
                result = (Boolean) ServiceUtils.executeService(userView, "EditCurriculum", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (result.booleanValue()) {
                return showCurricularCourses(mapping, form, request, response);
            }
            return null;

        }
        throw new FenixActionException();

    }

    public ActionForward prepareInsertCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);

            String curricularCourseIdString = request.getParameter("index");

            Object[] args = { new Integer(curricularCourseIdString) };
            SiteView siteView = null;
            try {
                siteView = (SiteView) ServiceUtils.executeService(userView,
                        "ReadCurricularCourseByOIdService", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
            request.setAttribute("siteView", siteView);
            return mapping.findForward("insertCurriculum");
        }
        throw new FenixActionException();

    }

    public ActionForward insertCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);

            String program = request.getParameter("program");
            String programEn = request.getParameter("programEn");
            String operacionalObjectives = request.getParameter("operacionalObjectives");
            String operacionalObjectivesEn = request.getParameter("operacionalObjectivesEn");
            String generalObjectives = request.getParameter("generalObjectives");
            String generalObjectivesEn = request.getParameter("generalObjectivesEn");
            String curricularCourseIdString = request.getParameter("curricularCourseId");

            Object[] args = { new Integer(curricularCourseIdString), program, programEn,
                    operacionalObjectives, operacionalObjectivesEn, generalObjectives,
                    generalObjectivesEn, new Boolean(true) };

            Boolean result;
            try {
                result = (Boolean) ServiceUtils.executeService(userView, "InsertCurriculum", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (result.booleanValue()) {
                return showCurricularCourses(mapping, form, request, response);
            }
            return null;

        }
        throw new FenixActionException();

    }

    private SiteView readSiteView(HttpServletRequest request, IUserView userView, Integer degreeId,
            List coursesIds, Integer degreeCurricularPlanId, ISiteComponent component)
            throws FenixActionException, FenixFilterException {
        Object[] args = { component, degreeId, coursesIds, degreeCurricularPlanId };
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView,
                    "ScientificCouncilComponentService", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        return siteView;
    }

}