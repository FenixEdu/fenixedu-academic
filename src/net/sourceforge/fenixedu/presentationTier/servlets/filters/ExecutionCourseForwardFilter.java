/*
 * Created on Jun 25, 2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * @author Pedro Santos & Rita Carvalho
 * 
 */
public class ExecutionCourseForwardFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    String forwardSiteList;

    String forwardExecutionCourse;

    String notFoundURI;

    String app;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();

        try {
            forwardSiteList = filterConfig.getInitParameter("forwardSiteList");
            forwardExecutionCourse = filterConfig.getInitParameter("forwardExecutionCourse");
            notFoundURI = filterConfig.getInitParameter("notFoundURI");
            app = filterConfig.getInitParameter("app");
        } catch (Exception e) {
        }
    }

    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();

        if (!StringUtils.contains(uri, "/disciplinas")) {
            chain.doFilter(request, response);
            return;
        }

        String context = request.getContextPath();
        String newURI = uri.replaceFirst(app, "");
        String[] tokens = newURI.split("/");
        if (tokens.length > 0 && tokens[0].length() == 0) {
            String[] tokensTemp = new String[tokens.length - 1];
            for (int i = 1; i < tokens.length; i++) {
                tokensTemp[i - 1] = tokens[i];
            }
            tokens = tokensTemp;
        }

        StringBuilder forwardURI = new StringBuilder(context);

        if (tokens.length >= 2 && !(tokens[1].equalsIgnoreCase("disciplinas"))) {
            forwardURI.append(notFoundURI);
        }

        else {
            String degreeCode = tokens[0];
            Integer degreeId;
            try {
                degreeId = getDegreeId(degreeCode);
            } catch (FenixServiceException e) {
                throw new ServletException(e);
            } catch (FenixFilterException e) {
                throw new ServletException(e);
            }

            if (degreeId != null) {
                if (tokens.length == 2) {
                    /* fenix/curso/disciplinas */
                    forwardURI.append(forwardSiteList);
                    forwardURI.append(degreeId);
                }

                if (tokens.length == 3) {
                    /* fenix/curso/disciplinas/sigla */
                    String acronym = tokens[2];
                    InfoCurricularCourse infoCurricularCourseRecent;

                    try {
                        infoCurricularCourseRecent = getCurricularCourseRecent(degreeId, acronym);
                    } catch (FenixServiceException e1) {
                        throw new ServletException(e1);
                    } catch (FenixFilterException e) {
                        throw new ServletException(e);
                    }

                    if (infoCurricularCourseRecent != null) {
                        InfoExecutionCourse infoExecutionCourseRecent;
                        try {
                            infoExecutionCourseRecent = getExecutionCourseRecent(infoCurricularCourseRecent);
                        } catch (FenixServiceException e2) {
                            throw new ServletException(e2);
                        } catch (FenixFilterException e) {
                            throw new ServletException(e);
                        }
                        if (infoExecutionCourseRecent != null) {
                            constructForwardExecutionCourseURI(forwardURI, infoExecutionCourseRecent);
                        } else {
                            forwardURI.append(notFoundURI);
                        }

                    } else {
                        forwardURI.append(notFoundURI);
                    }
                }

                if (tokens.length > 3) {
                    String token_2 = tokens[2];
                    InfoCurricularCourse infoCurricularCourse = null;
                    int i;

                    try {
                        if (isDegreeCurricularPlanYear(token_2, degreeId)) {
                            String acronym = tokens[3];
                            InfoDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(
                                    token_2, degreeId);
                            if (degreeCurricularPlan != null) {
                                infoCurricularCourse = getExecutionCourseByDegreeCurricularPlan(
                                        degreeCurricularPlan.getIdInternal(), acronym);
                            }
                            i = 4;
                        } else {
                            infoCurricularCourse = getCurricularCourseRecent(degreeId, token_2);
                            i = 3;
                        }

                        if (infoCurricularCourse != null) {
                            InfoExecutionCourse infoExecutionCourse;
                            if (i >= tokens.length) {
                                infoExecutionCourse = getExecutionCourseRecent(infoCurricularCourse);
                                constructForwardExecutionCourseURI(forwardURI, infoExecutionCourse);
                            } else {
                                String nextToken = tokens[i++];
                                if (isExecutionYear(nextToken.replaceFirst("-", "/"))) {
                                    if (i < tokens.length) {
                                        String semestre = tokens[i++];
                                        if (isExecutionPeriod(semestre.replaceFirst("-", " "))) {
                                            infoExecutionCourse = getExecutionCourse(
                                                    infoCurricularCourse, nextToken.replaceFirst("-",
                                                            "/"), semestre.replaceFirst("-", " "));
                                            if (infoExecutionCourse != null) {
                                                constructForwardExecutionCourseURI(forwardURI,
                                                        infoExecutionCourse);
                                            } else {
                                                forwardURI.append(notFoundURI);
                                            }
                                        } else {
                                            infoExecutionCourse = getExecutionCourseByExecutionYear(
                                                    infoCurricularCourse, nextToken.replaceFirst("-",
                                                            "/"));
                                            if (infoExecutionCourse != null) {
                                                constructForwardExecutionCourseURI(forwardURI,
                                                        infoExecutionCourse);
                                            } else {
                                                forwardURI.append(notFoundURI);
                                            }
                                        }
                                    } else {
                                        infoExecutionCourse = getExecutionCourseByExecutionYear(
                                                infoCurricularCourse, nextToken.replaceFirst("-", "/"));
                                        if (infoExecutionCourse != null) {
                                            constructForwardExecutionCourseURI(forwardURI,
                                                    infoExecutionCourse);
                                        } else {
                                            forwardURI.append(notFoundURI);
                                        }
                                    }
                                } else if (isExecutionPeriod(nextToken.replaceFirst("-", " "))) {
                                    infoExecutionCourse = getExecutionCourseByExecutionPeriod(
                                            infoCurricularCourse, nextToken.replaceFirst("-", " "));
                                    if (infoExecutionCourse != null) {
                                        constructForwardExecutionCourseURI(forwardURI,
                                                infoExecutionCourse);
                                    } else {
                                        forwardURI.append(notFoundURI);
                                    }
                                } else {
                                    forwardURI.append(notFoundURI);
                                }
                            }
                        }

                    } catch (FenixServiceException e3) {
                        throw new ServletException(e3);
                    } catch (FenixFilterException e) {
                        throw new ServletException(e);
                    }
                }
            } else {
                forwardURI.append(notFoundURI);
            }
        }
        response.sendRedirect(forwardURI.toString());
    }

    /**
     * @param idInternal
     * @param token_3
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private InfoCurricularCourse getExecutionCourseByDegreeCurricularPlan(Integer idInternal,
            final String token_3) throws FenixServiceException, FenixFilterException {
        Object args[] = { idInternal };

        List listCurricularCourses = (List) ServiceUtils.executeService(null,
                "ReadCurricularCoursesByDegreeCurricularPlan", args);

        if (listCurricularCourses != null) {
            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) CollectionUtils.find(
                    listCurricularCourses, new Predicate() {

                        public boolean evaluate(Object arg0) {
                            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) arg0;
                            return infoCurricularCourse.getAcronym().equalsIgnoreCase(token_3);
                        }
                    });
            return infoCurricularCourse;

        }
        return null;
    }

    /**
     * @param token_2
     * @param degreeId
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private InfoDegreeCurricularPlan getDegreeCurricularPlan(String token_2, Integer degreeId)
            throws FenixServiceException, FenixFilterException {
        try {
            String[] tokens = token_2.split("-");

            final Integer year = new Integer(tokens[0]);
            Object args[] = { degreeId };
            List listDegreeCurricularPlans = (List) ServiceUtils.executeService(null,
                    "ReadDegreeCurricularPlansByDegree", args);

            if (listDegreeCurricularPlans != null) {
                CollectionUtils.filter(listDegreeCurricularPlans, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        InfoDegreeCurricularPlan infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) arg0;

                        return ((infoDegreeCurricularPlan.getInitialDate().getYear() == year.intValue() - 1900)
                                && (!infoDegreeCurricularPlan.getState().equals(
                                        DegreeCurricularPlanState.CONCLUDED)) && (!infoDegreeCurricularPlan
                                .getState().equals(DegreeCurricularPlanState.PAST)));
                    }

                });
                InfoDegreeCurricularPlan infoDegreCurricularPlan = (InfoDegreeCurricularPlan) listDegreeCurricularPlans
                        .get(0);
                return infoDegreCurricularPlan;
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * @param forwardURI
     * @param infoExecutionCourseRecent
     */
    private void constructForwardExecutionCourseURI(StringBuilder forwardURI,
            InfoExecutionCourse infoExecutionCourseRecent) {
        Integer executionCourseId = infoExecutionCourseRecent.getIdInternal();
        forwardURI.append(forwardExecutionCourse);
        forwardURI.append(executionCourseId);
    }

    /**
     * @param token_3
     * @param degreeId
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private boolean isDegreeCurricularPlanYear(String token_3, Integer degreeId)
            throws FenixServiceException, FenixFilterException {
        try {

            String[] tokens = token_3.split("-");

            final Integer year = new Integer(tokens[0]);
            Object args[] = { degreeId };
            List listDegreeCurricularPlans = (List) ServiceUtils.executeService(null,
                    "ReadDegreeCurricularPlansByDegree", args);

            if (listDegreeCurricularPlans != null) {
                CollectionUtils.filter(listDegreeCurricularPlans, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        InfoDegreeCurricularPlan infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) arg0;

                        return (infoDegreeCurricularPlan.getInitialDate().getYear() == year.intValue() - 1900);
                    }

                });
                return !listDegreeCurricularPlans.isEmpty();
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param string
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private boolean isExecutionPeriod(final String newToken) throws FenixServiceException,
            FenixFilterException {
        List listInfoExecutionPeriods = (List) ServiceUtils.executeService(null, "ReadExecutionPeriods",
                null);
        if (listInfoExecutionPeriods != null) {
            CollectionUtils.filter(listInfoExecutionPeriods, new Predicate() {

                public boolean evaluate(Object arg0) {
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

                    return infoExecutionPeriod.getName().equalsIgnoreCase(newToken);
                }

            });
            return !listInfoExecutionPeriods.isEmpty();
        }
        return false;
    }

    /**
     * @param newToken
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private boolean isExecutionYear(String newToken) throws FenixServiceException, FenixFilterException {
        Object args[] = { newToken };
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(null,
                "ReadExecutionYear", args);
        return (infoExecutionYear != null);
    }

    /**
     * @param infoCurricularCourseRecent
     * @param string
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private InfoExecutionCourse getExecutionCourseByExecutionPeriod(
            InfoCurricularCourse infoCurricularCourseRecent, final String string)
            throws FenixServiceException, FenixFilterException {
        Object args1[] = { infoCurricularCourseRecent.getIdInternal() };
        List listExecutionCourses = (List) ServiceUtils.executeService(null,
                "ReadExecutionCoursesByCurricularCourse", args1);

        if (listExecutionCourses != null) {
            CollectionUtils.filter(listExecutionCourses, new Predicate() {

                public boolean evaluate(Object arg0) {
                    InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) arg0;

                    return infoExecutionCourse.getInfoExecutionPeriod().getName().equalsIgnoreCase(
                            string);
                }

            });

            Collections.sort(listExecutionCourses, new BeanComparator("infoExecutionPeriod.beginDate"));
            if (listExecutionCourses.isEmpty()) {
                return null;
            }
            return (InfoExecutionCourse) listExecutionCourses.get(listExecutionCourses.size() - 1);
        }
        return null;
    }

    /**
     * @param infoCurricularCourseRecent
     * @param newToken
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private InfoExecutionCourse getExecutionCourseByExecutionYear(
            InfoCurricularCourse infoCurricularCourseRecent, final String newToken)
            throws FenixServiceException, FenixFilterException {
        Object args1[] = { infoCurricularCourseRecent.getIdInternal() };
        List listExecutionCourses = (List) ServiceUtils.executeService(null,
                "ReadExecutionCoursesByCurricularCourse", args1);

        if (listExecutionCourses != null) {
            CollectionUtils.filter(listExecutionCourses, new Predicate() {

                public boolean evaluate(Object arg0) {
                    InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) arg0;

                    return infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getYear()
                            .equals(newToken);
                }

            });

            Collections.sort(listExecutionCourses, new BeanComparator("infoExecutionPeriod.beginDate"));

            if (listExecutionCourses.isEmpty()) {
                return null;
            }
            return (InfoExecutionCourse) listExecutionCourses.get(listExecutionCourses.size() - 1);
        }
        return null;
    }

    /**
     * @param infoCurricularCourseRecent
     * @param executionYear
     * @param executionPeriod
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private InfoExecutionCourse getExecutionCourse(InfoCurricularCourse infoCurricularCourseRecent,
            String executionYear, String executionPeriod) throws FenixServiceException,
            FenixFilterException {
        Object args1[] = { infoCurricularCourseRecent.getIdInternal() };
        List listExecutionCourses = (List) ServiceUtils.executeService(null,
                "ReadExecutionCoursesByCurricularCourse", args1);

        Object args2[] = { executionYear };
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(null,
                "ReadExecutionYear", args2);

        Object args3[] = { executionPeriod, infoExecutionYear };
        final InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils
                .executeService(null, "ReadExecutionPeriod", args3);

        if (listExecutionCourses != null && infoExecutionYear != null && infoExecutionPeriod != null) {
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) CollectionUtils.find(
                    listExecutionCourses, new Predicate() {

                        public boolean evaluate(Object arg0) {
                            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) arg0;

                            return infoExecutionCourse.getInfoExecutionPeriod().getIdInternal().equals(
                                    infoExecutionPeriod.getIdInternal());
                        }

                    });

            return infoExecutionCourse;
        }
        return null;
    }

    /**
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private InfoExecutionCourse getExecutionCourseRecent(InfoCurricularCourse infoCurricularCourseRecent)
            throws FenixServiceException, FenixFilterException {
        Object args[] = { infoCurricularCourseRecent.getIdInternal() };
        List listExecutionCourses = (List) ServiceUtils.executeService(null,
                "ReadExecutionCoursesByCurricularCourse", args);

        if (listExecutionCourses != null) {
            Collections.sort(listExecutionCourses, new BeanComparator("infoExecutionPeriod.beginDate"));

            final ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
            for (final Iterator<InfoExecutionCourse> iterator = listExecutionCourses.iterator(); iterator.hasNext(); ) {
            	final InfoExecutionCourse infoExecutionCourse = iterator.next();
            	final ExecutionCourse executionCourse = infoExecutionCourse.getExecutionCourse();
            	final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            	if (executionPeriod.compareTo(currentExecutionPeriod) > 0) {
            		iterator.remove();
            	}
            }

            return (InfoExecutionCourse) listExecutionCourses.get(listExecutionCourses.size() - 1);
        }
        return null;
    }

    /**
     * @param degreeId
     * @param acronym
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private InfoCurricularCourse getCurricularCourseRecent(Integer degreeId, String acronym)
            throws FenixServiceException, FenixFilterException {
        Object args1[] = { degreeId };

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(
                null, "ReadExecutionDegreeRecentByDegreeId", args1);
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree
                .getInfoDegreeCurricularPlan();

        if (infoDegreeCurricularPlan != null) {
            Object args2[] = { infoDegreeCurricularPlan.getIdInternal() };

            List listCurricularCourses = (List) ServiceUtils.executeService(null,
                    "ReadCurricularCoursesByDegreeCurricularPlan", args2);

            if (listCurricularCourses != null) {
                Iterator iter = listCurricularCourses.iterator();
                while (iter.hasNext()) {
                    InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();

                    if (infoCurricularCourse.getAcronym().equalsIgnoreCase(acronym))
                        return infoCurricularCourse;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    /**
     * @param degreeCode
     * @return
     * @throws FenixServiceException,
     *             FenixFilterException
     */
    private Integer getDegreeId(String degreeCode) throws FenixServiceException, FenixFilterException {
        Object args[] = { degreeCode };
        Integer executionCourseOID = new Integer(0);

        executionCourseOID = (Integer) ServiceUtils.executeService(null,
                "ReadDegreeIdInternalByDegreeCode", args);

        return executionCourseOID;
    }

}