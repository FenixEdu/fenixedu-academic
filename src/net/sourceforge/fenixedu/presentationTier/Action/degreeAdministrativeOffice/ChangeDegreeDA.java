package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class ChangeDegreeDA extends FenixDispatchAction {

    private static final Comparator EXECUTION_DEGREE_LABEL_VALUE_COMPARATOR = new BeanComparator("label");
    private static final ComparatorChain ENROLEMENT_COMPARATOR = new ComparatorChain();
    static {
        ENROLEMENT_COMPARATOR.addComparator(new BeanComparator("infoExecutionPeriod.infoExecutionYear.year"));
        ENROLEMENT_COMPARATOR.addComparator(new BeanComparator("infoExecutionPeriod.semester"));
        ENROLEMENT_COMPARATOR.addComparator(new BeanComparator("infoCurricularCourse.name"));
    }

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("showForm");
    }

    public ActionForward selectStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final DynaActionForm actionForm = (DynaActionForm) form;
        final String studentNumber = (String) actionForm.get("studentNumber");

        final IUserView userView = SessionUtils.getUserView(request);

        if (studentNumber != null && studentNumber.length() > 0 && StringUtils.isNumeric(studentNumber)) {
            final Object[] args = { Integer.valueOf(studentNumber), DegreeType.DEGREE };
            final InfoStudentCurricularPlan activeInfoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceUtils
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType", args);

            if (activeInfoStudentCurricularPlan == null) {
                final ActionMessages actionMessages = new ActionMessages();
                final ActionMessage actionMessage = new ActionMessage("error.no.student.found");
                actionMessages.add(ActionMessages.GLOBAL_MESSAGE, actionMessage);
                saveMessages(request, actionMessages);
                return prepare(mapping, form, request, response);
            }

            request.setAttribute("activeInfoStudentCurricularPlan", activeInfoStudentCurricularPlan);

            final List<InfoEnrolment> infoEnrolments = activeInfoStudentCurricularPlan.getInfoEnrolments();
            Collections.sort(infoEnrolments, ENROLEMENT_COMPARATOR);

            final List<String> enrolementsToTransferList = new ArrayList<String>();
            final List<String> enrolementsToMaintainList = new ArrayList<String>();
            final List<String> enrolementsToDeleteList = new ArrayList<String>();
            for (final InfoEnrolment infoEnrolment : infoEnrolments) {
                final InfoExecutionPeriod infoExecutionPeriod = infoEnrolment.getInfoExecutionPeriod();
                final InfoExecutionYear infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();
                final InfoEnrolmentEvaluation infoEnrolmentEvaluation = infoEnrolment
                        .getInfoEnrolmentEvaluation();
                final String grade = infoEnrolmentEvaluation.getGrade();
                if (!infoExecutionYear.getState().equals(PeriodState.CURRENT)) {
                    enrolementsToMaintainList.add(infoEnrolment.getIdInternal().toString());
                } else if (grade == null || grade.length() == 0) {
                    enrolementsToDeleteList.add(infoEnrolment.getIdInternal().toString());
                } else {
                    enrolementsToTransferList.add(infoEnrolment.getIdInternal().toString());
                }
            }

            String[] enrolementsToTransferFromForm = (String[]) actionForm.get("enrolementsToTransfer");
            String[] enrolementsToMaintainFromForm = (String[]) actionForm.get("enrolementsToMaintain");
            String[] enrolementsToDeleteFromForm = (String[]) actionForm.get("enrolementsToDelete");
            if ((enrolementsToTransferFromForm == null || enrolementsToTransferFromForm.length == 0)
                    && (enrolementsToMaintainFromForm == null || enrolementsToMaintainFromForm.length == 0)
                    && (enrolementsToDeleteFromForm == null || enrolementsToDeleteFromForm.length == 0)) {
                actionForm.set("enrolementsToTransfer", CollectionUtils.toArrayOfString(enrolementsToTransferList));
                actionForm.set("enrolementsToMaintain", CollectionUtils.toArrayOfString(enrolementsToMaintainList));
                actionForm.set("enrolementsToDelete", CollectionUtils.toArrayOfString(enrolementsToDeleteList));
            }

            request.setAttribute("changeDegreeForm", actionForm);

            final Object[] argsReadExecutionDegrees = { DegreeType.DEGREE };
            final List<InfoExecutionDegree> infoExecutionDegrees = (List<InfoExecutionDegree>) ServiceUtils
                    .executeService(userView, "ReadExecutionDegreesByExecutionYearAndType",
                            argsReadExecutionDegrees);
            final List<LabelValueBean> availableExecutionDegrees = new ArrayList(infoExecutionDegrees
                    .size() - 1);
            for (final InfoExecutionDegree infoExecutionDegree : infoExecutionDegrees) {
                if (!infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal().equals(
                        activeInfoStudentCurricularPlan.getIdInternal())) {
                    final InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan();
                    final InfoDegree infoDegree = infoDegreeCurricularPlan.getInfoDegree();
                    LabelValueBean labelValueBean = new LabelValueBean(
                            infoDegree.getNome() + " " + infoDegreeCurricularPlan.getName(),
                            infoExecutionDegree.getIdInternal().toString());
                    availableExecutionDegrees.add(labelValueBean);
                }
            }
            Collections.sort(availableExecutionDegrees, EXECUTION_DEGREE_LABEL_VALUE_COMPARATOR);
            request.setAttribute("availableExecutionDegrees", availableExecutionDegrees);

        }

        return mapping.findForward("showForm");
    }

    public ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final DynaActionForm actionForm = (DynaActionForm) form;

        selectStudent(mapping, form, request, response);

        final Set<String> enrolements = new HashSet<String>();

        final String[] enrolementsToTransfer = (String[]) actionForm.get("enrolementsToTransfer");
        final String[] enrolementsToMaintain = (String[]) actionForm.get("enrolementsToMaintain");
        final String[] enrolementsToDelete = (String[]) actionForm.get("enrolementsToDelete");

        for (int i = 0; i < enrolementsToTransfer.length; i++) {
            final String enrolement = enrolementsToTransfer[i];
            if (enrolements.contains(enrolement)) {
                final ActionMessages actionMessages = new ActionMessages();
                final ActionMessage actionMessage = new ActionMessage("error.only.one.operation.per.enrolement");
                actionMessages.add(ActionMessages.GLOBAL_MESSAGE, actionMessage);
                saveMessages(request, actionMessages);
                return prepare(mapping, form, request, response);
            } else {
                enrolements.add(enrolement);
            }
        }
        for (int i = 0; i < enrolementsToMaintain.length; i++) {
            final String enrolement = enrolementsToMaintain[i];
            if (enrolements.contains(enrolement)) {
                final ActionMessages actionMessages = new ActionMessages();
                final ActionMessage actionMessage = new ActionMessage("error.only.one.operation.per.enrolement");
                actionMessages.add(ActionMessages.GLOBAL_MESSAGE, actionMessage);
                saveMessages(request, actionMessages);
                return prepare(mapping, form, request, response);
            } else {
                enrolements.add(enrolement);
            }
        }
        for (int i = 0; i < enrolementsToDelete.length; i++) {
            final String enrolement = enrolementsToDelete[i];
            if (enrolements.contains(enrolement)) {
                final ActionMessages actionMessages = new ActionMessages();
                final ActionMessage actionMessage = new ActionMessage("error.only.one.operation.per.enrolement");
                actionMessages.add(ActionMessages.GLOBAL_MESSAGE, actionMessage);
                saveMessages(request, actionMessages);
                return prepare(mapping, form, request, response);
            } else {
                enrolements.add(enrolement);
            }
        }

        return mapping.findForward("showConfirmationScreen");
    }

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final DynaActionForm actionForm = (DynaActionForm) form;

        final String studentNumber = (String) actionForm.get("studentNumber");
        final String executionDegreeToChangeTo = (String) actionForm.get("executionDegreeToChangeTo");
        final String[] enrolementsToTransfer = (String[]) actionForm.get("enrolementsToTransfer");
        final String[] enrolementsToMaintain = (String[]) actionForm.get("enrolementsToMaintain");
        final String[] enrolementsToDelete = (String[]) actionForm.get("enrolementsToDelete");
        final String newStudentCurricularPlanStartDate = (String) actionForm.get("newStudentCurricularPlanStartDate");

        if (!validDate(newStudentCurricularPlanStartDate)) {
            final ActionMessages actionMessages = new ActionMessages();
            final ActionMessage actionMessage = new ActionMessage("error.invalid.date");
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, actionMessage);
            saveMessages(request, actionMessages);
            return prepare(mapping, form, request, response);
        }

        final Set<String> enrolements = new HashSet<String>();

        final Set<Integer> enrolementsToTransferIds = new HashSet<Integer>(enrolementsToTransfer.length);
        for (int i = 0; i < enrolementsToTransfer.length; i++) {
            final String enrolement = enrolementsToTransfer[i];
            if (enrolements.contains(enrolement)) {
                throw new IllegalArgumentException();
            } else {
                enrolements.add(enrolement);
                enrolementsToTransferIds.add(Integer.valueOf(enrolement));
            }
        }
        final Set<Integer> enrolementsToMaintainIds = new HashSet<Integer>(enrolementsToMaintain.length);
        for (int i = 0; i < enrolementsToMaintain.length; i++) {
            final String enrolement = enrolementsToMaintain[i];
            if (enrolements.contains(enrolement)) {
                throw new IllegalArgumentException();
            } else {
                enrolements.add(enrolement);
                enrolementsToMaintainIds.add(Integer.valueOf(enrolement));
            }
        }
        final Set<Integer> enrolementsToDeleteIds = new HashSet<Integer>(enrolementsToDelete.length);
        for (int i = 0; i < enrolementsToDelete.length; i++) {
            final String enrolement = enrolementsToDelete[i];
            if (enrolements.contains(enrolement)) {
                throw new IllegalArgumentException();
            } else {
                enrolements.add(enrolement);
                enrolementsToDeleteIds.add(Integer.valueOf(enrolement));
            }
        }

        final IUserView userView = SessionUtils.getUserView(request);

        final Object[] args = {
                userView.getUtilizador(),
                Integer.valueOf(studentNumber),
                Integer.valueOf(executionDegreeToChangeTo),
                enrolementsToTransferIds,
                enrolementsToMaintainIds,
                enrolementsToDeleteIds,
                dateFormat.parse(newStudentCurricularPlanStartDate)
                };
        ServiceUtils.executeService(userView, "ChangeDegree", args);

        return mapping.findForward("showSucessPage");
    }

    private boolean validDate(final String date) {
        return date != null && date.length() == 10 && date.charAt(2) == '/' && date.charAt(5) == '/';
    }

}