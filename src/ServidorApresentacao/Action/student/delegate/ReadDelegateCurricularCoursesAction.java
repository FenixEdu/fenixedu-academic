/*
 * Created on Feb 19, 2004
 *  
 */
package ServidorApresentacao.Action.student.delegate;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoCurricularCourse;
import DataBeans.student.InfoDelegate;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public class ReadDelegateCurricularCoursesAction extends FenixAction
{
    /*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm actionForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);

        HashMap parameters = new HashMap();
        parameters.put("user", userView.getUtilizador());
        Object args[] = { parameters };
        List infoCurricularCourses =
            (List) ServiceUtils.executeService(userView, "ReadDelegateCurricularCourses", args);

        Collections.sort(infoCurricularCourses, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                InfoCurricularCourse infoCurricularCourse1 = (InfoCurricularCourse) o1;
                InfoCurricularCourse infoCurricularCourse2 = (InfoCurricularCourse) o2;
                return infoCurricularCourse1.getName().compareTo(infoCurricularCourse2.getName());
            }
        });

        InfoDelegate infoDelegate =
            (InfoDelegate) ServiceUtils.executeService(userView, "ReadDelegate", args);
        
        request.setAttribute("infoCurricularCourses", infoCurricularCourses);
        request.setAttribute("infoDelegate", infoDelegate);

        return mapping.findForward("show-curricular-courses");
    }

}
