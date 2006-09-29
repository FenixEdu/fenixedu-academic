package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ConferenceArticlesBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.ResultsManagementAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultPublicationsManagementDispatchAction extends ResultsManagementAction {

    public ActionForward listPublications(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        setRequestAttributesToList(request);
        return mapping.findForward("ListPublications");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ResultPublication publication = (ResultPublication) getResultFromRequest(request);

        setRequestAttributes(request, publication);
        return mapping.findForward("ViewEditPublication");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject(null);

        if (publicationBean == null)
        {
            ResultPublicationType type = ResultPublicationType.getDefaultType();
            publicationBean = ResultPublicationBean.getBeanToCreate(type);

            publicationBean.setPerson(getLoggedPerson(request));
        }
        request.setAttribute("publicationBean", publicationBean);
        return mapping.findForward("PreparedToCreate");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject(null);
        ResultPublication publication = null;

        if (getFromRequest(request, "confirm") != null) {
            if (!checkBean(request, bean))
                return mapping.findForward("PreparedToCreate");

            try {
                final Object[] args = { bean };
                publication = (ResultPublication) executeService(request, "CreateResultPublication",
                        args);
            } catch (DomainException ex) {
                addActionMessage(request, ex.getKey());
                request.setAttribute("publicationBean", bean);
                return mapping.findForward("PreparedToCreate");
            } catch (Exception ex) {
                return listPublications(mapping, form, request, response);
            }
        } else {
            return listPublications(mapping, form, request, response);
        }

        request.setAttribute("resultId", publication.getIdInternal());
        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject("publicationData");

        if (bean == null) {
            ResultPublication publication = (ResultPublication) getResultFromRequest(request);
            bean = ResultPublicationBean.getBeanToEdit(publication);
            bean.setPerson(getLoggedPerson(request));
        }

        verifyMessageForTypeChange(request);

        request.setAttribute("publicationBean", bean);
        return mapping.findForward("PreparedToEdit");
    }

    public ActionForward editData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject(null);
        Result publicationChanged = Result.readByOid(bean.getIdInternal());

        RenderUtils.invalidateViewState();

        if (getFromRequest(request, "confirm") != null) {
            if (!checkBean(request, bean)) {
                return mapping.findForward("PreparedToEdit");
            }
            try {
                final Object[] args = { bean };
                publicationChanged = (ResultPublication) executeService(request,
                        "EditResultPublication", args);
            } catch (DomainException ex) {
                addActionMessage(request, ex.getKey());
                request.setAttribute("publicationBean", bean);
                return mapping.findForward("PreparedToEdit");
            } catch (Exception ex) {
                return listPublications(mapping, form, request, response);
            }
        } else {
            if (publicationChanged instanceof Unstructured)
                return listPublications(mapping, form, request, response);
        }

        request.setAttribute("resultId", publicationChanged.getIdInternal());
        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward prepareDelete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final ResultPublication publication = (ResultPublication) getResultFromRequest(request);

        setRequestAttributes(request, publication);
        return mapping.findForward("PreparedToDelete");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final Integer resultId = getRequestParameterAsInteger(request, "resultId");

        if (getFromRequest(request, "confirm") != null) {
            try {
                final Object[] args = { resultId };
                executeService(request, "DeleteResultPublication", args);
            } catch (Exception e) {
                return listPublications(mapping, form, request, response);
            }
        }

        return mapping.findForward("PublicationDeleted");
    }

    public ActionForward changeType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject("publicationData");

        if (bean != null) {
            ResultPublicationType type = bean.getPublicationType();
            if (type != null) {
                bean = bean.convertTo(type);
                if (bean.getIdInternal() != null) {
                    final ResultPublication result = (ResultPublication) Result.readByOid(bean
                            .getIdInternal());
                    if (result != null) {
                        if (!(ResultPublicationType.getTypeFromPublication(result) == type)) {
                            if (result.hasAnyResultDocumentFiles()) {
                                request.setAttribute("typeChanged", "true");
                            }
                        } else {
                            bean = ResultPublicationBean.getBeanToEdit(result);
                            bean.setPerson(getLoggedPerson(request));
                            request.setAttribute("typeChanged", "false");
                        }
                    }
                }
            }
        }

        RenderUtils.invalidateViewState();

        request.setAttribute("publicationBean", bean);
        if (bean != null && bean.getIdInternal() != null) {
            verifyMessageForTypeChange(request);
            return mapping.findForward("PreparedToEdit");
        }
        return mapping.findForward("PreparedToCreate");
    }

    private void verifyMessageForTypeChange(HttpServletRequest request) {
        String typeChangedStr = (String) request.getAttribute("typeChanged");
        Boolean typeChanged = false;
        if (typeChangedStr == null) {
            typeChangedStr = (String) getFromRequest(request, "typeChanged");
        }
        if (typeChangedStr != null) {
            typeChanged = new Boolean(typeChangedStr);
        }

        if (typeChanged) {
            addActionMessage(request, "researcher.ResultPublication.editData.hasDocumentFiles");
        }
    }

    /**
     * Auxiliary methods
     */
    private boolean checkBean(HttpServletRequest request, ResultPublicationBean bean) {
        if (bean instanceof ConferenceArticlesBean) {
            final ConferenceArticlesBean conferenceBean = (ConferenceArticlesBean) bean;
            if ((conferenceBean.getEvent() == null) && (conferenceBean.getCreateEvent() == false)) {
                bean.setCreateEvent(true);
                RenderUtils.invalidateViewState();
                request.setAttribute("publicationBean", conferenceBean);
                return false;
            }
        }
        return true;
    }

    private void setRequestAttributes(HttpServletRequest request, ResultPublication publication) {
        request.setAttribute("result", publication);
        if (publication instanceof Unstructured)
            request.setAttribute("resultPublicationType", "Unstructured");
        else
            request.setAttribute("resultPublicationType", ResultPublicationType
                    .getTypeFromPublication(publication));

        if (publication.getIsPossibleSelectPersonRole()) {
            request.setAttribute("participationsSchema", "resultParticipation.full");
        }
    }

    private void setRequestAttributesToList(HttpServletRequest request) {
        final IUserView userView = getUserView(request);
        final List<ResultPublication> publications = ResultPublication.sort(userView.getPerson()
                .getResultPublications());

        request.setAttribute("books", filterBySubType(publications, Book.class));
        request.setAttribute("inbooks", getFilteredBookParts(publications, BookPartType.Inbook));
        request.setAttribute("incollections", getFilteredBookParts(publications,
                BookPartType.Incollection));
        request.setAttribute("articles", filterBySubType(publications, Article.class));
        request.setAttribute("inproceedings", filterBySubType(publications, Inproceedings.class));
        request.setAttribute("proceedings", filterBySubType(publications, Proceedings.class));
        request.setAttribute("theses", filterBySubType(publications, Thesis.class));
        request.setAttribute("manuals", filterBySubType(publications, Manual.class));
        request.setAttribute("technicalReports", filterBySubType(publications, TechnicalReport.class));
        request.setAttribute("otherPublications", filterBySubType(publications, OtherPublication.class));
        request.setAttribute("unstructureds", filterBySubType(publications, Unstructured.class));
    }

    private List getFilteredBookParts(List<ResultPublication> publications, BookPartType type) {
        List<BookPart> bookParts = filterBySubType(publications, BookPart.class);
        List<BookPart> filteredBookParts = new ArrayList<BookPart>();
        for (BookPart bookPart : bookParts) {
            if (bookPart.getBookPartType().equals(type))
                filteredBookParts.add(bookPart);
        }
        return filteredBookParts;
    }

    private List filterBySubType(List<ResultPublication> publications,
            Class<? extends ResultPublication> clazz) {
        return (List) CollectionUtils.select(publications, new PublicationSubTypePredicate(clazz));
    }

    private class PublicationSubTypePredicate implements Predicate {
        private Class<? extends ResultPublication> clazz;

        public PublicationSubTypePredicate(Class<? extends ResultPublication> clazz) {
            if (clazz == null) {
                throw new DomainException("");
            }
            this.clazz = clazz;
        }

        public boolean evaluate(Object arg0) {
            return this.clazz.equals(arg0.getClass());
        }
    }

    // TODO: Verifiy if this method is necessary
    /*
     * private ResultPublicationType getTypeFromRequest(HttpServletRequest
     * request) { final String typeStr = (String) getFromRequest(request,
     * "publicationType"); ResultPublicationType type = null; if (typeStr !=
     * null) { type = ResultPublicationType.valueOf(typeStr); } return type; }
     */
}
