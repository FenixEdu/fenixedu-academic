/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.department;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class ReadTeacherProfessorshipsAction extends Action
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *          org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 *          javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        DynaActionForm oidForm = (DynaActionForm) form;
        Integer teacherIdInternal = infoTeacher != null ? infoTeacher.getIdInternal() : new Integer(
                oidForm.get("idInternal").toString());

        List detailedInfoProfessorshipList = (List) ServiceUtils.executeService(userView,
                "ReadDetailedTeacherProfessorshipsByExecutionPeriod", new Object[]{teacherIdInternal,
                    null});

        Comparator nameComparator = new BeanComparator("infoProfessorship.infoExecutionCourse.nome");
        Collections.sort(detailedInfoProfessorshipList, nameComparator);

        request.setAttribute("detailedProfessorshipList", detailedInfoProfessorshipList);

        return mapping.findForward("list-professorships");
    }

}