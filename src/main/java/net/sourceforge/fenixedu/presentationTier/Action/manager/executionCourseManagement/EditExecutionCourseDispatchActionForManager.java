package net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement.ReadExecutionDegreesByExecutionPeriodId;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Fernanda Quitério 19/Dez/2003
 * 
 */
@Mapping(path = "/editExecutionCourse", module = "manager",
        input = "/editExecutionCourse.do?method=prepareEditECChooseExecDegreeAndCurYear&amp;page=0", scope = "request",
        parameter = "method", attribute = "executionCourseForm", formBean = "executionCourseForm")
@Forwards({
        @Forward(name = "prepareEditECChooseExecutionPeriod",
                path = "/manager/executionCourseManagement/prepareEditECChooseExecutionPeriod.jsp"),
        @Forward(name = "prepareEditECChooseExecDegreeAndCurYear",
                path = "/manager/executionCourseManagement/prepareEditECChooseExecutionPeriod.jsp"),
        @Forward(name = "prepareEditExecutionCourse", path = "/manager/executionCourseManagement/prepareEditExecutionCourse.jsp"),
        @Forward(name = "editExecutionCourse", path = "/manager/executionCourseManagement/editExecutionCourse.jsp"),
        @Forward(name = "viewExecutionCourse", path = "/manager/executionCourseManagement/viewExecutionCourse.jsp"),
        @Forward(name = "listExecutionCourseActions", path = "/manager/executionCourseManagement/listExecutionCourseActions.jsp") })
public class EditExecutionCourseDispatchActionForManager extends EditExecutionCourseDispatchAction {

    public ActionForward prepareEditECChooseExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

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
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

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
            HttpServletResponse response) throws FenixActionException {

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
                    ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear.run(executionDegreeId,
                            executionPeriodId, curYear);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
        request.setAttribute(PresentationConstants.EXECUTION_COURSE_LIST_KEY, infoExecutionCourses);

        return mapping.findForward("prepareEditExecutionCourse");
    }

}
