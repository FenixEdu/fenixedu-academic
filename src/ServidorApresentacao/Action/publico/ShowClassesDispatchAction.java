/*
 * Created 2004/11/7
 * 
 */
package ServidorApresentacao.Action.publico;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ClassView;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

import commons.collections.Table;

import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz
 */
public class ShowClassesDispatchAction extends FenixContextDispatchAction {

    public ActionForward listClasses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer degreeOID = new Integer(request.getParameter("degreeOID"));
        request.setAttribute("degreeID", degreeOID);

        //List classViewsForCurrentAndPreviousPeriods = getClassViewsForCurrentAndPreviousPeriods(request,
        //        degreeOID);

        List classViewsForCurrentAndNextPeriods = getClassViewsForCurrentAndNextPeriods(request,
                degreeOID);

        getInfoDegreeCurricularPlan(request, degreeOID);

        //InfoExecutionPeriod previouseInfoExecutionPeriod = getPreviouseExecutionPeriod(request);

        InfoExecutionPeriod nextInfoExecutionPeriod = getNextExecutionPeriod(request);

        //organizeClassViews(request, classViewsForCurrentAndPreviousPeriods, previouseInfoExecutionPeriod);

        organizeClassViewsNext(request, classViewsForCurrentAndNextPeriods, nextInfoExecutionPeriod);

        return mapping.findForward("show-classes-list");
    }

    /**
     * @param request
     * @return
     * @throws FenixServiceException
     */
    private InfoExecutionPeriod getNextExecutionPeriod(HttpServletRequest request)
            throws FenixServiceException, FenixFilterException {
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object[] args = { infoExecutionPeriod.getIdInternal() };

        InfoExecutionPeriod previousInfoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils
                .executeService(null, "ReadNextExecutionPeriod", args);

        request.setAttribute("nextInfoExecutionPeriod", previousInfoExecutionPeriod);

        return previousInfoExecutionPeriod;
    }

    /**
     * @param request
     * @param classViews
     * @param previousExecutionPeriod
     */
    private void organizeClassViews(HttpServletRequest request, List classViews,
            InfoExecutionPeriod previousExecutionPeriod) {

        Table classViewsTableCurrent = new Table(5);
        Table classViewsTablePrevious = new Table(5);

        for (Iterator iterator = classViews.iterator(); iterator.hasNext();) {
            ClassView classView = (ClassView) iterator.next();

            if (classView.getExecutionPeriodOID().equals(previousExecutionPeriod.getIdInternal())) {
                classViewsTablePrevious.appendToColumn(classView.getCurricularYear().intValue() - 1,
                        classView);
            } else {
                classViewsTableCurrent.appendToColumn(classView.getCurricularYear().intValue() - 1,
                        classView);
            }
        }

        request.setAttribute("classViewsTableCurrent", classViewsTableCurrent);
        request.setAttribute("classViewsTablePrevious", classViewsTablePrevious);

    }

    /**
     * @param request
     * @param classViews
     * @param previousExecutionPeriod
     */
    private void organizeClassViewsNext(HttpServletRequest request, List classViews,
            InfoExecutionPeriod previousExecutionPeriod) {

        Table classViewsTableCurrent = new Table(5);
        Table classViewsTablePrevious = new Table(5);

        for (Iterator iterator = classViews.iterator(); iterator.hasNext();) {
            ClassView classView = (ClassView) iterator.next();

            if (classView.getExecutionPeriodOID().equals(previousExecutionPeriod.getIdInternal())) {
                classViewsTablePrevious.appendToColumn(classView.getCurricularYear().intValue() - 1,
                        classView);
            } else {
                classViewsTableCurrent.appendToColumn(classView.getCurricularYear().intValue() - 1,
                        classView);
            }
        }

        request.setAttribute("classViewsTableCurrent", classViewsTableCurrent);
        request.setAttribute("classViewsTableNext", classViewsTablePrevious);

    }

//    private InfoExecutionPeriod getPreviouseExecutionPeriod(HttpServletRequest request)
//            throws FenixServiceException {
//        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
//                .getAttribute(SessionConstants.EXECUTION_PERIOD);
//
//        Object[] args = { infoExecutionPeriod.getIdInternal() };
//
//        InfoExecutionPeriod previousInfoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils
//                .executeService(null, "ReadPreviousExecutionPeriod", args);
//
//        request.setAttribute("previousInfoExecutionPeriod", previousInfoExecutionPeriod);
//
//        return previousInfoExecutionPeriod;
//    }
//
//    private List getClassViewsForCurrentAndPreviousPeriods(HttpServletRequest request, Integer degreeOID)
//            throws FenixServiceException {
//        Object[] args = { degreeOID };
//
//        return (List) ServiceManagerServiceFactory.executeService(null,
//                "ReadClassesForCurrentAndPreviousPeriodByDegree", args);
//    }

    private List getClassViewsForCurrentAndNextPeriods(HttpServletRequest request, Integer degreeOID)
            throws FenixServiceException, FenixFilterException {
        Object[] args = { degreeOID };

        return (List) ServiceManagerServiceFactory.executeService(null,
                "ReadClassesForCurrentAndNextPeriodByDegree", args);
    }

    private void getInfoDegreeCurricularPlan(HttpServletRequest request, Integer degreeOID)
            throws FenixServiceException, FenixFilterException {
        Object[] args = { degreeOID };

        InfoDegree infoDegree = (InfoDegree) ServiceManagerServiceFactory.executeService(null,
                "ReadDegreeByOID", args);

        request.setAttribute("infoDegree", infoDegree);
    }

}