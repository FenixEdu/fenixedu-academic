/*
 * Created on 31/Jul/2003, 19:23:52
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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoStudent;
import DataBeans.Seminaries.InfoEquivalency;
import DataBeans.Seminaries.InfoSeminary;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 31/Jul/2003, 19:23:52
 * 
 */
public class ShowCandidacyOptions extends FenixAction
{
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        HttpSession session = this.getSession(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String seminaryIDString = request.getParameter("objectCode");
        Integer seminaryID;
        if (seminaryIDString == null)
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        try
        {
            seminaryID = new Integer(seminaryIDString);
        } catch (Exception ex)
        {
            throw new FenixActionException(mapping.findForward("invalidQueryString"));
        }
        InfoSeminary seminary = null;
        InfoStudent student = null;
        List disciplines = null;
        ActionForward destiny = null;
        try
        {
            Object[] argsReadSeminary = { seminaryID };
            Object[] argsReadStudent = { userView.getUtilizador()};
            GestorServicos gestor = GestorServicos.manager();
            seminary =
                (InfoSeminary) gestor.executar(userView, "Seminaries.GetSeminary", argsReadSeminary);
            student = (InfoStudent) gestor.executar(userView, "ReadStudentByUsername", argsReadStudent);
            Object[] ReadCurricularCoursesByUsername = { userView.getUtilizador()};
            disciplines =
                (List) gestor.executar(
                    userView,
                    "student.ReadCurricularCoursesByUsername",
                    ReadCurricularCoursesByUsername);
            List avaliableEquivalencies = new LinkedList();
            for (Iterator iterator = disciplines.iterator(); iterator.hasNext();)
            {
                InfoCurricularCourse curricularCourse = (InfoCurricularCourse) iterator.next();
                for (Iterator equivalencyIterator = seminary.getEquivalencies().iterator();
                    equivalencyIterator.hasNext();
                    )
                {
                    InfoEquivalency equivalency = (InfoEquivalency) equivalencyIterator.next();
                    if (equivalency
                        .getCurricularCourseIdInternal()
                        .equals(curricularCourse.getIdInternal()))
                    {
                        avaliableEquivalencies.add(equivalency);
                        break;
                    }
                }
            }
            seminary.setEquivalencies(avaliableEquivalencies);
            Object[] argsReadCandidacies = { student.getIdInternal()};
            List candidacies =
                (List) gestor.executar(
                    userView,
                    "Seminaries.GetCandidaciesByStudentID",
                    argsReadCandidacies);
            if (candidacies.size() >= seminary.getAllowedCandidaciesPerStudent().intValue())
            {
                ActionErrors actionErrors = new ActionErrors();

                ActionError actionError =
                    new ActionError(
                        "error.seminaries.candidaciesLimitReached",
                        seminary.getAllowedCandidaciesPerStudent());
                actionErrors.add("error.seminaries.candidaciesLimitReached", actionError);
                saveErrors(request, actionErrors);
                destiny = mapping.findForward("candidaciesLimitReached");
            } else
            {
                destiny = mapping.findForward("showCandidacyOptions");
                request.setAttribute("seminary", seminary);
            }

        } catch (Exception e)
        {
            throw new FenixActionException();
        }

        return destiny;
    }
}
