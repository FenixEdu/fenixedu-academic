package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Fernanda Quit�rio 06/Nov/2003
 */
public class DegreeCurricularPlanManagementDispatchAction extends FenixDispatchAction {
    public ActionForward showActiveCurricularCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        final HttpSession session = request.getSession(false);
        final IUserView userView = getUserView(request);

        final Integer degreeCurricularPlanID = getAndSetIntegerToRequest("degreeCurricularPlanID", request);
        
        List activeCurricularCourseScopes = null;      
        final Object[] args = { degreeCurricularPlanID };
        try {
            activeCurricularCourseScopes = (List) ServiceUtils.executeService(userView,
                    "ReadActiveDegreeCurricularPlanScopes", args);

        } catch (NonExistingServiceException e) {
            final ActionErrors errors = new ActionErrors();
            errors.add("chosenDegree", new ActionError("error.coordinator.noExecutionDegree"));
            saveErrors(request, errors);
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullDegree")) {
                final ActionErrors errors = new ActionErrors();
                
                errors.add("nullCode", new ActionError("error.coordinator.noExecutionDegree"));
                saveErrors(request, errors);
            } else {
                throw new FenixActionException(e);
            }
        }

        if (activeCurricularCourseScopes == null || activeCurricularCourseScopes.size() == 0) {
            final ActionErrors errors = new ActionErrors();
            
            errors.add("noDegreeCurricularPlan", new ActionError(
                    "error.nonExisting.AssociatedCurricularCourses"));
            saveErrors(request, errors);
            return mapping.findForward("showActiveCurricularCourses");
        }

        //order list by year, next semester, next course
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator(
                "infoCurricularSemester.infoCurricularYear.year"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
        Collections.sort(activeCurricularCourseScopes, comparatorChain);

        request.setAttribute("allActiveCurricularCourseScopes", activeCurricularCourseScopes);

        return mapping.findForward("showActiveCurricularCourses");
    }

    private Integer getAndSetIntegerToRequest(String parameter, HttpServletRequest request) {
        Integer parameterInteger = new Integer(request.getParameter(parameter));
        request.setAttribute(parameter, parameterInteger);

        return parameterInteger;
    }

    public ActionForward showCurricularCoursesHistory(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

        Integer degreeCurricularPlanID = getAndSetIntegerToRequest("degreeCurricularPlanID", request);       

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        ActionErrors errors = new ActionErrors();
        Object[] args = { degreeCurricularPlanID };
        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlanHistoryByDegreeCurricularPlanID", args);

        } catch (NonExistingServiceException e) {
            errors.add("chosenDegree", new ActionError("error.coordinator.noExecutionDegree"));
            saveErrors(request, errors);
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullDegree")) {
                errors.add("nullCode", new ActionError("error.coordinator.noExecutionDegree"));
                saveErrors(request, errors);
            } else {
                throw new FenixActionException(e);
            }
        }

        if (infoDegreeCurricularPlan == null) {
            errors.add("noDegreeCurricularPlan", new ActionError(
                    "error.nonExisting.AssociatedCurricularCourses"));
            saveErrors(request, errors);
        }

        if (infoDegreeCurricularPlan != null && infoDegreeCurricularPlan.getCurricularCourses() != null) {

            //order list by year, next semester, next course
            List allCurricularCourseScopes = new ArrayList();
            Iterator iter = infoDegreeCurricularPlan.getCurricularCourses().listIterator();
            while (iter.hasNext()) {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
                allCurricularCourseScopes.addAll(infoCurricularCourse.getInfoScopes());
            }
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator(
                    "infoCurricularSemester.infoCurricularYear.year"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            comparatorChain.addComparator(new BeanComparator("beginDate.time"));
            Collections.sort(allCurricularCourseScopes, comparatorChain);

            // build hashmap for jsp
            HashMap curricularCourseScopesHashMap = new HashMap();
            Integer lastKey = new Integer(0);
            Iterator iterCurricularCourseScopes = allCurricularCourseScopes.iterator();
            while (iterCurricularCourseScopes.hasNext()) {
                InfoCurricularCourseScope curricularCourseScope = (InfoCurricularCourseScope) iterCurricularCourseScopes
                        .next();

                List equalCurricularCourseScopes = null;
                if (lastKey.intValue() != 0) {
                    InfoCurricularCourseScope lastIntroduced = (InfoCurricularCourseScope) ((List) curricularCourseScopesHashMap
                            .get(lastKey)).get(0);

                    if (scopesAreEqual(curricularCourseScope, lastIntroduced)) {
                        equalCurricularCourseScopes = (List) curricularCourseScopesHashMap.get(lastKey);
                        equalCurricularCourseScopes.add(curricularCourseScope);
                        curricularCourseScopesHashMap.put(lastKey, equalCurricularCourseScopes);
                        continue;
                    }
                }
                equalCurricularCourseScopes = new ArrayList();
                equalCurricularCourseScopes.add(curricularCourseScope);
                curricularCourseScopesHashMap.put(curricularCourseScope.getIdInternal(),
                        equalCurricularCourseScopes);
                lastKey = curricularCourseScope.getIdInternal();
            }
            request.setAttribute("allCurricularCourseScopes", allCurricularCourseScopes);
            request.setAttribute("curricularCourseScopesHashMap", curricularCourseScopesHashMap);
        }
        return mapping.findForward("showCurricularCoursesHistory");
    }

    private boolean scopesAreEqual(InfoCurricularCourseScope curricularCourseScope,
            InfoCurricularCourseScope nextCurricularCourseScope) {
        boolean result = false;
        if (curricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear().equals(
                nextCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear())
                && curricularCourseScope.getInfoCurricularSemester().getSemester().equals(
                        nextCurricularCourseScope.getInfoCurricularSemester().getSemester())
                && curricularCourseScope.getInfoCurricularCourse().getName().equalsIgnoreCase(
                        nextCurricularCourseScope.getInfoCurricularCourse().getName())
                && curricularCourseScope.getInfoBranch().getCode().equalsIgnoreCase(
                        nextCurricularCourseScope.getInfoBranch().getCode())) {

            result = true;
        }
        return result;
    }

    //	===================================== Curricular Course Management

    public ActionForward viewActiveCurricularCourseInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

        Integer infoCurricularCourseCode = getAndSetIntegerToRequest("infoCurricularCourseCode", request);
        Integer degreeCurricularPlanID = getAndSetIntegerToRequest("degreeCurricularPlanID", request);
        
        Integer infoExecutionDegreeCode = null;
        Object[] infoArgs = { degreeCurricularPlanID };
 
	    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils
	    .executeService(userView, "ReadCurrentExecutionDegreeByDegreeCurricularPlanID", infoArgs);

        infoExecutionDegreeCode = infoExecutionDegree.getIdInternal();
        
        // check that this user can edit curricular course information
        Boolean canEdit = new Boolean(false);
        ActionErrors errors = new ActionErrors();
        Object[] argsAuthorization = { infoExecutionDegreeCode, infoCurricularCourseCode,
                userView.getUtilizador() };
        try {
            canEdit = (Boolean) ServiceUtils.executeService(null, "LoggedCoordinatorCanEdit",
                    argsAuthorization);

        } catch (NonExistingServiceException e) {
            if (e.getMessage().equals("nullExecutionDegreeCode")) {
                errors.add("nullExecutionDegreeCode", new ActionError(
                        "error.coordinator.noExecutionDegree"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullUsername")) {
                errors.add("nullUsername", new ActionError("error.coordinator.noUserView"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullCurricularCourseCode")) {
                errors.add("nullCurricularCourseCode", new ActionError(
                        "error.coordinator.noCurricularCourse"));
                saveErrors(request, errors);
            } else {
                throw new FenixActionException();
            }
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullExecutionDegreeCode")) {
                errors.add("nullExecutionDegreeCode", new ActionError(
                        "error.coordinator.noExecutionDegree"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullUsername")) {
                errors.add("nullUserView", new ActionError("error.coordinator.noUserView"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullCurricularCourseCode")) {
                errors.add("nullCurricularCourseCode", new ActionError(
                        "error.coordinator.noCurricularCourse"));
                saveErrors(request, errors);
            } else {
                throw new FenixActionException();
            }
        }
        if (!errors.isEmpty()) {
            return mapping.findForward("degreeCurricularPlanManagement");
        }

        request.setAttribute("infoExecutionDegreeCode", infoExecutionDegreeCode);
        request.setAttribute("canEdit", String.valueOf(canEdit.booleanValue()));

        // get curricular course information
        InfoCurriculum infoCurriculum = null;
        Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode };
        try {
            infoCurriculum = (InfoCurriculum) ServiceUtils.executeService(userView,
                    "ReadCurrentCurriculumByCurricularCourseCode", args);

        } catch (NonExistingServiceException e) {
            errors.add("chosenCurricularCourse", new ActionError(
                    "error.coordinator.chosenCurricularCourse"));
            saveErrors(request, errors);
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourse")) {
                errors.add("nullCode", new ActionError("error.coordinator.noCurricularCourse"));
                saveErrors(request, errors);
            } else {
                throw new FenixActionException(e);
            }
        }
        if (infoCurriculum == null) {
            errors.add("noCurriculum", new ActionError("error.coordinator.noCurriculum"));
            saveErrors(request, errors);
        }
        if (!errors.isEmpty()) {
            return mapping.findForward("degreeCurricularPlanManagement");
        }

        sortCurricularCourses(request, infoCurriculum);

        return mapping.findForward("viewCurricularCourseInformation");
    }

    private void sortCurricularCourses(HttpServletRequest request, InfoCurriculum infoCurriculum) {
        //order list by year, semester
        if (infoCurriculum.getInfoCurricularCourse() != null) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator(
                    "infoCurricularSemester.infoCurricularYear.year"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            Collections.sort(infoCurriculum.getInfoCurricularCourse().getInfoScopes(), comparatorChain);

            request.setAttribute("infoCurriculum", infoCurriculum);
        }
    }

    public ActionForward prepareViewCurricularCourseInformationHistory(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {
        List infoExecutionYears = null;
        Object[] args = {};
        try {
            infoExecutionYears = (List) ServiceUtils.executeService(null, "ReadNotClosedExecutionYears",
                    args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        getAndSetIntegerToRequest("infoCurricularCourseCode", request);
        getAndSetStringToRequest("infoCurricularCourseName", request);
        getAndSetStringToRequest("degreeCurricularPlanID", request);
        
        request.setAttribute("infoExecutionYears", infoExecutionYears);

        return mapping.findForward("prepareViewCurricularCourseInformationHistory");
    }

    private String getAndSetStringToRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        request.setAttribute(parameter, parameterString);

        return parameterString;
    }

    public ActionForward viewCurricularCourseInformationHistory(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

        Integer infoCurricularCourseCode = getAndSetIntegerToRequest("infoCurricularCourseCode", request);
        String executionYear = getAndSetStringToRequest("executionYear", request);
        getAndSetIntegerToRequest("degreeCurricularPlanID", request);        
        getAndSetStringToRequest("infoCurricularCourseName", request);

        InfoCurriculum infoCurriculum = null;
        ActionErrors errors = new ActionErrors();
        Object[] args = { null, infoCurricularCourseCode, executionYear };
        try {
            infoCurriculum = (InfoCurriculum) ServiceUtils.executeService(userView,
                    "ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName", args);

        } catch (NonExistingServiceException e) {
            if (e.getMessage().equals("noCurricularCourse")) {
                errors.add("chosenCurricularCourse", new ActionError(
                        "error.coordinator.chosenCurricularCourse"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("noExecutionYear")) {
                errors.add("chosenExecutionYear", new ActionError(
                        "error.coordinator.chosenExecutionYear", executionYear));
                saveErrors(request, errors);
            } else {
                throw new NonExistingActionException(e);
            }
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourse")) {
                errors.add("nullCode", new ActionError("error.coordinator.noCurricularCourse"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullExecutionYearName")) {
                errors.add("nullName", new ActionError("error.coordinator.noExecutionYear"));
                saveErrors(request, errors);
            } else {
                throw new FenixActionException(e);
            }
        }
        if (infoCurriculum == null) {
            errors.add("noCurriculum", new ActionError("error.coordinator.noCurriculumInExecutionYear",
                    executionYear));
            saveErrors(request, errors);
        }
        if (!errors.isEmpty()) {
            return mapping.findForward("degreeCurricularPlanManagementExecutionYears");
        }
        request.setAttribute("infoCurriculum", infoCurriculum);
        request.setAttribute("canEdit", "false");

        return mapping.findForward("viewCurricularCourseInformation");
    }

    public ActionForward prepareEditCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

        Integer infoExecutionDegreeCode = getAndSetIntegerToRequest("infoExecutionDegreeCode", request);
        Integer infoCurricularCourseCode = getAndSetIntegerToRequest("infoCurricularCourseCode", request);
        getAndSetIntegerToRequest("degreeCurricularPlanID", request);
        
        // check that this user can edit curricular course information
        Boolean canEdit = new Boolean(false);
        ActionErrors errors = new ActionErrors();
        Object[] argsAuthorization = { infoExecutionDegreeCode, infoCurricularCourseCode,
                userView.getUtilizador() };
        try {
            canEdit = (Boolean) ServiceUtils.executeService(null, "LoggedCoordinatorCanEdit",
                    argsAuthorization);

        } catch (NonExistingServiceException e) {
            errors.add("chosenCurricularCourse", new ActionError(
                    "error.coordinator.chosenCurricularCourse"));
            saveErrors(request, errors);
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullExecutionDegreeCode")) {
                errors.add("nullExecutionDegreeCode", new ActionError(
                        "error.coordinator.noExecutionDegree"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullUsername")) {
                errors.add("nullUsername", new ActionError("error.coordinator.noUserView"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullCurricularCourseCode")) {
                errors.add("nullCurricularCourseCode", new ActionError(
                        "error.coordinator.noCurricularCourse"));
                saveErrors(request, errors);
            } else {
                throw new FenixActionException();
            }
        }
        if (!errors.isEmpty()) {
            return mapping.findForward("degreeCurricularPlanManagement");
        }

        request.setAttribute("canEdit", String.valueOf(canEdit.booleanValue()));

        // get curricular course information
        InfoCurriculum infoCurriculum = null;
        Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode };
        try {
            infoCurriculum = (InfoCurriculum) ServiceUtils.executeService(userView,
                    "ReadCurrentCurriculumByCurricularCourseCode", args);

        } catch (NonExistingServiceException e) {
            errors.add("chosenCurricularCourse", new ActionError(
                    "error.coordinator.chosenCurricularCourse"));
            saveErrors(request, errors);
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourse")) {
                errors.add("nullCode", new ActionError("error.coordinator.noCurricularCourse"));
                saveErrors(request, errors);
            } else {
                throw new FenixActionException(e);
            }
        }
        if (infoCurriculum == null) {
            errors.add("noCurriculum", new ActionError("error.coordinator.noCurriculum"));
            saveErrors(request, errors);
        }

        if (!errors.isEmpty()) {
            return mapping.findForward("degreeCurricularPlanManagement");
        }

        fillForm(form, infoCurriculum);

        request.setAttribute("infoCurriculum", infoCurriculum);

        if (request.getParameter("language") != null
                && request.getParameter("language").equals("English")) {
            return mapping.findForward("editCurriculumEn");
        }
        return mapping.findForward("editCurriculum");

    }

    private void fillForm(ActionForm form, InfoCurriculum infoCurriculum) {
        DynaActionForm curriculumForm = (DynaActionForm) form;

        curriculumForm.set("generalObjectives", infoCurriculum.getGeneralObjectives());
        curriculumForm.set("operacionalObjectives", infoCurriculum.getOperacionalObjectives());
        curriculumForm.set("program", infoCurriculum.getProgram());
        curriculumForm.set("generalObjectivesEn", infoCurriculum.getGeneralObjectivesEn());
        curriculumForm.set("operacionalObjectivesEn", infoCurriculum.getOperacionalObjectivesEn());
        curriculumForm.set("programEn", infoCurriculum.getProgramEn());
    }

    public ActionForward editCurriculum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

        Integer infoExecutionDegreeCode = getAndSetIntegerToRequest("infoExecutionDegreeCode", request);
        Integer infoCurricularCourseCode = getAndSetIntegerToRequest("infoCurricularCourseCode", request);
        Integer infoCurriculumCode = getAndSetIntegerToRequest("infoCurriculumCode", request);
        
        String language = null;
        language = request.getParameter("language");

        InfoCurriculum infoCurriculum = getDataFromForm(form, language);

        Boolean result = Boolean.FALSE;
        ActionErrors errors = new ActionErrors();
        Object[] args = { infoExecutionDegreeCode, infoCurriculumCode, infoCurricularCourseCode,
                infoCurriculum, userView.getUtilizador(), language };
        try {
            result = (Boolean) ServiceUtils.executeService(userView,
                    "EditCurriculumForCurricularCourse", args);
        } catch (NonExistingServiceException e) {
            if (e.getMessage().equals("noCurricularCourse")) {
                errors.add("chosenCurricularCourse", new ActionError(
                        "error.coordinator.chosenCurricularCourse"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("noPerson")) {
                errors.add("chosenPerson", new ActionError("error.coordinator.noUserView"));
                saveErrors(request, errors);
            }
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourseCode")) {
                errors.add("nullCode", new ActionError("error.coordinator.noCurricularCourse"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullCurriculumCode")) {
                errors.add("nullCurriculumCode", new ActionError("error.coordinator.noCurriculum"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullCurriculum")) {
                errors.add("nullCurriculum", new ActionError("error.coordinator.noCurriculum"));
                saveErrors(request, errors);
            } else if (e.getMessage().equals("nullUsername")) {
                errors.add("nullUsername", new ActionError("error.coordinator.noUserView"));
                saveErrors(request, errors);
            } else {
                throw new FenixActionException(e);
            }
        }
        if (result.equals(Boolean.FALSE)) {
            throw new FenixActionException();
        }
        if (!errors.isEmpty()) {
            return mapping.findForward("degreeCurricularPlanManagement");
        }

        return viewActiveCurricularCourseInformation(mapping, form, request, response);
    }

    private InfoCurriculum getDataFromForm(ActionForm form, String language) {
        InfoCurriculum infoCurriculum = new InfoCurriculum();

        DynaActionForm curriculumForm = (DynaActionForm) form;

        if (language == null || language.equals("Portuguese")) {
            infoCurriculum.setGeneralObjectives((String) curriculumForm.get("generalObjectives"));
            infoCurriculum
                    .setOperacionalObjectives((String) curriculumForm.get("operacionalObjectives"));
            infoCurriculum.setProgram((String) curriculumForm.get("program"));
        } else if (language.equals("English")) {
            infoCurriculum.setGeneralObjectivesEn((String) curriculumForm.get("generalObjectivesEn"));
            infoCurriculum.setOperacionalObjectivesEn((String) curriculumForm
                    .get("operacionalObjectivesEn"));
            infoCurriculum.setProgramEn((String) curriculumForm.get("programEn"));
        }
        return infoCurriculum;
    }
}