/*
 * Created on 1/Set/2003, 15:06:55
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.Seminaries;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.SiteView;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCandidacyDetails;
import DataBeans.Seminaries.InfoCaseStudy;
import DataBeans.Seminaries.InfoCaseStudyChoice;
import DataBeans.Seminaries.InfoEquivalency;
import DataBeans.Seminaries.InfoModality;
import DataBeans.Seminaries.InfoSeminary;
import DataBeans.Seminaries.InfoTheme;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 1/Set/2003, 15:06:55
 * 
 */
public class ShowCandidacies extends FenixAction
{
	Object[] getReadCandidaciesArgs(HttpServletRequest request) throws FenixActionException
	{
		Integer modalityID;
		Integer themeID;
		Integer case1Id;
		Integer case2Id;
		Integer case3Id;
		Integer case4Id;
		Integer case5Id;
		Integer curricularCourseID;
		Integer degreeID;
		Integer seminaryID;
		Boolean approved= null;
		//
		//
		String stringApproved= (String) request.getParameter("approved");
		if (stringApproved != null && (stringApproved.equals("true") || stringApproved.equals("false")))
			approved= new Boolean(stringApproved);
		//
		//
		try
		{
			themeID= new Integer((String) request.getParameter("themeID"));
		}
		catch (NumberFormatException ex)
		{
			themeID= new Integer(-1);
		}
		try
		{
			modalityID= new Integer((String) request.getParameter("modalityID"));
		}
		catch (NumberFormatException ex)
		{
			modalityID= new Integer(-1);
		}
		try
		{
			seminaryID= new Integer((String) request.getParameter("seminaryID"));
		}
		catch (NumberFormatException ex)
		{
			seminaryID= new Integer(-1);
		}
		try
		{
			case1Id= new Integer((String) request.getParameter("case1ID"));
		}
		catch (NumberFormatException ex)
		{
			case1Id= new Integer(-1);
		}
		try
		{
			case2Id= new Integer((String) request.getParameter("case2ID"));
		}
		catch (NumberFormatException ex)
		{
			case2Id= new Integer(-1);
		}
		try
		{
			case3Id= new Integer((String) request.getParameter("case3ID"));
		}
		catch (NumberFormatException ex)
		{
			case3Id= new Integer(-1);
		}
		try
		{
			case4Id= new Integer((String) request.getParameter("case4ID"));
		}
		catch (NumberFormatException ex)
		{
			case4Id= new Integer(-1);
		}
		try
		{
			case5Id= new Integer((String) request.getParameter("case5ID"));
		}
		catch (NumberFormatException ex)
		{
			case5Id= new Integer(-1);
		}
		try
		{
			curricularCourseID= new Integer((String) request.getParameter("courseID"));
		}
		catch (NumberFormatException ex)
		{
			curricularCourseID= new Integer(-1);
		}
		try
		{
			degreeID= new Integer((String) request.getParameter("degreeID"));
		}
		catch (NumberFormatException ex)
		{
			degreeID= new Integer(-1);
		}
		Object[] arguments=
			{
				modalityID,
				seminaryID,
				themeID,
				case1Id,
				case2Id,
				case3Id,
				case4Id,
				case5Id,
				curricularCourseID,
				degreeID,
				approved };
		return arguments;
	}
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session= this.getSession(request);
		IUserView userView= (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		//
		List candidacies= new LinkedList();
		ActionForward destiny= null;
		List candidaciesExtendedInfo= new LinkedList();
		try
		{
			Object[] argsReadCandidacies= getReadCandidaciesArgs(request);
			GestorServicos gestor= GestorServicos.manager();
			candidacies= (List) gestor.executar(userView, "Seminaries.ReadCandidacies", argsReadCandidacies);
			for (Iterator iterator= candidacies.iterator(); iterator.hasNext();)
			{
				InfoStudent student= null;
				InfoCurricularCourse curricularCourse= null;
				InfoTheme theme= null;
				InfoModality modality= null;
				InfoSeminary seminary= null;
				InfoStudentCurricularPlan studentCurricularPlan= null;
				List casesChoices= null;
				List cases= new LinkedList();
				InfoCandidacy candidacy= (InfoCandidacy) iterator.next();
				Object[] argsReadStudent= { candidacy.getStudentIdInternal()};
				Object[] argsReadCurricularCourse= { candidacy.getCurricularCourseIdInternal()};
				Object[] argsReadTheme= { candidacy.getThemeIdInternal()};
				Object[] argsReadModality= { candidacy.getModalityIdInternal()};
				Object[] argsReadSeminary= { candidacy.getSeminaryIdInternal()};
				student= (InfoStudent) gestor.executar(userView, "student.ReadStudentById", argsReadStudent);
				Object[] argsReadCurricularPlan= { student.getNumber(), student.getDegreeType()};
				studentCurricularPlan=
					(InfoStudentCurricularPlan) gestor.executar(
						userView,
						"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
						argsReadCurricularPlan);
				curricularCourse=
					(InfoCurricularCourse) ((SiteView) gestor
						.executar(userView, "ReadCurricularCourseByOIdService", argsReadCurricularCourse))
						.getComponent();
				theme= (InfoTheme) gestor.executar(userView, "Seminaries.GetThemeById", argsReadTheme);
				modality= (InfoModality) gestor.executar(userView, "Seminaries.GetModalityById", argsReadModality);
				seminary= (InfoSeminary) gestor.executar(userView, "Seminaries.GetSeminary", argsReadSeminary);
			//	motivation= candidacy.getMotivation();
				casesChoices= candidacy.getCaseStudyChoices();
				//
				for (Iterator casesIterator= casesChoices.iterator(); casesIterator.hasNext();)
				{
					InfoCaseStudyChoice choice= (InfoCaseStudyChoice) casesIterator.next();
					Object[] argsReadCaseStudy= { choice.getCaseStudyIdInternal()};
					InfoCaseStudy infoCaseStudy=
						(InfoCaseStudy) gestor.executar(
							userView,
							"Seminaries.GetCaseStudyById",
							argsReadCaseStudy);
					cases.add(infoCaseStudy);
				}
				//  
				InfoCandidacyDetails infoCandidacyDetails= new InfoCandidacyDetails();
				infoCandidacyDetails.setCases(cases);
				infoCandidacyDetails.setIdInternal(candidacy.getIdInternal());
				infoCandidacyDetails.setModality(modality);
				infoCandidacyDetails.setSeminary(seminary);
				infoCandidacyDetails.setStudent(student);
				infoCandidacyDetails.setTheme(theme);
				infoCandidacyDetails.setApproved(candidacy.getApproved());
				if (studentCurricularPlan != null)
				{
					infoCandidacyDetails.setClassification(studentCurricularPlan.getClassification());
					infoCandidacyDetails.setCompletedCourses(studentCurricularPlan.getCompletedCourses());
				}
				else
				{
					infoCandidacyDetails.setClassification(null);
					infoCandidacyDetails.setCompletedCourses(null);
				}
				infoCandidacyDetails.setMotivation(candidacy.getMotivation());
				infoCandidacyDetails.setCurricularCourse(curricularCourse);
				candidaciesExtendedInfo.add(infoCandidacyDetails);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new FenixActionException(e);
		}
		this.setAvaliableOptionsForInputQueries(request, userView);
		request.setAttribute("candidacies", candidaciesExtendedInfo);
		destiny= mapping.findForward("allCandidaciesGrid");
		return destiny;
	}
	private void setAvaliableOptionsForInputQueries(HttpServletRequest request, IUserView userView)
		throws FenixActionException
	{
		List seminaries= null;
		List cases= null;
		List degrees= null;
		List modalities= null;
		List equivalencies= null;
		List themes= null;
		List curricularCoursesWithEquivalency= new LinkedList();
		
		try
		{
			
			Object[] argsReadSeminaries= {
			};
			Object[] argsReadCasesStudy= {
			};
			Object[] argsReadDegrees= {
			};
			Object[] argsReadModalities= {
			};
			Object[] argsReadThemes= {
			};
			Object[] argsReadEquivalencies= {
			};
			Object[] argsReadCurrentExecutionPeriod= {
			};
			GestorServicos gestor= GestorServicos.manager();
			seminaries= (List) gestor.executar(userView, "Seminaries.GetAllSeminaries", argsReadSeminaries);
			cases= (List) gestor.executar(userView, "Seminaries.GetAllCasesStudy", argsReadCasesStudy);
			degrees= (List) gestor.executar(userView, "manager.ReadDegreeCurricularPlans", argsReadDegrees);
			modalities= (List) gestor.executar(userView, "Seminaries.GetAllModalities", argsReadModalities);
			themes= (List) gestor.executar(userView, "Seminaries.GetAllThemes", argsReadThemes);
			equivalencies=
				(List) gestor.executar(userView, "Seminaries.GetAllEquivalencies", argsReadEquivalencies);
			 gestor.executar(
					userView,
					"ReadCurrentExecutionPeriod",
					argsReadCurrentExecutionPeriod);
			//
			//
			for (Iterator iterator= equivalencies.iterator(); iterator.hasNext();)
			{
				InfoEquivalency equivalency= (InfoEquivalency) iterator.next();
				Object[] argsReadCurricularCourse= { equivalency.getCurricularCourseIdInternal()};
				InfoCurricularCourse curricularCourse=
					(InfoCurricularCourse) gestor.executar(
						userView,
						"ReadCurricularCourse",
						argsReadCurricularCourse);
				curricularCoursesWithEquivalency.add(curricularCourse);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new FenixActionException(e);
		}
		//this is very ugly, but the boss asked for it :)
		//we will display only the Master Curricular Plans and the actual (current execution period) curricular plans for other degree types
		List avaliableCurricularPlans= new LinkedList();
		for (Iterator iter= degrees.iterator(); iter.hasNext();)
		{
			InfoDegreeCurricularPlan infoDegreeCurricularPlan= (InfoDegreeCurricularPlan) iter.next();
			if (infoDegreeCurricularPlan.getInfoDegree().getTipoCurso().equals(TipoCurso.MESTRADO_OBJ))
				avaliableCurricularPlans.add(infoDegreeCurricularPlan);
			else
				if (infoDegreeCurricularPlan.getName().endsWith("2003/2004"))
				{
					String newName= new String();
					newName= infoDegreeCurricularPlan.getName().replaceAll("2003/2004", "");
					infoDegreeCurricularPlan.setName(newName);
					avaliableCurricularPlans.add(infoDegreeCurricularPlan);
				}
		}
		Collections.sort(avaliableCurricularPlans, new BeanComparator("name"));
		request.setAttribute("seminaries", seminaries);
		request.setAttribute("cases", cases);
		request.setAttribute("degrees", avaliableCurricularPlans);
		request.setAttribute("modalities", modalities);
		request.setAttribute("courses", curricularCoursesWithEquivalency);
		request.setAttribute("themes", themes);
	}
}