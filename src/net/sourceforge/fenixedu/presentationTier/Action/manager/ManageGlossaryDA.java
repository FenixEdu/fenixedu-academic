/*
 * Created on 2004/08/27
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Crus
 */
public class ManageGlossaryDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        List infoGlossaryEntries = (List) ServiceUtils.executeService(userView, "ReadGlossaryEntries",
                null);
        Collections.sort(infoGlossaryEntries, new BeanComparator("term"));
        request.setAttribute("infoGlossaryEntries", infoGlossaryEntries);

        return mapping.findForward("Manage");
    }

    public ActionForward createGlossaryEntry(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String term = (String) dynaActionForm.get("term");
        String definition = (String) dynaActionForm.get("definition");

        InfoGlossaryEntry infoGlossaryEntry = new InfoGlossaryEntry();
        infoGlossaryEntry.setTerm(term);
        infoGlossaryEntry.setDefinition(definition);

        IUserView userView = SessionUtils.getUserView(request);
        Object[] args = { infoGlossaryEntry };
        ServiceUtils.executeService(userView, "CreateGlossaryEntry", args);

        return mapping.getInputForward();
    }

    public ActionForward deleteGlossaryEntry(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String entryIdString = request.getParameter("entryId");

        if (entryIdString != null && StringUtils.isNumeric(entryIdString)) {
            Integer entryId = new Integer(entryIdString);
            IUserView userView = SessionUtils.getUserView(request);
            Object[] args = { entryId };
            ServiceUtils.executeService(userView, "DeleteGlossaryEntry", args);
        }

        return mapping.getInputForward();
    }

}