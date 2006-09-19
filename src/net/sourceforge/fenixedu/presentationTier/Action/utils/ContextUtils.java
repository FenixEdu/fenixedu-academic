/*
 * Created on 2003/07/28
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.fenix.tools.util.StringAppender;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ContextUtils {

    public static final void setExecutionPeriodContext(HttpServletRequest request) {
        String executionPeriodOIDString = (String) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD_OID);
        if (executionPeriodOIDString == null) {
            executionPeriodOIDString = request.getParameter(SessionConstants.EXECUTION_PERIOD_OID);
        }

        Integer executionPeriodOID = null;
        if (executionPeriodOIDString != null && !executionPeriodOIDString.equals("")
                && !executionPeriodOIDString.equals("null")) {
            executionPeriodOID = new Integer(executionPeriodOIDString);
        }

        InfoExecutionPeriod infoExecutionPeriod = null;
        if (executionPeriodOID != null) {
            // Read from database
            try {
                Object[] args = { executionPeriodOID };
                infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(null,
                        "ReadExecutionPeriodByOID", args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
            } catch (FenixFilterException e) {
                e.printStackTrace();
            }
        } else {

            // Read current execution period from database
            try {
                infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(null,
                        "ReadCurrentExecutionPeriod", new Object[0]);
            } catch (FenixServiceException e) {
                e.printStackTrace();
            } catch (FenixFilterException e) {
                e.printStackTrace();
            }
        }
        if (infoExecutionPeriod != null) {
            // Place it in request
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod
                    .getIdInternal().toString());
            if (infoExecutionPeriod.getInfoExecutionYear() != null) {
                request.setAttribute("schoolYear", infoExecutionPeriod.getInfoExecutionYear().getYear());
            }
        }
    }

    /**
     * @param request
     */
    public static void setExecutionDegreeContext(HttpServletRequest request) {
        String executionDegreeOIDString = (String) request
                .getAttribute(SessionConstants.EXECUTION_DEGREE_OID);

        if ((executionDegreeOIDString == null) || (executionDegreeOIDString.length() == 0)) {
            executionDegreeOIDString = request.getParameter(SessionConstants.EXECUTION_DEGREE_OID);

            // No degree was chosen
            if ((executionDegreeOIDString == null) || (executionDegreeOIDString.length() == 0)) {
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, null);
            }
        }

        Integer executionDegreeOID = null;
        if (executionDegreeOIDString != null) {
            try {
                executionDegreeOID = new Integer(executionDegreeOIDString);
            } catch (NumberFormatException ex) {
                return;
            }
        }

        InfoExecutionDegree infoExecutionDegree = null;

        if (executionDegreeOID != null) {
            // Read from database
            try {
                Object[] args = { executionDegreeOID };
                infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(null,
                        "ReadExecutionDegreeByOID", args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
            } catch (FenixFilterException e) {
                e.printStackTrace();
            }

            if (infoExecutionDegree != null) {
                // Place it in request
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
                request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, infoExecutionDegree
                        .getIdInternal().toString());
            }
        }
    }

    /**
     * @param request
     */
    public static void setCurricularYearContext(HttpServletRequest request) {
        String curricularYearOIDString = (String) request
                .getAttribute(SessionConstants.CURRICULAR_YEAR_OID);
        if (curricularYearOIDString == null) {
            curricularYearOIDString = request.getParameter(SessionConstants.CURRICULAR_YEAR_OID);
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
                infoCurricularYear = (InfoCurricularYear) ServiceUtils.executeService(null,
                        "ReadCurricularYearByOID", args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
            } catch (FenixFilterException e) {
                e.printStackTrace();
            }

            if (infoCurricularYear != null) {
                // Place it in request
                request.setAttribute(SessionConstants.CURRICULAR_YEAR, infoCurricularYear);
                request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, infoCurricularYear
                        .getIdInternal().toString());
            }

        }
    }

    /**
     * @param request
     */
    public static void setCurricularYearsContext(HttpServletRequest request) {
        //        List curricularYearsB = (List)
        // request.getAttribute(SessionConstants.CURRICULAR_YEARS_LIST);

        String curricularYears_1 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_1);
        String curricularYears_2 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_2);
        String curricularYears_3 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_3);
        String curricularYears_4 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_4);
        String curricularYears_5 = (String) request.getAttribute(SessionConstants.CURRICULAR_YEARS_5);
        if (curricularYears_1 == null) {
            curricularYears_1 = request.getParameter(SessionConstants.CURRICULAR_YEARS_1);
        }
        if (curricularYears_2 == null) {
            curricularYears_2 = request.getParameter(SessionConstants.CURRICULAR_YEARS_2);
        }
        if (curricularYears_3 == null) {
            curricularYears_3 = request.getParameter(SessionConstants.CURRICULAR_YEARS_3);
        }
        if (curricularYears_4 == null) {
            curricularYears_4 = request.getParameter(SessionConstants.CURRICULAR_YEARS_4);
        }
        if (curricularYears_5 == null) {
            curricularYears_5 = request.getParameter(SessionConstants.CURRICULAR_YEARS_5);
        }

        List curricularYears = new ArrayList();

        if (curricularYears_1 != null) {
            try {
                curricularYears.add(new Integer(curricularYears_1));
            } catch (NumberFormatException ex) {
            }
        }
        if (curricularYears_2 != null) {
            try {
                curricularYears.add(new Integer(curricularYears_2));
            } catch (NumberFormatException ex) {
            }
        }
        if (curricularYears_3 != null) {
            try {
                curricularYears.add(new Integer(curricularYears_3));
            } catch (NumberFormatException ex) {
            }
        }
        if (curricularYears_4 != null) {
            try {
                curricularYears.add(new Integer(curricularYears_4));
            } catch (NumberFormatException ex) {
            }
        }
        if (curricularYears_5 != null) {
            try {
                curricularYears.add(new Integer(curricularYears_5));
            } catch (NumberFormatException ex) {
            }
        }

        request.setAttribute(SessionConstants.CURRICULAR_YEARS_LIST, curricularYears);
    }

    /**
     * @param request
     */
    public static void setExecutionCourseContext(HttpServletRequest request) {
        String executionCourseOIDString = (String) request
                .getAttribute(SessionConstants.EXECUTION_COURSE_OID);
        if (executionCourseOIDString == null) {
            executionCourseOIDString = request.getParameter(SessionConstants.EXECUTION_COURSE_OID);
        }

        Integer executionCourseOID = null;
        if (executionCourseOIDString != null && !executionCourseOIDString.equals("")
                && !executionCourseOIDString.equals("null")) {
            executionCourseOID = new Integer(executionCourseOIDString);
        }

        InfoExecutionCourse infoExecutionCourse = null;

        if (executionCourseOID != null) {
            // Read from database
            try {
                Object[] args = { executionCourseOID };
                infoExecutionCourse = (InfoExecutionCourse) ServiceUtils.executeService(null,
                        "ReadExecutionCourseByOID", args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
            } catch (FenixFilterException e) {
                e.printStackTrace();
            }

            if (infoExecutionCourse != null) {
                // Place it in request
                request.setAttribute(SessionConstants.EXECUTION_COURSE, infoExecutionCourse);
            }
        }
    }

    /**
     * @param request
     */
    public static void setShiftContext(HttpServletRequest request) {
        String shiftOIDString = (String) request.getAttribute(SessionConstants.SHIFT_OID);
        if (shiftOIDString == null) {
            shiftOIDString = request.getParameter(SessionConstants.SHIFT_OID);
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
                infoShift = (InfoShift) ServiceUtils.executeService(null, "ReadShiftByOID", args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
            } catch (FenixFilterException e) {
                e.printStackTrace();
            }

            if (infoShift != null) {
                // Place it in request
                request.setAttribute(SessionConstants.SHIFT, infoShift);
            }
        }
    }

    /**
     * @param request
     */
    public static void setClassContext(HttpServletRequest request) {
        String classOIDString = (String) request.getAttribute(SessionConstants.CLASS_VIEW_OID);
        if (classOIDString == null) {
            classOIDString = request.getParameter(SessionConstants.CLASS_VIEW_OID);
        }

        Integer classOID = null;
        if (classOIDString != null) {
            classOID = new Integer(classOIDString);
        }

        InfoClass infoClass = null;

        if (classOID != null) {
            // Read from database
            try {
                Object[] args = { classOID };
                infoClass = (InfoClass) ServiceUtils.executeService(null, "ReadClassByOID", args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
            } catch (FenixFilterException e) {
                e.printStackTrace();
            }

            // Place it in request
            request.setAttribute(SessionConstants.CLASS_VIEW, infoClass);
        }
    }

    /**
     * @param request
     */
    public static void setLessonContext(HttpServletRequest request) {
        String lessonOIDString = (String) request.getAttribute(SessionConstants.LESSON_OID);
        if (lessonOIDString == null) {
            lessonOIDString = request.getParameter(SessionConstants.LESSON_OID);
        }

        Integer lessonOID = null;
        if (lessonOIDString != null) {
            lessonOID = new Integer(lessonOIDString);
        }

        InfoLesson infoLesson = null;

        if (lessonOID != null) {
            // Read from database
            try {
                Object[] args = { lessonOID };
                infoLesson = (InfoLesson) ServiceUtils.executeService(null, "ReadLessonByOID", args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
            } catch (FenixFilterException e) {
                e.printStackTrace();
            }

            // Place it in request
            request.setAttribute(SessionConstants.LESSON, infoLesson);
        }
    }

    public static void setSelectedRoomsContext(HttpServletRequest request) throws FenixActionException {

        Object argsSelectRooms[] = { new InfoRoomEditor(readRequestValue(request, "selectRoomCriteria_Name"),
                readRequestValue(request, "selectRoomCriteria_Building"), readIntegerRequestValue(
                        request, "selectRoomCriteria_Floor"), readTypeRoomRequestValue(request,
                        "selectRoomCriteria_Type"), readIntegerRequestValue(request,
                        "selectRoomCriteria_CapacityNormal"), readIntegerRequestValue(request,
                        "selectRoomCriteria_CapacityExame")) };

        List selectedRooms = null;
        try {
            selectedRooms = (List) ServiceManagerServiceFactory.executeService(null, "SelectRooms",
                    argsSelectRooms);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        } catch (FenixFilterException e) {
            throw new FenixActionException(e);
        }
        if (selectedRooms != null && !selectedRooms.isEmpty()) {
            Collections.sort(selectedRooms);
        }
        request.setAttribute(SessionConstants.SELECTED_ROOMS, selectedRooms);

        setRoomSearchCriteriaContext(request);

    }

    /**
     * @param request
     */
    private static void setRoomSearchCriteriaContext(HttpServletRequest request) {

        request.setAttribute("selectRoomCriteria_Name", readRequestValue(request,
                "selectRoomCriteria_Name"));
        request.setAttribute("selectRoomCriteria_Building", readRequestValue(request,
                "selectRoomCriteria_Building"));
        request.setAttribute("selectRoomCriteria_Floor", readRequestValue(request,
                "selectRoomCriteria_Floor"));
        request.setAttribute("selectRoomCriteria_Type", readRequestValue(request,
                "selectRoomCriteria_Type"));
        request.setAttribute("selectRoomCriteria_CapacityNormal", readRequestValue(request,
                "selectRoomCriteria_CapacityNormal"));
        request.setAttribute("selectRoomCriteria_CapacityExame", readRequestValue(request,
                "selectRoomCriteria_CapacityExame"));

    }

    /**
     * @param request
     */
    public static void setSelectedRoomIndexContext(HttpServletRequest request) {
        //        String selectedRoomIndexString =
        //            (String) request.getAttribute(SessionConstants.SELECTED_ROOM_INDEX);
        Integer selectedRoomIndex = (Integer) request.getAttribute(SessionConstants.SELECTED_ROOM_INDEX);

        //        if (selectedRoomIndexString == null)
        //        {
        String selectedRoomIndexString = request.getParameter(SessionConstants.SELECTED_ROOM_INDEX);
        //        }

        //        Integer selectedRoomIndex = null;
        if (selectedRoomIndex != null) {
            // Place it in request
            request.setAttribute(SessionConstants.SELECTED_ROOM_INDEX, selectedRoomIndex);
        } else if (selectedRoomIndexString != null) {
            selectedRoomIndex = new Integer(selectedRoomIndexString);
            request.setAttribute(SessionConstants.SELECTED_ROOM_INDEX, selectedRoomIndex);
        }
    }

    public static void prepareChangeExecutionDegreeAndCurricularYear(HttpServletRequest request) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale());
        ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

        /* Obtain a list of curricular years */
        List labelListOfCurricularYears = getLabelListOfCurricularYears();
        request.setAttribute(SessionConstants.LABELLIST_CURRICULAR_YEARS, labelListOfCurricularYears);

        /* Obtain a list of degrees for the specified execution year */
        final ExecutionYear executionYear = infoExecutionPeriod.getExecutionPeriod().getExecutionYear();
        final Set<ExecutionDegree> executionDegrees = executionYear.getExecutionDegreesSet();

        final List<LabelValueBean> labelListOfExecutionDegrees = new ArrayList<LabelValueBean>();
        final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
        	infoExecutionDegrees.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));

        	final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        	final Degree degree = degreeCurricularPlan.getDegree();
        	final String degreeTypeString = bundle.getString(degree.getTipoCurso().toString());
        	final StringBuilder name = new StringBuilder();
        	name.append(degreeTypeString);
        	name.append(" ").append(applicationResources.getString("label.in")).append(" ");
        	name.append(degree.getName());
        	if (duplicateDegreeInList(degree, executionYear)) {
        		name.append(" - ");
        		name.append(degreeCurricularPlan.getName());
        	}
        	final LabelValueBean labelValueBean = new LabelValueBean(name.toString(), executionDegree.getIdInternal().toString());
        	labelListOfExecutionDegrees.add(labelValueBean);
        }
        Collections.sort(labelListOfExecutionDegrees);
        request.setAttribute("licenciaturas", labelListOfExecutionDegrees);
        Collections.sort(infoExecutionDegrees, InfoExecutionDegree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME);
        request.setAttribute("executionDegrees", infoExecutionDegrees);

        final List<ExecutionPeriod> executionPeriods = ExecutionPeriod.readNotClosedExecutionPeriods();
        Collections.sort(executionPeriods);
        final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        final List<LabelValueBean> executionPeriodLabelValueBeans = new ArrayList<LabelValueBean>();
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
        	infoExecutionPeriods.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        	final String name = StringAppender.append(executionPeriod.getName(), " - ", executionPeriod.getExecutionYear().getYear());
        	final LabelValueBean labelValueBean = new LabelValueBean(name, executionPeriod.getIdInternal().toString());
        	executionPeriodLabelValueBeans.add(labelValueBean);
        }
        request.setAttribute(SessionConstants.LIST_INFOEXECUTIONPERIOD, infoExecutionPeriods);
        request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD, executionPeriodLabelValueBeans);
    }

    private static boolean duplicateDegreeInList(final Degree degree, final ExecutionYear executionYear) {
    	boolean foundOne = false;
    	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
    		for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
    			if (executionYear == executionDegree.getExecutionYear()) {
    				if (foundOne) {
    					return true;
    				} else {
    					foundOne = true;
    				}
    			}
    		}
    	}
    	return false;
	}

	// -------------------------------------------------------------------------------
    // Read from request utils
    // -------------------------------------------------------------------------------
    private static String readRequestValue(HttpServletRequest request, String name) {
        String obj = null;
        if (((String) request.getAttribute(name)) != null
                && !((String) request.getAttribute(name)).equals(""))
            obj = (String) request.getAttribute(name);
        else if (request.getParameter(name) != null && !request.getParameter(name).equals("")
                && !request.getParameter(name).equals("null"))
            obj = request.getParameter(name);

        return obj;
    }

    private static Integer readIntegerRequestValue(HttpServletRequest request, String name) {
        String obj = readRequestValue(request, name);
        if (obj != null) {
            return new Integer(obj);
        }

        return null;
    }

    private static TipoSala readTypeRoomRequestValue(HttpServletRequest request, String name) {
        Integer obj = readIntegerRequestValue(request, name);
        if (obj != null) {
            return new TipoSala(obj);
        }

        return null;
    }

    public static List getLabelListOfCurricularYears() {
        List labelListOfCurricularYears = new ArrayList();
        labelListOfCurricularYears.add(new LabelValueBean("escolher", ""));
        labelListOfCurricularYears.add(new LabelValueBean("1 º", "1"));
        labelListOfCurricularYears.add(new LabelValueBean("2 º", "2"));
        labelListOfCurricularYears.add(new LabelValueBean("3 º", "3"));
        labelListOfCurricularYears.add(new LabelValueBean("4 º", "4"));
        labelListOfCurricularYears.add(new LabelValueBean("5 º", "5"));
        return labelListOfCurricularYears;
    }

    public static List getLabelListOfOptionalCurricularYears() {
        List labelListOfCurricularYears = new ArrayList();
        labelListOfCurricularYears.add(new LabelValueBean("todos", ""));
        labelListOfCurricularYears.add(new LabelValueBean("1 º", "1"));
        labelListOfCurricularYears.add(new LabelValueBean("2 º", "2"));
        labelListOfCurricularYears.add(new LabelValueBean("3 º", "3"));
        labelListOfCurricularYears.add(new LabelValueBean("4 º", "4"));
        labelListOfCurricularYears.add(new LabelValueBean("5 º", "5"));
        return labelListOfCurricularYears;
    }

    public static List getLabelListOfExecutionDegrees(List executionDegreeList) {
        List labelListOfExecutionDegrees = (List) CollectionUtils.collect(executionDegreeList,
                new EXECUTION_DEGREE_2_EXECUTION_DEGREE_LABEL());
        labelListOfExecutionDegrees.add(0, new LabelValueBean("escolher", ""));
        return labelListOfExecutionDegrees;
    }

    private static class EXECUTION_DEGREE_2_EXECUTION_DEGREE_LABEL implements Transformer {

        /*
         * (non-Javadoc)
         * 
         * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
         */
        public Object transform(Object arg0) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) arg0;

            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " de " + name;

            return new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString());
        }
    }

    /***************************************************************************
     * Novos metodos para os exames
     */

    public static List createCurricularYearList() {
        List anosCurriculares = new ArrayList();

        anosCurriculares.add(new LabelValueBean("1 º", "1"));
        anosCurriculares.add(new LabelValueBean("2 º", "2"));
        anosCurriculares.add(new LabelValueBean("3 º", "3"));
        anosCurriculares.add(new LabelValueBean("4 º", "4"));
        anosCurriculares.add(new LabelValueBean("5 º", "5"));

        return anosCurriculares;
    }

    public static List createExecutionDegreeList(HttpServletRequest request)
            throws FenixServiceException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        /* Cria o form bean com as licenciaturas em execucao. */
        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

        List executionDegreeList = (List) ServiceUtils.executeService(userView,
                "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

        List licenciaturas = new ArrayList();

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        Iterator iterator = executionDegreeList.iterator();

        //sint index = 0;
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " em " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            licenciaturas.add(new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString()));
        }

        return licenciaturas;
    }

    /**
     * Method existencesOfInfoDegree.
     * 
     * @param executionDegreeList
     * @param infoExecutionDegree
     * @return int
     */
    private static boolean duplicateInfoDegree(List<InfoExecutionDegree> executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
        final InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        for (final InfoExecutionDegree otherInfoExecutionDegree : executionDegreeList) {
        	final InfoDegreeCurricularPlan infoDegreeCurricularPlan = otherInfoExecutionDegree.getInfoDegreeCurricularPlan();
        	final InfoDegree otherInfoDegree = infoDegreeCurricularPlan.getInfoDegree();
        	if (infoDegree.equals(otherInfoDegree) && !infoExecutionDegree.equals(otherInfoExecutionDegree)) {
        		return true;
        	}
        }
        return false;
    }

}