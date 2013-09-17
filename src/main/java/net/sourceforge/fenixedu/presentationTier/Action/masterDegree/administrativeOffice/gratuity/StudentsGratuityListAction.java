/*
 * Created on 20/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearsByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.degree.execution.ReadExecutionDegreesByExecutionYearAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.ReadExecutionDegreeByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.commons.ReadExecutionDegreesByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadGratuitySituationListByExecutionDegreeAndSpecialization;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

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
import org.apache.struts.util.MessageResources;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Tânia Pousão
 * 
 */
public class StudentsGratuityListAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // execution years
        List executionYears = ReadNotClosedExecutionYears.run();

        if (executionYears != null && !executionYears.isEmpty()) {
            ComparatorChain comparator = new ComparatorChain();
            comparator.addComparator(new BeanComparator("year"), true);
            Collections.sort(executionYears, comparator);

            List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
            request.setAttribute("executionYears", executionYearLabels);
        }

        return mapping.findForward("chooseStudentsGratuityList");
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

    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    public ActionForward prepareChooseDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        IUserView userView = UserView.getUser();

        DynaActionForm studentListForm = (DynaActionForm) actionForm;
        String executionYear = (String) studentListForm.get("executionYear");
        request.setAttribute("executionYear", executionYear);

        List executionDegreeList = ReadExecutionDegreesByExecutionYearAndDegreeType.run(executionYear, DegreeType.MASTER_DEGREE);

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
        List executionDegreeLabels = buildExecutionDegreeLabelValueBean(executionDegreeList, request);

        request.setAttribute(PresentationConstants.DEGREES, executionDegreeLabels);
        request.setAttribute("showNextSelects", "true");

        return chooseExecutionYear(mapping, actionForm, request, response);
    }

    private List buildExecutionDegreeLabelValueBean(List executionDegreeList, HttpServletRequest request) {

        List executionDegreeLabels = new ArrayList();
        Iterator iterator = executionDegreeList.iterator();
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan();
            InfoDegree infoDegree = infoDegreeCurricularPlan.getInfoDegree();
            DegreeType degreeTypeEnum = infoDegree.getDegreeType();
            MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
            String degreeType = messageResources.getMessage(degreeTypeEnum.name());

            name = degreeType + " em " + name;

            name +=
                    duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            executionDegreeLabels.add(new LabelValueBean(name, name + ">" + infoExecutionDegree.getExternalId().toString()));
        }
        return executionDegreeLabels;
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

    public ActionForward studentsGratuityList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        IUserView userView = getUserView(request);

        // read data from form
        String executionYear = null;
        String specialization = null;
        String situation = null;
        String degree = null;

        String orderingType = request.getParameter("order");
        if (orderingType == null) {
            DynaActionForm studentGratuityListForm = (DynaActionForm) actionForm;
            executionYear = (String) studentGratuityListForm.get("executionYear");
            specialization = (String) studentGratuityListForm.get("specialization");
            situation = (String) studentGratuityListForm.get("situation");
            degree = (String) studentGratuityListForm.get("degree");
        } else {
            executionYear = request.getParameter("executionYear");
            specialization = request.getParameter("specialization");
            situation = request.getParameter("situation");
            degree = request.getParameter("degree");
        }

        String executionDegreeId = null;
        try {
            executionDegreeId = findExecutionDegreeId(degree);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            errors.add("noList", new ActionError("error.masterDegree.gratuity.impossible.studentsGratuityList"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        // put data in request
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("specialization", specialization);
        request.setAttribute("degree", degree);
        request.setAttribute("situation", situation);

        HashMap result = null;
        try {
            result =
                    (HashMap) ReadGratuitySituationListByExecutionDegreeAndSpecialization
                            .runReadGratuitySituationListByExecutionDegreeAndSpecialization(executionDegreeId, executionYear,
                                    specialization, situation);
        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            if (exception.getMessage().startsWith("error.impossible.noGratuityValues.degreeName")) {
                String msgError = exception.getMessage().substring(0, exception.getMessage().indexOf(">"));
                String nameExecutionDegree =
                        exception.getMessage()
                                .substring(exception.getMessage().indexOf(">") + 1, exception.getMessage().length());

                errors.add("gratuityValues", new ActionError(msgError, nameExecutionDegree));
            } else {
                errors.add("noList", new ActionError(exception.getLocalizedMessage()));
            }
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (result == null) {
            errors.add("noList", new ActionError("error.masterDegree.gratuity.impossible.studentsGratuityList"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        // order list
        List infoGratuitySituationList = (List) result.get(new Integer(0));
        orderList(infoGratuitySituationList, orderingType);
        request.setAttribute("infoGratuitySituationList", infoGratuitySituationList);

        Double totalPayedValue = (Double) result.get(new Integer(1));
        request.setAttribute("totalPayedValue", totalPayedValue);
        Double totalRemaingValue = (Double) result.get(new Integer(2));
        request.setAttribute("totalRemaingValue", totalRemaingValue);

        return mapping.findForward("studentsGratuityList");
    }

    public ActionForward coordinatorStudentsGratuityList(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        DynaActionForm studentGratuityListForm = (DynaActionForm) actionForm;
        IUserView userView = getUserView(request);

        // data from request
        String executionYearString = null;
        String degreeCurricularPlanID = null;
        String orderingType = null;

        try {

            orderingType = request.getParameter("order");
            if (orderingType != null) {
                executionYearString = request.getParameter("chosenYear");
                degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");

            } else {
                executionYearString = (String) studentGratuityListForm.get("chosenYear");
                degreeCurricularPlanID = (String) studentGratuityListForm.get("degreeCurricularPlanID");
                orderingType = (String) studentGratuityListForm.get("order");
            }
        } catch (NumberFormatException nfe) {
            errors.add("requestParameters", new ActionError("error.masterDegree.gratuity.impossible.studentsGratuityList"));
            return mapping.getInputForward();
        }

        if (executionYearString == null) {
            executionYearString = "";
        }

        InfoExecutionDegree infoExecutionDegree = null;
        try {
            infoExecutionDegree =
                    ReadExecutionDegreeByDegreeCurricularPlanID.runReadExecutionDegreeByDegreeCurricularPlanID(
                            degreeCurricularPlanID, executionYearString);
        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        // required data
        String degree =
                new String(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em "
                        + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
        String specialization = "all";
        String situation = "all";

        List executionYearList = ReadExecutionYearsByDegreeCurricularPlanID.run(degreeCurricularPlanID);

        // getting the gratuity list
        HashMap gratuityList = null;

        try {
            gratuityList =
                    (HashMap) ReadGratuitySituationListByExecutionDegreeAndSpecialization
                            .runReadGratuitySituationListByExecutionDegreeAndSpecialization(infoExecutionDegree.getExternalId(),
                                    infoExecutionDegree.getInfoExecutionYear().getYear(), specialization, situation);
        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            saveErrors(request, errors);
            // return mapping.getInputForward();
        }
        if (gratuityList == null) {
            errors.add("noList", new ActionError("error.masterDegree.gratuity.impossible.studentsGratuityList"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        // order list
        List infoGratuitySituationList = (List) gratuityList.get(new Integer(0));
        orderList(infoGratuitySituationList, orderingType);

        // getting total values
        Double totalPayedValue = (Double) gratuityList.get(new Integer(1));
        Double totalRemaingValue = (Double) gratuityList.get(new Integer(2));

        // setting data onto request
        request.setAttribute("totalPayedValue", totalPayedValue);
        request.setAttribute("totalRemaingValue", totalRemaingValue);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        request.setAttribute("executionYears", executionYearList);
        request.setAttribute("degree", degree);
        request.setAttribute("chosenYear", infoExecutionDegree.getInfoExecutionYear().getYear());
        request.setAttribute("order", orderingType);
        request.setAttribute("infoGratuitySituationList", infoGratuitySituationList);

        return mapping.findForward("studentsGratuityList");
    }

    public ActionForward prepareStudentsGratuityList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();
        IUserView userView = getUserView(request);
        DynaActionForm studentGratuityListForm = (DynaActionForm) actionForm;

        String degree = (String) studentGratuityListForm.get("degree");
        String executionDegreeID = null;

        try {
            executionDegreeID = findExecutionDegreeId(degree);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            errors.add("noList", new ActionError("error.masterDegree.gratuity.impossible.studentsGratuityList"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        InfoExecutionDegree infoExecutionDegree = null;

        try {
            infoExecutionDegree = ReadExecutionDegree.runReadExecutionDegree(executionDegreeID);
        } catch (FenixServiceException exception) {
            throw new FenixActionException(exception);
        }

        List infoExecutionDegrees = null;
        String degreeCurricularPlanID = infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId();

        try {
            infoExecutionDegrees =
                    ReadExecutionDegreesByDegreeCurricularPlanID
                            .runReadExecutionDegreesByDegreeCurricularPlanID(executionDegreeID);
        } catch (FenixServiceException exception) {
            throw new FenixActionException(exception);
        }

        String firstExecutionDegreeID = ((InfoExecutionDegree) infoExecutionDegrees.iterator().next()).getExternalId();
        String secondExecutionDegreeID = ((InfoExecutionDegree) infoExecutionDegrees.get(1)).getExternalId();

        if (executionDegreeID.equals(firstExecutionDegreeID)) {
            degree = (degree.substring(0, degree.lastIndexOf('>') + 1)).concat(secondExecutionDegreeID.toString());
        } else {
            degree = (degree.substring(0, degree.lastIndexOf('>') + 1)).concat(firstExecutionDegreeID.toString());
        }

        studentGratuityListForm.set("degree", degree);

        return studentsGratuityList(mapping, actionForm, request, response);
    }

    private void orderList(List infoGratuitySituationList, String orderingType) {
        if (orderingType == null) {
            // order list by student's number, it is the ordering by default
            Collections.sort(infoGratuitySituationList, new BeanComparator("infoStudentCurricularPlan.infoStudent.number"));
        } else if (orderingType.equals("studentNumber")) {
            // order list by student's number
            Collections.sort(infoGratuitySituationList, new BeanComparator("infoStudentCurricularPlan.infoStudent.number"));
        } else if (orderingType.equals("studentName")) {
            // order list by student's name
            Collections.sort(infoGratuitySituationList, new BeanComparator(
                    "infoStudentCurricularPlan.infoStudent.infoPerson.nome"));
        } else if (orderingType.equals("gratuitySituation")) {
            // order list by gratuity's state
            Collections.sort(infoGratuitySituationList, new BeanComparator("situationType"));
        } else if (orderingType.equals("payedValue")) {
            // order list by apyed value
            Collections.sort(infoGratuitySituationList, new BeanComparator("payedValue"));
        } else if (orderingType.equals("notPayedValue")) {
            // order list by total value
            Collections.sort(infoGratuitySituationList, new BeanComparator("remainingValue"));
        } else if (orderingType.equals("insurance")) {
            Collections.sort(infoGratuitySituationList, new BeanComparator("insurancePayed"));
        } else if (orderingType.equals("scplanState")) {
            Collections.sort(infoGratuitySituationList, new BeanComparator("infoStudentCurricularPlan.currentState.state"));
        } else if (orderingType.equals("dcplan")) {
            Collections.sort(infoGratuitySituationList, new BeanComparator(
                    "infoStudentCurricularPlan.infoDegreeCurricularPlan.name"));
        }
    }

    /**
     * @param degree
     *            that is a string like ' <degree's type>em <degree's name>#
     *            <execution degree's id internal>'
     * @return Integer whith execution degree id internal
     */
    private String findExecutionDegreeId(String degree) {
        String externalId = null;
        // if degree is the string "all", then all degrees are desirable
        if (!degree.equals("all")) {
            String idInString = degree.substring(degree.indexOf(">") + 1, degree.length());
            try {
                if (idInString.length() != 0) {
                    externalId = idInString;
                }
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
                throw new NumberFormatException();
            }
        }

        return externalId;
    }
}