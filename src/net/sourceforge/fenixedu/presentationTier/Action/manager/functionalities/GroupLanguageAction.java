package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilderRegistry;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.utl.fenix.utils.Pair;

public class GroupLanguageAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Pair<Class, GroupBuilder>> registeredBuilders = GroupBuilderRegistry.getRegisteredBuilders();
        
        List<GroupBuilderBean> result = new ArrayList<GroupBuilderBean>();
        for (String name : registeredBuilders.keySet()) {
            result.add(new GroupBuilderBean(name, registeredBuilders.get(name).getValue()));
        }
        
        Collections.sort(result);
        
        request.setAttribute("builders", result);
        return mapping.findForward("show");
    }

}
