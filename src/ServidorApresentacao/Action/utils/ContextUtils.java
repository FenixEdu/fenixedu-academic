/*
 * Created on 2003/07/28
 *
 */
package ServidorApresentacao.Action.utils;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoSala;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class ContextUtils {

	public static final void setExecutionPeriodContext(HttpServletRequest request) {
		System.out.println("### setExecutionPeriodContext - IN");
		String executionPeriodOIDString =
			(String) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD_OID);
		if (executionPeriodOIDString == null) {
			executionPeriodOIDString =
				request.getParameter(SessionConstants.EXECUTION_PERIOD_OID);
		}
		
		Integer executionPeriodOID = null;
		if (executionPeriodOIDString != null) {
			executionPeriodOID = new Integer(executionPeriodOIDString);
		}

		InfoExecutionPeriod infoExecutionPeriod = null;

		if (executionPeriodOID != null) {
			// Read from database
			try {
				Object[] args = { executionPeriodOID };
				infoExecutionPeriod =
					(InfoExecutionPeriod) ServiceUtils.executeService(
						null,
						"ReadExecutionPeriodByOID",
						args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("## executionPeriodOID nao em lado nenhum, vai ler current");				
			// Read current execution period from database
			try {
				infoExecutionPeriod =
					(InfoExecutionPeriod) ServiceUtils.executeService(
						null,
						"ReadCurrentExecutionPeriod",
						new Object[0]);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}
		}
		// Place it in request
		request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
		request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());
		System.out.println("### ExecutionPeriod in request- "+infoExecutionPeriod);
		System.out.println("### setExecutionPeriodContext - OUT");		
	}

	/**
	 * @param request
	 */
	public static void setExecutionDegreeContext(HttpServletRequest request) {
		String executionDegreeOIDString =
			(String) request.getAttribute(
				SessionConstants.EXECUTION_DEGREE_OID);
		System.out.println("ExecutionDegree from request: " + executionDegreeOIDString);
		if (executionDegreeOIDString == null) {
			executionDegreeOIDString =
				request.getParameter(SessionConstants.EXECUTION_DEGREE_OID);
			System.out.println("ExecutionDegree from parameter: " + executionDegreeOIDString);
		}

		Integer executionDegreeOID = null;
		if (executionDegreeOIDString != null) {
			executionDegreeOID = new Integer(executionDegreeOIDString);
		}

		InfoExecutionDegree infoExecutionDegree = null;

		if (executionDegreeOID != null) {
			// Read from database
			try {
				Object[] args = { executionDegreeOID };
				infoExecutionDegree =
					(InfoExecutionDegree) ServiceUtils.executeService(
						null,
						"ReadExecutionDegreeByOID",
						args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}

			// Place it in request
			request.setAttribute(
				SessionConstants.EXECUTION_DEGREE,
				infoExecutionDegree);
		}
	}

	/**
	 * @param request
	 */
	public static void setCurricularYearContext(HttpServletRequest request) {
		String curricularYearOIDString =
			(String) request.getAttribute(
				SessionConstants.CURRICULAR_YEAR_OID);
		System.out.println("Curricular Year from request: " + curricularYearOIDString);
		if (curricularYearOIDString == null) {
			curricularYearOIDString =
				request.getParameter(SessionConstants.CURRICULAR_YEAR_OID);
			System.out.println("Curricular Year from parameter: " + curricularYearOIDString);
		}

		Integer curricularYearOID = null;
		if (curricularYearOIDString != null) {
			curricularYearOID = new Integer(curricularYearOIDString);
		}

		InfoCurricularYear infoCurricularYear = null;

		if (curricularYearOID != null) {
			// Read from database
			try {
				Object[] args = { curricularYearOID };
				infoCurricularYear =
					(InfoCurricularYear) ServiceUtils.executeService(
						null,
						"ReadCurricularYearByOID",
						args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}

			// Place it in request
			request.setAttribute(
				SessionConstants.CURRICULAR_YEAR,
				infoCurricularYear);
		}
	}

	/**
	 * @param request
	 */
	public static void setExecutionCourseContext(HttpServletRequest request) {
		String executionCourseOIDString =
			(String) request.getAttribute(
				SessionConstants.EXECUTION_COURSE_OID);
		System.out.println("ExecutionCourse from request: " + executionCourseOIDString);
		if (executionCourseOIDString == null) {
			executionCourseOIDString =
				request.getParameter(SessionConstants.EXECUTION_COURSE_OID);
			System.out.println("ExecutionCourse from parameter: " + executionCourseOIDString);
		}

		Integer executionCourseOID = null;
		if (executionCourseOIDString != null) {
			executionCourseOID = new Integer(executionCourseOIDString);
		}

		InfoExecutionCourse infoExecutionCourse = null;

		if (executionCourseOID != null) {
			// Read from database
			try {
				Object[] args = { executionCourseOID };
				infoExecutionCourse =
					(InfoExecutionCourse) ServiceUtils.executeService(
						null,
						"ReadExecutionCourseByOID",
						args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}

			// Place it in request
			request.setAttribute(
				SessionConstants.EXECUTION_COURSE,
				infoExecutionCourse);
		}
	}

	/**
	 * @param request
	 */
	public static void setShiftContext(HttpServletRequest request) {
		String shiftOIDString =
			(String) request.getAttribute(
				SessionConstants.SHIFT_OID);
		System.out.println("Shift from request: " + shiftOIDString);
		if (shiftOIDString == null) {
			shiftOIDString =
				request.getParameter(SessionConstants.SHIFT_OID);
			System.out.println("Shift from parameter: " + shiftOIDString);
		}

		Integer shiftOID = null;
		if (shiftOIDString != null) {
			shiftOID = new Integer(shiftOIDString);
		}

		InfoShift infoShift = null;

		if (shiftOID != null) {
			// Read from database
			try {
				Object[] args = { shiftOID };
				infoShift =
					(InfoShift) ServiceUtils.executeService(
						null,
						"ReadShiftByOID",
						args);
			} catch (FenixServiceException e) {
				e.printStackTrace();
			}

			// Place it in request
			request.setAttribute(
				SessionConstants.SHIFT,
				infoShift);
		}
	}

	
	public static void setSelectedRoomsContext(HttpServletRequest request)
		throws FenixActionException {
//		System.out.println("### setSelectedRoomsContext - IN");
		GestorServicos gestor = GestorServicos.manager();
	
		Object argsSelectRooms[] =
			{
				 new InfoRoom(
					readRequestValue(request, "selectRoomCriteria_Name"),
					readRequestValue(request, "selectRoomCriteria_Building"),
					readIntegerRequestValue(request, "selectRoomCriteria_Floor"),
					readTypeRoomRequestValue(request, "selectRoomCriteria_Type"),
					readIntegerRequestValue(request, "selectRoomCriteria_CapacityNormal"),
					readIntegerRequestValue(request, "selectRoomCriteria_CapacityExame"))};

		List selectedRooms = null;
		try {
			selectedRooms =
				(List) gestor.executar(null, "SelectRooms", argsSelectRooms);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if (selectedRooms != null && !selectedRooms.isEmpty()) {
			Collections.sort(selectedRooms);
		}
		request.setAttribute(SessionConstants.SELECTED_ROOMS,selectedRooms);

		setRoomSearchCriteriaContext(request);
		
//		System.out.println("### setSelectedRoomsContext - OUT");
	}
		
	/**
	 * @param request
	 */
	private static void setRoomSearchCriteriaContext(HttpServletRequest request) {
//		System.out.println("### setRoomSearchCriteriaContext - IN");

		request.setAttribute("selectRoomCriteria_Name", readRequestValue(request, "selectRoomCriteria_Name"));
		request.setAttribute("selectRoomCriteria_Building", readRequestValue(request, "selectRoomCriteria_Building"));
		request.setAttribute("selectRoomCriteria_Floor", readRequestValue(request, "selectRoomCriteria_Floor"));
		request.setAttribute("selectRoomCriteria_Type", readRequestValue(request, "selectRoomCriteria_Type"));
		request.setAttribute("selectRoomCriteria_CapacityNormal",readRequestValue(request, "selectRoomCriteria_CapacityNormal"));
		request.setAttribute("selectRoomCriteria_CapacityExame",readRequestValue(request, "selectRoomCriteria_CapacityExame"));

//		System.out.println("### setRoomSearchCriteriaContext - OUT");		
	}

	/**
	 * @param request
	 */
	public static void setSelectedRoomIndexContext(HttpServletRequest request) {
		String selectedRoomIndexString =
			(String) request.getAttribute(
				SessionConstants.SELECTED_ROOM_INDEX);
		System.out.println("SelectedRoomIndex from request: " + selectedRoomIndexString);
		if (selectedRoomIndexString == null) {
			selectedRoomIndexString =
				request.getParameter(SessionConstants.SELECTED_ROOM_INDEX);
			System.out.println("SelectedRoomIndexString from parameter: " + selectedRoomIndexString);
		}

		Integer selectedRoomIndex = null;
		if (selectedRoomIndexString != null) {
			selectedRoomIndex = new Integer(selectedRoomIndexString); 
			// Place it in request
			request.setAttribute(SessionConstants.SELECTED_ROOM_INDEX, selectedRoomIndex);			
		}
		else {
			System.out.println("ERROR: Missing selectedRoomIndex in request");
		}
	}


// -------------------------------------------------------------------------------
// Read from request utils
// -------------------------------------------------------------------------------
	private static String readRequestValue(HttpServletRequest request, String name) {
		String obj = null;
		if (((String) request.getAttribute(name)) != null
			&& !((String) request.getAttribute(name)).equals(""))
			obj = (String) request.getAttribute(name);
		else if (
			request.getParameter(name) != null
				&& !request.getParameter(name).equals("")
				&& !request.getParameter(name).equals("null"))
			obj = (String) request.getParameter(name);
			
		if (obj != null) {
			System.out.println(name + " in request: " + obj);
		}
		else {
			System.out.println("ERROR: Missing (or null) " + name +" in request");
		}
		return obj;
	}
	
	private static Integer readIntegerRequestValue(
		HttpServletRequest request,
		String name) {
		String obj = readRequestValue(request, name);
		if (obj != null)
			return new Integer(obj);
		else
			return null;
	}

	private static TipoSala readTypeRoomRequestValue(
		HttpServletRequest request,
		String name) {
		Integer obj = readIntegerRequestValue(request, name);
		if (obj != null)
			return new TipoSala(obj);
		else
			return null;
	}	
}