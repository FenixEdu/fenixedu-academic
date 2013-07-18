package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.LoggedCoordinatorCanEdit;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadCurrentExecutionDegreeByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement.EditCurriculumForCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement.ReadActiveDegreeCurricularPlanScopes;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement.ReadCurrentCurriculumByCurricularCourseCode;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement.ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement.ReadDegreeCurricularPlanHistoryByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Fernanda Quit�rio 06/Nov/2003
 */
@Mapping(module = "coordinator", path = "/degreeCurricularPlanManagement", attribute = "curricularCourseInformationForm",
        formBean = "curricularCourseInformationForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "viewCurricularCourseInformation", path = "viewCurricularCourseInformation"),
        @Forward(name = "degreeCurricularPlanManagementExecutionYears",
                path = "/degreeCurricularPlanManagement.do?method=prepareViewCurricularCourseInformationHistory"),
        @Forward(name = "showCurricularCoursesHistory", path = "degreeCurricularPlanHistory"),
        @Forward(name = "editCurriculumEn", path = "editCurriculumEn"),
        @Forward(name = "editCurriculum", path = "editCurriculum"),
        @Forward(name = "prepareViewCurricularCourseInformationHistory", path = "prepareViewCurricularCourseInformationHistory"),
        @Forward(name = "degreeCurricularPlanManagement",
                path = "/degreeCurricularPlanManagement.do?method=showActiveCurricularCourses"),
        @Forward(name = "showActiveCurricularCourses", path = "activeDegreeCurricularPlan") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException.class,
        key = "resources.Action.exceptions.FenixActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class DegreeCurricularPlanManagementDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showActiveCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        final Integer degreeCurricularPlanID = getAndSetIntegerToRequest("degreeCurricularPlanID", request);
        List activeCurricularCourseScopes = null;
        try {
            activeCurricularCourseScopes =
                    ReadActiveDegreeCurricularPlanScopes.runReadActiveDegreeCurricularPlanScopes(degreeCurricularPlanID);

        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullDegree")) {
                addErrorMessage(request, "nullCode", "error.coordinator.noExecutionDegree");
            } else {
                throw new FenixActionException(e);
            }
        }

        if (activeCurricularCourseScopes == null || activeCurricularCourseScopes.size() == 0) {
            addErrorMessage(request, "noDegreeCurricularPlan", "error.nonExisting.AssociatedCurricularCourses");
            return mapping.findForward("showActiveCurricularCourses");
        }

        // order list by year, next semester, next course
        final ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
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

    public ActionForward showCurricularCoursesHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer degreeCurricularPlanID = getAndSetIntegerToRequest("degreeCurricularPlanID", request);

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        try {
            infoDegreeCurricularPlan = ReadDegreeCurricularPlanHistoryByDegreeCurricularPlanID.run(degreeCurricularPlanID);

        } catch (NonExistingServiceException e) {
            addErrorMessage(request, "chosenDegree", "error.coordinator.noExecutionDegree");
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullDegree")) {
                addErrorMessage(request, "nullCode", "error.coordinator.noExecutionDegree");
            } else {
                throw new FenixActionException(e);
            }
        }

        if (infoDegreeCurricularPlan == null) {
            addErrorMessage(request, "noDegreeCurricularPlan", "error.nonExisting.AssociatedCurricularCourses");
        }

        if (infoDegreeCurricularPlan != null && infoDegreeCurricularPlan.getCurricularCourses() != null) {

            // order list by year, next semester, next course
            List allCurricularCourseScopes = new ArrayList();
            Iterator iter = infoDegreeCurricularPlan.getCurricularCourses().listIterator();
            while (iter.hasNext()) {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
                allCurricularCourseScopes.addAll(getInfoCurricularCourseScopes(infoCurricularCourse));
            }
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            comparatorChain.addComparator(new BeanComparator("beginDate.time"));
            Collections.sort(allCurricularCourseScopes, comparatorChain);

            // build hashmap for jsp
            HashMap curricularCourseScopesHashMap = new HashMap();
            Integer lastKey = new Integer(0);
            Iterator iterCurricularCourseScopes = allCurricularCourseScopes.iterator();
            while (iterCurricularCourseScopes.hasNext()) {
                InfoCurricularCourseScope curricularCourseScope = (InfoCurricularCourseScope) iterCurricularCourseScopes.next();

                List equalCurricularCourseScopes = null;
                if (lastKey.intValue() != 0) {
                    InfoCurricularCourseScope lastIntroduced =
                            (InfoCurricularCourseScope) ((List) curricularCourseScopesHashMap.get(lastKey)).get(0);

                    if (scopesAreEqual(curricularCourseScope, lastIntroduced)) {
                        equalCurricularCourseScopes = (List) curricularCourseScopesHashMap.get(lastKey);
                        equalCurricularCourseScopes.add(curricularCourseScope);
                        curricularCourseScopesHashMap.put(lastKey, equalCurricularCourseScopes);
                        continue;
                    }
                }
                equalCurricularCourseScopes = new ArrayList();
                equalCurricularCourseScopes.add(curricularCourseScope);
                curricularCourseScopesHashMap.put(curricularCourseScope.getIdInternal(), equalCurricularCourseScopes);
                lastKey = curricularCourseScope.getIdInternal();
            }
            request.setAttribute("allCurricularCourseScopes", allCurricularCourseScopes);
            request.setAttribute("curricularCourseScopesHashMap", curricularCourseScopesHashMap);
        }
        return mapping.findForward("showCurricularCoursesHistory");
    }

    private List<InfoCurricularCourseScope> getInfoCurricularCourseScopes(InfoCurricularCourse infoCurricularCourse) {
        CurricularCourse curricularCourse =
                (CurricularCourse) rootDomainObject.readDegreeModuleByOID(infoCurricularCourse.getIdInternal());
        List<InfoCurricularCourseScope> infoScopes =
                (List) CollectionUtils.collect(curricularCourse.getScopes(), new Transformer() {

                    @Override
                    public Object transform(Object arg0) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
                        return InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
                    }

                });
        return infoScopes;
    }

    private boolean scopesAreEqual(InfoCurricularCourseScope curricularCourseScope,
            InfoCurricularCourseScope nextCurricularCourseScope) {
        boolean result = false;
        if (curricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear()
                .equals(nextCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear())
                && curricularCourseScope.getInfoCurricularSemester().getSemester()
                        .equals(nextCurricularCourseScope.getInfoCurricularSemester().getSemester())
                && curricularCourseScope.getInfoCurricularCourse().getName()
                        .equalsIgnoreCase(nextCurricularCourseScope.getInfoCurricularCourse().getName())
                && curricularCourseScope.getInfoBranch().getCode()
                        .equalsIgnoreCase(nextCurricularCourseScope.getInfoBranch().getCode())) {

            result = true;
        }
        return result;
    }

    // ===================================== Curricular Course Management

    public ActionForward viewActiveCurricularCourseInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixServiceException {

        IUserView userView = getUserView(request);

        Integer infoCurricularCourseCode = getAndSetIntegerToRequest("infoCurricularCourseCode", request);
        Integer degreeCurricularPlanID = getAndSetIntegerToRequest("degreeCurricularPlanID", request);

        Integer infoExecutionDegreeCode = null;

        InfoExecutionDegree infoExecutionDegree =
                ReadCurrentExecutionDegreeByDegreeCurricularPlanID
                        .runReadCurrentExecutionDegreeByDegreeCurricularPlanID(degreeCurricularPlanID);

        infoExecutionDegreeCode = infoExecutionDegree.getIdInternal();

        // check that this user can edit curricular course information
        Boolean canEdit = new Boolean(false);

        try {
            canEdit = LoggedCoordinatorCanEdit.run(infoExecutionDegreeCode, infoCurricularCourseCode, userView.getUtilizador());
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullExecutionDegreeCode")) {
                addErrorMessage(request, "nullExecutionDegreeCode", "error.coordinator.noExecutionDegree");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else if (e.getMessage().equals("nullUsername")) {
                addErrorMessage(request, "nullUserView", "error.coordinator.noUserView");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else if (e.getMessage().equals("nullCurricularCourseCode")) {
                addErrorMessage(request, "nullCurricularCourseCode", "error.coordinator.noCurricularCourse");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else {
                throw new FenixActionException();
            }
        }

        request.setAttribute("infoExecutionDegreeCode", infoExecutionDegreeCode);
        request.setAttribute("canEdit", String.valueOf(canEdit.booleanValue()));

        // get curricular course information
        InfoCurriculum infoCurriculum = null;

        try {
            infoCurriculum = ReadCurrentCurriculumByCurricularCourseCode.run(infoExecutionDegreeCode, infoCurricularCourseCode);

        } catch (NonExistingServiceException e) {
            addErrorMessage(request, "chosenCurricularCourse", "error.coordinator.chosenCurricularCourse");
            return mapping.findForward("degreeCurricularPlanManagement");

        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourse")) {
                addErrorMessage(request, "nullCode", "error.coordinator.noCurricularCourse");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else {
                throw new FenixActionException(e);
            }
        }
        if (infoCurriculum == null) {
            addErrorMessage(request, "noCurriculum", "error.coordinator.noCurriculum");
            return mapping.findForward("degreeCurricularPlanManagement");
        }

        sortCurricularCourses(request, infoCurriculum);

        return mapping.findForward("viewCurricularCourseInformation");
    }

    private void sortCurricularCourses(HttpServletRequest request, InfoCurriculum infoCurriculum) {
        // order list by year, semester
        if (infoCurriculum.getInfoCurricularCourse() != null) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.infoCurricularYear.year"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularSemester.semester"));
            comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name"));
            Collections.sort(infoCurriculum.getInfoCurricularCourse().getInfoScopes(), comparatorChain);

            request.setAttribute("infoCurriculum", infoCurriculum);
        }
    }

    public ActionForward prepareViewCurricularCourseInformationHistory(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        List infoExecutionYears = null;

        infoExecutionYears = ReadNotClosedExecutionYears.run();

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
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        Integer infoCurricularCourseCode = getAndSetIntegerToRequest("infoCurricularCourseCode", request);
        String executionYear = getAndSetStringToRequest("executionYear", request);
        getAndSetIntegerToRequest("degreeCurricularPlanID", request);
        getAndSetStringToRequest("infoCurricularCourseName", request);

        InfoCurriculum infoCurriculum = null;

        try {
            infoCurriculum =
                    ReadCurriculumHistoryByCurricularCourseCodeAndExecutionYearName.run(null, infoCurricularCourseCode,
                            executionYear);

        } catch (NonExistingServiceException e) {
            if (e.getMessage().equals("noCurricularCourse")) {
                addErrorMessage(request, "chosenCurricularCourse", "error.coordinator.chosenCurricularCourse");
                return mapping.findForward("degreeCurricularPlanManagementExecutionYears");

            } else if (e.getMessage().equals("noExecutionYear")) {
                addErrorMessage(request, "chosenExecutionYear", "error.coordinator.chosenExecutionYear", executionYear);
                return mapping.findForward("degreeCurricularPlanManagementExecutionYears");

            } else {
                throw new NonExistingActionException(e);
            }
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourse")) {
                addErrorMessage(request, "nullCode", "error.coordinator.noCurricularCourse");
                return mapping.findForward("degreeCurricularPlanManagementExecutionYears");

            } else if (e.getMessage().equals("nullExecutionYearName")) {
                addErrorMessage(request, "nullName", "error.coordinator.noExecutionYear");
                return mapping.findForward("degreeCurricularPlanManagementExecutionYears");

            } else {
                throw new FenixActionException(e);
            }
        }
        if (infoCurriculum == null) {
            addErrorMessage(request, "noCurriculum", "error.coordinator.noCurriculumInExecutionYear", executionYear);
            return mapping.findForward("degreeCurricularPlanManagementExecutionYears");
        }

        request.setAttribute("infoCurriculum", infoCurriculum);
        request.setAttribute("canEdit", "false");

        return mapping.findForward("viewCurricularCourseInformation");
    }

    public ActionForward prepareEditCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = getUserView(request);

        Integer infoExecutionDegreeCode = getAndSetIntegerToRequest("infoExecutionDegreeCode", request);
        Integer infoCurricularCourseCode = getAndSetIntegerToRequest("infoCurricularCourseCode", request);
        getAndSetIntegerToRequest("degreeCurricularPlanID", request);

        // check that this user can edit curricular course information
        Boolean canEdit = new Boolean(false);

        try {
            canEdit = LoggedCoordinatorCanEdit.run(infoExecutionDegreeCode, infoCurricularCourseCode, userView.getUtilizador());

        } catch (NonExistingServiceException e) {
            addErrorMessage(request, "chosenCurricularCourse", "error.coordinator.chosenCurricularCourse");
            return mapping.findForward("degreeCurricularPlanManagement");

        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullExecutionDegreeCode")) {
                addErrorMessage(request, "nullExecutionDegreeCode", "error.coordinator.noExecutionDegree");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else if (e.getMessage().equals("nullUsername")) {
                addErrorMessage(request, "nullUsername", "error.coordinator.noUserView");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else if (e.getMessage().equals("nullCurricularCourseCode")) {
                addErrorMessage(request, "nullCurricularCourseCode", "error.coordinator.noCurricularCourse");
                return mapping.findForward("degreeCurricularPlanManagement");
            } else {
                throw new FenixActionException();
            }
        }

        request.setAttribute("canEdit", String.valueOf(canEdit.booleanValue()));

        // get curricular course information
        InfoCurriculum infoCurriculum = null;

        try {
            infoCurriculum = ReadCurrentCurriculumByCurricularCourseCode.run(infoExecutionDegreeCode, infoCurricularCourseCode);

        } catch (NonExistingServiceException e) {
            addErrorMessage(request, "chosenCurricularCourse", "error.coordinator.chosenCurricularCourse");
            return mapping.findForward("degreeCurricularPlanManagement");

        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourse")) {
                addErrorMessage(request, "nullCode", "error.coordinator.noCurricularCourse");
                return mapping.findForward("degreeCurricularPlanManagement");
            } else {
                throw new FenixActionException(e);
            }
        }
        if (infoCurriculum == null) {
            addErrorMessage(request, "noCurriculum", "error.coordinator.noCurriculum");
            return mapping.findForward("degreeCurricularPlanManagement");
        }

        fillForm(form, infoCurriculum);
        request.setAttribute("infoCurriculum", infoCurriculum);

        if (request.getParameter("language") != null && request.getParameter("language").equals("English")) {
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

    public ActionForward editCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        IUserView userView = getUserView(request);

        Integer infoExecutionDegreeCode = getAndSetIntegerToRequest("infoExecutionDegreeCode", request);
        Integer infoCurricularCourseCode = getAndSetIntegerToRequest("infoCurricularCourseCode", request);
        Integer infoCurriculumCode = getAndSetIntegerToRequest("infoCurriculumCode", request);

        String language = request.getParameter("language");
        InfoCurriculum infoCurriculum = getDataFromForm(form, language);

        Boolean result = Boolean.FALSE;

        try {
            result =
                    EditCurriculumForCurricularCourse.runEditCurriculumForCurricularCourse(infoExecutionDegreeCode,
                            infoCurriculumCode, infoCurricularCourseCode, infoCurriculum, userView.getUtilizador(), language);
        } catch (NonExistingServiceException e) {
            if (e.getMessage().equals("noCurricularCourse")) {
                addErrorMessage(request, "chosenCurricularCourse", "error.coordinator.chosenCurricularCourse");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else if (e.getMessage().equals("noPerson")) {
                addErrorMessage(request, "chosenPerson", "error.coordinator.noUserView");
                return mapping.findForward("degreeCurricularPlanManagement");
            }
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullCurricularCourseCode")) {
                addErrorMessage(request, "nullCode", "error.coordinator.noCurricularCourse");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else if (e.getMessage().equals("nullCurriculumCode")) {
                addErrorMessage(request, "nullCurriculumCode", "error.coordinator.noCurriculum");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else if (e.getMessage().equals("nullCurriculum")) {
                addErrorMessage(request, "nullCurriculum", "error.coordinator.noCurriculum");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else if (e.getMessage().equals("nullUsername")) {
                addErrorMessage(request, "nullUsername", "error.coordinator.noUserView");
                return mapping.findForward("degreeCurricularPlanManagement");

            } else {
                throw new FenixActionException(e);
            }
        }
        if (result.equals(Boolean.FALSE)) {
            throw new FenixActionException();
        }

        return viewActiveCurricularCourseInformation(mapping, form, request, response);
    }

    private InfoCurriculum getDataFromForm(ActionForm form, String language) {
        InfoCurriculum infoCurriculum = new InfoCurriculum();

        DynaActionForm curriculumForm = (DynaActionForm) form;

        if (language == null || language.equals("Portuguese")) {
            infoCurriculum.setGeneralObjectives((String) curriculumForm.get("generalObjectives"));
            infoCurriculum.setOperacionalObjectives((String) curriculumForm.get("operacionalObjectives"));
            infoCurriculum.setProgram((String) curriculumForm.get("program"));
        } else if (language.equals("English")) {
            infoCurriculum.setGeneralObjectivesEn((String) curriculumForm.get("generalObjectivesEn"));
            infoCurriculum.setOperacionalObjectivesEn((String) curriculumForm.get("operacionalObjectivesEn"));
            infoCurriculum.setProgramEn((String) curriculumForm.get("programEn"));
        }
        return infoCurriculum;
    }
}