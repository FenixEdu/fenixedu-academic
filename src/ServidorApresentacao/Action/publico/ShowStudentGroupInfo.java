/*
 * Created on 10/Set/2003, 18:36:21
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.publico;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import DataBeans.StudentGroupAttendacyInformation;
import Dominio.IAula;
import Dominio.ICurricularCourse;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixActionException;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 10/Set/2003, 18:36:21
 * 
 */
public class ShowStudentGroupInfo extends Action
{
    public String buildInfo(Integer curricularCourseID, String username, String password)
    {
        String result = new String();
        if (username == null)
            return new String();
        else
        {
            GestorServicos gestor = GestorServicos.manager();
            try
            {
                Object argsAutenticacao[] = { username, password, "" };
                IUserView userView = (IUserView) gestor.executar(null, "Autenticacao", argsAutenticacao);
                Object[] argsReadAttendacyInfo = { curricularCourseID, username };
                StudentGroupAttendacyInformation info =
                    (StudentGroupAttendacyInformation) gestor.executar(
                        userView,
                        "publico.GetProjectGroupAttendantsByExecutionCourseIDANDStudentUsername",
                        argsReadAttendacyInfo);
                if (info != null)
                {
                    for (Iterator iter = info.getDegreesNames().iterator(); iter.hasNext();)
                    {
                        ICurricularCourse element = (ICurricularCourse) iter.next();
                        result = element.getDegreeCurricularPlan().getName() + "\n";
                    }
                    for (Iterator iterator = info.getGroupAttends().iterator(); iterator.hasNext();)
                    {
                        IStudentGroupAttend element = (IStudentGroupAttend) iterator.next();
                        result += element.getAttend().getAluno().getNumber() + "\n";
                    }
                    result += info.getGroupNumber() + "\n" + info.getShiftName() + "\n";
                    for (Iterator iterator = info.getLessons().iterator(); iterator.hasNext();)
                    {
                        IAula lesson = (IAula) iterator.next();
                        result += lesson.getDiaSemana().toString()
                            + " "
                            + lesson.getInicio().get(Calendar.HOUR_OF_DAY)
                            + ":"
                            + lesson.getInicio().get(Calendar.MINUTE)
                            + "\n";
                    }
                } else
                    return new String();
            } catch (Exception e)
            {
                result = "-1";
            }
        }

        return result;
    }

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        String studentUsername = request.getParameter("username");
        String studentPassword = request.getParameter("password");
        //these are the ids of the exectuion courses. TODO: get this working with any execution courses read by some property
        Integer[] coursesIds = { new Integer(34811), new Integer(34661), new Integer(34950)};
        String result = new String();
        for (int i = 0; i < coursesIds.length; i++)
        {
            result += this.buildInfo(coursesIds[i], studentUsername, studentPassword);
        }
        if (result.equals(""))
            result = "-1";
        try
        {
            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("text/plain");
            writer.print(result);
            writer.flush();
            response.flushBuffer();
        } catch (IOException ex)
        {
            throw new FenixActionException();
        }

        return null;
    }
}