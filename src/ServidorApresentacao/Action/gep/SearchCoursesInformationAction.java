/*
 * Created on Dec 17, 2003
 */
package ServidorApresentacao.Action.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoSiteCourseInformation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.framework.SearchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.mapping.framework.SearchActionMapping;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class SearchCoursesInformationAction extends SearchAction {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#doAfterSearch(ServidorApresentacao.mapping.framework.SearchActionMapping,
     *      javax.servlet.http.HttpServletRequest, java.util.Collection)
     */
    protected void doAfterSearch(SearchActionMapping mapping, HttpServletRequest request,
            Collection result) throws Exception {
        final InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute("infoExecutionDegree");

        //      sort the execution course list
        ComparatorChain comparatorChain1 = new ComparatorChain();
        comparatorChain1.addComparator(new Comparator() {

            private List getInfoScopes(List infoCurricularCourses,
                    InfoExecutionDegree infoExecutionDegree) {
                Iterator iter = infoCurricularCourses.iterator();
                List infoScopes = new ArrayList();
                while (iter.hasNext()) {
                    InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
                    if (infoExecutionDegree == null
                            || infoExecutionDegree.getInfoDegreeCurricularPlan().equals(
                                    infoCurricularCourse.getInfoDegreeCurricularPlan()))
                        infoScopes.addAll(infoCurricularCourse.getInfoScopes());
                }
                return infoScopes;
            }

            public int compare(Object o1, Object o2) {
                InfoSiteCourseInformation information1 = (InfoSiteCourseInformation) o1;
                InfoSiteCourseInformation information2 = (InfoSiteCourseInformation) o2;
                List infoScopes1 = getInfoScopes(information1.getInfoCurricularCourses(),
                        infoExecutionDegree);
                List infoScopes2 = getInfoScopes(information2.getInfoCurricularCourses(),
                        infoExecutionDegree);
                ComparatorChain comparatorChain2 = new ComparatorChain();
                comparatorChain2.addComparator(new Comparator() {

                    public int compare(Object o1, Object o2) {
                        InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                        InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                        return infoScope1.getInfoCurricularSemester().getInfoCurricularYear().getYear()
                                .compareTo(
                                        infoScope2.getInfoCurricularSemester().getInfoCurricularYear()
                                                .getYear());
                    }
                });
                comparatorChain2.addComparator(new Comparator() {

                    public int compare(Object o1, Object o2) {
                        InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                        InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                        return infoScope1.getInfoCurricularSemester().getSemester().compareTo(
                                infoScope2.getInfoCurricularSemester().getSemester());
                    }
                });

                comparatorChain2.addComparator(new Comparator() {

                    public int compare(Object o1, Object o2) {
                        InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                        InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                        if (infoScope1.getInfoBranch().getAcronym() == null
                                || infoScope2.getInfoBranch().getAcronym() == null)
                            return 0;
                        return infoScope1.getInfoBranch().getAcronym().compareTo(
                                infoScope2.getInfoBranch().getAcronym());
                    }
                });
                Collections.sort(infoScopes1, comparatorChain2);
                Collections.sort(infoScopes2, comparatorChain2);
                InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) infoScopes1.get(0);
                InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) infoScopes2.get(0);
                return comparatorChain2.compare(infoScope1, infoScope2);
            }
        });

        comparatorChain1.addComparator(new Comparator() {

            public int compare(Object o1, Object o2) {
                InfoSiteCourseInformation information1 = (InfoSiteCourseInformation) o1;
                InfoSiteCourseInformation information2 = (InfoSiteCourseInformation) o2;

                List infoCurricularCourses1 = getInfoCurricularCourses(information1);
                List infoCurricularCourses2 = getInfoCurricularCourses(information2);

                ComparatorChain comparatorChain = new ComparatorChain();
                comparatorChain.addComparator(new Comparator() {

                    public int compare(Object o1, Object o2) {
                        InfoCurricularCourse infoCurricularCourse1 = (InfoCurricularCourse) o1;
                        InfoCurricularCourse infoCurricularCourse2 = (InfoCurricularCourse) o2;
                        return infoCurricularCourse1.getName()
                                .compareTo(infoCurricularCourse2.getName());
                    }

                });

                InfoCurricularCourse infoCurricularCourse1 = (InfoCurricularCourse) infoCurricularCourses1
                        .get(0);
                InfoCurricularCourse infoCurricularCourse2 = (InfoCurricularCourse) infoCurricularCourses2
                        .get(0);

                return comparatorChain.compare(infoCurricularCourse1, infoCurricularCourse2);
            }

            /**
             * @param information1
             * @return
             */
            private List getInfoCurricularCourses(InfoSiteCourseInformation information1) {
                List infoCurricularCourses = information1.getInfoCurricularCourses();
                return (List) CollectionUtils.select(infoCurricularCourses, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) arg0;
                        return infoExecutionDegree == null
                                || infoCurricularCourse.getInfoDegreeCurricularPlan().equals(
                                        infoExecutionDegree.getInfoDegreeCurricularPlan());
                    }
                });
            }
        });
        Collections.sort((List) result, comparatorChain1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#materializeSearchCriteria(ServidorApresentacao.mapping.framework.SearchActionMapping,
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

        String executionYear = request.getParameter("executionYear");

        return new Object[] { executionDegreeId, basic, executionYear };
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.framework.SearchAction#prepareFormConstants(org.apache.struts.action.ActionMapping,
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

        Object[] args = { infoExecutionYear, TipoCurso.LICENCIATURA_OBJ };
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

        //infoExecutionDegrees = buildLabelValueBeans(infoExecutionDegrees);
        infoExecutionDegrees = InfoExecutionDegree.buildLabelValueBeansForList(infoExecutionDegrees);

        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
        request.setAttribute("showNextSelects", "true");
    }

}