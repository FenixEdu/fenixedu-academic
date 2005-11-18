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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoImprovmentEnrolmentContext;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
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
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();
        
        Integer studentNumber = (Integer) actionForm.get("studentNumber");
        Integer executionPeriod = Integer.valueOf(actionForm.getString("executionPeriod"));
        
        Object[] args = {studentNumber, executionPeriod};
        InfoImprovmentEnrolmentContext improvmentEnrolmentContext = null;
        try {
            improvmentEnrolmentContext = (InfoImprovmentEnrolmentContext) ServiceUtils.executeService(userView, "ReadImprovmentsToEnroll", args);
        }catch (NotAuthorizedException e) {
            e.printStackTrace();
            errors.add("notauthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }catch(InvalidArgumentsServiceException iase) {
            errors.add("noStudent", new ActionError(iase.getMessage(), studentNumber));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }catch(FenixServiceException fse) {
            throw new FenixActionException(fse);
        }
        
        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator(
        "infoExecutionPeriod.infoExecutionYear.year"));
        comparatorChain.addComparator(new BeanComparator("infoExecutionPeriod.semester"));
        comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name", Collator.getInstance()));
        Collections.sort(improvmentEnrolmentContext.getImprovmentsToEnroll(), comparatorChain);
        Collections.sort(improvmentEnrolmentContext.getAlreadyEnrolled(), comparatorChain);
        
        request.setAttribute("improvmentEnrolmentContext", improvmentEnrolmentContext);
        
        return mapping.findForward("prepareEnrolmentViewEnrolments");
        
    }

    public ActionForward improvmentEnrollStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();
        
        Integer studentNumber = (Integer) actionForm.get("studentNumber");
        Integer[] enrolments = (Integer[]) actionForm.get("enrolments");
        if(enrolments == null || enrolments.length == 0) {
            return mapping.findForward("viewEnrolments");
        }
        Object[] args = { studentNumber, userView.getUtilizador(), CollectionUtils.toList(enrolments)};
        
        try {
            ServiceUtils.executeService(userView, "ImprovmentEnrollService", args);
        }catch (NotAuthorizedException e) {
            e.printStackTrace();
            errors.add("notauthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }catch(FenixServiceException fse) {
            throw new FenixActionException(fse);
        }
        
        return mapping.findForward("viewEnrolments");
        
    }
    
    public ActionForward improvmentUnenrollStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();
        
        Integer studentNumber = (Integer) actionForm.get("studentNumber");
        Integer[] enrolments = (Integer[]) actionForm.get("unenrolments");
        if(enrolments == null || enrolments.length == 0) {
            return mapping.findForward("viewEnrolments");
        }
        Object[] args = { studentNumber, CollectionUtils.toList(enrolments)};
        
        try {
            ServiceUtils.executeService(userView, "ImprovmentUnEnrollService", args);
        }catch (NotAuthorizedException e) {
            e.printStackTrace();
            errors.add("notauthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }catch(FenixServiceException fse) {
            throw new FenixActionException(fse);
        }
        
        return mapping.findForward("viewEnrolments");
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
