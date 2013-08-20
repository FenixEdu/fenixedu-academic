package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.research.prizes.DeletePrize;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.components.state.ViewDestination;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.file.FileManagerException;

public class ResultsManagementAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String unitId = request.getParameter("unitId");
        if (unitId != null) {
            ResearchUnit unit = (ResearchUnit) AbstractDomainObject.fromExternalId(unitId);
            request.setAttribute("unit", unit);
        }
        return super.execute(mapping, form, request, response);
    }

    public ActionForward backToResult(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ResearchResult result = getResultFromRequest(request);
        if (result == null) {
            return backToResultList(mapping, form, request, response);
        }

        request.setAttribute("resultId", result.getExternalId());
        if (result instanceof ResearchResultPatent) {
            return mapping.findForward("editPatent");
        } else if (result instanceof ResearchResultPublication) {
            return mapping.findForward("viewEditPublication");
        }
        return null;
    }

    public ActionForward backToResultList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String resultType = (String) getFromRequest(request, "resultType");

        if (!(resultType == null || resultType.equals(""))) {
            if (resultType.compareTo(ResearchResultPatent.class.getSimpleName()) == 0) {
                return mapping.findForward("listPatents");
            }
            return mapping.findForward("ListPublications");
        }
        return null;
    }

    public ActionForward associatePrize(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ResearchResult result = getResultByIdFromRequest(request);
        request.setAttribute("publication", result);

        return mapping.findForward("associatePrize");

    }

    public ActionForward deletePrize(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        String prizeID = request.getParameter("oid");
        Prize prize = (Prize) AbstractDomainObject.fromExternalId(prizeID);
        if (prize.isDeletableByUser((getLoggedPerson(request)))) {
            try {
                DeletePrize.runDeletePrize(prize);
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }
        return associatePrize(mapping, form, request, response);
    }

    public ActionForward editPrize(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        String prizeID = request.getParameter("oid");
        Prize prize = (Prize) AbstractDomainObject.fromExternalId(prizeID);
        if (prize != null && prize.isEditableByUser(getLoggedPerson(request))) {
            request.setAttribute("prize", prize);
        }
        request.setAttribute("result", getResultByIdFromRequest(request));
        return mapping.findForward("editPrize");
    }

    protected ResearchResult getResultByIdFromRequest(HttpServletRequest request) {
        final String resultId = getFromRequest(request, "resultId").toString();

        if (resultId != null) {
            try {
                return ResearchResult.readByOid(resultId);
            } catch (DomainException e) {
                addMessage(request, e.getKey(), e.getArgs());
            }
        }
        return null;
    }

    public final ResearchResult getResultFromRequest(HttpServletRequest request) {
        ResearchResult result = null;
        try {
            result = getResultByIdFromRequest(request);
        } catch (Exception e) {
        }

        if (result == null) {
            try {
                final Object object = getRenderedObject(null);

                if (object instanceof ResearchResult) {
                    result = (ResearchResult) object;
                }
                if (object instanceof ResultPublicationBean) {
                    result = ResearchResult.readByOid(((ResultPublicationBean) object).getExternalId());
                }

            } catch (Exception e) {
            }
        }

        if (result != null) {
            request.setAttribute("result", result);
        }

        return result;
    }

    @Override
    public <RenderedObjectType> RenderedObjectType getRenderedObject(String id) {
        if (id == null || id.equals("")) {
            if (RenderUtils.getViewState() != null) {
                return (RenderedObjectType) RenderUtils.getViewState().getMetaObject().getObject();
            }
        } else {
            if (RenderUtils.getViewState(id) != null) {
                return (RenderedObjectType) RenderUtils.getViewState(id).getMetaObject().getObject();
            }
        }
        return null;
    }

    @Override
    public ActionForward processException(HttpServletRequest request, ActionMapping mapping, ActionForward input, Exception e) {
        if (e instanceof DomainException) {
            final DomainException ex = (DomainException) e;

            addMessage(request, ex.getKey(), ex.getArgs());

            if (RenderUtils.getViewState() != null) {
                ViewDestination destination = RenderUtils.getViewState().getDestination("exception");
                RenderUtils.invalidateViewState();
                if (destination != null) {
                    return destination.getActionForward();
                }
            }

        } else if (e instanceof FileManagerException) {
            addActionMessage(request, "label.communicationError");
            e.printStackTrace();
        } else {

            addMessage(request, e.getMessage());
            e.printStackTrace();

        }

        return input;
    }

    private void addMessage(HttpServletRequest request, String key, String... args) {
        ActionMessages messages = getMessages(request);

        if (messages == null) {
            messages = new ActionMessages();
        }

        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, args));
        saveMessages(request, messages);
    }
}
