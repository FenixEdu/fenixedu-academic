/*
 * Created on 8/Set/2003, 18:37:00
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.Seminaries;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolment;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.SiteView;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCaseStudy;
import DataBeans.Seminaries.InfoCaseStudyChoice;
import DataBeans.Seminaries.InfoClassification;
import DataBeans.Seminaries.InfoModality;
import DataBeans.Seminaries.InfoSeminary;
import DataBeans.Seminaries.InfoTheme;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 8/Set/2003, 18:37:00
 *  
 */
public class DownloadCandidaciesTable extends FenixAction
{
    static final String COLUMNS_HEADERS =
        "Nº\tNome\tMédia\tCadeiras Feitas\tAprovado\tE-Mail\tSeminário\tCurso\tDisciplina\tModalidade\tTema\tMotivação\tCaso1\tCaso2\tCaso3\tCaso4\tCaso5";
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
        if (stringApproved != null && (stringApproved.equals("true") || stringApproved.equals("false")))
            approved = new Boolean(stringApproved);
        //
        //
        try
        {
            themeID = new Integer(request.getParameter("themeID"));
        } catch (NumberFormatException ex)
        {
            themeID = new Integer(-1);
        }
        try
        {
            modalityID = new Integer(request.getParameter("modalityID"));
        } catch (NumberFormatException ex)
        {
            modalityID = new Integer(-1);
        }
        try
        {
            seminaryID = new Integer(request.getParameter("seminaryID"));
        } catch (NumberFormatException ex)
        {
            seminaryID = new Integer(-1);
        }
        try
        {
            case1Id = new Integer(request.getParameter("case1ID"));
        } catch (NumberFormatException ex)
        {
            case1Id = new Integer(-1);
        }
        try
        {
            case2Id = new Integer(request.getParameter("case2ID"));
        } catch (NumberFormatException ex)
        {
            case2Id = new Integer(-1);
        }
        try
        {
            case3Id = new Integer(request.getParameter("case3ID"));
        } catch (NumberFormatException ex)
        {
            case3Id = new Integer(-1);
        }
        try
        {
            case4Id = new Integer(request.getParameter("case4ID"));
        } catch (NumberFormatException ex)
        {
            case4Id = new Integer(-1);
        }
        try
        {
            case5Id = new Integer(request.getParameter("case5ID"));
        } catch (NumberFormatException ex)
        {
            case5Id = new Integer(-1);
        }
        try
        {
            curricularCourseID = new Integer(request.getParameter("courseID"));
        } catch (NumberFormatException ex)
        {
            curricularCourseID = new Integer(-1);
        }
        try
        {
            degreeID = new Integer(request.getParameter("degreeID"));
        } catch (NumberFormatException ex)
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
        String document = DownloadCandidaciesTable.COLUMNS_HEADERS + "\n";
        HttpSession session = this.getSession(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        InfoClassification ic = null;
        //
        List candidacies = new LinkedList();
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
                //String motivation= null;
                InfoSeminary seminary = null;
                List casesChoices = null;
                InfoStudentCurricularPlan studentCurricularPlan = null;
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
                document += "\"" + student.getNumber() + "\"" + "\t";
                document += "\"" + student.getInfoPerson().getNome() + "\"" + "\t";
                //
                //
                //
                //
                //
                //
                Object argsReadStudentCurricularPlans[] =
                    { new UserView(student.getInfoPerson().getUsername(), new LinkedList())};
                InfoStudentCurricularPlan selectedSCP = null;
                List cps =
                    (ArrayList) ServiceManagerServiceFactory.executeService(
                        userView,
                        "ReadStudentCurricularPlansForSeminaries",
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
                Object getCurriculumArgs[] = { null, selectedSCP.getIdInternal()};
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
                    if (stringGrade != null && !stringGrade.equals("RE") && !stringGrade.equals("NA"))
                    {
                        Float gradeObject = new Float(stringGrade);
                        grade = gradeObject.floatValue();
                        acc += grade;
                        i++;
                    }
                }
                if (i != 0)
                {
                    String value = new DecimalFormat("#0.0").format(acc / i);
                    ic.setAritmeticClassification(value);
                }
                ic.setCompletedCourses(new Integer(i).toString());
                document += "\"" + ic.getAritmeticClassification() + "\"" + "\t";
                document += "\"" + ic.getCompletedCourses() + "\"" + "\t";
                //
                //
                //
                String friendlyBoolean;
                if ((candidacy.getApproved() != null) && (candidacy.getApproved().booleanValue()))
                    friendlyBoolean = "Sim";
                else
                    friendlyBoolean = "Não";
                document += "\"" + friendlyBoolean + "\"" + "\t";
                document += "\"" + student.getInfoPerson().getEmail() + "\"" + "\t";
                document += "\"" + seminary.getName() + "\"" + "\t";
                document += "\""
                    + curricularCourse.getInfoDegreeCurricularPlan().getInfoDegree().getSigla()
                    + "\""
                    + "\t";
                document += "\"" + curricularCourse.getName() + "\"" + "\t";
                document += "\"" + modality.getName() + "\"" + "\t";
                if (theme != null)
                    document += "\"" + theme.getName() + "\"" + "\t";
                else
                    document += "\"" + "N/A\"\t";
                document += "\"" + candidacy.getMotivation() + "\"" + "\t";
                for (Iterator casesIterator = cases.iterator(); casesIterator.hasNext();)
                {
                    InfoCaseStudy caseStudy = (InfoCaseStudy) casesIterator.next();
                    document += "\"" + caseStudy.getName() + "\"" + "\t";
                }
                document += "\n";
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new FenixActionException(e);
        }
        try
        {
            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            writer.print(document);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e1)
        {
            throw new FenixActionException();
        }
        return null;
    }
}