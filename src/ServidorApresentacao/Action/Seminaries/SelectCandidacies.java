/*
 * Created on 25/Set/2003, 11:52:14 By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.Seminaries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import framework.factory.ServiceManagerServiceFactory;
import DataBeans.InfoEnrolment;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCandidacyDetails;
import DataBeans.Seminaries.InfoClassification;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at 25/Set/2003, 11:52:14
 */
//TODO: this action IS NOT ready to handle multiple seminaries. It will need a
// select box to select which seminary's candidacies to view
public class SelectCandidacies extends FenixDispatchAction
{
    public ActionForward prepare(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        HttpSession session = this.getSession(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        List candidacies = null;
        ActionForward destiny = null;
        List candidaciesExtendedInfo = new LinkedList();
        InfoClassification ic = null;
        String seminaryIDString = request.getParameter("seminaryID");
        Integer seminaryID = null;
        Integer wildcard = new Integer(-1);
        List seminaries = null;
        try
        {
            seminaryID = new Integer(seminaryIDString);
        } catch (NumberFormatException ex)
        {
            seminaryID = wildcard;
        }
        try
        {

            Object[] argsReadSeminaries = { new Boolean(false)};
            Object[] argsReadCandidacies =
                {
                    wildcard,
                    seminaryID,
                    wildcard,
                    wildcard,
                    wildcard,
                    wildcard,
                    wildcard,
                    wildcard,
                    wildcard,
                    wildcard,
                    null };
            seminaries =
                (List) ServiceManagerServiceFactory.executeService(
                    userView,
                    "Seminaries.GetAllSeminaries",
                    argsReadSeminaries);
            candidacies =
                (List) ServiceManagerServiceFactory.executeService(
                    userView,
                    "Seminaries.ReadCandidacies",
                    argsReadCandidacies);
            for (Iterator iterator = candidacies.iterator(); iterator.hasNext();)
            {
                InfoStudent student = null;
                //InfoStudentCurricularPlan studentCurricularPlan = null;
                InfoCandidacy candidacy = (InfoCandidacy) iterator.next();
                Object[] argsReadStudent = { candidacy.getStudentIdInternal()};
                student =
                    (InfoStudent) ServiceManagerServiceFactory.executeService(
                        userView,
                        "student.ReadStudentById",
                        argsReadStudent);
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
                    String stringGrade;
                    if (ie.getInfoEnrolmentEvaluation()!=null) {
                        
                     stringGrade = ie.getInfoEnrolmentEvaluation().getGrade();
                    } else {
                        stringGrade="NA";
                    }

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
                //				
                //  
                InfoCandidacyDetails infoCandidacyDetails = new InfoCandidacyDetails();
                infoCandidacyDetails.setIdInternal(candidacy.getIdInternal());
                infoCandidacyDetails.setInfoClassification(ic);
                infoCandidacyDetails.setStudent(student);
                if (candidacy.getApproved() == null)
                    candidacy.setApproved(new Boolean(false));
                infoCandidacyDetails.setApproved(candidacy.getApproved());
                infoCandidacyDetails.setMotivation(candidacy.getMotivation());
                candidaciesExtendedInfo.add(infoCandidacyDetails);
            }
        } catch (Exception e)
        {
            throw new FenixActionException();
        }
        request.setAttribute("seminaries", seminaries);
        request.setAttribute("candidacies", candidaciesExtendedInfo);
        destiny = mapping.findForward("showSelectCandidacies");
        return destiny;
    }

    public List getNewSelectedStudents(Integer[] selectedStudents, Integer[] previousUnselected)
    {
        List newSelectedStudents = new LinkedList();
        for (int i = 0; i < selectedStudents.length; i++)
        {
            for (int j = 0; j < previousUnselected.length; j++)
            {
                if (selectedStudents[i].equals(previousUnselected[j]))
                {
                    newSelectedStudents.add(selectedStudents[i]);
                    break;
                }
            }
        }
        return newSelectedStudents;
    }

    public List getNewUnselectedStudents(Integer[] selectedStudents, Integer[] previousSelected)
    {
        List newUnselectedStudents = new LinkedList();
        for (int i = 0; i < previousSelected.length; i++)
            newUnselectedStudents.add(previousSelected[i]);
        //
        //
        for (int i = 0; i < previousSelected.length; i++)
        {
            for (int j = 0; j < selectedStudents.length; j++)
            {
                if (previousSelected[i].equals(selectedStudents[j]))
                {
                    newUnselectedStudents.remove(previousSelected[i]);
                    break;
                }
            }
        }
        return newUnselectedStudents;
    }

    public ActionForward changeSelection(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        HttpSession session = this.getSession(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm selectCases = (DynaActionForm) form;
        Integer[] selectedStudents = null;
        Integer[] previousSelected = null;
        Integer[] previousUnselected = null;
        try
        {
            selectedStudents = (Integer[]) selectCases.get("selectedStudents");
            previousSelected = (Integer[]) selectCases.get("previousSelected");
            previousUnselected = (Integer[]) selectCases.get("previousUnselected");
        } catch (Exception ex)
        {
            ex.printStackTrace();
            throw new FenixActionException();
        }
        if (selectedStudents == null || previousSelected == null || previousUnselected == null)
        {
            throw new FenixActionException();
        }
        try
        {
            List changedStatusCandidaciesIds = new LinkedList();
            changedStatusCandidaciesIds.addAll(
                this.getNewSelectedStudents(selectedStudents, previousUnselected));
            changedStatusCandidaciesIds.addAll(
                this.getNewUnselectedStudents(selectedStudents, previousSelected));
            Object[] argsReadCandidacies = { changedStatusCandidaciesIds };
            ServiceManagerServiceFactory.executeService(
                userView,
                "Seminaries.ChangeCandidacyApprovanceStatus",
                argsReadCandidacies);
        } catch (FenixServiceException ex)
        {
            throw new FenixActionException(ex);
        }
        
        // modified by Fernanda Quitério
        //destiny = mapping.findForward("prepareForm");
        //return destiny;
        return prepare(mapping,form,request,response);
    }
}
