/*
 * Created on 5/Ago/2003, 9:10:07
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoStudent;
import DataBeans.Seminaries.InfoCandidacy;
import DataBeans.Seminaries.InfoCaseStudy;
import DataBeans.Seminaries.InfoCaseStudyChoice;
import DataBeans.Seminaries.InfoEquivalency;
import DataBeans.Seminaries.InfoSeminary;
import DataBeans.Seminaries.InfoTheme;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 5/Ago/2003, 9:10:07
 * 
 */
public class ManageCaseStudyChoices extends FenixAction
{
	public List extractCasesByPresentID(List casesIDs, List cases)
	{
		List extractedCases= new LinkedList();
		for (Iterator iterator= casesIDs.iterator(); iterator.hasNext();)
		{
			String idString= (String) iterator.next();
			Integer id= new Integer(idString);
			for (Iterator innerIterator= cases.iterator(); innerIterator.hasNext();)
			{
				InfoCaseStudy infoCaseStudy= (InfoCaseStudy) innerIterator.next();
				if ((id.equals(infoCaseStudy.getIdInternal())))
				{
					extractedCases.add(infoCaseStudy);
					break;
				}
			}
		}
		return extractedCases;
	}
	public List extractCasesByAbsentID(List casesIDs, List cases)
	{
		List extractedCases= new LinkedList();
		for (Iterator iterator= cases.iterator(); iterator.hasNext();)
		{
			boolean found= false;
			InfoCaseStudy infoCaseStudy= (InfoCaseStudy) iterator.next();
			for (Iterator innerIterator= casesIDs.iterator(); innerIterator.hasNext();)
			{
				String idString= (String) innerIterator.next();
				Integer id= new Integer(idString);
				if ((id.equals(infoCaseStudy.getIdInternal())))
				{
					found= true;
					break;
				}
			}
			if (found == false)
				extractedCases.add(infoCaseStudy);
		}
		return extractedCases;
	}
	public ActionForward removedCases(
		String[] selectedCasesArray,
		String[] unselectedCasesArray,
		String[] hiddenSelectedCasesArray,
		List cases,
		HttpServletRequest request,
		ActionMapping mapping)
	{
		ActionForward destiny= null;
		LinkedList selectedCasesIDs= new LinkedList();
		LinkedList unselectedCasesIDs= new LinkedList();
		LinkedList hiddenSelectedCasesIDs= new LinkedList();
		//
		//
		CollectionUtils.addAll(selectedCasesIDs, selectedCasesArray);
		CollectionUtils.addAll(unselectedCasesIDs, unselectedCasesArray);
		CollectionUtils.addAll(hiddenSelectedCasesIDs, hiddenSelectedCasesArray);
		//
        List newUnselectedCases = this.extractCasesByAbsentID(hiddenSelectedCasesIDs, cases);        
        newUnselectedCases.addAll(this.extractCasesByPresentID(unselectedCasesIDs, cases));
		//
		//
		List newHiddenCases= this.extractCasesByPresentID(hiddenSelectedCasesIDs, cases);
		newHiddenCases= this.extractCasesByAbsentID(unselectedCasesIDs, newHiddenCases);
		//
		//
		List newSelectedCases= new LinkedList(newHiddenCases);
		//
		//
		//
		request.setAttribute("unselectedCases", newUnselectedCases);
		request.setAttribute("selectedCases", newSelectedCases);
		request.setAttribute("hiddenSelectedCases", newHiddenCases);
		destiny= mapping.findForward("selectionChanged");
		return destiny;
	}
	public ActionForward addedCases(
		String[] selectedCasesArray,
		String[] unselectedCasesArray,
		String[] hiddenSelectedCasesArray,
		List cases,
		HttpServletRequest request,
		ActionMapping mapping)
	{
		ActionForward destiny= null;
		LinkedList selectedCasesIDs= new LinkedList();
		LinkedList unselectedCasesIDs= new LinkedList();
		LinkedList hiddenSelectedCasesIDs= new LinkedList();
		//
		//
		CollectionUtils.addAll(selectedCasesIDs, selectedCasesArray);
		CollectionUtils.addAll(unselectedCasesIDs, unselectedCasesArray);
		CollectionUtils.addAll(hiddenSelectedCasesIDs, hiddenSelectedCasesArray);
		//
        List newSelectedCases= this.extractCasesByPresentID(hiddenSelectedCasesIDs, cases);        
        newSelectedCases.addAll(this.extractCasesByPresentID(selectedCasesIDs, cases));
		//
		//
		List newHiddenCases= new LinkedList(newSelectedCases);
		List newSelectedCasesIDs= new LinkedList(selectedCasesIDs);
		newSelectedCasesIDs.addAll(hiddenSelectedCasesIDs);
		//
		//
		List newUnselectedCases= this.extractCasesByAbsentID(newSelectedCasesIDs, cases);
		//
		//
		//
		request.setAttribute("unselectedCases", newUnselectedCases);
		request.setAttribute("selectedCases", newSelectedCases);
		request.setAttribute("hiddenSelectedCases", newHiddenCases);
		destiny= mapping.findForward("selectionChanged");
		return destiny;
	}
	public InfoSeminary readSeminaryById(IUserView userView, Integer id) throws FenixActionException
	{
		InfoSeminary seminary= null;
		try
		{
			Object[] argsReadSeminary= { id };
			GestorServicos gestor= GestorServicos.manager();
			seminary= (InfoSeminary) gestor.executar(userView, "Seminaries.GetSeminary", argsReadSeminary);
		}
		catch (Exception e)
		{
            throw new FenixActionException();
		}
		return seminary;
	}
	public InfoStudent readStudentByUserView(IUserView userView) throws FenixActionException
	{
		InfoStudent student= null;
		try
		{
			Object[] argsReadStudent= { userView.getUtilizador()};
			GestorServicos gestor= GestorServicos.manager();
			student= (InfoStudent) gestor.executar(userView, "ReadStudentByUsername", argsReadStudent);
		}
		catch (Exception e)
		{
            throw new FenixActionException();
		}
		return student;
	}
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		ActionForward destiny= null;
		HttpSession session= this.getSession(request);
		DynaActionForm selectCases= (DynaActionForm) form;
		String[] selectedCasesArray= (String[]) selectCases.get("selectedCases");
		String[] unselectedCasesArray= (String[]) selectCases.get("unselectedCases");
		String[] hiddenSelectedCasesArray= (String[]) selectCases.get("hiddenSelectedCases");
		String equivalencyIDString= request.getParameter("equivalencyID");
		String themeIDString= request.getParameter("themeID");
		String motivation= request.getParameter("motivation");
		String submition= request.getParameter("submition");
		IUserView userView= (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer equivalencyID= null;
		Integer themeID= null;
		if (equivalencyIDString == null)
			throw new FenixActionException(mapping.findForward("invalidQueryString"));
		try
		{
			equivalencyID= new Integer(equivalencyIDString);
			/*struts translates null values to the string "null". bad thing.*/
			if (themeIDString != null && !themeIDString.equals("null"))
			{
				themeID= new Integer(themeIDString);
			}
		}
		catch (Exception ex)
		{
			throw new FenixActionException(mapping.findForward("invalidQueryString"));
		}
		InfoEquivalency equivalency= null;
		List cases= null;
		try
		{
			Object[] argsReadEquivalency= { equivalencyID };
			GestorServicos gestor= GestorServicos.manager();
			equivalency=
				(InfoEquivalency) gestor.executar(userView, "Seminaries.GetEquivalency", argsReadEquivalency);
			if (themeID != null) // we want the cases of ONE theme
			{
				Object[] argsReadCases= { themeID };
				cases= (List) gestor.executar(userView, "Seminaries.GetCaseStudiesByThemeID", argsReadCases);
			}
			else // we want ALL the cases of the equivalency (its a "Completa" modality)
				{
				Object[] argsReadCases= { equivalencyID };
				cases= (List) gestor.executar(userView, "Seminaries.GetCaseStudiesByEquivalencyID", argsReadCases);
			}
		}
		catch (Exception e)
		{
            throw new FenixActionException();
		}
		if (submition.equals("Adicionar"))
		{
			destiny=
				this.addedCases(
					selectedCasesArray,
					unselectedCasesArray,
					hiddenSelectedCasesArray,
					cases,
					request,
					mapping);
			request.setAttribute("equivalency", equivalency);
		}
		else
			if (submition.equals("Remover"))
			{
				destiny=
					this.removedCases(
						selectedCasesArray,
						unselectedCasesArray,
						hiddenSelectedCasesArray,
						cases,
						request,
						mapping);
				request.setAttribute("equivalency", equivalency);
			}
			else
			{
                InfoTheme theme = null;
				InfoCandidacy infoCandidacy= new InfoCandidacy();
				infoCandidacy.setCurricularCourseIdInternal(equivalency.getCurricularCourse().getIdInternal());
				infoCandidacy.setModalityIdInternal(equivalency.getModality().getIdInternal());
				infoCandidacy.setSeminaryIdInternal(equivalency.getSeminaryIdInternal());
                infoCandidacy.setSeminaryName(equivalency.getSeminaryName());
				infoCandidacy.setStudentIdInternal(this.readStudentByUserView(userView).getIdInternal());
				infoCandidacy.setThemeIdInternal(themeID);
				infoCandidacy.setMotivation(motivation);
				LinkedList hiddenSelectedCasesIDs= new LinkedList();
				CollectionUtils.addAll(hiddenSelectedCasesIDs, hiddenSelectedCasesArray);
				List choosenCases= this.extractCasesByPresentID(hiddenSelectedCasesIDs, cases);
				int i= 0;
				List caseStudyChoices= new LinkedList();
				for (Iterator iterator= choosenCases.iterator(); iterator.hasNext(); i++)
				{
					InfoCaseStudy infoCaseStudy= (InfoCaseStudy) iterator.next();
					InfoCaseStudyChoice caseChoice= new InfoCaseStudyChoice();
					caseChoice.setCaseStudyIdInternal(infoCaseStudy.getIdInternal());
					caseChoice.setOrder(new Integer(i));
					caseStudyChoices.add(caseChoice);
				}
				infoCandidacy.setCaseStudyChoices(caseStudyChoices);
				try
				{
					Object[] argsWriteCandidacy= { infoCandidacy };
					GestorServicos gestor= GestorServicos.manager();
					gestor.executar(userView, "Seminaries.WriteCandidacy", argsWriteCandidacy);
                    Object[] argsReadTheme= { themeID };
                    theme = (InfoTheme) gestor.executar(userView, "Seminaries.GetThemeById", argsReadTheme);
				}
				catch (Exception e)
				{
                    throw new FenixActionException();
				}
                request.setAttribute("cases",choosenCases);                
                request.setAttribute("motivation",motivation);
                request.setAttribute("modalityName",equivalency.getModality().getName());
                request.setAttribute("theme",theme);
                request.setAttribute("seminaryName",infoCandidacy.getSeminaryName());
				destiny= mapping.findForward("candidacySubmited");
            }
		return destiny;
	}
}
