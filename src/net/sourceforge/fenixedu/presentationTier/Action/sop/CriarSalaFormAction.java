package net.sourceforge.fenixedu.presentationTier.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author tfc130
 */
public class CriarSalaFormAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm criarSalaForm = (DynaActionForm) form;

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            IUserView userView = (IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
            Object argsCriarSala[] = { new InfoRoomEditor((String) criarSalaForm.get("name"),
                    (String) criarSalaForm.get("building"), new Integer((String) criarSalaForm
                            .get("floor")),
                    new TipoSala(new Integer((String) criarSalaForm.get("type"))), new Integer(
                            (String) criarSalaForm.get("capacityNormal")), new Integer(
                            (String) criarSalaForm.get("capacityExame"))) };

            try {
                ServiceManagerServiceFactory.executeService(userView, "CriarSala", argsCriarSala);
            } catch (ExistingServiceException e) {
                throw new ExistingActionException("A OldRoom", e);
            }
            return mapping.findForward("Sucesso");
        }
        throw new Exception();

    }
}