/*
 * Created on 29/Mai/2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.teacher.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * @author jpvl
 * 
 * @deprecated I think this is not used anymore
 */
public class TeacherCreditsDispatchAction extends DispatchAction
{

    public ActionForward prepare(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        //		IUserView userView = SessionUtils.getUserView(request);
        //		DynaActionForm creditsForm = (DynaActionForm) form;
        //		Integer teacherOID;
        //		try {
        //			teacherOID = new Integer((String) creditsForm.get("teacherOID"));
        //		} catch (NumberFormatException e) {
        //			InfoTeacher infoTeacher =
        //				(InfoTeacher) request.getAttribute("infoTeacher");
        //			teacherOID = infoTeacher.getIdInternal();
        //			creditsForm.set("teacherOID", teacherOID.toString());
        //		}
        //		Object[] args = { teacherOID };
        //		CreditsView creditsView =
        //			(CreditsView) ServiceUtils.executeService(
        //				userView,
        //				"ReadCreditsTeacher",
        //				args);
        //
        //		InfoCredits infoCredits = creditsView.getInfoCredits();
        //
        //		DynaActionForm creditsTeacherForm = (DynaActionForm) form;
        //		Integer tfcStudentsNumber = infoCredits.getTfcStudentsNumber();
        //		if (tfcStudentsNumber != null && tfcStudentsNumber.intValue() != 0)
        // {
        //			creditsTeacherForm.set(
        //				"tfcStudentsNumber",
        //				infoCredits.getTfcStudentsNumber().toString());
        //			if (infoCredits.getAdditionalCredits() != null
        //				&& infoCredits.getAdditionalCredits().doubleValue() != 0)
        //				creditsTeacherForm.set(
        //					"additionalCredits",
        //					infoCredits.getAdditionalCredits().toString());
        //			creditsTeacherForm.set(
        //				"additionalCreditsJustification",
        //				infoCredits.getAdditionalCreditsJustification());
        //
        //		}
        //		request.setAttribute("creditsView", creditsView);

        return mapping.findForward("showForm");
    }
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return @throws
     *         Exception
     */
    public ActionForward processForm(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
      

        return mapping.getInputForward();
    }

}
