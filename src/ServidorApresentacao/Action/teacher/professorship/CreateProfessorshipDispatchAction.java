/*
 * Created on Dec 10, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.teacher.professorship;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoProfessorship;
import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.PeriodState;

/**
 * @author jpvl
 */
public class CreateProfessorshipDispatchAction extends DispatchAction {

    public ActionForward createProfessorship(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;
        Integer teacherNumber = Integer
                .valueOf((String) teacherExecutionCourseForm.get("teacherNumber"));

        Boolean responsibleFor = (Boolean) teacherExecutionCourseForm.get("responsibleFor");

        Integer executionCourseId = Integer.valueOf((String) teacherExecutionCourseForm
                .get("executionCourseId"));

        InfoProfessorship infoProfessorship = new InfoProfessorship();

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(executionCourseId);
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(teacherNumber);

        infoProfessorship.setInfoExecutionCourse(infoExecutionCourse);
        infoProfessorship.setInfoTeacher(infoTeacher);

        Object arguments[] = { infoProfessorship, responsibleFor };

        executeService("InsertProfessorshipByDepartment", request, arguments);

        return mapping.findForward("final-step");
    }

    /**
     * @param string
     * @param request
     * @param arguments
     * @return
     */
    private Object executeService(String serviceName, HttpServletRequest request, Object[] arguments)
            throws FenixServiceException {
        IUserView userView = SessionUtils.getUserView(request);
        return ServiceUtils.executeService(userView, serviceName, arguments);
    }

    private List getExecutionDegrees(HttpServletRequest request) throws FenixServiceException {
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute("infoExecutionPeriod");
        Object[] arguments = { infoExecutionPeriod.getInfoExecutionYear(), null };
        List executionDegrees = (List) executeService(
                "ReadExecutionDegreesByExecutionYearAndDegreeType", request, arguments);

        ComparatorChain comparatorChain = new ComparatorChain();

        comparatorChain.addComparator(new BeanComparator(
                "infoDegreeCurricularPlan.infoDegree.tipoCurso.tipoCurso"));
        comparatorChain.addComparator(new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));

        Collections.sort(executionDegrees, comparatorChain);

        return executionDegrees;
    }

    /**
     * @param teacherExecutionCourseForm
     * @param request
     */
    private void prepareConstants(DynaActionForm teacherExecutionCourseForm, HttpServletRequest request)
            throws FenixServiceException {
        Integer teacherNumber = Integer
                .valueOf((String) teacherExecutionCourseForm.get("teacherNumber"));

        Object[] arguments = { teacherNumber };
        InfoTeacher infoTeacher = (InfoTeacher) executeService("ReadTeacherByNumber", request, arguments);

        request.setAttribute("infoTeacher", infoTeacher);
    }

    private void prepareFirstStep(DynaValidatorForm teacherExecutionCourseForm,
            HttpServletRequest request) throws FenixServiceException {
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
            HttpServletRequest request) throws FenixServiceException {
        prepareFirstStep(teacherExecutionCourseForm, request);
        List executionDegrees = getExecutionDegrees(request);
        request.setAttribute("executionDegrees", executionDegrees);
    }

    private void prepareThirdStep(DynaValidatorForm teacherExecutionCourseForm,
            HttpServletRequest request) throws FenixServiceException {
        prepareSecondStep(teacherExecutionCourseForm, request);
        Integer executionDegreeId = Integer.valueOf((String) teacherExecutionCourseForm
                .get("executionDegreeId"));
        Integer executionPeriodId = Integer.valueOf((String) teacherExecutionCourseForm
                .get("executionPeriodId"));
        Object[] arguments = { executionDegreeId, executionPeriodId };

        List executionCourses = (List) executeService("ReadExecutionCoursesByExecutionDegree", request,
                arguments);

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

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
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