/*
 * Created on 30/Jun/2003
 *
 * 
 */
package ServidorApresentacao.Action.sop;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoSiteClassesComponent;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.base.FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 * 
 * 30/Jun/2003 fenix-branch ServidorApresentacao.Action.sop
 *  
 */
public class AddShiftsToClassesDA extends
        FenixShiftAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward showClasses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        if (session != null) {

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            //DynaActionForm shiftForm = (DynaActionForm) form;

            //Integer shiftOID = new Integer((String)
            // shiftForm.get("shiftOID"));
            Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));
            Object serviceArgs[] = { shiftOID };
            SiteView siteView = null;
            try {
                siteView = (SiteView) ServiceUtils.executeService(userView,
                        "ReadAvailableClassesForShift", serviceArgs);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            Collections.sort(((InfoSiteClassesComponent) siteView.getComponent()).getInfoClasses(),
                    new BeanComparator("nome"));

            request.setAttribute("siteView", siteView);

            return mapping.findForward("showClasses");
        }
        throw new FenixActionException();

    }

    public ActionForward addShiftToClasses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            DynaActionForm classesForm = (DynaActionForm) form;
            IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);

            String[] classesList = (String[]) classesForm.get("classesList");
            //InfoShift infoShift = (InfoShift)
            // sessao.getAttribute("infoTurno");
            //Integer keyShift = infoShift.getIdInternal();
            Integer shiftOID = new Integer(request.getParameter(SessionConstants.SHIFT_OID));
            Object argsAdicionarAula[] = { shiftOID, classesList };

            try {
                ServiceUtils.executeService(userView, "AddShiftToClasses", argsAdicionarAula);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            return mapping.findForward("sucess");
        }
        throw new FenixActionException();

    }
}