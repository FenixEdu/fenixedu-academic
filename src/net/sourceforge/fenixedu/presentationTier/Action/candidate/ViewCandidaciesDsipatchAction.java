/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.candidate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.CandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ViewCandidaciesDsipatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("view");
    }

    public ActionForward viewDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Integer candidacyID = Integer.valueOf(request.getParameter("candidacyID"));
        Candidacy candidacy = RootDomainObject.getInstance().readCandidacyByOID(candidacyID);
        request.setAttribute("candidacy", candidacy);

        List<CandidacyDocumentUploadBean> candidacyDocuments = new ArrayList<CandidacyDocumentUploadBean>();
        for (CandidacyDocument candidacyDocument : candidacy.getCandidacyDocuments()) {
            candidacyDocuments.add(new CandidacyDocumentUploadBean(candidacyDocument));
        }
        request.setAttribute("candidacyDocuments", candidacyDocuments);

        return mapping.findForward("viewDetail");
    }

    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        List<CandidacyDocumentUploadBean> beans = (List<CandidacyDocumentUploadBean>) RenderUtils
                .getViewState("candidacyDocuments").getMetaObject().getObject();

        Object[] args = { beans };
        ServiceUtils.executeService(SessionUtils.getUserView(request), "SaveCandidacyDocumentFiles", args);
        
        return null;
    }

}
