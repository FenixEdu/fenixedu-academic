/**
 * 
 * Created on 27 of March de 2003
 * 
 * 
 * Autores : -Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 * 
 * modified by Fernanda Quitério
 *  
 */

package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

public class CoordinatedDegreeInfo extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = SessionUtils.getUserView(request);
            InfoExecutionYear currentExecutionYear = null;
            //gets the idInternal for the degreeCurricularCourse from the
            // request
            Integer degreeCurricularPlanID = findDegreeCurricularPlanID(request);

            List result = null;

            Object argsTemp[] = { degreeCurricularPlanID };

            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadExecutionDegreesByDegreeCurricularPlanID", argsTemp);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            try {
                currentExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(
                        userView, "ReadCurrentExecutionYear", null);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            InfoExecutionDegree infoExecutionDegree = chooseExecutionDegree(result, currentExecutionYear);

            argsTemp[0] = infoExecutionDegree.getIdInternal();

            // TODO remove this service invocation
            try {
                infoExecutionDegree = (InfoExecutionDegree) ServiceManagerServiceFactory.executeService(
                        userView, "ReadExecutionDegree", argsTemp);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            // Put the selected Degree in the request
            /*
             * InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree)
             * degreeList .get(choosenDegreePosition.intValue());
             */

            // request.setAttribute("infoExecutionDegree", infoExecutionDegree);
            session.setAttribute(SessionConstants.MASTER_DEGREE, infoExecutionDegree);

            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

            argsTemp[0] = degreeCurricularPlanID;
            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadDegreeCandidates", argsTemp);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT, new Integer(result
                    .size()));
            return mapping.findForward("Success");
        }
        throw new Exception();
    }

    /**
     * @param request
     * @return
     */
    private Integer findDegreeCurricularPlanID(HttpServletRequest request) {
        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        } else {
            degreeCurricularPlanID = Integer.valueOf((String) request
                    .getAttribute("degreeCurricularPlanID"));
        }
        return degreeCurricularPlanID;
    }

    private InfoExecutionDegree chooseExecutionDegree(List infoExecutionDegrees,
            InfoExecutionYear currentExecutionYear) {

        Iterator it = infoExecutionDegrees.iterator();

        Collections.sort(infoExecutionDegrees, new Comparator() {
            public int compare(Object obj1, Object obj2) {
                InfoExecutionDegree infoExecutionDegree1 = (InfoExecutionDegree) obj1;
                InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) obj2;

                if (infoExecutionDegree1.getInfoExecutionYear().getBeginDate().before(
                        infoExecutionDegree2.getInfoExecutionYear().getBeginDate())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        while (it.hasNext()) {

            InfoExecutionDegree temp = (InfoExecutionDegree) it.next();

            if (currentExecutionYear.equals((temp).getInfoExecutionYear())) {
                return temp;
            }
        }

        it = infoExecutionDegrees.iterator();
        if (it.hasNext()) {
            //((InfoExecutionDegree) it.next()).toString();
            return (InfoExecutionDegree) it.next();
        } else
            return null;
    }

}

