package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookletBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ConferenceArticlesBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InbookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.IncollectionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.MiscBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.UnpublishedBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Booklet;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.Misc;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unpublished;
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
            HttpServletRequest request, HttpServletResponse response) {
        final String publicationTypeStr = (String) getFromRequest(request, "resultPublicationType");
        final ResultPublicationBean bean = getPublicationBean(request, publicationTypeStr);

        request.setAttribute("publicationBean", bean);
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
            HttpServletRequest request, HttpServletResponse response) {
        final ResultPublicationBean publicationBean = getPublicationBean(request, null);

        request.setAttribute("publicationBean", publicationBean);

        return mapping.findForward("PreparedToEdit");
    }

    public ActionForward editData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject(null);

        if (getFromRequest(request, "confirm") != null) {
            if (!checkBean(request, bean)) {
                return mapping.findForward("PreparedToEdit");
            }
            try {
                final Object[] args = { bean };
                executeService(request, "EditResultPublication", args);
            } catch (DomainException ex) {
                addActionMessage(request, ex.getKey());
                request.setAttribute("publicationBean", bean);
                return mapping.findForward("PreparedToEdit");
            } catch (Exception ex) {
                return listPublications(mapping, form, request, response);
            }
        }

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

    /**
     * Auxiliary methods
     */
    private String getPublicationTypeString(ResultPublication publication) {
        String type = publication.getClass().getSimpleName();

        // add bookPart type
        if (type.equals("BookPart")) {
            //type = type + "." + ((BookPart) publication).getBookPartType().toString();
            type = ((BookPart) publication).getBookPartType().toString();
        }

        return type;
    }

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
        request.setAttribute("resultPublicationType", getPublicationTypeString(publication));

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
        request.setAttribute("incollections", getFilteredBookParts(publications, BookPartType.Incollection));
        request.setAttribute("articles", filterBySubType(publications, Article.class));
        request.setAttribute("inproceedings", filterBySubType(publications, Inproceedings.class));
        request.setAttribute("proceedings", filterBySubType(publications, Proceedings.class));
        request.setAttribute("theses", filterBySubType(publications, Thesis.class));
        request.setAttribute("manuals", filterBySubType(publications, Manual.class));
        request.setAttribute("technicalReports", filterBySubType(publications, TechnicalReport.class));
        request.setAttribute("booklets", filterBySubType(publications, Booklet.class));
        request.setAttribute("miscs", filterBySubType(publications, Misc.class));
        request.setAttribute("unpublisheds", filterBySubType(publications, Unpublished.class));
        request.setAttribute("unstructureds", filterBySubType(publications, Unstructured.class));
    }

    private List getFilteredBookParts(List<ResultPublication> publications, BookPartType type) {
        List<BookPart> bookParts = filterBySubType(publications, BookPart.class);
        List<BookPart> filteredBookParts = new ArrayList<BookPart>();
        for (BookPart bookPart : bookParts) {
            if(bookPart.getBookPartType().equals(type))
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

    private ResultPublicationBean getPublicationBean(HttpServletRequest request,
            String publicationTypeStr) {
        ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject(null);

        if (bean == null) {
            final ResultPublication publication = (ResultPublication) getResultFromRequest(request);
            ResultPublicationType type = ResultPublicationType.getDefaultType();

            if (publicationTypeStr == null && publication != null) {
                publicationTypeStr = getPublicationTypeString(publication);
                //publicationTypeStr = publication.getClass().getSimpleName();
                type = ResultPublicationType.valueOf(publicationTypeStr);
            }
            if (publicationTypeStr != null && publication == null) {
                type = ResultPublicationType.valueOf(publicationTypeStr);
            }

            switch (type) {
            case Book:
                bean = new BookBean((Book) publication);
                break;
            case Inbook:
                bean = new InbookBean((BookPart) publication);
                break;
            case Incollection:
                bean = new IncollectionBean((BookPart) publication);
                break;
            case Article:
                bean = new ArticleBean((Article) publication);
                break;
            case Inproceedings:
                bean = new InproceedingsBean((Inproceedings) publication);
                break;
            case Proceedings:
                bean = new ProceedingsBean((Proceedings) publication);
                break;
            case Thesis:
                bean = new ThesisBean((Thesis) publication);
                break;
            case Manual:
                bean = new ManualBean((Manual) publication);
                break;
            case TechnicalReport:
                bean = new TechnicalReportBean((TechnicalReport) publication);
                break;
            case Booklet:
                bean = new BookletBean((Booklet) publication);
                break;
            case Misc:
                bean = new MiscBean((Misc) publication);
                break;
            case Unpublished:
                bean = new UnpublishedBean((Unpublished) publication);
                break;
            default:
                bean = new BookBean((Book) publication);
                break;
            }
        }
        bean.setPerson(getUserView(request).getPerson());
        return bean;
    }

    
    //TODO: Unstructured conversion
    private void verifyIfBasedUnstructuredPublication(HttpServletRequest request,
            ResultPublicationBean publicationBean) {
        /* Verify if creating new publication based on unstructured publication */
        String unstructuredId = (String) request.getParameter("unstructuredId");
        if (unstructuredId != null) {
            Unstructured unstructured = (Unstructured) rootDomainObject.readResultByOID(Integer
                    .valueOf(unstructuredId));
            publicationBean.setUnstructuredPublication(unstructured);
            request.setAttribute("unstructuredPublication", unstructured);
        }
    }
}
