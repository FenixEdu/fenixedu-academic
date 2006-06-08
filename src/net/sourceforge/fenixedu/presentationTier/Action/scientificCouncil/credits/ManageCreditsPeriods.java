package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class ManageCreditsPeriods extends FenixDispatchAction {

    public ActionForward showPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        DynaActionForm dynaActionForm = (DynaActionForm) actionForm;
        Integer executionPeriodId = (Integer) dynaActionForm.get("executionPeriodId");
        
        ExecutionPeriod executionPeriod = null;
        if (executionPeriodId == null) {
            executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        } else {
            executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        }

        dynaActionForm.set("executionPeriodId", executionPeriod.getIdInternal());
        request.setAttribute("executionPeriod", executionPeriod);
        readAndSaveAllExecutionPeriods(request);
        return mapping.findForward("show-credits-periods");       
    }
    
    public ActionForward beforeShowPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ExecutionPeriod executionPeriod = (ExecutionPeriod) RenderUtils.getViewState().getMetaObject().getObject();
        request.setAttribute("executionPeriodId", executionPeriod.getIdInternal());
        request.setAttribute("executionPeriod", executionPeriod);
        readAndSaveAllExecutionPeriods(request);
        return mapping.findForward("show-credits-periods");
    }
    
    public ActionForward editTeacherCreditsPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        getExecutionPeriodToEditPeriod(request);
        return mapping.findForward("edit-teacher-credits-periods");        
    }
    
    public ActionForward editDepartmentAdmOfficeCreditsPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        getExecutionPeriodToEditPeriod(request);
        return mapping.findForward("edit-departmentAdmOffice-credits-periods");        
    }

    private void getExecutionPeriodToEditPeriod(HttpServletRequest request) {
        Integer executionPeriodId = Integer.valueOf((String) request.getParameter("executionPeriodId"));
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        request.setAttribute("executionPeriod", executionPeriod);
    }           

    private void readAndSaveAllExecutionPeriods(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
     
        List<InfoExecutionPeriod> notClosedExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        Object[] args = {};

        notClosedExecutionPeriods = (List<InfoExecutionPeriod>) ServiceManagerServiceFactory
                .executeService(null, "ReadNotClosedExecutionPeriods", args);

        List<LabelValueBean> executionPeriods = getNotClosedExecutionPeriods(notClosedExecutionPeriods);
        request.setAttribute("executionPeriods", executionPeriods);
    }

    private List<LabelValueBean> getNotClosedExecutionPeriods(
            List<InfoExecutionPeriod> allExecutionPeriods) {

        List<LabelValueBean> executionPeriods = new ArrayList<LabelValueBean>();
        for (InfoExecutionPeriod infoExecutionPeriod : allExecutionPeriods) {
            LabelValueBean labelValueBean = new LabelValueBean();
            labelValueBean.setLabel(infoExecutionPeriod.getInfoExecutionYear().getYear() + " - "
                    + infoExecutionPeriod.getSemester() + "º Semestre");
            labelValueBean.setValue(infoExecutionPeriod.getIdInternal().toString());
            executionPeriods.add(labelValueBean);
        }
        Collections.sort(executionPeriods, new BeanComparator("label"));
        return executionPeriods;
    }
}
