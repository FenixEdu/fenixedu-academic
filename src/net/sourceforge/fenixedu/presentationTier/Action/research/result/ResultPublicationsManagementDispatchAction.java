package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;

import net.sourceforge.fenixedu.domain.research.result.ResultPublication;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultPublicationsManagementDispatchAction extends FenixDispatchAction {

    public ActionForward listPublications(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);

        List<ResultPublication> publications = new ArrayList<ResultPublication>();
        ResultPublication publication = null;

        List<ResultPublication> books = new ArrayList<ResultPublication>();
        List<ResultPublication> bookParts = new ArrayList<ResultPublication>();
        List<ResultPublication> articles = new ArrayList<ResultPublication>();
        List<ResultPublication> thesis = new ArrayList<ResultPublication>();
        List<ResultPublication> conferences = new ArrayList<ResultPublication>();
        List<ResultPublication> technicalReports = new ArrayList<ResultPublication>();
        List<ResultPublication> otherPublications = new ArrayList<ResultPublication>();
        List<ResultPublication> unstructureds = new ArrayList<ResultPublication>();
/*
        for (Authorship authorship : userView.getPerson().getPersonAuthorshipsWithPublications()) {
            publication = (ResultPublication) authorship.getResult();

            if(publication.getResultPublicationType() != null)
            {
                switch (publication.getResultPublicationType().getPublicationType()) {
                case BOOK:
                    books.add(publication);
                    break;
                case BOOK_PART:
                    bookParts.add(publication);
                    break;
                case ARTICLE:
                    articles.add(publication);
                    break;
                case THESIS:
                    thesis.add(publication);
                    break;
                case CONFERENCE:
                    conferences.add(publication);
                    break;
                case TECHNICAL_REPORT:
                    technicalReports.add(publication);
                    break;
                case OTHER_PUBLICATION:
                    otherPublications.add(publication);
                    break;
                case UNSTRUCTURED:
                    unstructureds.add(publication);
                    break;
                }
                publications.add(publication);
            }
        }
        // request.setAttribute("publications", publications);

        //comparator by year in descendent order
        Comparator YearComparator = new Comparator()
        {
            public int compare(Object o1, Object o2) {
                Integer publication1Year = ((Publication) o1).getYear();
                Integer publication2Year = ((Publication) o2).getYear();
                if (publication1Year == null) {
                    return 1;
                } else if (publication2Year == null) {
                    return -1;
                }
                return (-1) * publication1Year.compareTo(publication2Year);
            }
        };
        //order publications
        Collections.sort(books, YearComparator);
        Collections.sort(bookParts, YearComparator);
        Collections.sort(articles, YearComparator);
        Collections.sort(thesis, YearComparator);
        Collections.sort(conferences, YearComparator);
        Collections.sort(technicalReports, YearComparator);
        Collections.sort(otherPublications, YearComparator);
        Collections.sort(otherPublications, YearComparator);
*/
        request.setAttribute("books", books);
        request.setAttribute("bookParts", bookParts);
        request.setAttribute("articles", articles);
        request.setAttribute("thesis", thesis);
        request.setAttribute("conferences", conferences);
        request.setAttribute("technicalReports", technicalReports);
        request.setAttribute("otherPublications", otherPublications);
        request.setAttribute("unstructureds", unstructureds);
        return mapping.findForward("ListPublications");
    }

    public ActionForward preparePublicationDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String publicationId = (String) request.getParameter("oid");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer.valueOf(publicationId));
        request.setAttribute("publication", publication);
        /*if((publication.getPublicationType().equals(PublicationType.BOOK)) || (publication.getPublicationType().equals(PublicationType.BOOK_PART)))
            request.setAttribute("authorshipsSchema","result.authorshipsWithRole");*/

        return mapping.findForward("ViewPublication");
    }
    
    public ActionForward prepareViewEditPublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        //If we get here from Publications Management, id comes in a paremeter
        String id = request.getParameter("publicationId");
        Integer publicationId = null;
        if(id != null)
            publicationId = Integer.valueOf(id);
        else
        {
            //If we get here from Result Participation Management, id comes in an attribute
            publicationId = (Integer) request.getAttribute("publicationId");
        }
        
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(publicationId);
        request.setAttribute("publication", publication);
        /*if((publication.getPublicationType().equals(PublicationType.BOOK)) || (publication.getPublicationType().equals(PublicationType.BOOK_PART)))
            request.setAttribute("authorshipsSchema","result.authorshipsWithRole");*/

        return mapping.findForward("ViewEditPublication");
    }
    
    public ActionForward prepareCreatePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
       
/*        List<ResultPublicationType> publicationTypes = rootDomainObject.getResultPublicationType();
        List<ResultPublicationType> publicationTypesPossibleToCreate = new ArrayList<ResultPublicationType>();
        ResultPublicationType resultPublicationType = null;
        
        for (ResultPublicationType publicationType : publicationTypes) {
            if(publicationType.getPublicationType().equals(PublicationType.BOOK))
            {
                resultPublicationType = publicationType;
            }
            if(!publicationType.getPublicationType().equals(PublicationType.UNSTRUCTURED))
            {
                publicationTypesPossibleToCreate.add(publicationType);
            }
        }
        
        String resultPublicationTypeId = request.getParameter("resultPublicationTypeId");
        if (resultPublicationTypeId != null)
        {
            resultPublicationType = rootDomainObject.readResultPublicationTypeByOID(Integer.valueOf(resultPublicationTypeId));
        }
        request.setAttribute("resultPublicationType",resultPublicationType);
        
        Collections.sort(publicationTypesPossibleToCreate,new BeanComparator("idInternal"));
        request.setAttribute("publicationTypes",publicationTypesPossibleToCreate);
        */
        
        request.setAttribute("participator", getUserView(request).getPerson());
        return mapping.findForward("PreparedToCreate");
    }
    
    public ActionForward prepareEditPublicationData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String publicationId = (String) request.getParameter("publicationId");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer
                .valueOf(publicationId));

        request.setAttribute("publication", publication);
        return mapping.findForward("PreparedToEdit");
    }

    public ActionForward prepareDeletePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String publicationId = (String) request.getParameter("publicationId");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer.valueOf(publicationId));
        request.setAttribute("publication", publication);
        /*if((publication.getPublicationType().equals(PublicationType.BOOK)) || (publication.getPublicationType().equals(PublicationType.BOOK_PART)))
            request.setAttribute("authorshipsSchema","result.authorshipsWithRole");*/

        return mapping.findForward("PreparedToDelete");
    }

    public ActionForward deletePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Object[] args = { Integer.valueOf(request.getParameter("resultId")) };
            ServiceManagerServiceFactory.executeService(getUserView(request), "DeletePublication", args);

        return mapping.findForward("PublicationDeleted");
    }

}