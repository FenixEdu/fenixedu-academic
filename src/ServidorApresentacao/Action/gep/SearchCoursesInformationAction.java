/*
 * Created on Dec 17, 2003
 *  
 */
package ServidorApresentacao.Action.gep;

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
import Util.TipoCurso;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class SearchCoursesInformationAction extends SearchAction
{
    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#materializeSearchCriteria(ServidorApresentacao.mapping.framework.SearchActionMapping,
     *      javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
     */
    protected void materializeSearchCriteria(
        SearchActionMapping mapping,
        HttpServletRequest request,
        ActionForm form)
        throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);

        if (request.getParameter("executionDegreeId").equals("all"))
            return;

        Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

        Object[] args = { executionDegreeId };
        InfoExecutionDegree infoExecutionDegree =
            (InfoExecutionDegree) ServiceUtils.executeService(
                userView,
                "ReadExecutionDegreeByOID",
                args);
        request.setAttribute("infoExecutionDegree", infoExecutionDegree);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#getSearchServiceArgs(javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
    protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form)
        throws Exception
    {
        Integer executionDegreeId = null;

        if (!request.getParameter("executionDegreeId").equals("all"))
            executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

        Boolean basic = Boolean.FALSE;
        if (request.getParameter("basic") != null)
            basic = Boolean.TRUE;

        return new Object[] { executionDegreeId, basic };
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#prepareFormConstants(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
     */
    protected void prepareFormConstants(
        ActionMapping mapping,
        HttpServletRequest request,
        ActionForm form)
        throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionYear infoExecutionYear =
            (InfoExecutionYear) ServiceUtils.executeService(
                userView,
                "ReadCurrentExecutionYear",
                new Object[] {});

        Object[] args = { infoExecutionYear, TipoCurso.LICENCIATURA_OBJ };
        List infoExecutionDegrees =
            (List) ServiceUtils.executeService(
                userView,
                "ReadExecutionDegreesByExecutionYearAndDegreeType",
                args);
        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
    }
}
