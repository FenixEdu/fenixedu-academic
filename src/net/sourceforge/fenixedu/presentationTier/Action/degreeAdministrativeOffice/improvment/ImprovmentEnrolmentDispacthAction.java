/*
 * Created on Nov 19, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.improvment;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author nmgo
 */
public class ImprovmentEnrolmentDispacthAction extends FenixDispatchAction{
    
    public ActionForward prepareEnrollmentChooseStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException  {
    	
    	ActionMessages messages = new ActionMessages();
        //execution years
        List executionPeriods = null;
        Object[] args = {DegreeType.DEGREE};
        try {
            executionPeriods = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadExecutionPeriodsEnrollmentFenix", args);
        } catch (FenixServiceException e) {
            messages.add("noExecutionYears", new ActionMessage("error.impossible.operations"));
            saveErrors(request, messages);
            return mapping.findForward("globalEnrolment");
        }
        if (executionPeriods == null || executionPeriods.size() <= 0) {
        	messages.add("noExecutionYears", new ActionMessage("error.impossible.operations"));
            saveErrors(request, messages);
            return mapping.findForward("globalEnrolment");
        }

        sortExecutionPeriods(executionPeriods, (DynaActionForm) form);

        List executionYearLabels = buildLabelValueBeanForJsp(executionPeriods);
        request.setAttribute("executionPeriods", executionYearLabels);
        
        return mapping.findForward("prepareEnrollmentChooseStudent");
    }
    
    public ActionForward start(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException  {
        DynaActionForm actionForm = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();
        
        Integer studentNumber = (Integer) actionForm.get("studentNumber");
        Integer executionPeriodID = Integer.valueOf(actionForm.getString("executionPeriod"));
        
        Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
        if (registration == null) {
        	errors.add("noStudent", new ActionError("error.student.notExist", studentNumber.toString()));
            saveErrors(request, errors);
            return prepareEnrollmentChooseStudent(mapping, form, request, response);
        }
        
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        
        return readEnrolments(registration, executionPeriod, mapping, form, request, response);
        
    }
    
    public ActionForward readEnrolments(Registration registration, ExecutionPeriod executionPeriod, ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)  {
    	
        List<Enrolment> enrolmentsToImprov = registration.getEnrolmentsToImprov(executionPeriod);
        List<Enrolment> enroledImprovements = registration.getEnroledImprovements();
        
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("executionPeriod"));
        comparatorChain.addComparator(new BeanComparator("curricularCourse.name", Collator.getInstance()));
        Collections.sort(enrolmentsToImprov, comparatorChain);
        Collections.sort(enroledImprovements, comparatorChain);
        
        request.setAttribute("student", registration);
        request.setAttribute("executionPeriod", executionPeriod);
        request.setAttribute("enrolmentsToImprov", enrolmentsToImprov);
        request.setAttribute("enroledImprovements", enroledImprovements);
        
        return mapping.findForward("prepareEnrolmentViewEnrolments");
    }

    public ActionForward improvmentEnrollStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();
        
        Integer[] enrolments = (Integer[]) actionForm.get("enrolments");
        if(enrolments == null || enrolments.length == 0) {
            return mapping.findForward("viewEnrolments");
        }
        
        Integer studentID = (Integer) actionForm.get("studentID");
        Registration registration = rootDomainObject.readRegistrationByOID(studentID);

        Integer executionPeriodID = Integer.valueOf(actionForm.getString("executionPeriod"));
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        
        Object[] args = { registration, executionPeriod, userView.getUtilizador(), CollectionUtils.toList(enrolments)};
        
        try {
            ServiceUtils.executeService(userView, "ImprovmentEnrollService", args);
        }catch (NotAuthorizedFilterException e) {
            errors.add("notauthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }catch(FenixServiceException fse) {
            throw new FenixActionException(fse);
        }
        
        return readEnrolments(registration, executionPeriod, mapping, form, request, response);
    }
    
    public ActionForward improvmentUnenrollStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();
        
        Integer[] enrolments = (Integer[]) actionForm.get("unenrolments");
        if(enrolments == null || enrolments.length == 0) {
            return mapping.findForward("viewEnrolments");
        }
        
        Integer studentID = (Integer) actionForm.get("studentID");
        Registration registration = rootDomainObject.readRegistrationByOID(studentID);

        Integer executionPeriodID = Integer.valueOf(actionForm.getString("executionPeriod"));
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);

        Object[] args = { registration, CollectionUtils.toList(enrolments)};
        
        try {
            ServiceUtils.executeService(userView, "ImprovmentUnEnrollService", args);
        }catch (NotAuthorizedFilterException e) {
            errors.add("notauthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }catch(FenixServiceException fse) {
            throw new FenixActionException(fse);
        }
        
        return readEnrolments(registration, executionPeriod, mapping, form, request, response);
    }
    
    private void sortExecutionPeriods(List executionPeriods, DynaActionForm form) {
        ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
        comparator.addComparator(new BeanComparator("semester"), true);
        Collections.sort(executionPeriods, comparator);

        int size = executionPeriods.size();
        for (int i = (size - 1); i >= 0; i--) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
            if (infoExecutionPeriod.getState().equals(PeriodState.CURRENT)) {
                form.set("executionPeriod", infoExecutionPeriod.getIdInternal().toString());
                break;
            }
        }
    }
    
    private List buildLabelValueBeanForJsp(List infoExecutionPeriods) {
        List executionPeriodsLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionPeriods, new Transformer() {
            public Object transform(Object arg0) {
                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

                LabelValueBean executionYear = new LabelValueBean(infoExecutionPeriod.getName() + " - " + infoExecutionPeriod.getInfoExecutionYear().getYear(),
                        infoExecutionPeriod.getIdInternal().toString());
                return executionYear;
            }
        }, executionPeriodsLabels);
        return executionPeriodsLabels;
    }    

}