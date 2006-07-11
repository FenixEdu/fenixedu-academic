package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;

import net.sourceforge.fenixedu.domain.research.result.publication.*;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication.ResultPublicationType;
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
/*        List<ResultPublication> bookParts = new ArrayList<ResultPublication>();
        List<ResultPublication> articles = new ArrayList<ResultPublication>();
        List<ResultPublication> thesis = new ArrayList<ResultPublication>();
        List<ResultPublication> conferences = new ArrayList<ResultPublication>();
        List<ResultPublication> technicalReports = new ArrayList<ResultPublication>();
        List<ResultPublication> otherPublications = new ArrayList<ResultPublication>();
        List<ResultPublication> unstructureds = new ArrayList<ResultPublication>();*/
        
        for (ResultPublication resultPublication : userView.getPerson().getResultPublications()) {
            if(resultPublication instanceof Book) {
                books.add(resultPublication);
            }
              
        }

        //comparator by year in descendent order
        Comparator YearComparator = new Comparator()
        {
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
        //order publications
        Collections.sort(books, YearComparator);

        request.setAttribute("books", books);
        /*request.setAttribute("bookParts", bookParts);
        request.setAttribute("articles", articles);
        request.setAttribute("thesis", thesis);
        request.setAttribute("conferences", conferences);
        request.setAttribute("technicalReports", technicalReports);
        request.setAttribute("otherPublications", otherPublications);
        request.setAttribute("unstructureds", unstructureds);*/
        return mapping.findForward("ListPublications");
    }

    public ActionForward preparePublicationDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String publicationId = (String) request.getParameter("oid");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer.valueOf(publicationId));
        request.setAttribute("publication", publication);
        
        ResultPublicationType resultPublicationType = publication.getResultPublicationType();
        request.setAttribute("resultPublicationType", resultPublicationType.toString());
        if(publication.haveResultPublicationRole())
            request.setAttribute("participationsSchema","result.participationsWithRole");
        
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
        
        ResultPublicationType resultPublicationType = publication.getResultPublicationType();
        request.setAttribute("resultPublicationType", resultPublicationType.toString());
        if(publication.haveResultPublicationRole())
            request.setAttribute("participationsSchema","result.participationsWithRole");

        return mapping.findForward("ViewEditPublication");
    }
    
    private String getAssociatedClassString(ResultPublicationType resultPublicationType)
    {
        String className="";
        switch (resultPublicationType) {
        case Book:
            className=Book.class.getName();
            break;
        case BookPart:
            className=BookPart.class.getName();
            break;
        case Article:
            className=Article.class.getName();
            break;
        case Inproceedings:
            className=Inproceedings.class.getName();
            break;
        case Proceedings:
            className=Proceedings.class.getName();
            break;
        case Thesis:
            className=Thesis.class.getName();
            break;
        case Manual:
            className=Manual.class.getName();
            break;
        case TechnicalReport:
            className=TechnicalReport.class.getName();
            break;
        case Booklet:
            className=Booklet.class.getName();
            break;
        case Misc:
            className=Misc.class.getName();
            break;
        case Unpublished:
            className=Unpublished.class.getName();
            break;
        default:
            break;
        }
        return className;
    }
    
    public ActionForward prepareCreatePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
       
        String resultPublicationType = request.getParameter("resultPublicationType");
        if (resultPublicationType != null)
        {
            request.setAttribute("resultPublicationType",resultPublicationType);
            request.setAttribute("className",getAssociatedClassString(ResultPublicationType.valueOf(resultPublicationType)));
        }
        else
        {
            request.setAttribute("resultPublicationType",ResultPublicationType.getDefaultResultPublicationType().toString());
            request.setAttribute("className",getAssociatedClassString(ResultPublicationType.getDefaultResultPublicationType()));
        }
        
        request.setAttribute("participator", getUserView(request).getPerson());
        return mapping.findForward("PreparedToCreate");
    }
    
    public ActionForward prepareEditPublicationData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String publicationId = (String) request.getParameter("publicationId");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer
                .valueOf(publicationId));

        request.setAttribute("publication", publication);
        
        ResultPublicationType resultPublicationType = publication.getResultPublicationType();
        request.setAttribute("resultPublicationType", resultPublicationType.toString());
        
        return mapping.findForward("PreparedToEdit");
    }

    public ActionForward prepareDeletePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String publicationId = (String) request.getParameter("publicationId");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer.valueOf(publicationId));
        request.setAttribute("publication", publication);
        
        ResultPublicationType resultPublicationType = publication.getResultPublicationType();
        request.setAttribute("resultPublicationType", resultPublicationType.toString());
        if(publication.haveResultPublicationRole())
            request.setAttribute("participationsSchema","result.participationsWithRole");

        return mapping.findForward("PreparedToDelete");
    }

    public ActionForward deletePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Object[] args = { Integer.valueOf(request.getParameter("resultId")) };
            ServiceManagerServiceFactory.executeService(getUserView(request), "DeletePublication", args);

        return mapping.findForward("PublicationDeleted");
    }

}