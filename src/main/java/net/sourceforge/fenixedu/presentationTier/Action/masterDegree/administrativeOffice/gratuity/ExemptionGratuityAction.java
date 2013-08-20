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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlansByNumberAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadGratuitySituationByStudentCurricularPlanByGratuityValues;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gratuity.ExemptionGratuityType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

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

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Tânia Pousão
 * 
 */
public class ExemptionGratuityAction extends FenixDispatchAction {

    public ActionForward prepareReadStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        // execution years
        List executionYears = null;

        executionYears = ReadNotClosedExecutionYears.run();
        if (executionYears == null || executionYears.size() <= 0) {
            errors.add("noExecutionYears", new ActionError("error.impossible.insertExemptionGratuity"));
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
            @Override
            public Object transform(Object arg0) {
                InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

                LabelValueBean executionYear = new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear());
                return executionYear;
            }
        }, executionYearLabels);
        return executionYearLabels;
    }

    public ActionForward readStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        IUserView userView = UserView.getUser();

        // Read parameters
        String executionYearStr = request.getParameter("executionYear");
        request.setAttribute("executionYear", executionYearStr);

        ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearStr);

        String parameter = request.getParameter("studentNumber");
        Integer studentNumber = null;
        try {
            studentNumber = new Integer(parameter);
        } catch (NumberFormatException e) {
            errors.add("errors", new ActionError("error.tutor.numberAndRequired"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        request.setAttribute("studentNumber", studentNumber);

        List<InfoStudentCurricularPlan> infoStudentCurricularPlans = new ArrayList<InfoStudentCurricularPlan>();

        try {
            List<StudentCurricularPlan> studentCurricularPlans =
                    ReadStudentCurricularPlansByNumberAndDegreeType.run(studentNumber, DegreeType.MASTER_DEGREE);

            for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
                if (studentCurricularPlan.getDegreeCurricularPlan().getExecutionYears().contains(executionYear)) {
                    infoStudentCurricularPlans.add(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
                }
            }

        } catch (FenixServiceException fenixServiceException) {
            fenixServiceException.printStackTrace();
            errors.add("noStudentCurricularPlans", new ActionError("error.impossible.readStudent"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        if (infoStudentCurricularPlans.size() == 1) {
            request.setAttribute("studentCurricularPlanID", (infoStudentCurricularPlans.get(0)).getExternalId());
            return mapping.findForward("readExemptionGratuity");
        }

        request.setAttribute("studentCurricularPlans", infoStudentCurricularPlans);
        return mapping.findForward("chooseStudentCurricularPlan");

    }

    public ActionForward readExemptionGratuity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        IUserView userView = getUserView(request);

        request.setAttribute("percentageOfExemption", ExemptionGratuityType.percentageOfExemption());
        request.setAttribute("exemptionGratuityList", ExemptionGratuityType.values());

        // Read executionYear
        String executionYear = (String) request.getAttribute("executionYear");
        if (executionYear == null) {
            executionYear = request.getParameter("executionYear");
        }
        request.setAttribute("executionYear", executionYear);

        String studentCurricularPlanID = getFromRequest("studentCurricularPlanID", request);
        request.setAttribute("studentCurricularPlanID", studentCurricularPlanID);

        // read student curricular plan only for show in jsp
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;

        try {
            infoStudentCurricularPlan = ReadStudentCurricularPlan.run(studentCurricularPlanID);
        } catch (FenixServiceException fenixServiceException) {
            fenixServiceException.printStackTrace();
            errors.add("noStudentCurricularPlans", new ActionError("error.impossible.readStudent"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        request.setAttribute("studentCurricularPlan", infoStudentCurricularPlan);

        // read gratuity values of the execution course
        InfoGratuityValues infoGratuityValues = null;

        try {
            infoGratuityValues =
                    (InfoGratuityValues) ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear.run(infoStudentCurricularPlan
                            .getInfoDegreeCurricularPlan().getExternalId(), executionYear);
        } catch (FenixServiceException fenixServiceException) {
            fenixServiceException.printStackTrace();
            errors.add("noGratuitySituation", new ActionError("error.impossible.insertExemptionGratuity"));
            errors.add("noGratuityValues", new ActionError("error.impossible.problemsWithDegree", infoStudentCurricularPlan
                    .getInfoDegreeCurricularPlan().getInfoDegree().getNome()));
            saveErrors(request, errors);
            return mapping.findForward("chooseStudent");
        }
        // if infoGratuityValues is null than it will be informed to the user
        // that this degree hasn't gratuity values defined
        if (infoGratuityValues == null) {
            request.setAttribute("noGratuityValues", "true");
            errors.add("noGratuityValues", new ActionError("error.impossible.noGratuityValues"));
            saveErrors(request, errors);
            return mapping.findForward("chooseStudent");
        }

        request.setAttribute("gratuityValuesID", infoGratuityValues.getExternalId());

        // read gratuity situation of the student
        InfoGratuitySituation infoGratuitySituation = null;

        try {
            infoGratuitySituation =
                    (InfoGratuitySituation) ReadGratuitySituationByStudentCurricularPlanByGratuityValues.run(
                            studentCurricularPlanID, infoGratuityValues.getExternalId());
        } catch (FenixServiceException fenixServiceException) {
            fenixServiceException.printStackTrace();
            errors.add("noGratuitySituation", new ActionError("error.impossible.insertExemptionGratuity"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        // if infoGratuitySituation is null than it will be created in next step
        if (infoGratuitySituation != null) {
            request.setAttribute("gratuitySituationID", infoGratuitySituation.getExternalId());
        }

        DynaActionForm exemptionGrauityForm = (DynaActionForm) actionForm;
        fillForm(infoGratuitySituation, request, exemptionGrauityForm);

        return mapping.findForward("manageExemptionGratuity");
    }

    private void fillForm(InfoGratuitySituation infoGratuitySituation, HttpServletRequest request,
            DynaActionForm exemptionGrauityForm) {
        if (infoGratuitySituation != null) {
            Integer exemptionPercentage = infoGratuitySituation.getExemptionPercentage();
            if (exemptionPercentage != null) {
                if (ExemptionGratuityType.percentageOfExemption().contains(exemptionPercentage)) {
                    exemptionGrauityForm.set("valueExemptionGratuity", String.valueOf(exemptionPercentage));
                } else if (exemptionPercentage.intValue() > 0) {
                    exemptionGrauityForm.set("valueExemptionGratuity", "-1");
                    exemptionGrauityForm.set("otherValueExemptionGratuity", String.valueOf(exemptionPercentage));
                }
            }

            if (infoGratuitySituation.getExemptionValue() != null) {
                exemptionGrauityForm.set("adHocValueExemptionGratuity", infoGratuitySituation.getExemptionValue());
            }

            if (infoGratuitySituation.getExemptionType() != null) {
                exemptionGrauityForm.set("justificationExemptionGratuity", infoGratuitySituation.getExemptionType().name());
            }
            exemptionGrauityForm.set("otherJustificationExemptionGratuity", infoGratuitySituation.getExemptionDescription());
        }
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString != null) // parameter
        {
            try {
                parameterCode = parameterCodeString;
            } catch (Exception exception) {
                return null;
            }
        } else // request
        {
            if (request.getAttribute(parameter) instanceof String) {
                try {
                    parameterCode = (String) request.getAttribute(parameter);
                } catch (Exception exception) {
                    return null;
                }
            }
        }
        return parameterCode;
    }
}