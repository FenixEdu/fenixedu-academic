/*
 * Created on Nov 19, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.improvment;

import java.text.Collator;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoImprovmentEnrolmentContext;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author nmgo
 */
public class ImprovmentEnrolmentDispacthAction extends FenixDispatchAction{
    
    public ActionForward prepareEnrollmentChooseStudent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        return mapping.findForward("prepareEnrollmentChooseStudent");
    }
    
    public ActionForward start(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        ActionErrors errors = new ActionErrors();
        
        Integer studentNumber = (Integer) actionForm.get("studentNumber");
        
        Object[] args = {studentNumber};
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

}
