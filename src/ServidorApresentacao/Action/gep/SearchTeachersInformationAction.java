/*
 * Created on Dec 17, 2003
 *  
 */
package ServidorApresentacao.Action.gep;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.framework.SearchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.mapping.framework.SearchActionMapping;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class SearchTeachersInformationAction extends SearchAction {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#materializeSearchCriteria(ServidorApresentacao.mapping.framework.SearchActionMapping,
     *      javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#getSearchServiceArgs(javax.servlet.http.HttpServletRequest,
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

        return new Object[] { executionDegreeId, basic };
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#prepareFormConstants(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
     */
    protected void prepareFormConstants(ActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(userView,
                "ReadCurrentExecutionYear", new Object[] {});

        Object[] args = { infoExecutionYear, null };
        List infoExecutionDegrees = (List) ServiceUtils.executeService(userView,
                "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
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

        infoExecutionDegrees = buildLabelValueBeans(infoExecutionDegrees);

        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
    }
}