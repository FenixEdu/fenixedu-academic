/*
 * Created on Oct 20, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package DataBeans.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import DataBeans.DataTranferObject;
import DataBeans.InfoBranch;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoEnrolment;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudentCurricularPlan;

/**
 * @author Andre Fernandes / Joao Brito
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class InfoStudentCurricularPlansWithSelectedEnrollments extends
        DataTranferObject
{
    /*
     * Map em que as chaves sao os InfoStudentCurricularPlan's (+WithInfoStudentWithPersonAndDegree) e 
     * os valores sao List de InfoEnrollment's
     */
    private Map infoStudentCurricularPlans;

    /**
     *  
     */
    public InfoStudentCurricularPlansWithSelectedEnrollments()
    {
        super();
        infoStudentCurricularPlans = new HashMap();
    }
    
    public List getInfoStudentCurricularPlans()
    {
        // cuidado com as referencias... read-only!!!1111
        return new ArrayList(infoStudentCurricularPlans.keySet());
    }
        
    public List getInfoEnrollmentsForStudentCP(InfoStudentCurricularPlan infoSCP)
    {
        return (List)infoStudentCurricularPlans.get(infoSCP);        
    }
    
    public InfoStudentCurricularPlan getInfoStudentCurricularPlanById(String cpId)
    {
        List SCPs = getInfoStudentCurricularPlans();
        Iterator it = SCPs.iterator();
        InfoStudentCurricularPlan scp = null;
        
        while(it.hasNext())
        {
            scp = (InfoStudentCurricularPlan)it.next();
            
            //InfoEnrolment ie = (InfoEnrolment)scp.getInfoEnrolments().get(0);
            // ie.getInfoCurricularCourse().getInfoCurricularCourseScope().
            
            if (scp.getIdInternal().intValue() == Integer.parseInt(cpId))
                break;
        }
        
        return scp;
    }
    
    public List getInfoEnrollmentsForStudentCPById(String SCPid)
    {
        List SCPs = getInfoStudentCurricularPlans();
        Iterator it = SCPs.iterator();
        InfoStudentCurricularPlan scp = null;
        
        while(it.hasNext())
        {
            scp = (InfoStudentCurricularPlan)it.next();
            
            //InfoEnrolment ie = (InfoEnrolment)scp.getInfoEnrolments().get(0);
            // ie.getInfoCurricularCourse().getInfoCurricularCourseScope().
            
            if (scp.getIdInternal().intValue() == Integer.parseInt(SCPid))
                break;
        }
        
        if (scp == null)
            return new ArrayList(); // seria melhor Exception?
        
        return getInfoEnrollmentsForStudentCP(scp);
    }
    
    
    public List getInfoEnrollmentsForStudentCPByIdAndCurricularYearAndSemester (String cpId, Integer cYear, Integer semester)
    {
        List enrollmentsResult = new ArrayList();
        List enrollments = getInfoEnrollmentsForStudentCPById(cpId);
        InfoStudentCurricularPlan infoSCP = getInfoStudentCurricularPlanById(cpId);
        
        Iterator it = enrollments.iterator();
        while(it.hasNext())
        {
            InfoEnrolment enrol = (InfoEnrolment)it.next();
            InfoBranch branch = infoSCP.getInfoBranch();
            Integer actualSemester = enrol.getInfoExecutionPeriod().getSemester();
            Integer actualCYear = enrol.getInfoCurricularCourse().getInfoCurricularCourseScope(branch, actualSemester).getInfoCurricularSemester().getInfoCurricularYear().getYear();
            
            if (actualCYear.equals(cYear) && actualSemester.equals(semester))
            {
                enrollmentsResult.add(enrol);
            }
        }
         
        return enrollmentsResult;
        
    }
    
    
    public List getCurricularYearsByStudentCPId(String cpId)
    {
        
        List curricularYears = new ArrayList();
        List enrollments = getInfoEnrollmentsForStudentCPById(cpId);
        InfoStudentCurricularPlan infoSCP = getInfoStudentCurricularPlanById(cpId);
        
        Iterator it = enrollments.iterator();
        while(it.hasNext())
        {
            InfoEnrolment enrol = (InfoEnrolment)it.next();
            InfoBranch branch = infoSCP.getInfoBranch(); // SARILHO
            InfoExecutionPeriod infoExecutionPeriod = enrol.getInfoExecutionPeriod(); 
            Integer actualSemester = infoExecutionPeriod.getSemester();
            InfoCurricularCourse infoCurricularCourse = enrol.getInfoCurricularCourse();
            InfoCurricularCourseScope infoCurricularCourseScope = infoCurricularCourse.getInfoCurricularCourseScope(branch, actualSemester);
          /*  //InfoCurricularSemester infoCurricularSemester = infoCurricularCourseScope.getInfoCurricularSemester();
            //InfoCurricularYear infoCurricularYear = infoCurricularSemester.getInfoCurricularYear();
            //Integer actualCYear = infoCurricularYear.getYear();
            
            if (!curricularYears.contains(actualCYear))
            {
                curricularYears.add(actualCYear);
            }*/
        }
        
        Collections.sort(curricularYears);
        
        return curricularYears;        
    }
    
    
    
    
    public List getSemestersByStudentCPId(String cpId)
    {
        
        List semesters = new ArrayList();
        List enrollments = getInfoEnrollmentsForStudentCPById(cpId);
                
        Iterator it = enrollments.iterator();
        while(it.hasNext())
        {
            InfoEnrolment enrol = (InfoEnrolment)it.next();
            Integer actualSemester = enrol.getInfoExecutionPeriod().getSemester();
            
            if (!semesters.contains(actualSemester))
            {
                semesters.add(actualSemester);
            }
        }
        
        Collections.sort(semesters);
        
        return semesters;
        
    }
    

    
    
    public void addInfoStudentCurricularPlan(InfoStudentCurricularPlan infoSCP, List selectedEnrolledCourses)
    {
        infoStudentCurricularPlans.put(infoSCP,selectedEnrolledCourses);
    }
    
    public void merge (InfoStudentCurricularPlansWithSelectedEnrollments infoSCP)
    {
        List infoSCPs = infoSCP.getInfoStudentCurricularPlans();
        Iterator infoSCPsIterator = infoSCPs.iterator();
        
        while (infoSCPsIterator.hasNext())
        {
            InfoStudentCurricularPlan key = (InfoStudentCurricularPlan)infoSCPsIterator.next();
            List values = infoSCP.getInfoEnrollmentsForStudentCP(key);
            
            this.addInfoStudentCurricularPlan(key,values);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
