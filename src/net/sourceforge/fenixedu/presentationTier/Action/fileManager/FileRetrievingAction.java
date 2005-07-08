/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.fileManager;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorApresentacao.Action.scientificCouncil
 *  
 */
public class FileRetrievingAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        String fileName = request.getParameter("fileName");
        String itemCodeString = request.getParameter("itemCode");
        Integer itemCode = new Integer(itemCodeString);
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object[] args = { itemCode, fileName };
        FileSuportObject file = null;
        try {
            file = (FileSuportObject) ServiceUtils.executeService(userView, "RetrieveItemFile", args);
            response.setHeader("Content-disposition", "attachment;filename=" + file.getFileName());
            response.setContentType(file.getContentType());
            DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            dos.write(file.getContent());
            dos.close();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        } catch (IOException e) {

        }

        return null;

    }

}