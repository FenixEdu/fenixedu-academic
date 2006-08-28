/*
 * Created on Dec 10, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author jpvl
 */
public class CreateProfessorshipDispatchAction extends FenixDispatchAction {

    public ActionForward createProfessorship(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        final DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;
        
        final Integer executionCourseId = Integer.valueOf((String) teacherExecutionCourseForm.get("executionCourseId"));
        final Integer teacherNumber = Integer.valueOf((String) teacherExecutionCourseForm.get("teacherNumber"));
        final Boolean responsibleFor = (Boolean) teacherExecutionCourseForm.get("responsibleFor");
        
        final Object arguments[] = { executionCourseId, teacherNumber, responsibleFor, 0.0 };
        executeService("InsertProfessorshipByDepartment", request, arguments);
        
        return mapping.findForward("final-step");
    }

    private Object executeService(String serviceName, HttpServletRequest request, Object[] arguments)
            throws FenixServiceException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        return ServiceUtils.executeService(userView, serviceName, arguments);
    }

    private List getExecutionDegrees(HttpServletRequest request) throws FenixServiceException, FenixFilterException {
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute("infoExecutionPeriod");
        Object[] arguments = { infoExecutionPeriod.getInfoExecutionYear().getYear(), null };
        List<InfoExecutionDegree> executionDegrees = (List) executeService(
                "ReadExecutionDegreesByExecutionYearAndDegreeType", request, arguments);

        ComparatorChain comparatorChain = new ComparatorChain();

        comparatorChain.addComparator(new BeanComparator(
                "infoDegreeCurricularPlan.infoDegree.tipoCurso"));
        comparatorChain.addComparator(new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));

        Collections.sort(executionDegrees, comparatorChain);

        MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
        executionDegrees = InfoExecutionDegree.buildLabelValueBeansForList(executionDegrees, messageResources);

        return executionDegrees;
    }

    private void prepareConstants(DynaActionForm teacherExecutionCourseForm, HttpServletRequest request)
            throws FenixServiceException, FenixFilterException {
        Integer teacherNumber = Integer
                .valueOf((String) teacherExecutionCourseForm.get("teacherNumber"));

        Object[] arguments = { teacherNumber };
        InfoTeacher infoTeacher = (InfoTeacher) executeService("ReadTeacherByNumber", request, arguments);

        request.setAttribute("infoTeacher", infoTeacher);
    }

    private void prepareFirstStep(DynaValidatorForm teacherExecutionCourseForm,
            HttpServletRequest request) throws FenixServiceException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        prepareConstants(teacherExecutionCourseForm, request);

        List executionPeriodsNotClosed = (List) ServiceUtils.executeService(userView,
                "ReadNotClosedExecutionPeriods", null);

        setChoosedExecutionPeriod(request, executionPeriodsNotClosed, teacherExecutionCourseForm);

        BeanComparator initialDateComparator = new BeanComparator("beginDate");
        Collections.sort(executionPeriodsNotClosed, new ReverseComparator(initialDateComparator));

        request.setAttribute("executionPeriods", executionPeriodsNotClosed);
    }

    private void prepareSecondStep(DynaValidatorForm teacherExecutionCourseForm,
            HttpServletRequest request) throws FenixServiceException, FenixFilterException {
        prepareFirstStep(teacherExecutionCourseForm, request);
        List executionDegrees = getExecutionDegrees(request);
        request.setAttribute("executionDegrees", executionDegrees);
    }

    private void prepareThirdStep(DynaValidatorForm teacherExecutionCourseForm,
            HttpServletRequest request) throws FenixServiceException, FenixFilterException {
        prepareSecondStep(teacherExecutionCourseForm, request);
        Integer executionDegreeId = Integer.valueOf((String) teacherExecutionCourseForm
                .get("executionDegreeId"));
        Integer executionPeriodId = Integer.valueOf((String) teacherExecutionCourseForm
                .get("executionPeriodId"));
        Object[] arguments = { executionDegreeId, executionPeriodId };

        List executionCourses = (List) executeService("ReadExecutionCoursesByExecutionDegree", request,
                arguments);
        Integer teacherNumber = Integer
                .valueOf((String) teacherExecutionCourseForm.get("teacherNumber"));
        Object[] args = { teacherNumber };
        List executionCoursesToRemove = (List) executeService(
                "ReadExecutionCoursesByTeacherResponsibility", request, args);
        executionCourses.removeAll(executionCoursesToRemove);
        Collections.sort(executionCourses, new BeanComparator("nome"));

        request.setAttribute("executionCourses", executionCourses);

    }

    private void setChoosedExecutionPeriod(HttpServletRequest request, List executionPeriodsNotClosed,
            DynaValidatorForm teacherExecutionCourseForm) {
        Integer executionPeriodIdValue = null;
        try {
            executionPeriodIdValue = Integer.valueOf((String) teacherExecutionCourseForm
                    .get("executionPeriodId"));
        } catch (Exception e) {
            //do nothing
        }
        final Integer executionPeriodId = executionPeriodIdValue;
        InfoExecutionPeriod infoExecutionPeriod = null;
        if (executionPeriodId == null) {
            infoExecutionPeriod = (InfoExecutionPeriod) CollectionUtils.find(executionPeriodsNotClosed,
                    new Predicate() {

                        public boolean evaluate(Object input) {
                            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;

                            return infoExecutionPeriod.getState().equals(PeriodState.CURRENT);
                        }
                    });
        } else {
            infoExecutionPeriod = (InfoExecutionPeriod) CollectionUtils.find(executionPeriodsNotClosed,
                    new Predicate() {

                        public boolean evaluate(Object input) {
                            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;

                            return infoExecutionPeriod.getIdInternal().equals(executionPeriodId);
                        }
                    });

        }
        request.setAttribute("infoExecutionPeriod", infoExecutionPeriod);
    }

    public ActionForward showExecutionDegreeExecutionCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm teacherExecutionCourseForm = (DynaValidatorForm) form;
        prepareFirstStep(teacherExecutionCourseForm, request);

        prepareThirdStep(teacherExecutionCourseForm, request);
        teacherExecutionCourseForm.set("page", new Integer(3));
        return mapping.findForward("third-step");
    }

    public ActionForward showExecutionDegrees(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm teacherExecutionCourseForm = (DynaValidatorForm) form;
        prepareSecondStep(teacherExecutionCourseForm, request);
        teacherExecutionCourseForm.set("page", new Integer(2));
        return mapping.findForward("second-step");
    }

    public ActionForward showExecutionYearExecutionPeriods(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm teacherExecutionCourseForm = (DynaValidatorForm) form;

        prepareFirstStep(teacherExecutionCourseForm, request);
        teacherExecutionCourseForm.set("page", new Integer(1));
        return mapping.findForward("second-step");
    }
}