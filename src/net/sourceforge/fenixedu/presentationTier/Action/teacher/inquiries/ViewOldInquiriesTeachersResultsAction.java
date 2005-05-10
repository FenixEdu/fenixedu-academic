/*
 * Created on Feb 2, 2005
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesCoursesRes;
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
        InfoTeacher it = (InfoTeacher)session.getAttribute(SessionConstants.INFO_TEACHER);
        
        request.setAttribute("infoTeacher", it);

        List executionPeriodList = (List)ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);
        
        Object[] args = { it.getTeacherNumber() };
        List teachersRes = (List)ServiceUtils.executeService(userView, "ReadOldInquiriesTeachersResByTeacherNumber", args);
        
        Iterator periodIter = executionPeriodList.iterator();
        while(periodIter.hasNext()) {
            InfoExecutionPeriod iep = (InfoExecutionPeriod)periodIter.next();
            boolean found = false;
            Iterator teachersResIter = teachersRes.listIterator();

            while(teachersResIter.hasNext()) {
                InfoOldInquiriesTeachersRes ioits = (InfoOldInquiriesTeachersRes)teachersResIter.next();
                if(iep.getIdInternal() == ioits.getKeyExecutionPeriod()) {
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
//        	        request.setAttribute("oldInquiryTeachersResList", teachersRes);
//        	        return actionMapping.findForward("showOldInquiriesTeacherRes");
//                }
                                                
                request.setAttribute("oldInquiriesTeachersResListOfLists", joinSimilarOldInquiries(teachersRes));
            
            } else if(executionPeriodId.intValue() < 0) {
                                
//                if(teachersRes.size() == 1) {
//        	        request.setAttribute("oldInquiryTeachersResList", teachersRes);
//        	        return actionMapping.findForward("showOldInquiriesTeacherRes");
//                }
                request.setAttribute("oldInquiriesTeachersResListOfLists", joinSimilarOldInquiries(teachersRes));
            }
        }


        return actionMapping.findForward("chooseExecutionPeriodAndCourse");
    }
    
    public ActionForward viewResults(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);
        Integer oldInquiryTeacherId = getIntegerFromRequest("oldInquiryTeacherId", request);
        Integer executionPeriodId = getIntegerFromRequest("executionPeriodId", request);
        Integer degreeId = getIntegerFromRequest("degreeId", request);
        Integer curricularYear = getIntegerFromRequest("curricularYear", request);
        String courseCode = getStringFromRequest("courseCode", request);
        
        request.setAttribute("infoTeacher", request.getSession().getAttribute("info_teacher"));
        
        if((executionPeriodId != null) && (degreeId != null) && (courseCode != null)) {
            Object args[] = { executionPeriodId, degreeId, courseCode };
            InfoOldInquiriesCoursesRes ioicr = (InfoOldInquiriesCoursesRes) ServiceUtils.executeService(userView,
                    "ReadOldInquiryCoursesResByExecutionPeriodAndDegreeIdAndCourseCode", args);
            
            request.setAttribute("oldInquiriesCoursesRes", ioicr);
        }

        if(oldInquiryTeacherId != null) {
        
	        Object args[] = { oldInquiryTeacherId };
	        InfoOldInquiriesTeachersRes ioitr = (InfoOldInquiriesTeachersRes) ServiceUtils.executeService(userView,
	                "ReadOldInquiryTeachersResById", args);
	        
	        List ioitrList = new ArrayList();
	        ioitrList.add(ioitr);
	        
	        request.setAttribute("oldInquiryTeachersResList", ioitrList);

        } else if((executionPeriodId != null) && (degreeId != null) && (curricularYear != null) && (courseCode != null)) {

            InfoTeacher it = (InfoTeacher)request.getSession().getAttribute(SessionConstants.INFO_TEACHER);
            Object args[] = { executionPeriodId, degreeId, curricularYear, courseCode, it.getTeacherNumber() };
            
            List oldInquiryTeachersResList = (List) ServiceUtils.executeService(userView,
                    "ReadOldInquiriesTeachersResByExecutionPeriodAndDegreeIdAndCurricularYearAndCourseCodeAndTeacherNumber",
                    args);
            
	        request.setAttribute("oldInquiryTeachersResList", oldInquiryTeachersResList);
        }
        
        //Get the execution course information
//        Object argsIep[] = { executionPeriodId };
//        InfoExecutionPeriod iep = (InfoExecutionPeriod) ServiceUtils.executeService(userView, "ReadExecutionPeriodByOID", argsIep);
//
//        Object argsIec[] = { executionPeriodId, courseCode };
//        InfoExecutionCourse iec = (InfoExecutionCourse) ServiceUtils.executeService(null, "teacher.ReadExecutionCourseByCodeAndExecutionPeriodId", argsIec);
        
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
     * executionPeriod, degree and curricular year
     * @return
     */
    private List joinSimilarOldInquiries(List oldInquiriesTeachersRes) {
        List result = new ArrayList();
        if(oldInquiriesTeachersRes.size() > 0) {
	
	        final ComparatorChain comparatorChain = new ComparatorChain();
	        comparatorChain.addComparator(new BeanComparator("executionPeriod.beginDate", new ReverseComparator(new ComparableComparator())));
	        comparatorChain.addComparator(new BeanComparator("degree.nome"));
	        comparatorChain.addComparator(new BeanComparator("curricularYear"));
	        comparatorChain.addComparator(new BeanComparator("gepCourseName"));
	        comparatorChain.addComparator(new BeanComparator("classType"));
	        
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
	                
	                previousList.add(current);

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
