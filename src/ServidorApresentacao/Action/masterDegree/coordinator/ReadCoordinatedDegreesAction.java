/*
 * 
 * Created on 27 of March de 2003
 *
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

public class ReadCoordinatedDegreesAction extends ServidorApresentacao.Action.base.FenixAction
{

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);
        if (session != null)
        {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            GestorServicos gestor = GestorServicos.manager();

            Object args[] = new Object[1];
            args[0] = userView;
            List degrees = null;
            List candidates = new ArrayList();
            try
            {
                degrees = (List) gestor.executar(userView, "ReadCoordinatedDegrees", args);

            } catch (FenixServiceException e)
            {
                throw new FenixActionException(e);
            }

            Iterator iterator = degrees.iterator();

            while (iterator.hasNext())
            {
                InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
                try
                {
                    Object argsTemp[] = { infoExecutionDegree };

                    List result = (List) gestor.executar(userView, "ReadDegreeCandidates", argsTemp);

                    candidates.add(new Integer(result.size()));

                } catch (FenixServiceException e)
                {
                    throw new FenixActionException(e);
                }
            }
            if (degrees.size() == 1)
            {
                session.setAttribute(SessionConstants.MASTER_DEGREE, degrees.get(0));
                session.setAttribute(
                    SessionConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT,
                    candidates.get(0));
                return mapping.findForward("Success");
            }
            session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATES_AMMOUNT, candidates);
            session.setAttribute(SessionConstants.MASTER_DEGREE_LIST, degrees);
            return mapping.findForward("ChooseDegree");

        } else
            throw new Exception();
    }

}
