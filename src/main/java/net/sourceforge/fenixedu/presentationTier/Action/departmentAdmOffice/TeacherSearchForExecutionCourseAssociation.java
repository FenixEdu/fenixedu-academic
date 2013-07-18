package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.FindPersonService;
import net.sourceforge.fenixedu.presentationTier.Action.framework.SearchAction;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;

public class TeacherSearchForExecutionCourseAssociation extends SearchAction {

    @Override
    protected Collection searchIt(ActionForm form, HttpServletRequest request) throws Exception {
        return new FindPersonService().run(new HashMap(BeanUtils.describe(form)));
    }

    @Override
    protected String getObjectAttribute() {
        return "infoPerson";
    }

    @Override
    protected String getListAttribute() {
        return "infoPersonList";
    }

    @Override
    protected String getNotFoundMessageKey() {
        return "errors.teacher.not.found";
    }

    @Override
    protected String getDefaultSortBy() {
        return null;
    }

}
