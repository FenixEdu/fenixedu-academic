/*
 * Created on 5/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Tânia Pousão
 * 
 */
public class ExemptionGratuityAction extends FenixDispatchAction {

    public ActionForward prepareReadStudent(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        // execution years
        List executionYears = null;
        Object[] args = {};
        try {
            executionYears = (List) ServiceManagerServiceFactory
                    .executeService(null, "ReadNotClosedExecutionYears", args);
        } catch (FenixServiceException e) {
            errors.add("noExecutionYears", new ActionError(
                    "error.impossible.insertExemptionGratuity"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (executionYears == null || executionYears.size() <= 0) {
            errors.add("noExecutionYears", new ActionError(
                    "error.impossible.insertExemptionGratuity"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(new BeanComparator("year"), true);
        Collections.sort(executionYears, comparator);

        List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
        request.setAttribute("executionYears", executionYearLabels);

        DynaActionForm studentForm = (DynaActionForm) actionForm;
        studentForm.set("studentNumber", null);

        return mapping.findForward("chooseStudent");
    }

    private List buildLabelValueBeanForJsp(List infoExecutionYears) {
        List executionYearLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionYears, new Transformer() {
            public Object transform(Object arg0) {
                InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

                LabelValueBean executionYear = new LabelValueBean(
                        infoExecutionYear.getYear(), infoExecutionYear
                                .getYear());
                return executionYear;
            }
        }, executionYearLabels);
        return executionYearLabels;
    }

    public ActionForward readStudent(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session
                .getAttribute(SessionConstants.U_VIEW);

        // Read parameters
        String executionYear = request.getParameter("executionYear");
        request.setAttribute("executionYear", executionYear);

        String parameter = request.getParameter("studentNumber");
        Integer studentNumber = null;
        try {
            studentNumber = new Integer(parameter);
        } catch (NumberFormatException e) {
            errors.add("errors", new ActionError(
                    "error.tutor.numberAndRequired"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        request.setAttribute("studentNumber", studentNumber);

        List studentCurricularPlans = null;
        Object[] args = { studentNumber, DegreeType.MASTER_DEGREE};
        try {
            studentCurricularPlans = (List) ServiceManagerServiceFactory
                    .executeService(
                            userView,
                            "ReadStudentCurricularPlansByNumberAndDegreeTypeInMasterDegree",
                            args);
        } catch (FenixServiceException fenixServiceException) {
            fenixServiceException.printStackTrace();
            errors.add("noStudentCurricularPlans", new ActionError(
                    "error.impossible.readStudent"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        if (studentCurricularPlans.size() == 1) {
            request.setAttribute("studentCurricularPlanID",
                    ((InfoStudentCurricularPlan) studentCurricularPlans.get(0))
                            .getIdInternal());
            return mapping.findForward("readExemptionGratuity");
        }

        request.setAttribute("studentCurricularPlans", studentCurricularPlans);
        return mapping.findForward("chooseStudentCurricularPlan");

    }

    public ActionForward readExemptionGratuity(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session
                .getAttribute(SessionConstants.U_VIEW);

        request.setAttribute("percentageOfExemption", ExemptionGratuityType
                .percentageOfExemption());
        request.setAttribute("exemptionGratuityList", ExemptionGratuityType
                .values());

        // Read executionYear
        String executionYear = (String) request.getAttribute("executionYear");
        if (executionYear == null) {
            executionYear = request.getParameter("executionYear");
        }
        request.setAttribute("executionYear", executionYear);

        Integer studentCurricularPlanID = getFromRequest(
                "studentCurricularPlanID", request);
        request
                .setAttribute("studentCurricularPlanID",
                        studentCurricularPlanID);

        // read student curricular plan only for show in jsp
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        Object[] args = { studentCurricularPlanID };
        try {
            infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "ReadStudentCurricularPlanInMasterDegree", args);
        } catch (FenixServiceException fenixServiceException) {
            fenixServiceException.printStackTrace();
            errors.add("noStudentCurricularPlans", new ActionError(
                    "error.impossible.readStudent"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        request
                .setAttribute("studentCurricularPlan",
                        infoStudentCurricularPlan);

        // read gratuity values of the execution course
        InfoGratuityValues infoGratuityValues = null;
        Object args3[] = {
                infoStudentCurricularPlan.getInfoDegreeCurricularPlan()
                        .getIdInternal(), executionYear };
        try {
            infoGratuityValues = (InfoGratuityValues) ServiceManagerServiceFactory
                    .executeService(
                            userView,
                            "ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear",
                            args3);
        } catch (FenixServiceException fenixServiceException) {
            fenixServiceException.printStackTrace();
            errors.add("noGratuitySituation", new ActionError(
                    "error.impossible.insertExemptionGratuity"));
            errors.add("noGratuityValues", new ActionError(
                    "error.impossible.problemsWithDegree",
                    infoStudentCurricularPlan.getInfoDegreeCurricularPlan()
                            .getInfoDegree().getNome()));
            saveErrors(request, errors);
            return mapping.findForward("chooseStudent");
        }
        // if infoGratuityValues is null than it will be informed to the user
        // that this degree hasn't gratuity values defined
        if (infoGratuityValues == null) {
            request.setAttribute("noGratuityValues", "true");
            errors.add("noGratuityValues", new ActionError(
                    "error.impossible.noGratuityValues"));
            saveErrors(request, errors);
            return mapping.findForward("chooseStudent");
        }

        request.setAttribute("gratuityValuesID", infoGratuityValues
                .getIdInternal());

        // read gratuity situation of the student
        InfoGratuitySituation infoGratuitySituation = null;
        Object args2[] = { studentCurricularPlanID,
                infoGratuityValues.getIdInternal() };
        try {
            infoGratuitySituation = (InfoGratuitySituation) ServiceManagerServiceFactory
                    .executeService(
                            userView,
                            "ReadGratuitySituationByStudentCurricularPlanByGratuityValues",
                            args2);
        } catch (FenixServiceException fenixServiceException) {
            fenixServiceException.printStackTrace();
            errors.add("noGratuitySituation", new ActionError(
                    "error.impossible.insertExemptionGratuity"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        // if infoGratuitySituation is null than it will be created in next step
        if (infoGratuitySituation != null) {
            request.setAttribute("gratuitySituationID", infoGratuitySituation
                    .getIdInternal());
        }

        DynaActionForm exemptionGrauityForm = (DynaActionForm) actionForm;
        fillForm(infoGratuitySituation, request, exemptionGrauityForm);

        return mapping.findForward("manageExemptionGratuity");
    }

    private void fillForm(InfoGratuitySituation infoGratuitySituation,
            HttpServletRequest request, DynaActionForm exemptionGrauityForm) {
        if (infoGratuitySituation != null) {
            Integer exemptionPercentage = infoGratuitySituation
                    .getExemptionPercentage();
            if (exemptionPercentage != null) {
                if (ExemptionGratuityType.percentageOfExemption().contains(
                        exemptionPercentage)) {
                    exemptionGrauityForm.set("valueExemptionGratuity", String
                            .valueOf(exemptionPercentage));
                } else if (exemptionPercentage.intValue() > 0) {
                    exemptionGrauityForm.set("valueExemptionGratuity", "-1");
                    exemptionGrauityForm.set("otherValueExemptionGratuity",
                            String.valueOf(exemptionPercentage));
                }
            }

            if (infoGratuitySituation.getExemptionValue() != null) {
                exemptionGrauityForm.set("adHocValueExemptionGratuity",
                        infoGratuitySituation.getExemptionValue());
            }

            if (infoGratuitySituation.getExemptionType() != null) {
                exemptionGrauityForm.set("justificationExemptionGratuity",
                        infoGratuitySituation.getExemptionType().name());
            }
            exemptionGrauityForm.set("otherJustificationExemptionGratuity",
                    infoGratuitySituation.getExemptionDescription());
        }
    }

    private Integer getFromRequest(String parameter, HttpServletRequest request) {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString != null) // parameter
        {
            try {
                parameterCode = new Integer(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        } else // request
        {
            if (request.getAttribute(parameter) instanceof String) {
                try {
                    parameterCode = new Integer((String) request
                            .getAttribute(parameter));
                } catch (Exception exception) {
                    return null;
                }
            } else if (request.getAttribute(parameter) instanceof Integer) {
                parameterCode = (Integer) request.getAttribute(parameter);
            }
        }
        return parameterCode;
    }
}