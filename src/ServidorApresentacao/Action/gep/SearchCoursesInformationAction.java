/*
 * Created on Dec 17, 2003
 *  
 */
package ServidorApresentacao.Action.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoSiteCourseInformation;
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
	 * @see ServidorApresentacao.Action.framework.SearchAction#doAfterSearch(ServidorApresentacao.mapping.framework.SearchActionMapping,
	 *      javax.servlet.http.HttpServletRequest, java.util.Collection)
	 */
    protected void doAfterSearch(
        SearchActionMapping mapping,
        HttpServletRequest request,
        Collection result)
        throws Exception
    {
        Collections.sort((List) result, new Comparator()
        {
            public List getInfoScopes(List infoCurricularCourses)
            {
                Iterator iter = infoCurricularCourses.iterator();
                List infoScopes = new ArrayList();
                while (iter.hasNext())
                {
                    InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
                    infoScopes.addAll(infoCurricularCourse.getInfoScopes());

                }
                return infoScopes;
            }

            public int compare(Object o1, Object o2)
            {
                InfoSiteCourseInformation infoSiteCourseInformation1 = (InfoSiteCourseInformation) o1;
                InfoSiteCourseInformation infoSiteCourseInformation2 = (InfoSiteCourseInformation) o2;

                List infoScopes1 = getInfoScopes(infoSiteCourseInformation1.getInfoCurricularCourses());
                Collections.sort(infoScopes1, new Comparator()
                {
                    public int compare(Object o1, Object o2)
                    {
                        InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                        InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                        return infoScope1
                            .getInfoCurricularSemester()
                            .getInfoCurricularYear()
                            .getYear()
                            .compareTo(
                            infoScope2.getInfoCurricularSemester().getInfoCurricularYear().getYear());
                    }

                });
                List infoScopes2 = getInfoScopes(infoSiteCourseInformation2.getInfoCurricularCourses());
                Collections.sort(infoScopes2, new Comparator()
                {
                    public int compare(Object o1, Object o2)
                    {
                        InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                        InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                        return infoScope1
                            .getInfoCurricularSemester()
                            .getInfoCurricularYear()
                            .getYear()
                            .compareTo(
                            infoScope2.getInfoCurricularSemester().getInfoCurricularYear().getYear());
                    }

                });
                InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) infoScopes1.get(0);
                InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) infoScopes2.get(0);
                return infoScope1
                    .getInfoCurricularSemester()
                    .getInfoCurricularYear()
                    .getYear()
                    .compareTo(
                    infoScope2.getInfoCurricularSemester().getInfoCurricularYear().getYear());
            }
        });
    }

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

        if (!request.getParameter("executionDegreeId").equals("all"))
        {
            Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

            Object[] args = { executionDegreeId };
            InfoExecutionDegree infoExecutionDegree =
                (InfoExecutionDegree) ServiceUtils.executeService(
                    userView,
                    "ReadExecutionDegreeByOID",
                    args);
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        }
        String basic = request.getParameter("basic");
        if (basic != null)
            request.setAttribute("basic", basic);
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

        Boolean basic = null;
        if (request.getParameter("basic") != null)
            basic = Boolean.TRUE;
        if ((request.getParameter("basic") != null) && request.getParameter("basic").equals("false"))
            basic = Boolean.FALSE;

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
        Collections.sort(infoExecutionDegrees, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                InfoExecutionDegree infoExecutionDegree1 = (InfoExecutionDegree) o1;
                InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) o2;
                return infoExecutionDegree1
                    .getInfoDegreeCurricularPlan()
                    .getInfoDegree()
                    .getNome()
                    .compareTo(
                    infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
            }
        });
        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
    }
}
