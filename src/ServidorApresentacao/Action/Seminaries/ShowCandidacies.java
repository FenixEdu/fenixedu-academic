/*
 * Created on 1/Set/2003, 15:06:55
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.Seminaries;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import DataBeans.InfoEnrolment;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.SiteView;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCandidacyDetails;
import DataBeans.Seminaries.InfoCaseStudy;
import DataBeans.Seminaries.InfoCaseStudyChoice;
import DataBeans.Seminaries.InfoClassification;
import DataBeans.Seminaries.InfoEquivalency;
import DataBeans.Seminaries.InfoModality;
import DataBeans.Seminaries.InfoSeminary;
import DataBeans.Seminaries.InfoTheme;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;
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
		Boolean approved = null;
		//
		//
		String stringApproved = request.getParameter("approved");
		if (stringApproved != null
			&& (stringApproved.equals("true") || stringApproved.equals("false")))
			approved = new Boolean(stringApproved);
		//
		//
		try
		{
			themeID = new Integer(request.getParameter("themeID"));
		}
		catch (NumberFormatException ex)
		{
			themeID = new Integer(-1);
		}
		try
		{
			modalityID = new Integer(request.getParameter("modalityID"));
		}
		catch (NumberFormatException ex)
		{
			modalityID = new Integer(-1);
		}
		try
		{
			seminaryID = new Integer(request.getParameter("seminaryID"));
		}
		catch (NumberFormatException ex)
		{
			seminaryID = new Integer(-1);
		}
		try
		{
			case1Id = new Integer(request.getParameter("case1ID"));
		}
		catch (NumberFormatException ex)
		{
			case1Id = new Integer(-1);
		}
		try
		{
			case2Id = new Integer(request.getParameter("case2ID"));
		}
		catch (NumberFormatException ex)
		{
			case2Id = new Integer(-1);
		}
		try
		{
			case3Id = new Integer(request.getParameter("case3ID"));
		}
		catch (NumberFormatException ex)
		{
			case3Id = new Integer(-1);
		}
		try
		{
			case4Id = new Integer(request.getParameter("case4ID"));
		}
		catch (NumberFormatException ex)
		{
			case4Id = new Integer(-1);
		}
		try
		{
			case5Id = new Integer(request.getParameter("case5ID"));
		}
		catch (NumberFormatException ex)
		{
			case5Id = new Integer(-1);
		}
		try
		{
			curricularCourseID = new Integer(request.getParameter("courseID"));
		}
		catch (NumberFormatException ex)
		{
			curricularCourseID = new Integer(-1);
		}
		try
		{
			degreeID = new Integer(request.getParameter("degreeID"));
		}
		catch (NumberFormatException ex)
		{
			degreeID = new Integer(-1);
		}
		Object[] arguments =
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
		HttpSession session = this.getSession(request);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		//
		List candidacies = new LinkedList();
		ActionForward destiny = null;
		List candidaciesExtendedInfo = new LinkedList();
		InfoClassification ic = null;
		try
		{
			Object[] argsReadCandidacies = getReadCandidaciesArgs(request);
			candidacies =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"Seminaries.ReadCandidacies",
					argsReadCandidacies);
			for (Iterator iterator = candidacies.iterator(); iterator.hasNext();)
			{
				InfoStudent student = null;
				InfoCurricularCourse curricularCourse = null;
				InfoTheme theme = null;
				InfoModality modality = null;
				InfoSeminary seminary = null;
				InfoStudentCurricularPlan studentCurricularPlan = null;
				List casesChoices = null;
				List cases = new LinkedList();
				InfoCandidacy candidacy = (InfoCandidacy) iterator.next();
				Object[] argsReadStudent = { candidacy.getStudentIdInternal()};
				Object[] argsReadCurricularCourse = { candidacy.getCurricularCourseIdInternal()};
				Object[] argsReadTheme = { candidacy.getThemeIdInternal()};
				Object[] argsReadModality = { candidacy.getModalityIdInternal()};
				Object[] argsReadSeminary = { candidacy.getSeminaryIdInternal()};
				student =
					(InfoStudent) ServiceManagerServiceFactory.executeService(
						userView,
						"student.ReadStudentById",
						argsReadStudent);
				Object[] argsReadCurricularPlan = { student.getNumber(), student.getDegreeType()};
				studentCurricularPlan =
					(InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
						userView,
						"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
						argsReadCurricularPlan);
				curricularCourse =
					(InfoCurricularCourse) ((SiteView) ServiceManagerServiceFactory
						.executeService(
							userView,
							"ReadCurricularCourseByOIdService",
							argsReadCurricularCourse))
						.getComponent();
				theme =
					(InfoTheme) ServiceManagerServiceFactory.executeService(
						userView,
						"Seminaries.GetThemeById",
						argsReadTheme);
				modality =
					(InfoModality) ServiceManagerServiceFactory.executeService(
						userView,
						"Seminaries.GetModalityById",
						argsReadModality);
				seminary =
					(InfoSeminary) ServiceManagerServiceFactory.executeService(
						userView,
						"Seminaries.GetSeminary",
						argsReadSeminary);
				//	motivation= candidacy.getMotivation();
				casesChoices = candidacy.getCaseStudyChoices();
				//
				for (Iterator casesIterator = casesChoices.iterator(); casesIterator.hasNext();)
				{
					InfoCaseStudyChoice choice = (InfoCaseStudyChoice) casesIterator.next();
					Object[] argsReadCaseStudy = { choice.getCaseStudyIdInternal()};
					InfoCaseStudy infoCaseStudy =
						(InfoCaseStudy) ServiceManagerServiceFactory.executeService(
							userView,
							"Seminaries.GetCaseStudyById",
							argsReadCaseStudy);
					cases.add(infoCaseStudy);
				}
				//
				//
				//
				Object argsReadStudentCurricularPlans[] =
					{ new UserView(student.getInfoPerson().getUsername(), new LinkedList())};
				InfoStudentCurricularPlan selectedSCP = null;
				List cps =
					(ArrayList) ServiceManagerServiceFactory.executeService(
						userView,
						"ReadStudentCurricularPlans",
						argsReadStudentCurricularPlans);
				long startDate = Long.MAX_VALUE;
				for (Iterator iter = cps.iterator(); iter.hasNext();)
				{
					InfoStudentCurricularPlan cp = (InfoStudentCurricularPlan) iter.next();
					if (cp.getStartDate().getTime() < startDate)
					{
						startDate = cp.getStartDate().getTime();
						selectedSCP = cp;
					}
				}
				Object getCurriculumArgs[] = { userView, selectedSCP.getIdInternal()};
				List enrollments =
					(ArrayList) ServiceManagerServiceFactory.executeService(
						userView,
						"ReadStudentCurriculum",
						getCurriculumArgs);
				//  
				//
				ic = new InfoClassification();
				int i = 0;
				float acc = 0;
				float grade = 0;
				for (Iterator iter = enrollments.iterator(); iter.hasNext();)
				{
					InfoEnrolment ie = (InfoEnrolment) iter.next();
					String stringGrade = ie.getInfoEnrolmentEvaluation().getGrade();
					if (!stringGrade.equals("RE") && !stringGrade.equals("NA"))
					{
						grade = new Float(stringGrade).floatValue();
						acc += grade;
						i++;
					}
				}
				if (i != 0)
				{
					String value = new DecimalFormat("#0.0").format(acc/i);
					ic.setAritmeticClassification(value);
				}
				ic.setCompletedCourses(new Integer(i).toString());
				//
				//
				//
				InfoCandidacyDetails infoCandidacyDetails = new InfoCandidacyDetails();
				infoCandidacyDetails.setCases(cases);
				infoCandidacyDetails.setIdInternal(candidacy.getIdInternal());
				infoCandidacyDetails.setModality(modality);
				infoCandidacyDetails.setSeminary(seminary);
				infoCandidacyDetails.setStudent(student);
				infoCandidacyDetails.setTheme(theme);
				infoCandidacyDetails.setApproved(candidacy.getApproved());
				infoCandidacyDetails.setInfoClassification(ic);
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
		destiny = mapping.findForward("allCandidaciesGrid");
		return destiny;
	}
	private void setAvaliableOptionsForInputQueries(HttpServletRequest request, IUserView userView)
		throws FenixActionException
	{
		List seminaries = null;
		List cases = null;
		List degrees = null;
		List modalities = null;
		List equivalencies = null;
		List themes = null;
		List curricularCoursesWithEquivalency = new LinkedList();
		try
		{
			Object[] argsReadSeminaries = { new Boolean(false)};
			Object[] argsReadCasesStudy = {
			};
			Object[] argsReadDegrees = {
			};
			Object[] argsReadModalities = {
			};
			Object[] argsReadThemes = {
			};
			Object[] argsReadEquivalencies = {
			};
			Object[] argsReadCurrentExecutionPeriod = {
			};
			seminaries =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"Seminaries.GetAllSeminaries",
					argsReadSeminaries);
			cases =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"Seminaries.GetAllCasesStudy",
					argsReadCasesStudy);
			degrees =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"manager.ReadDegreeCurricularPlans",
					argsReadDegrees);
			modalities =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"Seminaries.GetAllModalities",
					argsReadModalities);
			themes =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"Seminaries.GetAllThemes",
					argsReadThemes);
			equivalencies =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"Seminaries.GetAllEquivalencies",
					argsReadEquivalencies);
			ServiceManagerServiceFactory.executeService(
				userView,
				"ReadCurrentExecutionPeriod",
				argsReadCurrentExecutionPeriod);
			//
			//
			for (Iterator iterator = equivalencies.iterator(); iterator.hasNext();)
			{
				InfoEquivalency equivalency = (InfoEquivalency) iterator.next();
				Object[] argsReadCurricularCourse = { equivalency.getCurricularCourseIdInternal()};
				InfoCurricularCourse curricularCourse =
					(InfoCurricularCourse) ServiceManagerServiceFactory.executeService(
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
		List avaliableCurricularPlans = new LinkedList();
		for (Iterator iter = degrees.iterator(); iter.hasNext();)
		{
			InfoDegreeCurricularPlan infoDegreeCurricularPlan =
				(InfoDegreeCurricularPlan) iter.next();
			if (infoDegreeCurricularPlan
				.getInfoDegree()
				.getTipoCurso()
				.equals(TipoCurso.MESTRADO_OBJ))
				avaliableCurricularPlans.add(infoDegreeCurricularPlan);
			else
				if (infoDegreeCurricularPlan.getName().endsWith("2003/2004"))
				{
					String newName = new String();
					newName = infoDegreeCurricularPlan.getName().replaceAll("2003/2004", "");
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