/*
 * Created on Mar 31, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.executionDegreesManagement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class ExecutionDegreesManagementDispatchAction extends FenixDispatchAction {
    private final static ResourceBundle enumerationResources = ResourceBundle.getBundle("resources/EnumerationResources");

    public ActionForward readDegreeCurricularPlans(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

        readAndSetDegrees(request);
        final DynaActionForm form = (DynaActionForm) actionForm;
        final String degreeType = form.getString("degreeType");
        if (degreeType != null && degreeType.length() != 0) {
            readAndSetDegreeCurricularPlans(request, degreeType);
        }
        
        return mapping.findForward("executionDegreeManagement");
    }
    
    public ActionForward readExecutionDegrees(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer degreeCurricularPlanID = (Integer) form.get("degreeCurricularPlanID");
        if (degreeCurricularPlanID != null) {
            final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
            if (degreeCurricularPlan != null) {
                request.setAttribute("executionDegrees", degreeCurricularPlan.getExecutionDegreesSet());
            }
        }
        return readDegreeCurricularPlans(mapping, actionForm, request, response);
    }
    
    public ActionForward readCoordinators(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        return readExecutionDegree(mapping, actionForm, request, "manageCoordinators");
    }

    public ActionForward prepareInsertCoordinator(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        return readExecutionDegree(mapping, actionForm, request, "insertCoordinator");
    }
    
    public ActionForward insertCoordinator(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer coordinatorNumber = (Integer) form.get("coordinatorNumber");
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");
        
        try {
            final Object[] args = {executionDegreeID, coordinatorNumber};
            ServiceManagerServiceFactory.executeService(getUserView(request), "AddCoordinatorByManager", args);
            // TODO: error messages           
        } catch (FenixFilterException e) {
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            return mapping.getInputForward();
        }
        return readCoordinators(mapping, actionForm, request, response);
    }
    
    public ActionForward saveCoordinatorsInformation(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");
        
        try {
            final Integer[] coordinatorsToBeResponsibleIDs = (Integer[]) form.get("responsibleCoordinatorsIDs");
            ServiceManagerServiceFactory.executeService(getUserView(request), "ResponsibleCoordinatorsByManager", 
                    new Object[] { executionDegreeID, Arrays.asList(coordinatorsToBeResponsibleIDs) });
            
            final Integer[] coordinatorsToRemoveIDs = (Integer[]) form.get("removeCoordinatorsIDs");
            ServiceManagerServiceFactory.executeService(getUserView(request), "RemoveCoordinatorsByManager",
                    new Object[] { executionDegreeID, Arrays.asList(coordinatorsToRemoveIDs) });
            // TODO: error messages
        } catch (FenixFilterException e) {
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            return mapping.getInputForward();
        }
        return readCoordinators(mapping, actionForm, request, response);
    }
    
    public ActionForward prepareEditExecutionDegree(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");
        if (executionDegreeID != null) {
            final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);

            request.setAttribute("executionDegree", executionDegree);
            request.setAttribute("executionYears", ExecutionYear.readNotClosedExecutionYears());
            request.setAttribute("campus", rootDomainObject.getCampussSet());
            
            form.set("executionYearID", executionDegree.getExecutionYear().getIdInternal());
            form.set("campusID", executionDegree.getCampus().getIdInternal());
            
            final String dateFormat = "dd/MM/yyyy";
            form.set("periodLessonsFirstSemesterBegin", DateFormatUtil.format(dateFormat, executionDegree.getPeriodLessonsFirstSemester().getStart()));
            form.set("periodLessonsFirstSemesterEnd", DateFormatUtil.format(dateFormat, executionDegree.getPeriodLessonsFirstSemester().getEnd()));
            form.set("periodExamsFirstSemesterBegin", DateFormatUtil.format(dateFormat, executionDegree.getPeriodExamsFirstSemester().getStart()));
            form.set("periodExamsFirstSemesterEnd", DateFormatUtil.format(dateFormat, executionDegree.getPeriodExamsFirstSemester().getEnd()));
            form.set("periodLessonsSecondSemesterBegin", DateFormatUtil.format(dateFormat, executionDegree.getPeriodLessonsSecondSemester().getStart()));
            form.set("periodLessonsSecondSemesterEnd", DateFormatUtil.format(dateFormat, executionDegree.getPeriodLessonsSecondSemester().getEnd()));
            form.set("periodExamsSecondSemesterBegin", DateFormatUtil.format(dateFormat, executionDegree.getPeriodExamsSecondSemester().getStart()));
            form.set("periodExamsSecondSemesterEnd", DateFormatUtil.format(dateFormat, executionDegree.getPeriodExamsSecondSemester().getEnd()));
                        
        }
        return mapping.findForward("editExecutionDegree");
    }
    
    public ActionForward editExecutionDegree(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");
        final Integer executionYearID = (Integer) form.get("executionYearID");
        final Integer campusID = (Integer) form.get("campusID");
        final String dateFormat = "dd/MM/yyyy";
        final Boolean temporaryExamMap = Boolean.valueOf((String) form.get("temporaryExamMap"));
        
        try {
            final Date periodLessonsFirstSemesterBegin = DateFormatUtil.parse(dateFormat, (String) form.get("periodLessonsFirstSemesterBegin"));
            final Date periodLessonsFirstSemesterEnd = DateFormatUtil.parse(dateFormat, (String) form.get("periodLessonsFirstSemesterEnd"));
            final Date periodExamsFirstSemesterBegin = DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsFirstSemesterBegin"));
            final Date periodExamsFirstSemesterEnd = DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsFirstSemesterEnd"));
            final Date periodLessonsSecondSemesterBegin = DateFormatUtil.parse(dateFormat, (String) form.get("periodLessonsSecondSemesterBegin"));
            final Date periodLessonsSecondSemesterEnd = DateFormatUtil.parse(dateFormat, (String) form.get("periodLessonsSecondSemesterEnd"));
            final Date periodExamsSecondSemesterBegin = DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsSecondSemesterBegin"));
            final Date periodExamsSecondSemesterEnd = DateFormatUtil.parse(dateFormat, (String) form.get("periodExamsSecondSemesterEnd"));
            
            ServiceUtils.executeService(getUserView(request), "EditBolonhaExecutionDegree",
                    new Object[] { executionDegreeID, executionYearID, campusID, temporaryExamMap,
                            periodLessonsFirstSemesterBegin, periodLessonsFirstSemesterEnd,
                            periodExamsFirstSemesterBegin, periodExamsFirstSemesterEnd,
                            periodLessonsSecondSemesterBegin, periodLessonsSecondSemesterEnd,
                            periodExamsSecondSemesterBegin, periodExamsSecondSemesterEnd });
            //TODO: error messages
        } catch (ParseException e) {
            return mapping.getInputForward();
        } catch (FenixFilterException e) {
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            return mapping.getInputForward();
        }
        return readDegreeCurricularPlans(mapping, actionForm, request, response);        
    }
    
    private void readAndSetDegrees(HttpServletRequest request) {
        final List<LabelValueBean> degreeTypes = new ArrayList<LabelValueBean>(BolonhaDegreeType.values().length);
        for (final BolonhaDegreeType bolonhaDegreeType : BolonhaDegreeType.values()) {
            degreeTypes.add(new LabelValueBean(enumerationResources.getString(bolonhaDegreeType.name()),
                    bolonhaDegreeType.name()));
        }
        degreeTypes.add(0, new LabelValueBean(enumerationResources.getString("dropDown.Default"), ""));
        request.setAttribute("degreeTypes", degreeTypes);
    }

    private void readAndSetDegreeCurricularPlans(HttpServletRequest request, final String degreeType) {
        final BolonhaDegreeType bolonhaDegreeType = BolonhaDegreeType.valueOf(degreeType);
        final List<LabelValueBean> degreeCurricularPlans = new ArrayList<LabelValueBean>();
        for (final DegreeCurricularPlan degreeCurricularPlan : rootDomainObject
                .getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.getDegree().isBolonhaDegree()
                    && degreeCurricularPlan.getDegree().getBolonhaDegreeType() == bolonhaDegreeType) {
                degreeCurricularPlans.add(new LabelValueBean(degreeCurricularPlan.getDegree()
                        .getName()
                        + " - " + degreeCurricularPlan.getName(), degreeCurricularPlan.getIdInternal()
                        .toString()));
            }
        }
        Collections.sort(degreeCurricularPlans, new BeanComparator("label"));
        degreeCurricularPlans.add(0, new LabelValueBean(enumerationResources.getString("dropDown.Default"), ""));
        request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
    }
    
    private ActionForward readExecutionDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, String findForward) {
        final DynaActionForm form = (DynaActionForm) actionForm;
        final Integer executionDegreeID = (Integer) form.get("executionDegreeID");
        if (executionDegreeID != null) {
            final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
            request.setAttribute("executionDegree", executionDegree);
            return mapping.findForward(findForward);
        } else {
            return mapping.getInputForward();
        }
    }

}
