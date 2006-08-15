package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ManageClassesDA extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward listClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request.getAttribute(SessionConstants.CURRICULAR_YEAR);

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute(SessionConstants.EXECUTION_DEGREE);
        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());

        Object argsLerTurmas[] = { infoExecutionDegree, infoExecutionPeriod, infoCurricularYear.getYear() };

        List classesList = (List) ServiceUtils.executeService(SessionUtils.getUserView(request), "LerTurmas", argsLerTurmas);

        if (classesList != null && !classesList.isEmpty()) {
            BeanComparator nameComparator = new BeanComparator("nome");
            Collections.sort(classesList, nameComparator);

            request.setAttribute(SessionConstants.CLASSES, classesList);
        }

        request.setAttribute("executionDegreeD", executionDegree);

        return mapping.findForward("ShowClassList");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaValidatorForm classForm = (DynaValidatorForm) form;

        String className = (String) classForm.get("className");

        IUserView userView = SessionUtils.getUserView(request);

        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request
                .getAttribute(SessionConstants.CURRICULAR_YEAR);
        Integer curricularYear = infoCurricularYear.getYear();
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE);
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object argsCriarTurma[] = { className, curricularYear, infoExecutionDegree, infoExecutionPeriod };

        try {
            ServiceUtils.executeService(userView, "CriarTurma", argsCriarTurma);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("A SchoolClass", e);
        }

        return listClasses(mapping, form, request, response);
    }

    /**
     * Delete class.
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextUtils.setClassContext(request);

        InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

        IUserView userView = SessionUtils.getUserView(request);

        Object argsApagarTurma[] = { infoClass };
        ServiceUtils.executeService(userView, "ApagarTurma", argsApagarTurma);

        request.removeAttribute(SessionConstants.CLASS_VIEW);

        return listClasses(mapping, form, request, response);
    }

    public ActionForward deleteClasses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm deleteClassesForm = (DynaActionForm) form;
        String[] selectedClasses = (String[]) deleteClassesForm.get("selectedItems");

        if (selectedClasses.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors
                    .add("errors.classes.notSelected", new ActionError("errors.classes.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }
        List classOIDs = new ArrayList();
        for (int i = 0; i < selectedClasses.length; i++) {
            classOIDs.add(new Integer(selectedClasses[i]));
        }

        Object args[] = { classOIDs };
        ServiceUtils.executeService(SessionUtils.getUserView(request), "DeleteClasses", args);

        return mapping.findForward("ShowShiftList");

    }

}