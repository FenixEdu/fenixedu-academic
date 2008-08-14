package net.sourceforge.fenixedu.presentationTier.Action.manager.teachersManagement;

import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class TeacherCategoriesManagement extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Set<Category> sortedCategories = new TreeSet<Category>(Category.readTeacherCategories());

	request.setAttribute("categories", sortedCategories);

	return mapping.findForward("editCategories");
    }

}
