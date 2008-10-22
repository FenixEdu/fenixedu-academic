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
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.degree.execution.ReadExecutionDegreesByExecutionYearAndDegreeType;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.framework.SearchAction;
import net.sourceforge.fenixedu.presentationTier.mapping.framework.SearchActionMapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

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
	if (basic != null && basic.length() > 0)
	    request.setAttribute("basic", basic);

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
     * @see
     * presentationTier.Action.framework.SearchAction#prepareFormConstants(org
     * .apache.struts.action.ActionMapping,
     * javax.servlet.http.HttpServletRequest,
     * org.apache.struts.action.ActionForm)
     */
    @Override
    protected void prepareFormConstants(ActionMapping mapping, HttpServletRequest request, ActionForm form) throws Exception {
	IUserView userView = UserView.getUser();

	String executionYear = request.getParameter("executionYear");

	InfoExecutionYear infoExecutionYear = null;
	if (executionYear != null) {

	    infoExecutionYear = ReadExecutionYear.run(executionYear);
	} else {
	    infoExecutionYear = ReadCurrentExecutionYear.run();
	}

	request.setAttribute("executionYear", infoExecutionYear.getYear());

	List infoExecutionDegrees = ReadExecutionDegreesByExecutionYearAndDegreeType.run(executionYear, DegreeType.DEGREE);
	Collections.sort(infoExecutionDegrees, new Comparator() {
	    public int compare(Object o1, Object o2) {
		InfoExecutionDegree infoExecutionDegree1 = (InfoExecutionDegree) o1;
		InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) o2;
		return infoExecutionDegree1.getInfoDegreeCurricularPlan().getInfoDegree().getNome().compareTo(
			infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
	    }
	});

	MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
	infoExecutionDegrees = InfoExecutionDegree.buildLabelValueBeansForList(infoExecutionDegrees, messageResources);

	request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
	request.setAttribute("showNextSelects", "true");
    }
}