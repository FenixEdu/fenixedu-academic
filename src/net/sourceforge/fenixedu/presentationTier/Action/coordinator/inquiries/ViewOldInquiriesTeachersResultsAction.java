/*
 * Created on Feb 2, 2005
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;



/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ViewOldInquiriesTeachersResultsAction extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);

        HttpSession session = request.getSession();
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        InfoExecutionDegree ied = (InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);
        
        Integer degreeId = ied.getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal();
        
        List executionPeriodList = (List)ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);
        
        Object[] args = { degreeId };
        List teachersRes = (List)ServiceUtils.executeService(userView, "ReadOldInquiriesTeachersResByDegreeId", args);
        
        Iterator periodIter = executionPeriodList.iterator();
        while(periodIter.hasNext()) {
            InfoExecutionPeriod iep = (InfoExecutionPeriod)periodIter.next();
            boolean found = false;
            Iterator teachersResIter = teachersRes.listIterator();

            while(teachersResIter.hasNext()) {
                InfoOldInquiriesTeachersRes ioits = (InfoOldInquiriesTeachersRes)teachersResIter.next();
                if(iep.equals(ioits.getExecutionPeriod())) {
                    found = true;
                    break;
                }
            }
            
            if(!found) {
                periodIter.remove();
            }
        }
        
        
        Collections.sort(executionPeriodList, new BeanComparator("beginDate", new ReverseComparator(new ComparableComparator())));

        request.setAttribute("executionPeriodList", executionPeriodList);

        DynaActionForm inquiriesForm = (DynaActionForm) actionForm;
        Integer executionPeriodId = (Integer) inquiriesForm.get("executionPeriodId");
        
        if(executionPeriodId != null) {
            
            if(executionPeriodId.intValue() > 0) {
                
                Iterator teachersResIter = teachersRes.listIterator();

                while(teachersResIter.hasNext()) {
                    InfoOldInquiriesTeachersRes ioits = (InfoOldInquiriesTeachersRes)teachersResIter.next();
                    if(!ioits.getKeyExecutionPeriod().equals(executionPeriodId)) {
                        teachersResIter.remove();
                    }
                }
                
//                if(teachersRes.size() == 1) {
//        	        request.setAttribute("oldInquiriesTeachersResList", teachersRes);
//        	        return actionMapping.findForward("showOldInquiriesTeacherRes");
//                }
                                                
                request.setAttribute("oldInquiriesTeachersResListOfLists", joinSimilarOldInquiriesByExecutionPeriodAndCurricularYearAndGepCourseName(teachersRes));
            
            } else if(executionPeriodId.intValue() < 0) {
                                
//                if(teachersRes.size() == 1) {
//        	        request.setAttribute("oldInquiriesTeachersResList", teachersRes);
//        	        return actionMapping.findForward("showOldInquiriesTeacherRes");
//                }
                request.setAttribute("oldInquiriesTeachersResListOfLists", joinSimilarOldInquiriesByExecutionPeriodAndCurricularYearAndGepCourseName(teachersRes));
            }
        }


        return actionMapping.findForward("chooseExecutionPeriodAndCourse");
    }
    
    public ActionForward viewResults(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);
        Integer executionPeriodId = getIntegerFromRequest("executionPeriodId", request);
        Integer degreeId = getIntegerFromRequest("degreeId", request);
        Integer curricularYear = getIntegerFromRequest("curricularYear", request);
        String courseCode = getStringFromRequest("courseCode", request);
        Integer teacherNumber  = getIntegerFromRequest("teacherNumber", request);
        
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        if((teacherNumber != null) && (executionPeriodId != null) && (degreeId != null) && (curricularYear != null) && (courseCode != null)) {
        
            Object args[] = { executionPeriodId, degreeId, curricularYear, courseCode, teacherNumber };
            
            List oldInquiriesTeachersResList = (List) ServiceUtils.executeService(userView,
                    "ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber",
                    args);
            
	        request.setAttribute("oldInquiriesTeachersResListOfLists", joinSimilarOldInquiriesByTeacher(oldInquiriesTeachersResList));

        } else if((executionPeriodId != null) && (degreeId != null) && (curricularYear != null) && (courseCode != null)) {

            Object args[] = { executionPeriodId, degreeId, curricularYear, courseCode };
            
            List oldInquiriesTeachersResList = (List) ServiceUtils.executeService(userView,
                    "ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCode",
                    args);
            
	        request.setAttribute("oldInquiriesTeachersResListOfLists", joinSimilarOldInquiriesByTeacher(oldInquiriesTeachersResList));
        }
                
        return actionMapping.findForward("showOldInquiriesTeacherRes");
    }
    
    private Integer getIntegerFromRequest(String parameter, HttpServletRequest request) {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            try {
                parameterCode = new Integer(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        }
        return parameterCode;
    }
    
    private String getStringFromRequest(String parameter, HttpServletRequest request) {
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        return parameterCodeString;
    }
    
    /**
     *
     * @param oldInquiriesTeachersRes
     * It creates a list of list. Each sublist has similar old inquiries wich have the same
     * executionPeriod, course and curricular year
     * @return
     */
    private List joinSimilarOldInquiriesByExecutionPeriodAndCurricularYearAndGepCourseName(List oldInquiriesTeachersRes) {
        List result = new ArrayList();
        if(oldInquiriesTeachersRes.size() > 0) {
	
	        final ComparatorChain comparatorChain = new ComparatorChain();
	        comparatorChain.addComparator(new BeanComparator("executionPeriod.beginDate", new ReverseComparator(new ComparableComparator())));
	        comparatorChain.addComparator(new BeanComparator("curricularYear"));
	        comparatorChain.addComparator(new BeanComparator("gepCourseName"));
	        comparatorChain.addComparator(new BeanComparator("teacherNumber"));
	        
	        Collections.sort(oldInquiriesTeachersRes, comparatorChain);
	        
	        Iterator iter = oldInquiriesTeachersRes.iterator();
	        List previousList = new ArrayList();
	        InfoOldInquiriesTeachersRes previousRes = (InfoOldInquiriesTeachersRes) iter.next();
	        previousList.add(previousRes);
	        
	        while(iter.hasNext()) {
	            InfoOldInquiriesTeachersRes current = (InfoOldInquiriesTeachersRes) iter.next();
	            if((current.getExecutionPeriod().equals(previousRes.getExecutionPeriod())) &&
	                    current.getDegree().getIdInternal().equals(previousRes.getDegree().getIdInternal()) &&
	                    current.getCurricularYear().equals(previousRes.getCurricularYear()) &&
	                    current.getCourseCode().equals(previousRes.getCourseCode())) {
	                
	                if(!previousRes.getTeacherNumber().equals(current.getTeacherNumber()))
	                    previousList.add(current);
	                previousRes = current;

	            } else {
	                result.add(previousList);
	                previousRes = current;
	                previousList = new ArrayList();
	                previousList.add(current);
	            }
	        }
	        
	        result.add(previousList);
        
        }
        
        
        return result;

    }

    /**
    *
    * @param oldInquiriesTeachersRes
    * It creates a list of list. Each sublist has similar old inquiries wich have the same
    * executionPeriod, course and curricular year
    * @return
    */
   private List joinSimilarOldInquiriesByTeacher(List oldInquiriesTeachersRes) {
       List result = new ArrayList();
       if(oldInquiriesTeachersRes.size() > 0) {
	
	        final ComparatorChain comparatorChain = new ComparatorChain();
	        comparatorChain.addComparator(new BeanComparator("teacherNumber"));
	        
	        Collections.sort(oldInquiriesTeachersRes, comparatorChain);
	        
	        Iterator iter = oldInquiriesTeachersRes.iterator();
	        List previousList = new ArrayList();
	        InfoOldInquiriesTeachersRes previousRes = (InfoOldInquiriesTeachersRes) iter.next();
	        previousList.add(previousRes);
	        
	        while(iter.hasNext()) {
	            InfoOldInquiriesTeachersRes current = (InfoOldInquiriesTeachersRes) iter.next();
	            if(current.getTeacherNumber().equals(previousRes.getTeacherNumber())) {
	                
	                previousList.add(current);
	                previousRes = current;

	            } else {
	                result.add(previousList);
	                previousRes = current;
	                previousList = new ArrayList();
	                previousList.add(current);
	            }
	        }
	        
	        result.add(previousList);
       
       }
       
       
       return result;

   }

//    private Boolean getFromRequestBoolean(String parameter,
//            HttpServletRequest request) {
//        Boolean parameterBoolean = null;
//
//        String parameterCodeString = request.getParameter(parameter);
//        if (parameterCodeString == null) {
//            parameterCodeString = (String) request.getAttribute(parameter);
//        }
//        if (parameterCodeString != null) {
//            try {
//                parameterBoolean = new Boolean(parameterCodeString);
//            } catch (Exception exception) {
//                return null;
//            }
//        }
//
//        return parameterBoolean;
//    }

}
