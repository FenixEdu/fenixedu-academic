/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop.utils
 * 
 * Created on 16/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoSection;
import DataBeans.InfoSite;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import Util.TipoAula;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author jpvl
 */
public abstract class RequestUtils {

    public static final InfoExecutionCourse getExecutionCourseBySigla(HttpServletRequest request,
            String infoExecutionCourseInitials) throws Exception {

        List executionCourseList = SessionUtils.getExecutionCourses(request);

        //HttpSession session = request.getSession(false);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
        infoExecutionCourse.setSigla(infoExecutionCourseInitials);
        int indexOf = executionCourseList.indexOf(infoExecutionCourse);

        if (indexOf != -1) {
            return (InfoExecutionCourse) executionCourseList.get(indexOf);
        }

        throw new IllegalArgumentException("Not find executionCourse!");
    }

    public static final InfoExecutionCourse getExecutionCourseFromRequest(HttpServletRequest request)
            throws FenixActionException, FenixFilterException {
        InfoExecutionCourse infoExecutionCourse = null;
        try {
            InfoExecutionPeriod infoExecutionPeriod = getExecutionPeriodFromRequest(request);
            String code = request.getParameter("exeCode");
            Object[] args = { infoExecutionPeriod, code };
            infoExecutionCourse = (InfoExecutionCourse) ServiceUtils.executeService(null,
                    "ReadExecutionCourse", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (infoExecutionCourse == null) {
            System.out.println("there is a link missing the exeCode parameter");
        }
        return infoExecutionCourse;
    }

    public static final InfoExecutionYear getExecutionYearFromRequest(HttpServletRequest request)
            throws FenixActionException, FenixFilterException {
        InfoExecutionYear infoExecutionYear = null;
        try {
            String year = (String) request.getAttribute("eYName");
            if (year == null) {
                year = request.getParameter("eYName");
            }

            if (year != null) {
                Object[] args = { year };
                infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(null,
                        "ReadExecutionYear", args);
            }

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (infoExecutionYear == null) {
            System.out.println("ERROR: There is a link missing the eYName parameter: from("
                    + request.getHeader("Referer") + ") to(" + request.getRequestURI() + ")");
        }
        return infoExecutionYear;
    }

    public static final InfoExecutionPeriod getExecutionPeriodFromRequest(HttpServletRequest request)
            throws FenixActionException, FenixFilterException {
        InfoExecutionPeriod infoExecutionPeriod = null;
        try {
            InfoExecutionYear infoExecutionYear = getExecutionYearFromRequest(request);
            String name = (String) request.getAttribute("ePName");
            if (name == null) {
                name = request.getParameter("ePName");
            }

            if (name != null & infoExecutionYear != null) {
                Object[] args = { name, infoExecutionYear };
                infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(null,
                        "ReadExecutionPeriod", args);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (infoExecutionPeriod == null) {
            System.out.println("ERROR: There is a link missing the ePName parameter! from("
                    + request.getHeader("Referer") + ") to(" + request.getRequestURI() + ")");
        }
        return infoExecutionPeriod;
    }

    public static final InfoSite getSiteFromRequest(HttpServletRequest request)
            throws FenixActionException, FenixFilterException {
        InfoSite infoSite = null;

        try {
            InfoExecutionCourse infoExecutionCourse = getExecutionCourseFromRequest(request);
            Object[] args = { infoExecutionCourse };
            infoSite = (InfoSite) ServiceUtils.executeService(null, "ReadSite", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return infoSite;
    }

    public static final InfoSite getSiteFromAnyScope(HttpServletRequest request)
            throws FenixActionException, FenixFilterException {
        InfoSite infoSite = null;
        HttpSession session = request.getSession(true);

        infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
        if (infoSite == null) {

            try {
                InfoExecutionCourse infoExecutionCourse = getExecutionCourseFromRequest(request);
                Object[] args = { infoExecutionCourse };
                infoSite = (InfoSite) ServiceUtils.executeService(null, "ReadSite", args);

            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }
        return infoSite;
    }

    public static final List getSectionsFromRequest(HttpServletRequest request)
            throws FenixActionException, FenixFilterException {
        List sections = null;
        try {
            InfoSite infoSite = getSiteFromRequest(request);
            Object[] args = { infoSite };
            sections = (List) ServiceUtils.executeService(null, "ReadSections", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return sections;
    }

    public static final InfoExecutionDegree getExecutionDegreeFromRequest(HttpServletRequest request,
            InfoExecutionYear infoExecutionYear) throws FenixActionException, FenixFilterException {

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute("exeDegree");
        if (infoExecutionDegree != null) {
            return infoExecutionDegree;
        }

        String degreeInitials = (String) request.getAttribute("degreeInitials");
        String nameDegreeCurricularPlan = (String) request.getAttribute("nameDegreeCurricularPlan");

        if (degreeInitials == null) {
            degreeInitials = request.getParameter("degreeInitials");
        }
        if (nameDegreeCurricularPlan == null) {
            nameDegreeCurricularPlan = request.getParameter("nameDegreeCurricularPlan");
        }

        Object[] args1 = { infoExecutionYear, degreeInitials, nameDegreeCurricularPlan };

        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(null,
                    "ReadExecutionDegreesByExecutionYearAndDegreeInitials", args1);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return infoExecutionDegree;
    }

    public static final void setSiteFirstPageToRequest(HttpServletRequest request, InfoSite infoSite) {
        if (infoSite != null) {
            request.setAttribute("siteMail", infoSite.getMail());
            request.setAttribute("altSite", infoSite.getAlternativeSite());
            request.setAttribute("initStat", infoSite.getInitialStatement());
            request.setAttribute("intro", infoSite.getIntroduction());

        }
    }

    public static final void setExecutionCourseToRequest(HttpServletRequest request,
            InfoExecutionCourse infoExecutionCourse) {
        if (infoExecutionCourse != null) {
            request.setAttribute("exeName", infoExecutionCourse.getNome());
            request.setAttribute("exeCode", infoExecutionCourse.getSigla());
            request.setAttribute("ePName", infoExecutionCourse.getInfoExecutionPeriod().getName());
            request.setAttribute("eYName", infoExecutionCourse.getInfoExecutionPeriod()
                    .getInfoExecutionYear().getYear());

        }
    }

    public static final void setExecutionPeriodToRequest(HttpServletRequest request,
            InfoExecutionPeriod infoExecutionPeriod) {
        if (infoExecutionPeriod != null) {

            request.setAttribute("ePName", infoExecutionPeriod.getName());
            request.setAttribute("eYName", infoExecutionPeriod.getInfoExecutionYear().getYear());

        }
    }

    public static final void setSectionsToRequest(HttpServletRequest request, InfoSite infoSite)
            throws FenixActionException, FenixFilterException {
        if (infoSite != null) {
            Object argsReadSections[] = { infoSite };

            List infoSections = null;

            try {
                infoSections = (List) ServiceManagerServiceFactory.executeService(null, "ReadSections",
                        argsReadSections);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (infoSections != null) {
                Collections.sort(infoSections);
            }

            request.setAttribute("sections", infoSections);

        }
    }

    public static final void setSectionToRequest(HttpServletRequest request) {

        InfoSection infoSection = (InfoSection) request.getAttribute("infoSection");
        if (infoSection != null) {
            request.setAttribute("infoSection", infoSection);
        }

    }

    public static final void setExecutionDegreeToRequest(HttpServletRequest request,
            InfoExecutionDegree executionDegree) {
        if (executionDegree != null) {
            request.setAttribute("exeDegree", executionDegree);

            request.setAttribute("nameDegreeCurricularPlan", executionDegree
                    .getInfoDegreeCurricularPlan().getName());
			if (executionDegree.getInfoDegreeCurricularPlan()
			.getInfoDegree() != null)
	            request.setAttribute("degreeInitials", executionDegree.getInfoDegreeCurricularPlan()
	                    .getInfoDegree().getSigla());
			request.setAttribute("degreeCurricularPlanID", executionDegree.getInfoDegreeCurricularPlan()
							   .getIdInternal());

        }

    }

    public static final void keepExecutionPeriodInRequest(HttpServletRequest request) {
        request.setAttribute("ePName", request.getParameter("ePName"));
        request.setAttribute("eYName", request.getParameter("eYName"));
    }

    public static final InfoExecutionPeriod setExecutionContext(HttpServletRequest request)
            throws FenixActionException, FenixFilterException {

        //HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);

        // Read executionPeriod from request
        InfoExecutionPeriod infoExecutionPeriod = RequestUtils.getExecutionPeriodFromRequest(request);

        // If executionPeriod not in request nor in DB, read current
        if (infoExecutionPeriod == null) {
            userView = SessionUtils.getUserView(request);
            try {
                infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView,
                        "ReadCurrentExecutionPeriod", new Object[0]);
            } catch (FenixServiceException e) {
                e.printStackTrace();
            }
        }
        return infoExecutionPeriod;
    }

    public static final void setLessonTypes(HttpServletRequest request) {
        List tiposAula = new ArrayList();
        tiposAula.add(new LabelValueBean("Teorica", (new Integer(TipoAula.TEORICA)).toString()));
        tiposAula.add(new LabelValueBean("Pratica", (new Integer(TipoAula.PRATICA)).toString()));
        tiposAula.add(new LabelValueBean("Teorico-Pratica", (new Integer(TipoAula.TEORICO_PRATICA))
                .toString()));
        tiposAula
                .add(new LabelValueBean("Laboratorial", (new Integer(TipoAula.LABORATORIAL)).toString()));
        tiposAula.add(new LabelValueBean("Dúvidas", (new Integer(TipoAula.DUVIDAS)).toString()));
        tiposAula.add(new LabelValueBean("Reserva", (new Integer(TipoAula.RESERVA)).toString()));
        request.setAttribute("tiposAula", tiposAula);
    }

}