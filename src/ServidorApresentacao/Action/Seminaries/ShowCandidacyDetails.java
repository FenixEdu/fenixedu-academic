/*
 * Created on 26/Ago/2003, 14:48:56
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoStudent;
import DataBeans.SiteView;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCaseStudy;
import DataBeans.Seminaries.InfoCaseStudyChoice;
import DataBeans.Seminaries.InfoModality;
import DataBeans.Seminaries.InfoSeminary;
import DataBeans.Seminaries.InfoTheme;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 26/Ago/2003, 14:48:56
 * 
 */
public class ShowCandidacyDetails extends FenixAction
{
    public ActionForward execute(
             ActionMapping mapping,
             ActionForm form,
             HttpServletRequest request,
             HttpServletResponse response)
             throws FenixActionException
         {
             HttpSession session= this.getSession(request);
             IUserView userView= (IUserView) session.getAttribute(SessionConstants.U_VIEW);
             String candidacyIDString= request.getParameter("objectCode");
             Integer candidacyID;
             if (candidacyIDString == null)
                 throw new FenixActionException(mapping.findForward("invalidQueryString"));
             try
             {
                 candidacyID= new Integer(candidacyIDString);
             }
             catch (Exception ex)
             {
                 throw new FenixActionException(mapping.findForward("invalidQueryString"));
             }
             InfoCandidacy candidacy= null;
             InfoStudent student = null;
             InfoCurricularCourse curricularCourse = null;
             InfoTheme theme = null;
             InfoModality modality = null;            
             String motivation = null;
             InfoSeminary seminary = null;
             List casesChoices = null;
             List cases = new LinkedList();
             
             
             ActionForward destiny= null;
             try
             {
                 Object[] argsReadCandidacy= { candidacyID };         
                 candidacy= (InfoCandidacy) ServiceManagerServiceFactory.executeService(userView, "Seminaries.GetCandidacyById", argsReadCandidacy);                 
                 //
                 //
                 Object[] argsReadStudent= { candidacy.getStudentIdInternal() };               
                 Object[] argsReadCurricularCourse= { candidacy.getCurricularCourseIdInternal() };
                 Object[] argsReadTheme= { candidacy.getThemeIdInternal() };
                 Object[] argsReadModality= { candidacy.getModalityIdInternal() };
                 Object[] argsReadSeminary= { candidacy.getSeminaryIdInternal() };                 
                 
                 student = (InfoStudent) ServiceManagerServiceFactory.executeService(userView, "student.ReadStudentById", argsReadStudent);
                 curricularCourse = (InfoCurricularCourse)((SiteView) ServiceManagerServiceFactory.executeService(userView, "ReadCurricularCourseByOIdService",argsReadCurricularCourse )).getComponent();
                 theme = (InfoTheme) ServiceManagerServiceFactory.executeService(userView, "Seminaries.GetThemeById",argsReadTheme );
                 modality = (InfoModality) ServiceManagerServiceFactory.executeService(userView, "Seminaries.GetModalityById",argsReadModality );
                 seminary = (InfoSeminary) ServiceManagerServiceFactory.executeService(userView, "Seminaries.GetSeminary",argsReadSeminary );
                 motivation = candidacy.getMotivation();
                 casesChoices = candidacy.getCaseStudyChoices();
                 

                 //
                 for (Iterator iterator= casesChoices.iterator(); iterator.hasNext();)
				{
					InfoCaseStudyChoice choice= (InfoCaseStudyChoice) iterator.next();
                    Object[] argsReadCaseStudy= { choice.getCaseStudyIdInternal() };
                    InfoCaseStudy infoCaseStudy = (InfoCaseStudy) ServiceManagerServiceFactory.executeService(userView, "Seminaries.GetCaseStudyById", argsReadCaseStudy);
                    cases.add(infoCaseStudy);                    
				}
                //              
                 
             }
             catch (Exception e)
             {
                 throw new FenixActionException();
             }
             
             
             destiny= mapping.findForward("showCandidacyDetails");
             request.setAttribute("cases",cases);
             request.setAttribute("student",student);
             request.setAttribute("curricularCourse",curricularCourse);
             request.setAttribute("theme",theme);
             request.setAttribute("motivation",motivation);
             request.setAttribute("seminary",seminary);              
             request.setAttribute("modality",modality);
             return destiny;
         }
}
