/*
 * Created on 11:36:06,28/Fev/2005
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
 * Created at 11:36:06, 28/Fev/2005
 */
public class InfoGroupsByExecutionCourse extends FenixAction
{
    private final String SEPARATOR = ","; //$NON-NLS-1$
    private String studentUsername;
    private String studentPassword;
    private Integer[] executionCourseIds;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException
    {

        this.readRequestInformation(request);

        StringBuffer buffer = new StringBuffer();
        try
        {

            IUserView userView = authenticate(this.studentUsername, this.studentPassword);
            for (int i = 0; i < this.executionCourseIds.length; i++)
            {
                buffer.append(this.buildInfo(this.executionCourseIds[i], userView));
            }
            if (buffer.toString().equals(""))
                buffer = new StringBuffer("-1");

        }
        catch (Exception e)
        {
            buffer = new StringBuffer();
            buffer = new StringBuffer();
            buffer.append("<error><cause>");
            buffer.append(e.getMessage());
            buffer.append("</cause></error>");
        }

        try
        {
            sendAnswer(response, buffer.toString());
        }
        catch (IOException ex)
        {
            throw new FenixActionException();
        }


        return null;
    }

    private String buildInfo(Integer integer, IUserView userView) throws FenixFilterException,
            FenixServiceException
    {
        Object[] args = { integer, userView.getUtilizador() };
        Collection info = (Collection) ServiceManagerServiceFactory.executeService(userView,
                "ReadStudentGroupsExternalInformationByExecutionCourseIDAndStudentUsername", args); //$NON-NLS-1$
        XStream xstream = new XStream();
        String data = xstream.toXML(info);

        return data;
    }

    /**
     * @param response
     * @param result
     * @throws IOException
     */
    private void sendAnswer(HttpServletResponse response, String result) throws IOException
    {
        ServletOutputStream writer = response.getOutputStream();
        response.setContentType("text/plain");
        writer.print(result);
        writer.flush();
        response.flushBuffer();
    }

    /**
     * @param request
     * 
     */
    private void readRequestInformation(HttpServletRequest request)
    {
        this.studentUsername = request.getParameter("username"); //$NON-NLS-1$
        this.studentPassword = request.getParameter("password"); //$NON-NLS-1$
        String idsString = request.getParameter("ids"); //$NON-NLS-1$
        this.executionCourseIds = buildIdsArray(idsString);
    }

    private IUserView authenticate(String username, String password) throws FenixServiceException,
            FenixFilterException
    {
        Object argsAutenticacao[] = { username, password, "" };
        IUserView userView = (IUserView) ServiceManagerServiceFactory.executeService(null,
                "Autenticacao", argsAutenticacao); //$NON-NLS-1$

        return userView;
    }

    private Integer[] buildIdsArray(String idsString)
    {
        Integer[] coursesIds = { new Integer(34811), new Integer(34661), new Integer(34950) };
        if (idsString != null)
        {
            String[] ids = idsString.split(this.SEPARATOR);
            coursesIds = new Integer[ids.length];
            for (int i = 0; i < ids.length; i++)
                coursesIds[i] = new Integer(ids[i]);
        }
        return coursesIds;
    }
}
