/*
 * Created on 27/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.tiles.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ControllerSupport;
import org.apache.struts.util.LabelValueBean;

/**
 * @author jpvl
 */
public class MyProfessorships extends ControllerSupport {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.tiles.Controller#perform(org.apache.struts.tiles.ComponentContext,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse,
     *      javax.servlet.ServletContext)
     */

    public InfoExecutionPeriod getExecutionPeriods(HttpServletRequest request, IUserView userView)
            throws FenixServiceException, FenixFilterException {

        InfoExecutionPeriod selectedExecutionPeriod = null;

        String selectedExecutionPeriodId = request.getParameter("selectedExecutionPeriodId");
        
        Object argsReadOpenExecutionPeriodsByTeacherExecutionCourses[] = { userView };
        List executionPeriods = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadOpenExecutionPeriodsByTeacherExecutionCourses",
                argsReadOpenExecutionPeriodsByTeacherExecutionCourses);

        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"),true);
        chainComparator.addComparator(new BeanComparator("semester"),true);
        Collections.sort(executionPeriods, chainComparator);

        List executionPeriodsLabelValueList = new ArrayList();

        Iterator iterExecutionPeriods = executionPeriods.iterator();
        String label, value;
        while (iterExecutionPeriods.hasNext()) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) iterExecutionPeriods.next();
            if (selectedExecutionPeriodId != null
                    && selectedExecutionPeriodId.equals(infoExecutionPeriod.getIdInternal().toString())) {
                selectedExecutionPeriod = infoExecutionPeriod;
            }
            label = infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear();
            value = infoExecutionPeriod.getIdInternal().toString();
            executionPeriodsLabelValueList.add(new LabelValueBean(label, value));
            
        }
        executionPeriodsLabelValueList.add(new LabelValueBean("Todos os períodos", ""));

        request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodsLabelValueList);
 
        if (selectedExecutionPeriodId == null && !executionPeriods.isEmpty()) {
            Iterator iterExecutionPeriods1 = executionPeriods.iterator();
            while (iterExecutionPeriods1.hasNext()) {
                InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) iterExecutionPeriods1.next();
                if(infoExecutionPeriod.getState().getStateCode().equals("C")) {
                    selectedExecutionPeriod = infoExecutionPeriod;
                }
                
            }
            
            if(selectedExecutionPeriod == null){
                selectedExecutionPeriod = (InfoExecutionPeriod)executionPeriods.get(0);
            }

            request.setAttribute("selectedExecutionPeriodId", selectedExecutionPeriod.getIdInternal().toString());

            DynaActionForm dynaActionForm = (DynaActionForm) request.getAttribute("executionPeriodForm");
            if (dynaActionForm != null) {
                dynaActionForm.set("selectedExecutionPeriodId", selectedExecutionPeriod.getIdInternal().toString());
                request.setAttribute("executionPeriodForm", dynaActionForm);
            }
        }
        
        
        return selectedExecutionPeriod;
    }

    public void perform(ComponentContext tileContext, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext) {

        IUserView userView = SessionUtils.getUserView(request);

        List professorShipList = new ArrayList();

        try {
            InfoExecutionPeriod infoExecutionPeriod = getExecutionPeriods(request, userView);
            Integer executionPeriodId = (infoExecutionPeriod != null) ? infoExecutionPeriod
                    .getIdInternal() : null;

            Object[] args = { userView, executionPeriodId };
            professorShipList = (List) ServiceUtils.executeService(userView, "ReadProfessorships", args);

        } catch (FenixServiceException e) {
            e.printStackTrace();
        } catch (FenixFilterException e) {
            e.printStackTrace();
        }

        tileContext.putAttribute("professorshipList", professorShipList);
    }

}