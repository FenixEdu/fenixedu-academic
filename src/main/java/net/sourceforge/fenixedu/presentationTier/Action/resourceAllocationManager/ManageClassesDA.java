package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ApagarTurma;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.CriarTurma;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.DeleteClasses;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ManageClassesDA extends FenixExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward listClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString((String) request
                        .getAttribute(PresentationConstants.ACADEMIC_INTERVAL));
        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());

        final Set<SchoolClass> classes;
        Integer curricularYear = infoCurricularYear.getYear();
        if (curricularYear != null) {
            classes = executionDegree.findSchoolClassesByAcademicIntervalAndCurricularYear(academicInterval, curricularYear);
        } else {
            classes = executionDegree.findSchoolClassesByAcademicInterval(academicInterval);
        }

        final List<InfoClass> infoClassesList = new ArrayList<InfoClass>();

        for (final SchoolClass schoolClass : classes) {
            InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
            infoClassesList.add(infoClass);
        }

        if (infoClassesList != null && !infoClassesList.isEmpty()) {
            BeanComparator nameComparator = new BeanComparator("nome");
            Collections.sort(infoClassesList, nameComparator);

            request.setAttribute(PresentationConstants.CLASSES, infoClassesList);
        }
        request.setAttribute("executionDegreeD", executionDegree);

        return mapping.findForward("ShowClassList");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaValidatorForm classForm = (DynaValidatorForm) form;
        String className = (String) classForm.get("className");
        User userView = Authenticate.getUser();

        InfoCurricularYear infoCurricularYear = (InfoCurricularYear) request.getAttribute(PresentationConstants.CURRICULAR_YEAR);
        InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) request.getAttribute(PresentationConstants.EXECUTION_DEGREE);
        AcademicInterval academicInterval =
                AcademicInterval.getAcademicIntervalFromResumedString((String) request
                        .getAttribute(PresentationConstants.ACADEMIC_INTERVAL));

        Integer curricularYear = infoCurricularYear.getYear();

        try {
            CriarTurma.run(className, curricularYear, infoExecutionDegree, academicInterval);

        } catch (DomainException e) {
            throw new ExistingActionException("A SchoolClass", e);
        }

        return listClasses(mapping, form, request, response);
    }

    /**
     * Delete class.
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ContextUtils.setClassContext(request);

        InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);

        User userView = Authenticate.getUser();

        ApagarTurma.run(infoClass);

        request.removeAttribute(PresentationConstants.CLASS_VIEW);

        return listClasses(mapping, form, request, response);
    }

    public ActionForward deleteClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm deleteClassesForm = (DynaActionForm) form;
        String[] selectedClasses = (String[]) deleteClassesForm.get("selectedItems");

        if (selectedClasses.length == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("errors.classes.notSelected", new ActionError("errors.classes.notSelected"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();

        }
        List<String> classOIDs = new ArrayList<String>();
        for (String selectedClasse : selectedClasses) {
            classOIDs.add(selectedClasse);
        }

        DeleteClasses.run(classOIDs);

        return mapping.findForward("ShowShiftList");

    }

}