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
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

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

/**
 * @author Tânia Pousão
 * 
 */
public class StudentsGratuityListAction extends FenixDispatchAction {

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // execution years
        List executionYears = null;
        Object[] args = {};
        try {
            executionYears = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadNotClosedExecutionYears", args);
        } catch (FenixServiceException e) {
            throw new FenixServiceException();
        }

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
            public Object transform(Object arg0) {
                InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

                LabelValueBean executionYear = new LabelValueBean(infoExecutionYear.getYear(),
                        infoExecutionYear.getYear());
                return executionYear;
            }
        }, executionYearLabels);
        return executionYearLabels;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward prepareChooseDegree(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm studentListForm = (DynaActionForm) actionForm;
        String executionYear = (String) studentListForm.get("executionYear");
        request.setAttribute("executionYear", executionYear);

        Object args[] = { executionYear, DegreeType.MASTER_DEGREE };
        List executionDegreeList = null;
        try {
            executionDegreeList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
        } catch (FenixServiceException e) {
            errors.add("impossibleOperation", new ActionError(
                    "error.masterDegree.gratuity.impossible.operation"));
            saveErrors(request, errors);
            return mapping.findForward("choose");
        }

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
        List executionDegreeLabels = buildExecutionDegreeLabelValueBean(executionDegreeList, request);

        request.setAttribute(SessionConstants.DEGREES, executionDegreeLabels);
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
            DegreeType degreeTypeEnum = (DegreeType) infoDegree.getTipoCurso();
            MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
            String degreeType = messageResources.getMessage(degreeTypeEnum.name());

            name = degreeType + " em " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            executionDegreeLabels.add(new LabelValueBean(name, name + ">"
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

    public ActionForward studentsGratuityList(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
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

        Integer executionDegreeId = null;
        try {
            executionDegreeId = findExecutionDegreeId(degree);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            errors.add("noList", new ActionError(
                    "error.masterDegree.gratuity.impossible.studentsGratuityList"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        // put data in request
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("specialization", specialization);
        request.setAttribute("degree", degree);
        request.setAttribute("situation", situation);

        Object[] args = { executionDegreeId, executionYear, specialization, situation };
        HashMap result = null;
        try {
            result = (HashMap) ServiceManagerServiceFactory.executeService(userView,
                    "ReadGratuitySituationListByExecutionDegreeAndSpecialization", args);
        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            if (exception.getMessage().startsWith("error.impossible.noGratuityValues.degreeName")) {
                String msgError = exception.getMessage().substring(0,
                        exception.getMessage().indexOf(">"));
                String nameExecutionDegree = exception.getMessage().substring(
                        exception.getMessage().indexOf(">") + 1, exception.getMessage().length());

                errors.add("gratuityValues", new ActionError(msgError, nameExecutionDegree));
            } else {
                errors.add("noList", new ActionError(exception.getLocalizedMessage()));
            }
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        if (result == null) {
            errors.add("noList", new ActionError(
                    "error.masterDegree.gratuity.impossible.studentsGratuityList"));
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
        Integer degreeCurricularPlanID = null;
        String orderingType = null;
        

        try {

            orderingType = request.getParameter("order");
            if (orderingType != null) {
                executionYearString = request.getParameter("chosenYear");
                degreeCurricularPlanID = new Integer(request
                        .getParameter("degreeCurricularPlanID"));

            } else {
                executionYearString = (String) studentGratuityListForm.get("chosenYear");
                degreeCurricularPlanID = (Integer) studentGratuityListForm.get("degreeCurricularPlanID");
                orderingType = (String) studentGratuityListForm.get("order");
            }
        } catch (NumberFormatException nfe) {
            errors.add("requestParameters", new ActionError(
                    "error.masterDegree.gratuity.impossible.studentsGratuityList"));
            return mapping.getInputForward();
        }
        
        if(executionYearString == null){
            executionYearString = "";
        }
        Object[] args = { degreeCurricularPlanID, executionYearString };
        
        InfoExecutionDegree infoExecutionDegree = null;
        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                    userView, "ReadExecutionDegreeByDegreeCurricularPlanID", args);
        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        // required data
        String degree = new String(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                .getTipoCurso().toString()
                + " em " + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
        String specialization = "all";
        String situation = "all";

        Object[] yearArgs = { degreeCurricularPlanID };
        List executionYearList = null;
        try {
            executionYearList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadExecutionYearsByDegreeCurricularPlanID", yearArgs);
        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            saveErrors(request, errors);
            return mapping.getInputForward();
        }


        // getting the gratuity list
        Object[] gratuityArgs = { infoExecutionDegree.getIdInternal(),
                infoExecutionDegree.getInfoExecutionYear().getYear(), specialization, situation };
        HashMap gratuityList = null;

        try {
            gratuityList = (HashMap) ServiceManagerServiceFactory.executeService(userView,
                    "ReadGratuitySituationListByExecutionDegreeAndSpecialization", gratuityArgs);
        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            saveErrors(request, errors);
            // return mapping.getInputForward();
        }
        if (gratuityList == null) {
            errors.add("noList", new ActionError(
                    "error.masterDegree.gratuity.impossible.studentsGratuityList"));
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

    public ActionForward prepareStudentsGratuityList(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        IUserView userView = getUserView(request);
        DynaActionForm studentGratuityListForm = (DynaActionForm) actionForm;

        String degree = (String) studentGratuityListForm.get("degree");
        Integer executionDegreeID = null;

        try {
            executionDegreeID = findExecutionDegreeId(degree);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
            errors.add("noList", new ActionError(
                    "error.masterDegree.gratuity.impossible.studentsGratuityList"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        InfoExecutionDegree infoExecutionDegree = null;
        Object args[] = { executionDegreeID };

        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                    userView, "ReadExecutionDegree", args);
        } catch (FenixServiceException exception) {
            throw new FenixActionException(exception);
        }

        List infoExecutionDegrees = null;
        Integer degreeCurricularPlanID = infoExecutionDegree.getInfoDegreeCurricularPlan()
                .getIdInternal();
        args[0] = degreeCurricularPlanID;

        try {
            infoExecutionDegrees = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadExecutionDegreesByDegreeCurricularPlanID", args);
        } catch (FenixServiceException exception) {
            throw new FenixActionException(exception);
        }

        Integer firstExecutionDegreeID = ((InfoExecutionDegree) infoExecutionDegrees.get(0))
                .getIdInternal();
        Integer secondExecutionDegreeID = ((InfoExecutionDegree) infoExecutionDegrees.get(1))
                .getIdInternal();

        if (executionDegreeID.equals(firstExecutionDegreeID)) {
            degree = (degree.substring(0, degree.lastIndexOf('>') + 1)).concat(secondExecutionDegreeID
                    .toString());
        } else {
            degree = (degree.substring(0, degree.lastIndexOf('>') + 1)).concat(firstExecutionDegreeID
                    .toString());
        }

        studentGratuityListForm.set("degree", degree);

        return studentsGratuityList(mapping, actionForm, request, response);
    }

    private void orderList(List infoGratuitySituationList, String orderingType) {
        if (orderingType == null) {
            // order list by student's number, it is the ordering by default
            Collections.sort(infoGratuitySituationList, new BeanComparator(
                    "infoStudentCurricularPlan.infoStudent.number"));
        } else if (orderingType.equals(new String("studentNumber"))) {
            // order list by student's number
            Collections.sort(infoGratuitySituationList, new BeanComparator(
                    "infoStudentCurricularPlan.infoStudent.number"));
        } else if (orderingType.equals(new String("studentName"))) {
            // order list by student's name
            Collections.sort(infoGratuitySituationList, new BeanComparator(
                    "infoStudentCurricularPlan.infoStudent.infoPerson.nome"));
        } else if (orderingType.equals(new String("gratuitySituation"))) {
            // order list by gratuity's state
            Collections.sort(infoGratuitySituationList, new BeanComparator("situationType"));
        } else if (orderingType.equals(new String("payedValue"))) {
            // order list by apyed value
            Collections.sort(infoGratuitySituationList, new BeanComparator("payedValue"));
        } else if (orderingType.equals(new String("notPayedValue"))) {
            // order list by total value
            Collections.sort(infoGratuitySituationList, new BeanComparator("remainingValue"));
        } else if (orderingType.equals(new String("insurance"))) {
            Collections.sort(infoGratuitySituationList, new BeanComparator("insurancePayed"));
        } else if (orderingType.equals(new String("scplanState"))) {
            Collections.sort(infoGratuitySituationList, new BeanComparator(
                    "infoStudentCurricularPlan.currentState.state"));
        } else if (orderingType.equals(new String("dcplan"))) {
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
    private Integer findExecutionDegreeId(String degree) {
        Integer idInternal = null;
        // if degree is the string "all", then all degrees are desirable
        if (!degree.equals(new String("all"))) {
            String idInString = degree.substring(degree.indexOf(">") + 1, degree.length());
            try {
                if (idInString.length() != 0)
                    idInternal = Integer.valueOf(idInString);
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
                throw new NumberFormatException();
            }
        }

        return idInternal;
    }
}