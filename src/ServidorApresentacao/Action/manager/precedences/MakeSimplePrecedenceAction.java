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
        System.out.println("last Index Restriction: " + RestrictionByNumberOfDoneCurricularCourses.class.getName().lastIndexOf("."));
        System.out.println("SubString Restriction: " + RestrictionByNumberOfDoneCurricularCourses.class.getName().substring(RestrictionByNumberOfDoneCurricularCourses.class.getName().lastIndexOf(".")+1));
        
        if (className == null || className.length() <= 0) {
            return mapping.getInputForward();
        } 
        
        request.setAttribute("className", className);
        
       if(className.equals(RestrictionByNumberOfDoneCurricularCourses.class.getName().substring(RestrictionByNumberOfDoneCurricularCourses.class.getName().lastIndexOf(".")+1))) {
            System.out.println("-->Class Name Restriction: " + RestrictionByNumberOfDoneCurricularCourses.class.getName());
            
            return mapping.findForward("insertRestrictionByNumber");
        } else if(className.equals(RestrictionPeriodToApply.class.getName().substring(RestrictionPeriodToApply.class.getName().lastIndexOf(".")+1))) {            
            System.out.println("-->Class Name Restriction: " + RestrictionPeriodToApply.class.getName());

            return mapping.findForward("insertRestrictionByPeriodToApply");
        } else if( (className.equals(RestrictionDoneCurricularCourse.class.getName().substring(RestrictionDoneCurricularCourse.class.getName().lastIndexOf(".")+1))) 
                || (className.equals(RestrictionNotDoneCurricularCourse.class.getName().substring(RestrictionNotDoneCurricularCourse.class.getName().lastIndexOf(".")+1))) 
                || (className.equals(RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class.getName().substring(RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class.getName().lastIndexOf(".")+1))) 
                || (className.equals(RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class.getName().substring(RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class.getName().lastIndexOf(".")+1))) 
                || (className.equals(RestrictionNotDoneCurricularCourse.class.getName().substring(RestrictionNotDoneCurricularCourse.class.getName().lastIndexOf(".")+1))) ) {
            System.out.println("-->Class Name Restriction: RestrictionByCurricularCourse");
            
            return mapping.findForward("insertRestrictionByCurricularCourse");
        }   
    
        return mapping.findForward("insertRestriction");
    }}