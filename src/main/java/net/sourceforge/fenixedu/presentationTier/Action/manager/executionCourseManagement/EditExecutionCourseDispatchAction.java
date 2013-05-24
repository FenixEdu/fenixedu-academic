package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteExecutionCourses;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.EditExecutionCourseInfo;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadInfoExecutionCourseByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.CourseLoadBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Fernanda Quitério 19/Dez/2003
 * 
 */
public class EditExecutionCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEditECChooseExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = UserView.getUser();
        List infoExecutionPeriods = null;

        infoExecutionPeriods = ReadExecutionPeriods.run();
        if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
            infoExecutionPeriods = (List) CollectionUtils.select(infoExecutionPeriods, new Predicate() {
                @Override
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
            request.setAttribute(PresentationConstants.LIST_EXECUTION_PERIODS, executionPeriodLabels);
        }

        return mapping.findForward("prepareEditECChooseExecutionPeriod");
    }

    private List buildLabelValueBeanForJsp(List infoExecutionPeriods) {

        List executionPeriodLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionPeriods, new Transformer() {

            @Override
            public Object transform(Object arg0) {

                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;
                LabelValueBean executionPeriod =
                        new LabelValueBean(infoExecutionPeriod.getName() + " - "
                                + infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod.getName() + " - "
                                + infoExecutionPeriod.getInfoExecutionYear().getYear() + "~"
                                + infoExecutionPeriod.getIdInternal().toString());
                return executionPeriod;
            }
        }, executionPeriodLabels);
        return executionPeriodLabels;
    }

    public ActionForward prepareEditECChooseExecDegreeAndCurYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = UserView.getUser();
        buildCurricularYearLabelValueBean(request);

        Integer executionPeriodId = separateLabel(form, request, "executionPeriod", "executionPeriodId", "executionPeriodName");
        request.setAttribute("executionPeriodId", executionPeriodId);

        List<InfoExecutionDegree> executionDegreeList = null;
        try {
            executionDegreeList = ReadExecutionDegreesByExecutionPeriodId.run(executionPeriodId);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List courses = new ArrayList();
        courses.add(new LabelValueBean("escolher", ""));
        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        buildExecutionDegreeLabelValueBean(executionDegreeList, courses);
        request.setAttribute(PresentationConstants.DEGREES, courses);

        return mapping.findForward("prepareEditECChooseExecDegreeAndCurYear");
    }

    private void buildExecutionDegreeLabelValueBean(List executionDegreeList, List courses) {

        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name =
                    infoExecutionDegree.getInfoDegreeCurricularPlan().getDegreeCurricularPlan()
                            .getPresentationName(infoExecutionDegree.getInfoExecutionYear().getExecutionYear());
            /*
            TODO: DUPLICATE check really needed?
            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em " + name;
            name +=
                    duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";
            */
            courses.add(new LabelValueBean(name, name + "~" + infoExecutionDegree.getIdInternal().toString()));
        }
    }

    private void buildCurricularYearLabelValueBean(HttpServletRequest request) {
        List curricularYears = RequestUtils.buildCurricularYearLabelValueBean();
        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_LIST_KEY, curricularYears);
    }

    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {

        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2))) {
                return true;
            }
        }
        return false;
    }

    public ActionForward prepareEditExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = UserView.getUser();

        Integer executionPeriodId = separateLabel(form, request, "executionPeriod", "executionPeriodId", "executionPeriodName");

        DynaActionForm executionCourseForm = (DynaValidatorForm) form;
        Boolean getNotLinked = (Boolean) executionCourseForm.get("executionCoursesNotLinked");
        Integer executionDegreeId = null;
        Integer curYear = null;
        if (getNotLinked == null || getNotLinked.equals(Boolean.FALSE)) {
            executionDegreeId = separateLabel(form, request, "executionDegree", "executionDegreeId", "executionDegreeName");

            curYear = Integer.valueOf((String) executionCourseForm.get("curYear"));
            request.setAttribute("curYear", curYear);
        } else {
            request.setAttribute("executionCoursesNotLinked", getNotLinked.toString());
        }

        List infoExecutionCourses;
        try {
            infoExecutionCourses =
                    (List) ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear.run(executionDegreeId,
                            executionPeriodId, curYear);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
        request.setAttribute(PresentationConstants.EXECUTION_COURSE_LIST_KEY, infoExecutionCourses);

        return mapping.findForward("prepareEditExecutionCourse");
    }

    private DynaActionForm prepareReturnAttributes(ActionForm form, HttpServletRequest request) {
        separateLabel(form, request, "executionPeriod", "executionPeriodId", "executionPeriodName");

        String executionCoursesNotLinked = RequestUtils.getAndSetStringToRequest(request, "executionCoursesNotLinked");
        DynaActionForm executionCourseForm = (DynaValidatorForm) form;
        Boolean chooseNotLinked = null;
        if (executionCoursesNotLinked == null || executionCoursesNotLinked.equals("null")
                || executionCoursesNotLinked.equals(Boolean.FALSE.toString())) {

            separateLabel(form, request, "executionDegree", "executionDegreeId", "executionDegreeName");
            separateLabel(form, request, "curYear", "curYearId", "curYearName");
            //String curYear = getAndSetStringToRequest(request, "curYear");
            String curYear = (String) request.getAttribute("curYear");
            executionCourseForm.set("curYear", curYear);
            chooseNotLinked = new Boolean(false);
        } else {
            chooseNotLinked = new Boolean(executionCoursesNotLinked);
            executionCourseForm.set("executionCoursesNotLinked", chooseNotLinked);
        }
        return executionCourseForm;
    }

    public ActionForward editExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        DynaActionForm executionCourseForm = prepareReturnAttributes(form, request);

        InfoExecutionCourse infoExecutionCourse;
        try {
            infoExecutionCourse = ReadInfoExecutionCourseByOID.run(Integer.valueOf(executionCourseId));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoExecutionCourse.getAssociatedInfoCurricularCourses() != null) {
            Collections.sort(infoExecutionCourse.getAssociatedInfoCurricularCourses(), new BeanComparator("name"));
        }

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);
        fillForm(form, infoExecutionCourse, request);

        List<LabelValueBean> entryPhases = new ArrayList<LabelValueBean>();
        for (EntryPhase entryPhase : EntryPhase.values()) {
            LabelValueBean labelValueBean = new LabelValueBean(entryPhase.getLocalizedName(), entryPhase.getName());
            entryPhases.add(labelValueBean);
        }
        request.setAttribute("entryPhases", entryPhases);

        prepareReturnSessionBean(request, (Boolean) executionCourseForm.get("executionCoursesNotLinked"), executionCourseId);

        return mapping.findForward("editExecutionCourse");
    }

    private void prepareReturnSessionBean(HttpServletRequest request, Boolean chooseNotLinked, String executionCourseId) {
        // Preparing sessionBean for the EditExecutionCourseDA.list...Actions...
        Integer executionPeriodId = (Integer) request.getAttribute("executionPeriodId");

        ExecutionCourse executionCourse = null;
        if (!net.sourceforge.fenixedu.util.StringUtils.isEmpty(executionCourseId)) {
            executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(Integer.valueOf(executionCourseId));
        }
        ExecutionSemester executionPeriod = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);

        ExecutionCourseBean sessionBean = new ExecutionCourseBean();

        sessionBean.setSourceExecutionCourse(executionCourse);
        sessionBean.setExecutionSemester(executionPeriod);
        sessionBean.setChooseNotLinked(chooseNotLinked);

        if (!chooseNotLinked) {
            Integer executionDegreeId = (Integer) request.getAttribute("executionDegreeId");
            Integer curricularYearId = (Integer) request.getAttribute("curYearId");

            ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);
            CurricularYear curYear = RootDomainObject.getInstance().readCurricularYearByOID(curricularYearId);

            sessionBean.setExecutionDegree(executionDegree);
            sessionBean.setCurricularYear(curYear);
        }

        request.setAttribute("sessionBean", sessionBean);
    }

    public ActionForward updateExecutionCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");
        DynaActionForm executionCourseForm = prepareReturnAttributes(actionForm, request);

        final InfoExecutionCourseEditor infoExecutionCourseEditor = fillInfoExecutionCourseFromForm(actionForm, request);
        InfoExecutionCourse infoExecutionCourse = null;

        try {
            infoExecutionCourse = EditExecutionCourseInfo.run(infoExecutionCourseEditor);

        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        if (infoExecutionCourse.getAssociatedInfoCurricularCourses() != null) {
            Collections.sort(infoExecutionCourse.getAssociatedInfoCurricularCourses(), new BeanComparator("name"));
        }

        request.setAttribute(PresentationConstants.EXECUTION_COURSE, infoExecutionCourse);

        prepareReturnSessionBean(request, (Boolean) executionCourseForm.get("executionCoursesNotLinked"), executionCourseId);

        return mapping.findForward("viewExecutionCourse");
    }

    public ActionForward deleteExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        String executionCourseId = RequestUtils.getAndSetStringToRequest(request, "executionCourseId");

        DynaActionForm executionCourseForm = prepareReturnAttributes(form, request);

        List<Integer> internalIds = new ArrayList<Integer>();
        internalIds.add(new Integer(executionCourseId));

        List<String> errorCodes = new ArrayList<String>();

        ExecutionCourse executionCourseToBeDeleted =
                RootDomainObject.getInstance().readExecutionCourseByOID(Integer.valueOf(executionCourseId));
        String executionCourseName = executionCourseToBeDeleted.getNome();
        String executionCourseSigla = executionCourseToBeDeleted.getSigla();

        try {
            errorCodes = DeleteExecutionCourses.run(internalIds);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        if (!errorCodes.isEmpty()) {
            for (String errorCode : errorCodes) {
                addActionMessage("error", request, "errors.invalid.delete.not.empty.execution.course", errorCode);
            }
        } else {
            addActionMessage("success", request, "message.manager.executionCourseManagement.deleteExecutionCourse.success",
                    executionCourseName, executionCourseSigla);
        }

        prepareReturnSessionBean(request, (Boolean) executionCourseForm.get("executionCoursesNotLinked"), null);

        return mapping.findForward("listExecutionCourseActions");
    }

    private Integer separateLabel(ActionForm form, HttpServletRequest request, String property, String id, String name) {

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

    private InfoExecutionCourseEditor fillInfoExecutionCourseFromForm(ActionForm actionForm, HttpServletRequest request) {

        InfoExecutionCourseEditor infoExecutionCourse = new InfoExecutionCourseEditor();

        DynaActionForm editExecutionCourseForm = (DynaActionForm) actionForm;

        try {
            infoExecutionCourse.setIdInternal(new Integer((String) editExecutionCourseForm.get("executionCourseId")));
            infoExecutionCourse.setNome((String) editExecutionCourseForm.get("name"));
            infoExecutionCourse.setSigla((String) editExecutionCourseForm.get("code"));
            infoExecutionCourse.setComment((String) editExecutionCourseForm.get("comment"));
            infoExecutionCourse.setAvailableGradeSubmission(Boolean.valueOf(editExecutionCourseForm
                    .getString("availableGradeSubmission")));
            infoExecutionCourse.setEntryPhase(EntryPhase.valueOf(editExecutionCourseForm.getString("entryPhase")));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return infoExecutionCourse;
    }

    private void fillForm(ActionForm form, InfoExecutionCourse infoExecutionCourse, HttpServletRequest request) {

        DynaActionForm executionCourseForm = (DynaActionForm) form;
        executionCourseForm.set("name", infoExecutionCourse.getNome());
        executionCourseForm.set("code", infoExecutionCourse.getSigla());
        executionCourseForm.set("comment", infoExecutionCourse.getComment());
        executionCourseForm.set("entryPhase", infoExecutionCourse.getEntryPhase().getName());
        if (infoExecutionCourse.getAvailableGradeSubmission() != null) {
            executionCourseForm.set("availableGradeSubmission", infoExecutionCourse.getAvailableGradeSubmission().toString());
        }
        request.setAttribute("courseLoadBean", new CourseLoadBean(infoExecutionCourse.getExecutionCourse()));
    }
}