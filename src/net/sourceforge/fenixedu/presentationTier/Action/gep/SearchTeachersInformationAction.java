/*
 * Created on Dec 17, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.framework.SearchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.mapping.framework.SearchActionMapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class SearchTeachersInformationAction extends SearchAction {
    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.framework.SearchAction#materializeSearchCriteria(presentationTier.mapping.framework.SearchActionMapping,
     *      javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
    protected void materializeSearchCriteria(SearchActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        if (!request.getParameter("executionDegreeId").equals("all")) {
            Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

            Object[] args = { executionDegreeId };
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(
                    userView, "ReadExecutionDegreeByOID", args);
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        }
        String basic = request.getParameter("basic");
        if (basic != null && basic.length() > 0)
            request.setAttribute("basic", basic);

        request.setAttribute("executionYear", request.getParameter("executionYear"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.framework.SearchAction#getSearchServiceArgs(javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
    protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form)
            throws Exception {
        Integer executionDegreeId = null;

        if (!request.getParameter("executionDegreeId").equals("all"))
            executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

        Boolean basic = null;
        if ((request.getParameter("basic") != null) && request.getParameter("basic").equals("true")) {
            basic = Boolean.TRUE;
        }
        if ((request.getParameter("basic") != null) && request.getParameter("basic").equals("false")) {
            basic = Boolean.FALSE;
        }

        String executionYear = request.getParameter("executionYear");

        return new Object[] { executionDegreeId, basic, executionYear };
    }

    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.framework.SearchAction#prepareFormConstants(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
    protected void prepareFormConstants(ActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        String executionYear = request.getParameter("executionYear");

        InfoExecutionYear infoExecutionYear = null;
        try {
            if (executionYear != null) {
                Object[] args = { executionYear };

                infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(
                        null, "ReadExecutionYear", args);
            } else {
                infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(userView,
                        "ReadCurrentExecutionYear", new Object[] {});
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        request.setAttribute("executionYear", infoExecutionYear.getYear());

        Object[] argsExecutionDegrees = { executionYear, DegreeType.DEGREE };
        List infoExecutionDegrees = (List) ServiceUtils.executeService(userView,
                "ReadExecutionDegreesByExecutionYearAndDegreeType", argsExecutionDegrees);
        Collections.sort(infoExecutionDegrees, new Comparator() {
            public int compare(Object o1, Object o2) {
                InfoExecutionDegree infoExecutionDegree1 = (InfoExecutionDegree) o1;
                InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) o2;
                return infoExecutionDegree1.getInfoDegreeCurricularPlan().getInfoDegree().getNome()
                        .compareTo(
                                infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree()
                                        .getNome());
            }
        });

        MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
        infoExecutionDegrees = InfoExecutionDegree.buildLabelValueBeansForList(infoExecutionDegrees, messageResources);

        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
        request.setAttribute("showNextSelects", "true");
    }
}