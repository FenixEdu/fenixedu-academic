/*
 * Created on 1:42:45 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.thoughtworks.xstream.XStream;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz </a>
 * 
 * Created at 1:42:45 PM, Mar 11, 2005
 */
public class StudentInfoByUsername extends FenixAction
{

    private String studentUsername;
    private String studentPassword;
    private String format;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException
    {

        this.readRequestInformation(request);

        String result = new String();
        try
        {
            authenticate(this.studentUsername, this.studentPassword);
            Collection students = this.readInformation(this.studentUsername);
            result = this.buildInfo(students);
            if (result.equals(""))
                result = "-1";
        }
        catch (Exception e)
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<error><cause>");
            buffer.append(e.getMessage());
            buffer.append("</cause></error>");
            result = buffer.toString();
        }

        try
        {
            sendAnswer(response, result);
        }
        catch (IOException ex)
        {
            throw new FenixActionException();
        }

        return null;
    }

    /**
     * @param studentUsername2
     * @return
     * @throws FenixServiceException
     * @throws FenixFilterException
     */
    private Collection readInformation(String username) throws FenixFilterException,
            FenixServiceException
    {
        Object args[] = { username };
        return (Collection) ServiceManagerServiceFactory.executeService(null, "ReadStudentExternalInformation", args); //$NON-NLS-1$


    }

    private String buildInfo(Collection students)
    {
        XStream xstream = new XStream();
        return xstream.toXML(students);
    }

    private void readRequestInformation(HttpServletRequest request)
    {
        this.studentUsername = request.getParameter("username"); //$NON-NLS-1$
        this.studentPassword = request.getParameter("password"); //$NON-NLS-1$     
    }


    private IUserView authenticate(String username, String password) throws FenixServiceException,
            FenixFilterException
    {
        Object argsAutenticacao[] = { username, password, "" };
        IUserView userView = (IUserView) ServiceManagerServiceFactory.executeService(null,
                "Autenticacao", argsAutenticacao); //$NON-NLS-1$

        return userView;
    }

    private void sendAnswer(HttpServletResponse response, String result) throws IOException
    {
        ServletOutputStream writer = response.getOutputStream();
        response.setContentType("text/plain");
        writer.print(result);
        writer.flush();
        response.flushBuffer();
    }
}
