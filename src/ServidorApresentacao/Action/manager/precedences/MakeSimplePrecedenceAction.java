/*
 * Created on 28/Jul/2004
 *
 */
package ServidorApresentacao.Action.manager.precedences;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import Dominio.precedences.RestrictionByNumberOfCurricularCourses;
import Dominio.precedences.RestrictionByNumberOfDoneCurricularCourses;
import Dominio.precedences.RestrictionDoneCurricularCourse;
import Dominio.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import Dominio.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import Dominio.precedences.RestrictionNotDoneCurricularCourse;
import Dominio.precedences.RestrictionPeriodToApply;
import ServidorApresentacao.Action.base.FenixDispatchAction;

/**
 * @author Tânia Pousão
 *  
 */
public class MakeSimplePrecedenceAction extends FenixDispatchAction {

    public ActionForward showAllRestrictions(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        return mapping.findForward("showAllRestrictions");
    }

    public ActionForward insertRestriction(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String className = request.getParameter("className");
        System.out.println("Class Name Restriction: " + className);
        System.out.println("Class Name Restriction: " + RestrictionByNumberOfCurricularCourses.class.getName());
        System.out.println("Package Name Restriction: " + RestrictionByNumberOfCurricularCourses.class.getPackage());
        
        if (className == null || className.length() <= 0) {
            return mapping.getInputForward();
        } 
        
        request.setAttribute("className", className);
        
        if(className.equals(RestrictionByNumberOfCurricularCourses.class.getName().split(RestrictionByNumberOfCurricularCourses.class.getPackage().toString())[0].substring(1))) {      
            System.out.println("-->Class Name Restriction: " + RestrictionByNumberOfCurricularCourses.class.getName());
        } else if(className.equals(RestrictionByNumberOfDoneCurricularCourses.class.getName().split(RestrictionByNumberOfDoneCurricularCourses.class.getPackage().toString())[0].substring(1))) {
            System.out.println("-->Class Name Restriction: " + RestrictionByNumberOfDoneCurricularCourses.class.getName());
        } else if(className.equals(RestrictionDoneCurricularCourse.class.getName().split(RestrictionDoneCurricularCourse.class.getPackage().toString())[0].substring(1))) {
            System.out.println("-->Class Name Restriction: " + RestrictionDoneCurricularCourse.class.getName());
        } else if(className.equals(RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class.getName().split(RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class.getPackage().toString())[0].substring(1))) {
            System.out.println("-->Class Name Restriction: " + RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class.getName());
        } else if(className.equals(RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class.getName().split(RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class.getPackage().toString())[0].substring(1))) {
            System.out.println("-->Class Name Restriction: " + RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class.getName());
        } else if(className.equals(RestrictionNotDoneCurricularCourse.class.getName().split(RestrictionNotDoneCurricularCourse.class.getPackage().toString())[0].substring(1))) {
            System.out.println("-->Class Name Restriction: " + RestrictionNotDoneCurricularCourse.class.getName());
        } else if(className.equals(RestrictionPeriodToApply.class.getName().split(RestrictionPeriodToApply.class.getPackage().toString())[0].substring(1))) {            
            System.out.println("-->Class Name Restriction: " + RestrictionPeriodToApply.class.getName());
        }   
    
        return mapping.findForward("insertRestriction");
    }
}