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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
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
import org.joda.time.DateTime;

/**
 * @author João Mota
 * 
 *         23/Jul/2003 fenix-head presentationTier.Action.scientificCouncil
 * 
 */
public class CurricularCourseManagerDA extends FenixDispatchAction {

    public ActionForward prepareSelectDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);
	ISiteComponent component = new InfoSiteSCDegrees();
	readSiteView(request, userView, null, null, null, component);
	return mapping.findForward("selectDegree");

    }

    public ActionForward showDegreeCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);

	String degreeIdString = request.getParameter("index");

	Integer degreeId = new Integer(degreeIdString);
	ISiteComponent component = new InfoSiteDegreeCurricularPlans();
	readSiteView(request, userView, degreeId, null, null, component);
	return mapping.findForward("showDegreeCurricularPlans");
    }

    public ActionForward showCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);

	String degreeCurricularPlanIdString = request.getParameter("index");
	Integer degreeCurricularPlanId = new Integer(degreeCurricularPlanIdString);

	ISiteComponent component = new InfoSiteCurricularCourses();
	readSiteView(request, userView, null, null, degreeCurricularPlanId, component);
	return mapping.findForward("showCurricularCourses");
    }

    public ActionForward showBasicCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);

	String degreeCurricularPlanIdString = request.getParameter("index");
	Integer degreeCurricularPlanId = new Integer(degreeCurricularPlanIdString);

	ISiteComponent component = new InfoSiteBasicCurricularCourses();
	SiteView siteView = readSiteView(request, userView, null, null, degreeCurricularPlanId, component);

	DynaActionForm coursesForm = (DynaActionForm) form;
	List curricularCoursesIds = ((InfoSiteBasicCurricularCourses) siteView.getComponent()).getBasicCurricularCoursesIds();
	List nonBasicCurricularCourses = ((InfoSiteBasicCurricularCourses) siteView.getComponent())
		.getNonBasicCurricularCourses();

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
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);

	DynaActionForm basicCoursesList = (DynaActionForm) form;
	String[] coursesIdsString = (String[]) basicCoursesList.get("basicCurricularCourses");
	List coursesIds = new ArrayList();
	for (int i = 0; i < coursesIdsString.length; i++) {
	    coursesIds.add(new Integer(coursesIdsString[i]));
	}

	String curricularPlanId = request.getParameter("curricularIndex");

	try {
	    SetBasicCurricularCoursesService.run(coursesIds, new Integer(curricularPlanId));
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	return mapping.findForward("firstPage");
    }

    public ActionForward viewCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);

	String curricularCourseIdString = request.getParameter("index");

	SiteView siteView = null;
	try {
	    siteView = ScientificCouncilCurricularCourseCurriculumComponentService.run(new InfoSiteCurriculum(), new Integer(
		    curricularCourseIdString), null);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}
	request.setAttribute("siteView", siteView);
	return mapping.findForward("viewCurriculum");
    }

    public ActionForward prepareEditCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);

	String curriculumIdString = request.getParameter("index");

	SiteView siteView = null;
	try {
	    siteView = ReadCurriculumByOIdService.run(new Integer(curriculumIdString));
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}
	request.setAttribute("siteView", siteView);
	return mapping.findForward("editCurriculum");
    }

    public ActionForward editCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);

	String program = request.getParameter("program");
	String programEn = request.getParameter("programEn");
	String operacionalObjectives = request.getParameter("operacionalObjectives");
	String operacionalObjectivesEn = request.getParameter("operacionalObjectivesEn");
	String generalObjectives = request.getParameter("generalObjectives");
	String generalObjectivesEn = request.getParameter("generalObjectivesEn");
	String curriculumIdString = request.getParameter("curriculumId");

	Boolean result;
	try {
	    result = EditCurriculum.run(new Integer(curriculumIdString), program, programEn, operacionalObjectives,
		    operacionalObjectivesEn, generalObjectives, generalObjectivesEn, new Boolean(true));
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	if (result.booleanValue()) {
	    return showCurricularCourses(mapping, form, request, response);
	}
	return null;
    }

    public ActionForward prepareInsertCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);

	String curricularCourseIdString = request.getParameter("index");

	SiteView siteView = null;
	try {
	    siteView = ReadCurricularCourseByOIdService.run(new Integer(curricularCourseIdString));
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}
	request.setAttribute("siteView", siteView);
	return mapping.findForward("insertCurriculum");
    }

    public ActionForward insertCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = getUserView(request);

	String program = request.getParameter("program");
	String programEn = request.getParameter("programEn");
	String operacionalObjectives = request.getParameter("operacionalObjectives");
	String operacionalObjectivesEn = request.getParameter("operacionalObjectivesEn");
	String generalObjectives = request.getParameter("generalObjectives");
	String generalObjectivesEn = request.getParameter("generalObjectivesEn");
	String curricularCourseIdString = request.getParameter("curricularCourseId");

	Boolean result;
	try {
	    result = InsertCurriculum.run(new Integer(curricularCourseIdString), program, programEn, operacionalObjectives,
		    operacionalObjectivesEn, generalObjectives, generalObjectivesEn, new DateTime(), Boolean.TRUE);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	if (result.booleanValue()) {
	    return showCurricularCourses(mapping, form, request, response);
	}
	return null;
    }

    private SiteView readSiteView(HttpServletRequest request, IUserView userView, Integer degreeId, Integer coursesIds,
	    Integer degreeCurricularPlanId, ISiteComponent component) throws FenixActionException, FenixFilterException {

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