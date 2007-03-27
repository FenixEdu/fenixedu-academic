package net.sourceforge.fenixedu.presentationTier.Action.research;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ScientificJournalsManagement extends FenixDispatchAction {

    public ActionForward showScientificJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
	
	String journalId  = request.getParameter("journalId");
	if(journalId!=null) {
	    ScientificJournal scientificJournal = rootDomainObject.readScientificJournalByOID(Integer.valueOf(journalId));
	    request.setAttribute("journal",scientificJournal);
	}
	
	return mapping.findForward("showJournal");
    }
}
