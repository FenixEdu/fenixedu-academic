/*
 * Created on 25/Set/2003, 11:52:14
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
import org.apache.struts.action.DynaActionForm;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCandidacyDetails;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 25/Set/2003, 11:52:14
 * 
 */
//TODO: this action IS NOT ready to handle multiple seminaries. It will need a select box to select which seminary's candidacies to view
public class SelectCandidacies extends FenixDispatchAction
{
	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session= this.getSession(request);
		IUserView userView= (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		List candidacies= null;
		ActionForward destiny= null;
		List candidaciesExtendedInfo= new LinkedList();
		try
		{
			Integer wildcard= new Integer(-1);
			Object[] argsReadCandidacies=
				{
					wildcard,
					wildcard,
					wildcard,
					wildcard,
					wildcard,
					wildcard,
					wildcard,
					wildcard,
					wildcard,
					wildcard,
                    null };
			GestorServicos gestor= GestorServicos.manager();
			candidacies= (List) gestor.executar(userView, "Seminaries.ReadCandidacies", argsReadCandidacies);
			for (Iterator iterator= candidacies.iterator(); iterator.hasNext();)
			{
				InfoStudent student= null;
				InfoStudentCurricularPlan studentCurricularPlan= null;
				InfoCandidacy candidacy= (InfoCandidacy) iterator.next();
				Object[] argsReadStudent= { candidacy.getStudentIdInternal()};
				student= (InfoStudent) gestor.executar(userView, "student.ReadStudentById", argsReadStudent);
				Object[] argsReadCurricularPlan= { student.getNumber(), student.getDegreeType()};
				studentCurricularPlan=
					(InfoStudentCurricularPlan) gestor.executar(
						userView,
						"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
						argsReadCurricularPlan);
				//				
				//  
				InfoCandidacyDetails infoCandidacyDetails= new InfoCandidacyDetails();
				infoCandidacyDetails.setIdInternal(candidacy.getIdInternal());
				infoCandidacyDetails.setStudent(student);
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
				candidaciesExtendedInfo.add(infoCandidacyDetails);
			}
		}
		catch (Exception e)
		{
			throw new FenixActionException();
		}
		request.setAttribute("candidacies", candidaciesExtendedInfo);
		destiny= mapping.findForward("showSelectCandidacies");
		return destiny;
	}
	public List getNewSelectedStudents(Integer[] selectedStudents, Integer[] previousUnselected)
	{
		List newSelectedStudents= new LinkedList();
		for (int i= 0; i < selectedStudents.length; i++)
		{
			for (int j= 0; j < previousUnselected.length; j++)
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
		List newUnselectedStudents= new LinkedList();
		for (int i= 0; i < previousSelected.length; i++)
			newUnselectedStudents.add(previousSelected[i]);
		//
		//
		for (int i= 0; i < previousSelected.length; i++)
		{
			for (int j= 0; j < selectedStudents.length; j++)
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
        HttpSession session= this.getSession(request);
        IUserView userView= (IUserView) session.getAttribute(SessionConstants.U_VIEW);        
		ActionForward destiny= null;
		DynaActionForm selectCases= (DynaActionForm) form;
		Integer[] selectedStudents= null;
		Integer[] previousSelected= null;
		Integer[] previousUnselected= null;
		try
		{
			selectedStudents= (Integer[]) selectCases.get("selectedStudents");
			previousSelected= (Integer[]) selectCases.get("previousSelected");
			previousUnselected= (Integer[]) selectCases.get("previousUnselected");
		}
		catch (Exception ex)
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
            changedStatusCandidaciesIds.addAll(this.getNewSelectedStudents(selectedStudents,previousUnselected));
            changedStatusCandidaciesIds.addAll(this.getNewUnselectedStudents(selectedStudents,previousSelected));
            Object[] argsReadCandidacies= {changedStatusCandidaciesIds};
                        GestorServicos gestor= GestorServicos.manager();
            gestor.executar(userView, "Seminaries.ChangeCandidacyApprovanceStatus", argsReadCandidacies);
        }
        catch (FenixServiceException ex)
        {
            throw new FenixActionException(ex);
        }
		destiny= mapping.findForward("prepareForm");
		return destiny;
	}
}
