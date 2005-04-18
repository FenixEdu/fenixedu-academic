/*
 * Created on 1:42:45 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

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
    private String xslt;


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException
    {

        this.readRequestInformation(request);

        String result = new String();
        try
        {
            final String requestURL = request.getRequestURL().toString();
            authenticate(this.studentUsername, this.studentPassword, requestURL);
            Collection students = this.readInformation(this.studentUsername);
            result = this.buildInfo(students);
            result = this.transformResult(result);
            if (result.equals(""))
                result = "-1";
        } catch (Exception e)
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
        } catch (IOException ex)
        {
            throw new FenixActionException();
        }

        return null;
    }

    private String transformResult(String result)
    {
        String resultString = result;
        try
        {
            InputStream xsltStream = this.getClass().getResourceAsStream(this.getStyleFilename());
            StringWriter sw = new StringWriter();
            StringReader sr = new StringReader(result);

            Result transformationResult = new StreamResult(sw);
            Source source = new StreamSource(sr);
            javax.xml.transform.TransformerFactory transFact = javax.xml.transform.TransformerFactory
                    .newInstance();
            javax.xml.transform.Transformer trans = transFact
                    .newTransformer(new javax.xml.transform.stream.StreamSource(xsltStream));
            trans.setOutputProperty("encoding", "ISO-8859-1");
            trans.transform(source, transformationResult);
            resultString = sw.toString();
        } catch (Exception e)
        {
            // let us proceed with default layout
            resultString = result;
        }

        return resultString;
    }

    private String getStyleFilename()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("/xslt/external/studentInfoByUsername/").append(this.xslt).append(".xslt");
        
        return buffer.toString();
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
        Object args[] =
        { username };
        return (Collection) ServiceManagerServiceFactory.executeService(null,
                "ReadStudentExternalInformation", args);


    }

    private String buildInfo(Collection students)
    {
        XStream xstream = new XStream();
        return xstream.toXML(students);
    }

    private void readRequestInformation(HttpServletRequest request)
    {
        this.studentUsername = request.getParameter("username");
        this.studentPassword = request.getParameter("password");
        this.xslt = request.getParameter("style");
    }


    private IUserView authenticate(String username, String password, String requestURL)
            throws FenixServiceException, FenixFilterException
    {
        Object argsAutenticacao[] =
        { username, password, "", requestURL };
        IUserView userView = (IUserView) ServiceManagerServiceFactory.executeService(null,
                "Autenticacao", argsAutenticacao);

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
