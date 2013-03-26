/*
 * Created on Dec 17, 2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.framework.SearchAction;
import net.sourceforge.fenixedu.presentationTier.mapping.framework.SearchActionMapping;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class SearchTeachersInformationAction extends SearchAction {
    /*
     * (non-Javadoc)
     * 
     * @see
     * presentationTier.Action.framework.SearchAction#materializeSearchCriteria
     * (presentationTier.mapping.framework.SearchActionMapping,
     * javax.servlet.http.HttpServletRequest,
     * org.apache.struts.action.ActionForm)
     */
    @Override
    protected void materializeSearchCriteria(SearchActionMapping mapping, HttpServletRequest request, ActionForm form)
            throws Exception {
        IUserView userView = UserView.getUser();

        if (!request.getParameter("executionDegreeId").equals("all")) {
            Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

            InfoExecutionDegree infoExecutionDegree = ReadExecutionDegreeByOID.run(executionDegreeId);
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        }
        String basic = request.getParameter("basic");
        if (basic != null && basic.length() > 0) {
            request.setAttribute("basic", basic);
        }

        request.setAttribute("executionYear", request.getParameter("executionYear"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * presentationTier.Action.framework.SearchAction#getSearchServiceArgs(javax
     * .servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
     */
    @Override
    protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form) throws Exception {
        Integer executionDegreeId = null;

        if (!request.getParameter("executionDegreeId").equals("all")) {
            executionDegreeId = new Integer(request.getParameter("executionDegreeId"));
        }

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

    @Override
    protected void prepareFormConstants(ActionMapping mapping, HttpServletRequest request, ActionForm form) throws Exception {
        final ExecutionYear executionYear =
                StringUtils.isEmpty(request.getParameter("executionYear")) ? ExecutionYear.readCurrentExecutionYear() : ExecutionYear
                        .readExecutionYearByName(request.getParameter("executionYear"));

        final List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYear(executionYear);
        Collections.sort(executionDegrees, ExecutionDegree.COMPARATOR_BY_DEGREE_NAME);

        request.setAttribute("executionYear", executionYear.getYear());
        request.setAttribute("executionDegrees", executionDegrees);
        request.setAttribute("showNextSelects", "true");
    }
}