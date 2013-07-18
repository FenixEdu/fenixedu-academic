/*
 * Created on 10/Set/2003, 18:36:21 changed on 4/Jan/2004, 19:45:11 (generalize
 * for any execution course) By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl
 * [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher.ReadTeacherByUsername;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 10/Set/2003, 18:36:21
 * 
 */
public class GetEncryptedPassword extends Action {

    /**
     * error codes: 1-> service unavailable 2-> input data error 3-> no such
     * user
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
             {
        String username = request.getParameter("login");
        String timestamp = request.getParameter("timestamp");
        String url = request.getParameter("url");

        InfoTeacher infoTeacher = null;
        url = url + "?";
        Integer error = new Integer(0);
        if (username.equals("") || timestamp.equals("") || url.equals("?")) {
            error = new Integer(2);
        } else {

            infoTeacher = ReadTeacherByUsername.run(username);
        }
        if (infoTeacher == null) {
            error = new Integer(3);
        } else {
            url = url + "encrypted_password=" + infoTeacher.getInfoPerson().getPassword() + "&";

        }
        url = url + "timestamp=" + timestamp + "&";
        url = url + "error=" + error.toString();

        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
            // ignored exception
        }
        return null;
    }
}