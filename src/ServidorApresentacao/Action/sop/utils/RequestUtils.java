/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop.utils
 * 
 * Created on 16/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author jpvl
 */
public abstract class RequestUtils {

	public static final InfoExecutionCourse getExecutionCourseBySigla(
		HttpServletRequest request,
		String infoExecutionCourseInitials)
		throws Exception {

		List executionCourseList = SessionUtils.getExecutionCourses(request);

		HttpSession session = request.getSession(false);

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();

		infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
		infoExecutionCourse.setSigla(infoExecutionCourseInitials);
		int indexOf = executionCourseList.indexOf(infoExecutionCourse);

		if (indexOf != -1)
			return (InfoExecutionCourse) executionCourseList.get(indexOf);
		else
			throw new IllegalArgumentException("Not find executionCourse!");
	}

	public static final InfoExecutionCourse getExecutionCourseFromRequest(HttpServletRequest request)
		throws FenixActionException {
		InfoExecutionCourse infoExecutionCourse = null;
		try {
			InfoExecutionPeriod infoExecutionPeriod =
				getExecutionPeriodFromRequest(request);
			String code = request.getParameter("exeCode");
			Object[] args = { infoExecutionPeriod, code };
			infoExecutionCourse =
				(InfoExecutionCourse) ServiceUtils.executeService(
					null,
					"ReadExecutionCourse",
					args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if (infoExecutionCourse == null) {
			System.out.println("there is a link missing the exeCode parameter");
		}
		return infoExecutionCourse;
	}

	public static final InfoExecutionYear getExecutionYearFromRequest(HttpServletRequest request)
		throws FenixActionException {
		InfoExecutionYear infoExecutionYear = null;
		try {
			String year = request.getParameter("eYName");
			Object[] args = { year };
			infoExecutionYear =
				(InfoExecutionYear) ServiceUtils.executeService(
					null,
					"ReadExecutionYear",
					args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if (infoExecutionYear == null) {
			System.out.println("there is a link missing the eYName parameter");
		}
		return infoExecutionYear;
	}

	public static final InfoExecutionPeriod getExecutionPeriodFromRequest(HttpServletRequest request)
		throws FenixActionException {
		InfoExecutionPeriod infoExecutionPeriod = null;
		try {
			InfoExecutionYear infoExecutionYear =
				getExecutionYearFromRequest(request);
			String name = request.getParameter("ePName");
			Object[] args = { name, infoExecutionYear };
			infoExecutionPeriod =
				(InfoExecutionPeriod) ServiceUtils.executeService(
					null,
					"ReadExecutionPeriod",
					args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if (infoExecutionPeriod == null) {
			System.out.println("there is a link missing the ePName parameter");
		}
		return infoExecutionPeriod;
	}

	public static final InfoSite getSiteFromRequest(HttpServletRequest request)
		throws FenixActionException {
		InfoSite infoSite = null;
		
		try {
			InfoExecutionCourse infoExecutionCourse = getExecutionCourseFromRequest(request);
			Object[] args= {infoExecutionCourse	};
			infoSite = (InfoSite) ServiceUtils.executeService(null,"ReadSite",args);
			
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return infoSite;
	}
	
	public static final InfoSite getSiteFromAnyScope(HttpServletRequest request)
			throws FenixActionException {
			InfoSite infoSite = null;
			HttpSession session = request.getSession();
			
			infoSite = (InfoSite)session.getAttribute(SessionConstants.INFO_SITE);
			if (infoSite==null) {
			
			try {
				InfoExecutionCourse infoExecutionCourse = getExecutionCourseFromRequest(request);
				Object[] args= {infoExecutionCourse	};
				infoSite = (InfoSite) ServiceUtils.executeService(null,"ReadSite",args);
			
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			}
			return infoSite;
		}
	
	public static final List getSectionsFromRequest(HttpServletRequest request) throws FenixActionException {
		List sections = null;
		try {
					InfoSite infoSite = getSiteFromRequest(request);
					Object[] args= {infoSite};
					sections = (List) ServiceUtils.executeService(null,"ReadSections",args);
			
				} catch (FenixServiceException e) {
					throw new FenixActionException(e);
				}
		
		return sections;
	}
	
	public static final void setSiteToRequest(HttpServletRequest request,InfoSite infoSite) {
		if (infoSite!=null) {
			request.setAttribute("siteMail",infoSite.getMail());
			request.setAttribute("altSite",infoSite.getAlternativeSite());
			request.setAttribute("initStat",infoSite.getInitialStatement());
			request.setAttribute("intro",infoSite.getIntroduction());
			
		}
	}
	
	public static final void setExecutionCourseToRequest(HttpServletRequest request,InfoExecutionCourse infoExecutionCourse) {
			if (infoExecutionCourse!=null) {
				request.setAttribute("exeCode",infoExecutionCourse.getSigla());
				request.setAttribute("ePName",infoExecutionCourse.getInfoExecutionPeriod().getName());
				request.setAttribute("eYName",infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getYear());
				
			
			}
		}
}
