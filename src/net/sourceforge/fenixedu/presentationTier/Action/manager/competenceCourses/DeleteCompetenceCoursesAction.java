package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.DeleteCompetenceCourses;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class DeleteCompetenceCoursesAction extends FenixDispatchAction {

    public ActionForward deleteCompetenceCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	IUserView userView = UserView.getUser();
	DynaActionForm actionForm = (DynaActionForm) form;

	Integer[] competenceCoursesIDs = (Integer[]) actionForm.get("competenceCoursesIds");

	DeleteCompetenceCourses.run(competenceCoursesIDs);
	return mapping.findForward("showAllCompetenceCourses");
    }
}