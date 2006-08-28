/*
 * Created on Sep 3, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ViewPhotoAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = getUserView(request);
        Integer personId = new Integer(request.getParameter("personCode"));

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