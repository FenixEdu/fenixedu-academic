package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Fernanda Quitério 19/Dez/2003
 *  
 */
public class EditExecutionCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEditECChooseExecutionPeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        List infoExecutionPeriods = null;
        try {
            infoExecutionPeriods = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods",
                    null);
        } catch (FenixServiceException ex) {
            throw new FenixActionException();
        }
        if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
            // exclude closed execution periods
            infoExecutionPeriods = (List) CollectionUtils.select(infoExecutionPeriods, new Predicate() {

                public boolean evaluate(Object input) {

                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;
                    if (!infoExecutionPeriod.getState().equals(PeriodState.CLOSED)) {
                        return true;
                    }
                    return false;
                }
            });

            ComparatorChain comparator = new ComparatorChain();
            comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
            comparator.addComparator(new BeanComparator("name"), true);
            Collections.sort(infoExecutionPeriods, comparator);

            List executionPeriodLabels = buildLabelValueBeanForJsp(infoExecutionPeriods);

            request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, executionPeriodLabels);
        }
        return mapping.findForward("prepareEditECChooseExecutionPeriod");
    }

    private List buildLabelValueBeanForJsp(List infoExecutionPeriods) {

        List executionPeriodLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionPeriods, new Transformer() {

            public Object transform(Object arg0) {

                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;
                LabelValueBean executionPeriod = new LabelValueBean(infoExecutionPeriod.getName()
                        + " - " + infoExecutionPeriod.getInfoExecutionYear().getYear(),
                        infoExecutionPeriod.getName() + " - "
                                + infoExecutionPeriod.getInfoExecutionYear().getYear() + "~"
                                + infoExecutionPeriod.getIdInternal().toString());
                return executionPeriod;
            }
        }, executionPeriodLabels);
        return executionPeriodLabels;
    }

    public ActionForward prepareEditECChooseExecDegreeAndCurYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        buildCurricularYearLabelValueBean(request);

        Integer executionPeriodId = separateLabel(form, request, "executionPeriod", "executionPeriodId",
                "executionPeriodName");

        request.setAttribute("executionPeriodId", executionPeriodId);

        Object args[] = { executionPeriodId };
        List executionDegreeList = null;
        try {
            executionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionPeriodId", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List courses = new ArrayList();
        courses.add(new LabelValueBean("escolher", ""));
        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        buildExecutionDegreeLabelValueBean(executionDegreeList, courses);
        request.setAttribute(SessionConstants.DEGREES, courses);

        return mapping.findForward("prepareEditECChooseExecDegreeAndCurYear");
    }

    private void buildExecutionDegreeLabelValueBean(List executionDegreeList, List courses) {

        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();
            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " em " + name;
            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";
            courses.add(new LabelValueBean(name, name + "~"
                    + infoExecutionDegree.getIdInternal().toString()));
        }
    }

    private void buildCurricularYearLabelValueBean(HttpServletRequest request) {
        List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
        request.setAttribute(SessionConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);
    }

    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {

        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;
        }
        return false;
    }

    public ActionForward prepareEditExecutionCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer executionPeriodId = separateLabel(form, request, "executionPeriod", "executionPeriodId",
                "executionPeriodName");

        DynaActionForm executionCourseForm = (DynaValidatorForm) form;
        Boolean getNotLinked = (Boolean) executionCourseForm.get("executionCoursesNotLinked");
        Integer executionDegreeId = null;
        Integer curYear = null;
        if (getNotLinked == null || getNotLinked.equals(Boolean.FALSE)) {
            executionDegreeId = separateLabel(form, request, "executionDegree", "executionDegreeId",
                    "executionDegreeName");

            curYear = Integer.valueOf((String) executionCourseForm.get("curYear"));
            request.setAttribute("curYear", curYear);
        } else {
            request.setAttribute("executionCoursesNotLinked", getNotLinked.toString());
        }

        Object args[] = { executionDegreeId, executionPeriodId, curYear };
        List infoExecutionCourses;
        try {
            infoExecutionCourses = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
        request.setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, infoExecutionCourses);

        return mapping.findForward("prepareEditExecutionCourse");
    }

    public ActionForward editExecutionCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        String executionCourseId = getAndSetStringToRequest(request, "executionCourseId");
        separateLabel(form, request, "executionPeriod", "executionPeriodId", "executionPeriodName");

        String executionCoursesNotLinked = getAndSetStringToRequest(request, "executionCoursesNotLinked");
        DynaActionForm executionCourseForm = (DynaValidatorForm) form;
        if (executionCoursesNotLinked == null || executionCoursesNotLinked.equals("null")
                || executionCoursesNotLinked.equals(Boolean.FALSE.toString())) {
            separateLabel(form, request, "executionDegree", "executionDegreeId", "executionDegreeName");

            String curYear = getAndSetStringToRequest(request, "curYear");
            executionCourseForm.set("curYear", curYear);
        } else {
            executionCourseForm.set("executionCoursesNotLinked", new Boolean(executionCoursesNotLinked));
        }

        Object args[] = { Integer.valueOf(executionCourseId) };
        InfoExecutionCourse infoExecutionCourse;
        try {
            infoExecutionCourse = (InfoExecutionCourse) ServiceManagerServiceFactory.executeService(
                    userView, "ReadInfoExecutionCourseByOID", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoExecutionCourse.getAssociatedInfoCurricularCourses() != null) {
            Collections.sort(infoExecutionCourse.getAssociatedInfoCurricularCourses(),
                    new BeanComparator("name"));
        }

        request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourse);
        fillForm(form, infoExecutionCourse);

        return mapping.findForward("editExecutionCourse");
    }

    public ActionForward updateExecutionCourse(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);

        getAndSetStringToRequest(request, "executionCourseId");
        separateLabel(actionForm, request, "executionPeriod", "executionPeriodId", "executionPeriodName");

        DynaActionForm executionCourseForm = (DynaValidatorForm) actionForm;
        Boolean executionCoursesNotLinked = (Boolean) executionCourseForm
                .get("executionCoursesNotLinked");
        if (executionCoursesNotLinked == null || executionCoursesNotLinked.equals(Boolean.FALSE)) {
            separateLabel(actionForm, request, "executionDegree", "executionDegreeId",
                    "executionDegreeName");

            String curYear = getAndSetStringToRequest(request, "curYear");
            executionCourseForm.set("curYear", curYear);
        } else {
            executionCourseForm.set("executionCoursesNotLinked", executionCoursesNotLinked);
        }

        final InfoExecutionCourseEditor infoExecutionCourseEditor = fillInfoExecutionCourseFromForm(actionForm, request);
        InfoExecutionCourse infoExecutionCourse = null;
        Object args[] = { infoExecutionCourseEditor };
        try {
            infoExecutionCourse = (InfoExecutionCourse) ServiceManagerServiceFactory.executeService(
                    userView, "EditExecutionCourseByManager", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }
        if (infoExecutionCourse.getAssociatedInfoCurricularCourses() != null) {
            Collections.sort(infoExecutionCourse.getAssociatedInfoCurricularCourses(),
                    new BeanComparator("name"));
        }

        request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourse);

        return mapping.findForward("viewExecutionCourse");
    }

    public ActionForward deleteExecutionCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        String executionCourseId = getAndSetStringToRequest(request, "executionCourseId");
        separateLabel(form, request, "executionPeriod", "executionPeriodId", "executionPeriodName");

        String executionCoursesNotLinked = getAndSetStringToRequest(request, "executionCoursesNotLinked");
        DynaActionForm executionCourseForm = (DynaValidatorForm) form;
        if (executionCoursesNotLinked == null || executionCoursesNotLinked.equals("null")
                || executionCoursesNotLinked.equals(Boolean.FALSE.toString())) {
            separateLabel(form, request, "executionDegree", "executionDegreeId", "executionDegreeName");

            String curYear = getAndSetStringToRequest(request, "curYear");
            executionCourseForm.set("curYear", curYear);
        } else {
            executionCourseForm.set("executionCoursesNotLinked", new Boolean(executionCoursesNotLinked));
        }

        //		List internalIds = Arrays.asList((Integer[])
        // deleteForm.get("internalIds"));

        List internalIds = new ArrayList();
        internalIds.add(new Integer(executionCourseId));

        List errorCodes = new ArrayList();
        Object args[] = { internalIds };
        try {
            errorCodes = (List) ServiceUtils.executeService(userView, "DeleteExecutionCourses", args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        if (!errorCodes.isEmpty()) {
            ActionErrors actionErrors = new ActionErrors();
            Iterator codesIter = errorCodes.iterator();
            ActionError error = null;

            while (codesIter.hasNext()) {
                error = new ActionError("errors.invalid.delete.not.empty.execution.course", codesIter
                        .next());
                actionErrors.add("errors.invalid.delete.not.empty.execution.course", error);
            }
            saveErrors(request, actionErrors);
        }

        return prepareEditExecutionCourse(mapping, form, request, response);
    }

    private Integer separateLabel(ActionForm form, HttpServletRequest request, String property,
            String id, String name) {

        DynaActionForm executionCourseForm = (DynaActionForm) form;

        // the value returned to action is a string name~idInternal
        String object = (String) executionCourseForm.get(property);
        if (object == null || object.length() <= 0) {
            object = (String) request.getAttribute(property);
            if (object == null) {
                object = request.getParameter(property);
            }
        }

        Integer objectId = null;
        String objectName = null;
        if (object != null && object.length() > 0 && object.indexOf("~") > 0) {
            executionCourseForm.set(property, object);
            request.setAttribute(property, object);

            objectId = Integer.valueOf(StringUtils.substringAfter(object, "~"));
            objectName = object.substring(0, object.indexOf("~"));

            request.setAttribute(name, objectName);
            request.setAttribute(id, objectId);
        }

        return objectId;
    }

    private String getAndSetStringToRequest(HttpServletRequest request, String name) {

        String parameter = request.getParameter(name);
        if (parameter == null) {
            parameter = (String) request.getAttribute(name);
        }
        request.setAttribute(name, parameter);
        return parameter;
    }

    private InfoExecutionCourseEditor fillInfoExecutionCourseFromForm(ActionForm actionForm,
            HttpServletRequest request) {

        InfoExecutionCourseEditor infoExecutionCourse = new InfoExecutionCourseEditor();

        DynaActionForm editExecutionCourseForm = (DynaActionForm) actionForm;

        try {
            infoExecutionCourse.setIdInternal(new Integer((String) editExecutionCourseForm
                    .get("executionCourseId")));
            infoExecutionCourse.setNome((String) editExecutionCourseForm.get("name"));
            infoExecutionCourse.setSigla((String) editExecutionCourseForm.get("code"));
            infoExecutionCourse.setTheoreticalHours(new Double((String) editExecutionCourseForm.get("theoreticalHours")));
            infoExecutionCourse.setTheoPratHours(new Double((String) editExecutionCourseForm.get("theoPratHours")));
            infoExecutionCourse.setPraticalHours(new Double((String) editExecutionCourseForm.get("praticalHours")));
            infoExecutionCourse.setLabHours(new Double((String) editExecutionCourseForm.get("labHours")));            
            infoExecutionCourse.setSeminaryHours(new Double((String) editExecutionCourseForm.get("seminaryHours")));
            infoExecutionCourse.setProblemsHours(new Double((String) editExecutionCourseForm.get("problemsHours")));
            infoExecutionCourse.setFieldWorkHours(new Double((String) editExecutionCourseForm.get("fieldWorkHours")));
            infoExecutionCourse.setTrainingPeriodHours(new Double((String) editExecutionCourseForm.get("trainingPeriodHours")));
            infoExecutionCourse.setTutorialOrientationHours(new Double((String) editExecutionCourseForm.get("tutorialOrientationHours")));
            infoExecutionCourse.setComment((String) editExecutionCourseForm.get("comment"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return infoExecutionCourse;
    }

    private void fillForm(ActionForm form, InfoExecutionCourse infoExecutionCourse) {
        DynaActionForm executionCourseForm = (DynaActionForm) form;
        executionCourseForm.set("name", infoExecutionCourse.getNome());
        executionCourseForm.set("code", infoExecutionCourse.getSigla());
        if (infoExecutionCourse.getTheoreticalHours() != null) {
            executionCourseForm.set("theoreticalHours", infoExecutionCourse.getTheoreticalHours()
                    .toString());
        }
        if (infoExecutionCourse.getPraticalHours() != null) {
            executionCourseForm.set("praticalHours", infoExecutionCourse.getPraticalHours().toString());
        }
        if (infoExecutionCourse.getTheoPratHours() != null) {
            executionCourseForm.set("theoPratHours", infoExecutionCourse.getTheoPratHours().toString());
        }
        if (infoExecutionCourse.getLabHours() != null) {
            executionCourseForm.set("labHours", infoExecutionCourse.getLabHours().toString());
        }
        if (infoExecutionCourse.getSeminaryHours() != null) {
            executionCourseForm.set("seminaryHours", infoExecutionCourse.getSeminaryHours().toString());
        }
        if (infoExecutionCourse.getProblemsHours() != null) {
            executionCourseForm.set("problemsHours", infoExecutionCourse.getProblemsHours().toString());
        }
        if (infoExecutionCourse.getFieldWorkHours() != null) {
            executionCourseForm.set("fieldWorkHours", infoExecutionCourse.getFieldWorkHours().toString());
        }
        if (infoExecutionCourse.getTrainingPeriodHours() != null) {
            executionCourseForm.set("trainingPeriodHours", infoExecutionCourse.getTrainingPeriodHours().toString());
        }
        if (infoExecutionCourse.getTutorialOrientationHours() != null) {
            executionCourseForm.set("tutorialOrientationHours", infoExecutionCourse.getTutorialOrientationHours().toString());
        }
        executionCourseForm.set("comment", infoExecutionCourse.getComment());
    }
}