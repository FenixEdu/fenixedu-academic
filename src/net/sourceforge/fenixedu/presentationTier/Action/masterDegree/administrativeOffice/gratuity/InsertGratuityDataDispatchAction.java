package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

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
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues;
import net.sourceforge.fenixedu.dataTransferObject.InfoPaymentPhase;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.Data;

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
 * @author Fernanda Quitério 7/Jan/2003
 *  
 */
public class InsertGratuityDataDispatchAction extends FenixDispatchAction {

    public ActionForward prepareInsertChooseExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        //execution years
        List executionYears = null;
        Object[] args = {};
        try {
            executionYears = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadNotClosedExecutionYears", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        if (executionYears != null && !executionYears.isEmpty()) {
            ComparatorChain comparator = new ComparatorChain();
            comparator.addComparator(new BeanComparator("year"), true);
            Collections.sort(executionYears, comparator);

            List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
            request.setAttribute("executionYears", executionYearLabels);
        }
        return mapping.findForward("prepareInsertGratuityData");

    }

    private List buildLabelValueBeanForJsp(List infoExecutionYears) {
        List executionYearLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionYears, new Transformer() {
            public Object transform(Object arg0) {
                InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

                LabelValueBean executionYear = new LabelValueBean(infoExecutionYear.getYear(),
                        infoExecutionYear.getYear());
                return executionYear;
            }
        }, executionYearLabels);
        return executionYearLabels;
    }

    public ActionForward prepareInsertGratuityDataChooseDegree(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm gratuityForm = (DynaActionForm) form;

        String executionYear = (String) gratuityForm.get("executionYear");
        request.setAttribute("executionYear", executionYear);

        Object args[] = { executionYear, DegreeType.MASTER_DEGREE };
        List executionDegreeList = null;
        try {
            executionDegreeList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
        List executionDegreeLabels = buildExecutionDegreeLabelValueBean(executionDegreeList);

        request.setAttribute(SessionConstants.DEGREES, executionDegreeLabels);
        request.setAttribute("showNextSelects", "true");

        return prepareInsertChooseExecutionYear(mapping, form, request, response);
    }

    private List buildExecutionDegreeLabelValueBean(List executionDegreeList) {
        List executionDegreeLabels = new ArrayList();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " em " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            executionDegreeLabels.add(new LabelValueBean(name, name + "#"
                    + infoExecutionDegree.getIdInternal().toString()));
        }
        return executionDegreeLabels;
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

    public ActionForward prepareInsertGratuityData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        DynaValidatorForm gratuityForm = (DynaValidatorForm) form;
        ActionErrors errors = new ActionErrors();

        String executionYear = (String) gratuityForm.get("executionYear");
        String degree = (String) gratuityForm.get("degree");
        Integer degreeId = Integer.valueOf(StringUtils.substringAfter(degree, "#"));
        String degreeName = degree.substring(0, degree.indexOf("#"));
        gratuityForm.set("degreeName", degreeName);
        request.setAttribute("degreeName", degreeName);
        request.setAttribute("degree", degree);
        request.setAttribute("executionYear", executionYear);

        InfoGratuityValues infoGratuityValues = null;
        Object args[] = { degreeId };
        try {
            infoGratuityValues = (InfoGratuityValues) ServiceUtils.executeService(userView,
                    "ReadGratuityValuesByExecutionDegree", args);
        } catch (FenixServiceException ex) {
            errors.add("gratuityValues", new ActionError(ex.getMessage()));
            saveErrors(request, errors);

        }

        if (infoGratuityValues == null) {
            infoGratuityValues = new InfoGratuityValues();
        }
        if (request.getAttribute("infoPaymentPhases") == null) {

            fillForm(gratuityForm, infoGratuityValues);
            request.setAttribute("infoPaymentPhases", infoGratuityValues.getInfoPaymentPhases());
        }

        return mapping.findForward("insertGratuityData");
    }

    private void fillForm(DynaValidatorForm aForm, InfoGratuityValues infoGratuityValues) {
        if (infoGratuityValues.getIdInternal() != null) {
            aForm.set("gratuityId", infoGratuityValues.getIdInternal().toString());
        }
        if (doubleFilled(infoGratuityValues.getAnualValue())) {
            aForm.set("annualValue", infoGratuityValues.getAnualValue().toString());
        }
        if (doubleFilled(infoGratuityValues.getScholarShipValue())) {
            aForm.set("scholarPart", infoGratuityValues.getScholarShipValue().toString());
        }
        if (doubleFilled(infoGratuityValues.getFinalProofValue())) {
            aForm.set("thesisPart", infoGratuityValues.getFinalProofValue().toString());
        }
        if (infoGratuityValues.getProofRequestPayment() != null
                && infoGratuityValues.getProofRequestPayment().equals(Boolean.TRUE)) {
            aForm.set("paymentWhen", Boolean.TRUE);
        }
        if (doubleFilled(infoGratuityValues.getCourseValue())) {
            aForm.set("unitaryValueCourse", infoGratuityValues.getCourseValue().toString());
        }
        if (doubleFilled(infoGratuityValues.getCreditValue())) {
            aForm.set("unitaryValueCredit", infoGratuityValues.getCreditValue().toString());
        }
        if (infoGratuityValues.getEndPayment() != null) {
            aForm.set("finalDateTotalPayment", Data.format2DayMonthYear(infoGratuityValues
                    .getEndPayment(), "/"));
            aForm.set("totalPayment", Boolean.TRUE);
        }
        if (infoGratuityValues.getStartPayment() != null) {
            aForm.set("initialDateTotalPayment", Data.format2DayMonthYear(infoGratuityValues
                    .getStartPayment(), "/"));
        }
        if (infoGratuityValues.getInfoPaymentPhases() != null
                && infoGratuityValues.getInfoPaymentPhases().size() > 0) {
            aForm.set("partialPayment", Boolean.TRUE);

            if (infoGratuityValues.getRegistrationPayment() != null
                    && infoGratuityValues.getRegistrationPayment().booleanValue()) {
                aForm.set("registrationPayment", infoGratuityValues.getRegistrationPayment());

                InfoPaymentPhase infoPaymentPhase = findRegistrationPayment(infoGratuityValues);

                aForm.set("finalDateRegistrationPayment", Data.format2DayMonthYear(infoPaymentPhase
                        .getEndDate(), "/"));
                aForm.set("registrationValue", infoPaymentPhase.getValue().toString());
                if (infoPaymentPhase.getStartDate() != null) {
                    aForm.set("initialDateRegistrationPayment", Data.format2DayMonthYear(
                            infoPaymentPhase.getStartDate(), "/"));
                }

                removeRegistrationPaymentFromPhases(infoGratuityValues);
            }
            Collections.sort(infoGratuityValues.getInfoPaymentPhases(), new BeanComparator("endDate"));
        }

    }

    private boolean doubleFilled(Double field) {
        if (field != null && field.toString().length() > 0 && !field.toString().equals("0.0")) {
            return true;
        }
        return false;
    }

    private void removeRegistrationPaymentFromPhases(InfoGratuityValues infoGratuityValues) {
        CollectionUtils.filter(infoGratuityValues.getInfoPaymentPhases(), new Predicate() {
            public boolean evaluate(Object arg0) {
                InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) arg0;

                if (infoPaymentPhase.getDescription() == null
                        || !infoPaymentPhase
                                .getDescription()
                                .equals(
                                        net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants.REGISTRATION_PAYMENT)) {
                    return true;
                }
                return false;
            }
        });
    }

    private InfoPaymentPhase findRegistrationPayment(InfoGratuityValues infoGratuityValues) {
        InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) CollectionUtils.find(infoGratuityValues
                .getInfoPaymentPhases(), new Predicate() {
            public boolean evaluate(Object arg0) {
                InfoPaymentPhase infoPaymentPhase = (InfoPaymentPhase) arg0;
                if (infoPaymentPhase.getDescription() != null
                        && infoPaymentPhase
                                .getDescription()
                                .equals(
                                        net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants.REGISTRATION_PAYMENT)) {
                    return true;
                }
                return false;
            }
        });
        return infoPaymentPhase;
    }
}