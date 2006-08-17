package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.*;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.event.EventType;
import net.sourceforge.fenixedu.domain.research.result.publication.*;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultPublicationsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward listPublications(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final IUserView userView = getUserView(request);

        List<ResultPublication> books = new ArrayList<ResultPublication>();
        List<ResultPublication> bookParts = new ArrayList<ResultPublication>();
        List<ResultPublication> articles = new ArrayList<ResultPublication>();
        List<ResultPublication> inproceedings = new ArrayList<ResultPublication>();
        List<ResultPublication> proceedings = new ArrayList<ResultPublication>();
        List<ResultPublication> theses = new ArrayList<ResultPublication>();
        List<ResultPublication> manuals = new ArrayList<ResultPublication>();
        List<ResultPublication> technicalReports = new ArrayList<ResultPublication>();
        List<ResultPublication> booklets = new ArrayList<ResultPublication>();
        List<ResultPublication> miscs = new ArrayList<ResultPublication>();
        List<ResultPublication> unpublisheds = new ArrayList<ResultPublication>();
        List<ResultPublication> unstructureds = new ArrayList<ResultPublication>();

        for (ResultPublication resultPublication : userView.getPerson().getResultPublications()) {
            if (resultPublication instanceof Book)
                books.add(resultPublication);
            else if (resultPublication instanceof BookPart)
                bookParts.add(resultPublication);
            else if (resultPublication instanceof Article)
                articles.add(resultPublication);
            else if (resultPublication instanceof Inproceedings)
                inproceedings.add(resultPublication);
            else if (resultPublication instanceof Proceedings)
                proceedings.add(resultPublication);
            else if (resultPublication instanceof Thesis)
                theses.add(resultPublication);
            else if (resultPublication instanceof Manual)
                manuals.add(resultPublication);
            else if (resultPublication instanceof TechnicalReport)
                technicalReports.add(resultPublication);
            else if (resultPublication instanceof Booklet)
                booklets.add(resultPublication);
            else if (resultPublication instanceof Misc)
                miscs.add(resultPublication);
            else if (resultPublication instanceof Unpublished)
                unpublisheds.add(resultPublication);
            else if (resultPublication instanceof Unstructured)
                unstructureds.add(resultPublication);
        }

        // comparator by year in descendent order
        Comparator YearComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Integer publication1Year = ((ResultPublication) o1).getYear();
                Integer publication2Year = ((ResultPublication) o2).getYear();
                if (publication1Year == null) {
                    return 1;
                } else if (publication2Year == null) {
                    return -1;
                }
                return (-1) * publication1Year.compareTo(publication2Year);
            }
        };
        // order publications
        Collections.sort(books, YearComparator);
        Collections.sort(bookParts, YearComparator);
        Collections.sort(articles, YearComparator);
        Collections.sort(inproceedings, YearComparator);
        Collections.sort(proceedings, YearComparator);
        Collections.sort(theses, YearComparator);
        Collections.sort(manuals, YearComparator);
        Collections.sort(technicalReports, YearComparator);
        Collections.sort(booklets, YearComparator);
        Collections.sort(miscs, YearComparator);
        Collections.sort(unpublisheds, YearComparator);

        request.setAttribute("books", books);
        request.setAttribute("bookParts", bookParts);
        request.setAttribute("articles", articles);
        request.setAttribute("inproceedings", inproceedings);
        request.setAttribute("proceedings", proceedings);
        request.setAttribute("theses", theses);
        request.setAttribute("manuals", manuals);
        request.setAttribute("technicalReports", technicalReports);
        request.setAttribute("booklets", booklets);
        request.setAttribute("miscs", miscs);
        request.setAttribute("unpublisheds", unpublisheds);
        request.setAttribute("unstructureds", unstructureds);
        return mapping.findForward("ListPublications");
    }

    public ActionForward preparePublicationDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        String publicationId = (String) request.getParameter("publicationId");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer
                .valueOf(publicationId));
        request.setAttribute("publication", publication);
        request.setAttribute("resultPublicationType", getPublicationTypeString(publication));

        if (publication.hasResultPublicationRole())
            request.setAttribute("participationsSchema", "result.participationsWithRole");

        return mapping.findForward("ViewPublication");
    }

    public ActionForward prepareViewEditPublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        // If we get here from Publications Management, id comes in a paremeter
        String id = request.getParameter("publicationId");
        Integer publicationId = null;
        if (id != null)
            publicationId = Integer.valueOf(id);
        else {
            // If we get here from Result Participation Management, id comes in
            // an attribute
            publicationId = (Integer) request.getAttribute("publicationId");
        }

        ResultPublication publication = (ResultPublication) rootDomainObject
                .readResultByOID(publicationId);
        request.setAttribute("publication", publication);
        request.setAttribute("resultPublicationType", getPublicationTypeString(publication));

        if (publication.hasResultPublicationRole())
            request.setAttribute("participationsSchema", "result.participationsWithRole");

        return mapping.findForward("ViewEditPublication");
    }

    public ActionForward prepareCreatePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        String resultPublicationType = request.getParameter("resultPublicationType");
        ResultPublicationBean publicationBean = null;

        if (resultPublicationType == null) {
            // invalid submit or enter create publication
            if (RenderUtils.getViewState() == null) {
                // enter create publication (default Book)
                publicationBean = new BookBean();
                publicationBean.setPublicationType(ResultPublicationType.Book);
                publicationBean.setActiveSchema("result.publication.create."
                        + publicationBean.getPublicationType().toString());
            } else {
                // invalid submit
                publicationBean = (ResultPublicationBean) RenderUtils.getViewState().getMetaObject()
                        .getObject();
                request.setAttribute("publicationBean", publicationBean);
                return mapping.findForward("PreparedToCreate");
            }
        } else {
            // changing publication type
            ResultPublicationType type = ResultPublicationType.valueOf(resultPublicationType);
            switch (type) {
            case Book:
                publicationBean = new BookBean();
                break;
            case BookPart:
                publicationBean = new BookPartBean();
                break;
            case Article:
                publicationBean = new ArticleBean();
                break;
            case Inproceedings:
                publicationBean = new InproceedingsBean();
                break;
            case Proceedings:
                publicationBean = new ProceedingsBean();
                break;
            case Thesis:
                publicationBean = new ThesisBean();
                break;
            case Manual:
                publicationBean = new ManualBean();
                break;
            case TechnicalReport:
                publicationBean = new TechnicalReportBean();
                break;
            case Booklet:
                publicationBean = new BookletBean();
                break;
            case Misc:
                publicationBean = new MiscBean();
                break;
            case Unpublished:
                publicationBean = new UnpublishedBean();
                break;
            }
            publicationBean.setPublicationType(type);

            if (publicationBean instanceof BookPartBean) {
                ((BookPartBean) publicationBean).setBookPartType(BookPartType.Inbook);
                publicationBean.setActiveSchema("result.publication.create.BookPart.Inbook");
            } else {
                publicationBean.setActiveSchema("result.publication.create."
                        + publicationBean.getPublicationType().toString());
            }
        }

        if ((publicationBean instanceof BookBean) || (publicationBean instanceof BookPartBean)
                || (publicationBean instanceof InproceedingsBean))
            publicationBean.setParticipationSchema("result.participationsWithRole");
        else
            publicationBean.setParticipationSchema("result.participations");

        publicationBean.setPerson(getUserView(request).getPerson());
        request.setAttribute("publicationBean", publicationBean);
        
        return mapping.findForward("PreparedToCreate");
    }

    public ActionForward changeBookPartTypePostBack(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        if (RenderUtils.getViewState() == null)
            return listPublications(mapping, form, request, response);

        BookPartBean bookPartBean = (BookPartBean) RenderUtils.getViewState().getMetaObject()
                .getObject();

        if (bookPartBean.getBookPartType() == null)
            bookPartBean.setBookPartType(BookPartType.Inbook);
        if (bookPartBean.getBookPartType().equals(BookPartType.Inbook))
            bookPartBean.setActiveSchema("result.publication.create.BookPart.Inbook");
        else
            bookPartBean.setActiveSchema("result.publication.create.BookPart.Incollection");

        RenderUtils.invalidateViewState();
        request.setAttribute("publicationBean", bookPartBean);

        // check if editing or creating publication
        if (bookPartBean.getIdInternal() != null)
            return mapping.findForward("PreparedToEdit");
        return mapping.findForward("PreparedToCreate");
    }

    public ActionForward createResultPublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ResultPublication publication = null;
        ResultPublicationBean publicationBean = (ResultPublicationBean) RenderUtils.getViewState()
                .getMetaObject().getObject();
        if (publicationBean instanceof ConferenceArticlesBean) {
            ConferenceArticlesBean conferenceArticleBean = (ConferenceArticlesBean) publicationBean;
            if ((conferenceArticleBean.getEvent() == null)
                    && (conferenceArticleBean.getCreateEvent() == false)) {
                prepareEventCreation(conferenceArticleBean);

                RenderUtils.invalidateViewState();
                request.setAttribute("publicationBean", conferenceArticleBean);
                return mapping.findForward("PreparedToCreate");
            }
        }
        try {
            publication = (ResultPublication) ServiceUtils.executeService(getUserView(request),
                    "CreateResultPublication", new Object[] { publicationBean });
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());

            request.setAttribute("publicationBean", publicationBean);
            return mapping.findForward("PreparedToCreate");
        } catch (Exception ex) {
            return listPublications(mapping, form, request, response);
        }
        request.setAttribute("publicationId", publication.getIdInternal());
        return prepareViewEditPublication(mapping, form, request, response);
        // return listPublications(mapping,form,request,response);
    }

    private ConferenceArticlesBean prepareEventCreation(ConferenceArticlesBean conferenceArticleBean) {
        if (conferenceArticleBean instanceof InproceedingsBean)
            conferenceArticleBean.setActiveSchema("result.publication.create.InproceedingsAndEvent");
        else
            conferenceArticleBean.setActiveSchema("result.publication.create.ProceedingsAndEvent");

        conferenceArticleBean.setEventName(new MultiLanguageString(conferenceArticleBean
                .getEventNameAutoComplete()));
        // default: event is a conference
        conferenceArticleBean.setEventType(EventType.Conference);
        conferenceArticleBean.setCreateEvent(true);
        return conferenceArticleBean;
    }

    public ActionForward prepareEditPublicationData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ResultPublicationBean publicationBean = null;

        if (RenderUtils.getViewState() == null) {
            // enter edit publication
            String publicationId = (String) request.getParameter("publicationId");
            ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer
                    .valueOf(publicationId));

            if (publication instanceof Book)
                publicationBean = new BookBean((Book) publication);
            else if (publication instanceof BookPart)
                publicationBean = new BookPartBean((BookPart) publication);
            else if (publication instanceof Article)
                publicationBean = new ArticleBean((Article) publication);
            else if (publication instanceof Inproceedings)
                publicationBean = new InproceedingsBean((Inproceedings) publication);
            else if (publication instanceof Proceedings)
                publicationBean = new ProceedingsBean((Proceedings) publication);
            else if (publication instanceof Thesis)
                publicationBean = new ThesisBean((Thesis) publication);
            else if (publication instanceof Manual)
                publicationBean = new ManualBean((Manual) publication);
            else if (publication instanceof TechnicalReport)
                publicationBean = new TechnicalReportBean((TechnicalReport) publication);
            else if (publication instanceof Booklet)
                publicationBean = new BookletBean((Booklet) publication);
            else if (publication instanceof Misc)
                publicationBean = new MiscBean((Misc) publication);
            else if (publication instanceof Unpublished)
                publicationBean = new UnpublishedBean((Unpublished) publication);

            if (publicationBean instanceof BookPartBean)
                publicationBean.setActiveSchema("result.publication.create.BookPart."
                        + ((BookPartBean) publicationBean).getBookPartType());
            else
                publicationBean.setActiveSchema("result.publication.create."
                        + publicationBean.getPublicationType().toString());
        } else {
            // invalid edition
            publicationBean = (ResultPublicationBean) RenderUtils.getViewState().getMetaObject()
                    .getObject();
        }

        request.setAttribute("publicationBean", publicationBean);
        return mapping.findForward("PreparedToEdit");
    }

    public ActionForward editPublicationData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ResultPublicationBean publicationBean = (ResultPublicationBean) RenderUtils.getViewState()
                .getMetaObject().getObject();
        if (publicationBean instanceof ConferenceArticlesBean) {
            ConferenceArticlesBean conferenceArticleBean = (ConferenceArticlesBean) publicationBean;
            if ((conferenceArticleBean.getEvent() == null)
                    && (conferenceArticleBean.getCreateEvent() == false)) {
                prepareEventCreation(conferenceArticleBean);

                RenderUtils.invalidateViewState();
                request.setAttribute("publicationBean", conferenceArticleBean);
                return mapping.findForward("PreparedToEdit");
            }
        }
        try {
            ServiceUtils.executeService(getUserView(request), "EditResultPublication",
                    new Object[] { publicationBean });
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());

            request.setAttribute("publicationBean", publicationBean);
            return mapping.findForward("PreparedToEdit");
        } catch (Exception ex) {
            return listPublications(mapping, form, request, response);
        }

        return prepareViewEditPublication(mapping, form, request, response);
    }

    public ActionForward prepareDeletePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        String publicationId = (String) request.getParameter("publicationId");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer
                .valueOf(publicationId));

        request.setAttribute("publication", publication);
        request.setAttribute("resultPublicationType", getPublicationTypeString(publication));

        if (publication.hasResultPublicationRole())
            request.setAttribute("participationsSchema", "result.participationsWithRole");

        return mapping.findForward("PreparedToDelete");
    }

    public ActionForward deletePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        Object[] args = { Integer.valueOf(request.getParameter("resultId")) };
        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), "DeleteResultPublication",
                    args);
        } catch (Exception e) {
            return listPublications(mapping, form, request, response);
        }

        return mapping.findForward("PublicationDeleted");
    }

    private String getPublicationTypeString(ResultPublication publication) {
        String className = publication.getClass().getName();
        Integer lastPoint = className.lastIndexOf(".");
        String type = className.substring(lastPoint + 1);

        if (type.equals("BookPart")) {
            //add bookPart type
            type = type + "." + ((BookPart) publication).getBookPartType().toString();
        }
        return type;
    }
}
