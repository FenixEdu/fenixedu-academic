/*
 * Created on Sep 3, 2004
 *
 */
package ServidorApresentacao.Action.person;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import fileSuport.FileSuportObject;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ViewPhotoAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer personId = new Integer((String) request.getParameter("personCode"));

        Object[] args = { personId };
        FileSuportObject file = null;
        try {
            file = (FileSuportObject) ServiceUtils.executeService(userView, "RetrievePhoto", args);

            if (file != null) {
                response.setHeader("Content-disposition", "attachment;filename=" + file.getFileName());
                response.setContentType(file.getContentType());
                DataOutputStream dos = new DataOutputStream(response.getOutputStream());
                dos.write(file.getContent());
                dos.close();
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        } catch (IOException e) {

        }

        return null;

    }
}