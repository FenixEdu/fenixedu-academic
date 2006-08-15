package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
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
public class ManageClassDA extends FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);
        final SchoolClass schoolClass = rootDomainObject.readSchoolClassByOID(infoClass.getIdInternal());
        request.setAttribute("schoolClass", schoolClass);

        // Fill out the form with the name of the class
        DynaActionForm classForm = (DynaActionForm) form;
        classForm.set("className", schoolClass.getEditablePartOfName());

        //Get list of shifts and place them in request
        Object args[] = { infoClass };

        List infoShifts = (List) ServiceUtils.executeService(SessionUtils.getUserView(request),
                "ReadShiftsByClass", args);

        if (infoShifts != null && !infoShifts.isEmpty()) {
            /* Sort the list of shifts */
            ComparatorChain chainComparator = new ComparatorChain();
            chainComparator.addComparator(new BeanComparator("infoDisciplinaExecucao.nome"));
            chainComparator.addComparator(new BeanComparator("tipo"));
            chainComparator.addComparator(new BeanComparator("nome"));
            Collections.sort(infoShifts, chainComparator);

            /* Place list of shifts in request */
            request.setAttribute(SessionConstants.SHIFTS, infoShifts);
        }

        return mapping.findForward("EditClass");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaValidatorForm classForm = (DynaValidatorForm) form;

        String className = (String) classForm.get("className");

        IUserView userView = SessionUtils.getUserView(request);

        InfoClass infoClassOld = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

        Object argsCriarTurma[] = { infoClassOld.getIdInternal(), className, infoClassOld.getAnoCurricular(),
        		infoClassOld.getInfoExecutionDegree(), infoClassOld.getInfoExecutionPeriod() };

        InfoClass infoClassNew = null;
        try {
            infoClassNew = (InfoClass) ServiceUtils.executeService(userView, "EditarTurma", argsCriarTurma);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("A SchoolClass", e);
        }

        request.removeAttribute(SessionConstants.CLASS_VIEW);
        request.setAttribute(SessionConstants.CLASS_VIEW, infoClassNew);

        return prepare(mapping, form, request, response);
    }

    public ActionForward removeShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

        Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));

        Object[] args = { shiftOID };
        InfoShift infoShift = null;
        try {
            infoShift = (InfoShift) ServiceUtils.executeService(userView, "ReadShiftByOID", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        Object argsRemove[] = { infoShift, infoClass };
        ServiceUtils.executeService(userView, "RemoverTurno", argsRemove);

        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareAddShifts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

        //Get list of available shifts and place them in request
        Object args[] = { infoClass };

        List infoShifts = (List) ServiceUtils.executeService(SessionUtils.getUserView(request),
                "ReadAvailableShiftsForClass", args);

        /* Sort the list of shifts */
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoDisciplinaExecucao.nome"));
        chainComparator.addComparator(new BeanComparator("tipo"));
        chainComparator.addComparator(new BeanComparator("nome"));
        Collections.sort(infoShifts, chainComparator);

        /* Place list of shifts in request */
        request.setAttribute(SessionConstants.SHIFTS, infoShifts);

        return mapping.findForward("AddShifts");
    }

    public ActionForward viewSchedule(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

        // Fill out the form with the name of the class
        DynaActionForm classForm = (DynaActionForm) form;
        classForm.set("className", infoClass.getNome());

        // Place list of lessons in request
        Object argsApagarTurma[] = { infoClass };

        /** InfoLesson List */
        List lessonList = (ArrayList) ServiceUtils.executeService(userView, "LerAulasDeTurma",
                argsApagarTurma);

        request.setAttribute(SessionConstants.LESSON_LIST_ATT, lessonList);

        return mapping.findForward("ViewSchedule");
    }

    public ActionForward removeShifts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm removeShiftsForm = (DynaActionForm) form;
        String[] selectedShifts = (String[]) removeShiftsForm.get("selectedItems");

        if (selectedShifts.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.shifts.notSelected", new ActionError("errors.shifts.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }
        List shiftOIDs = new ArrayList();
        for (int i = 0; i < selectedShifts.length; i++) {
            shiftOIDs.add(new Integer(selectedShifts[i]));
        }

        InfoClass infoClass = (InfoClass) request.getAttribute(SessionConstants.CLASS_VIEW);

        Object args[] = { infoClass, shiftOIDs };
        ServiceUtils.executeService(SessionUtils.getUserView(request), "RemoveShifts", args);

        return mapping.findForward("EditClass");

    }

}