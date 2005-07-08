/*
 * Created on 2003/07/16
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateExecutionPeriod.ExistingExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateExecutionPeriod.InvalidExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ExecutionPeriodComparator;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Luis Crus & Sara Ribeiro
 */
public class ManageExecutionPeriodsDA extends FenixDispatchAction {

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        try {
            List infoExecutionPeriods = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionPeriods", null);

            if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {

                Collections.sort(infoExecutionPeriods, new ExecutionPeriodComparator());

                if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty()) {
                    request.setAttribute(SessionConstants.LIST_EXECUTION_PERIODS, infoExecutionPeriods);
                }

            }
        } catch (FenixServiceException ex) {
            throw new FenixActionException("Problemas de comunicação com a base de dados.", ex);
        }

        return mapping.findForward("Manage");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward createExecutionPeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaValidatorForm createExecutionPeriodForm = (DynaValidatorForm) form;

        Integer semesterToCreate = new Integer((String) createExecutionPeriodForm
                .get("semesterToCreate"));
        String yearToCreate = (String) createExecutionPeriodForm.get("yearToCreate");
        Integer semesterToExportDataFrom = new Integer((String) createExecutionPeriodForm
                .get("semesterToExportDataFrom"));
        String yearToExportDataFrom = (String) createExecutionPeriodForm.get("yearToExportDataFrom");

        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionYear infoExecutionYearToCreate = new InfoExecutionYear(yearToCreate);
        InfoExecutionPeriod infoExecutionPeriodToCreate = new InfoExecutionPeriod("" + semesterToCreate
                + " Semestre", infoExecutionYearToCreate);
        infoExecutionPeriodToCreate.setSemester(new Integer(semesterToCreate.intValue()));
        InfoExecutionYear infoExecutionYearToExportDataFrom = new InfoExecutionYear(yearToExportDataFrom);
        InfoExecutionPeriod infoExecutionPeriodToExportDataFrom = new InfoExecutionPeriod(""
                + semesterToExportDataFrom + " Semestre", infoExecutionYearToExportDataFrom);
        infoExecutionPeriodToExportDataFrom.setSemester(semesterToExportDataFrom);

        Object[] argsCreateWorkingArea = { infoExecutionPeriodToCreate,
                infoExecutionPeriodToExportDataFrom };
        try {
            ServiceUtils.executeService(userView, "CreateExecutionPeriod", argsCreateWorkingArea);
        } catch (InvalidExecutionPeriod ex) {
            throw new InvalidArgumentsActionException("O periodo indicado para importar os dados", ex);
        } catch (ExistingExecutionPeriod ex) {
            throw new ExistingActionException("O periodo indicado", ex);
        } catch (FenixServiceException ex) {
            throw new FenixActionException("Problemas a criar o periodo execução.", ex);
        }

        return mapping.findForward("Sucess");
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward deleteWorkingArea(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String year = request.getParameter("year");
        Integer semester = new Integer(request.getParameter("semester"));

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear(year);
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("AT:" + semester
                + "º Semestre", infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(semester.intValue()));

        IUserView userView = SessionUtils.getUserView(request);

        Object[] argsDeleteWorkingArea = { infoExecutionPeriod };
        try {
            ServiceUtils.executeService(userView, "DeleteWorkingArea", argsDeleteWorkingArea);
        } catch (FenixServiceException ex) {
            throw new FenixActionException("Problemas a apagar a área de trabalho.", ex);
        }

        return prepare(mapping, form, request, response);
    }

    /**
     * Prepare information to show existing execution periods and working areas.
     */
    public ActionForward alterExecutionPeriodState(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String year = request.getParameter("year");
        Integer semester = new Integer(request.getParameter("semester"));
        String periodStateToSet = request.getParameter("periodState");

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear(year);
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod(semester + " Semestre",
                infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(semester.intValue()));

        PeriodState periodState = new PeriodState(periodStateToSet);

        IUserView userView = SessionUtils.getUserView(request);

        Object[] argsAlterExecutionPeriodState = { infoExecutionPeriod, periodState };
        try {
            ServiceUtils.executeService(userView, "AlterExecutionPeriodState",
                    argsAlterExecutionPeriodState);
        } catch (InvalidExecutionPeriod ex) {
            throw new FenixActionException("O periodo execução selecionado não existe.", ex);
        } catch (FenixServiceException ex) {
            throw new FenixActionException("Problemas a apagar a área de trabalho.", ex);
        }

        return prepare(mapping, form, request, response);
    }

}