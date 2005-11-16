/*
 * Created 2004/11/7
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.collections.Table;
import net.sourceforge.fenixedu.dataTransferObject.ClassView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz
 */
public class ShowClassesDispatchAction extends FenixContextDispatchAction {

    public ActionForward listClasses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer degreeOID = new Integer(request.getParameter("degreeOID"));
        request.setAttribute("degreeID", degreeOID);

        InfoExecutionPeriod nextInfoExecutionPeriod = getNextExecutionPeriod(request);

        List classViewsForCurrentAndPreviousPeriods = null;
        List classViewsForCurrentAndNextPeriods = null;
        if (nextInfoExecutionPeriod == null || nextInfoExecutionPeriod.getState().equals(PeriodState.NOT_OPEN)) {
            classViewsForCurrentAndPreviousPeriods = getClassViewsForCurrentAndPreviousPeriods(request,
                    degreeOID);
        } else {
            classViewsForCurrentAndNextPeriods = getClassViewsForCurrentAndNextPeriods(request,
                    degreeOID);
        }
        String language = getLocaleLanguageFromRequest(request);
        getInfoDegreeCurricularPlan(request, degreeOID,language);

        InfoExecutionPeriod previouseInfoExecutionPeriod = getPreviouseExecutionPeriod(request);

        //organizeClassViews(request, classViewsForCurrentAndPreviousPeriods, previouseInfoExecutionPeriod);

        if (nextInfoExecutionPeriod == null || nextInfoExecutionPeriod.getState().equals(PeriodState.NOT_OPEN)) {
            organizeClassViewsNext(request, classViewsForCurrentAndPreviousPeriods, previouseInfoExecutionPeriod);
        } else {
            organizeClassViewsNext(request, classViewsForCurrentAndNextPeriods, nextInfoExecutionPeriod);
        }

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
    private void organizeClassViewsNext(HttpServletRequest request, List classViews,
            InfoExecutionPeriod previousExecutionPeriod) {

        Table classViewsTableCurrent = new Table(5);
        Table classViewsTablePrevious = new Table(5);

        for (Iterator iterator = classViews.iterator(); iterator.hasNext();) {
            ClassView classView = (ClassView) iterator.next();

            if (previousExecutionPeriod != null && classView.getExecutionPeriodOID().equals(previousExecutionPeriod.getIdInternal())) {
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

    private InfoExecutionPeriod getPreviouseExecutionPeriod(HttpServletRequest request)
            throws FenixServiceException, FenixFilterException {
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Object[] args = { infoExecutionPeriod.getIdInternal() };

        InfoExecutionPeriod previousInfoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils
                .executeService(null, "ReadPreviousExecutionPeriod", args);

        request.setAttribute("previousInfoExecutionPeriod", previousInfoExecutionPeriod);

        return previousInfoExecutionPeriod;
    }

    private List getClassViewsForCurrentAndPreviousPeriods(HttpServletRequest request, Integer degreeOID)
            throws FenixServiceException, FenixFilterException {
        Object[] args = { degreeOID };

        return (List) ServiceManagerServiceFactory.executeService(null,
                "ReadClassesForCurrentAndPreviousPeriodByDegree", args);
    }

    private List getClassViewsForCurrentAndNextPeriods(HttpServletRequest request, Integer degreeOID)
            throws FenixServiceException, FenixFilterException {
        Object[] args = { degreeOID };

        return (List) ServiceManagerServiceFactory.executeService(null,
                "ReadClassesForCurrentAndNextPeriodByDegree", args);
    }

    private void getInfoDegreeCurricularPlan(HttpServletRequest request, Integer degreeOID,String language)
            throws FenixServiceException, FenixFilterException {
        Object[] args = { degreeOID };

        InfoDegree infoDegree = (InfoDegree) ServiceManagerServiceFactory.executeService(null,
                "ReadDegreeByOID", args);
        infoDegree.prepareEnglishPresentation(language);
        request.setAttribute("infoDegree", infoDegree);
    }

    private String getLocaleLanguageFromRequest(HttpServletRequest request) {
        Locale locale = (Locale) request.getSession(false).getAttribute(Globals.LOCALE_KEY);
        return  locale.getLanguage();

    }
}