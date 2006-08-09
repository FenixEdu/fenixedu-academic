package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean.ResultPublicationType;
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
        
        for (ResultPublication resultPublication : userView.getPerson().getResultPublications()) {
            if(resultPublication instanceof Book) {
                books.add(resultPublication);
            }
            else if(resultPublication instanceof BookPart) {
                bookParts.add(resultPublication);
            }
            else if(resultPublication instanceof Article) {
                articles.add(resultPublication);
            }
            else if(resultPublication instanceof Inproceedings) {
                inproceedings.add(resultPublication);
            }
            else if(resultPublication instanceof Proceedings) {
                proceedings.add(resultPublication);
            }
            else if(resultPublication instanceof Thesis) {
                theses.add(resultPublication);
            }
            else if(resultPublication instanceof Manual) {
                manuals.add(resultPublication);
            }
            else if(resultPublication instanceof TechnicalReport) {
                technicalReports.add(resultPublication);
            }
            else if(resultPublication instanceof Booklet) {
                booklets.add(resultPublication);
            }
            else if(resultPublication instanceof Misc) {
                miscs.add(resultPublication);
            }
            else if(resultPublication instanceof Unpublished) {
                unpublisheds.add(resultPublication);
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
        return mapping.findForward("ListPublications");
    }

    public ActionForward preparePublicationDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        String publicationId = (String) request.getParameter("publicationId");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer.valueOf(publicationId));
        request.setAttribute("publication", publication);
        
        request.setAttribute("resultPublicationType", publication.getResultPublicationTypeString());
        
        if(publication.haveResultPublicationRole())
            request.setAttribute("participationsSchema","result.participationsWithRole");
        
        return mapping.findForward("ViewPublication");
    }
    
    public ActionForward prepareViewEditPublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

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
        
        request.setAttribute("resultPublicationType", publication.getResultPublicationTypeString());
        
        if(publication.haveResultPublicationRole())
            request.setAttribute("participationsSchema","result.participationsWithRole");

        return mapping.findForward("ViewEditPublication");
    }
    
    public ActionForward prepareCreatePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
       
        String resultPublicationType = request.getParameter("resultPublicationType");
        ResultPublicationCreationBean publicationBean = null;
        
        if (resultPublicationType != null)
        {
            //changing publication type
            publicationBean = new ResultPublicationCreationBean();
            publicationBean.setPublicationType(ResultPublicationType.valueOf(resultPublicationType));
            
            if(!publicationBean.getPublicationType().equals(ResultPublicationType.BookPart))
                publicationBean.setActiveSchema("result.publication.create." + publicationBean.getPublicationType().toString());
            else
            {
                publicationBean.setBookPartType(BookPartType.Inbook);
                publicationBean.setActiveSchema("result.publication.create.BookPart.Inbook");
            }
        }
        else
        {
            //invalid submit or enter create publication
            if(RenderUtils.getViewState() != null)
            {
                //invalid submit
                publicationBean = (ResultPublicationCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            }
            else
            {
                //enter create publication
                publicationBean = new ResultPublicationCreationBean();
                publicationBean.setPublicationType(ResultPublicationType.getDefaultResultPublicationType());
                publicationBean.setActiveSchema("result.publication.create." + publicationBean.getPublicationType().toString());
            }
        }
        
        publicationBean.setParticipator(getUserView(request).getPerson());
        request.setAttribute("publicationBean", publicationBean);
        return mapping.findForward("PreparedToCreate");
    }

    public ActionForward changeBookPartTypePostBack(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
       
        if (RenderUtils.getViewState() == null)
            return listPublications(mapping, form, request, response);

        ResultPublicationCreationBean publicationBean = (ResultPublicationCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        if(publicationBean.getBookPartType() != null)
        {
            switch (publicationBean.getBookPartType()) {
                case Inbook: 
                    publicationBean.setActiveSchema("result.publication.create.BookPart.Inbook");
                    break;
                case Incollection:
                    publicationBean.setActiveSchema("result.publication.create.BookPart.Incollection");
                    break;
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("publicationBean", publicationBean);
        
        //check if editing or creating publication
        if(publicationBean.getIdInternal() != null)
            return mapping.findForward("PreparedToEdit");
        return mapping.findForward("PreparedToCreate");
    }
    
    public ActionForward createResultPublication(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ResultPublicationCreationBean publicationBean = (ResultPublicationCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        if((publicationBean.getPublicationType().equals(ResultPublicationType.Inproceedings)) || publicationBean.getPublicationType().equals(ResultPublicationType.Proceedings))
        {
        	if((publicationBean.getEvent() == null) && (publicationBean.getCreateEvent() == false))
        	{
        		if(publicationBean.getPublicationType().equals(ResultPublicationType.Inproceedings))
        			publicationBean.setActiveSchema("result.publication.create.InproceedingsAndEvent");
        		else
        			publicationBean.setActiveSchema("result.publication.create.ProceedingsAndEvent");
        		publicationBean.setEventName(new MultiLanguageString(publicationBean.getEventNameAutoComplete()));
        		//default: event is a conference
        		publicationBean.setEventType(EventType.Conference);
        		publicationBean.setCreateEvent(true);
        		RenderUtils.invalidateViewState();
                request.setAttribute("publicationBean", publicationBean);
                
                return mapping.findForward("PreparedToCreate");
        	}
        }
        try{
            ServiceUtils.executeService(getUserView(request), "CreateResultPublication", new Object[] { publicationBean});
        }catch(DomainException ex){
            addActionMessage(request,ex.getKey());

            request.setAttribute("publicationBean", publicationBean);
            return mapping.findForward("PreparedToCreate");
        }
        catch(FenixServiceException ex) {
            return listPublications(mapping,form,request,response);
        }
        return listPublications(mapping,form,request,response);
    }
    
    public ActionForward prepareEditPublicationData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        ResultPublicationCreationBean publicationBean = null;

        if(RenderUtils.getViewState() == null)
        {
            //enter edit publication
            String publicationId = (String) request.getParameter("publicationId");
            ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer.valueOf(publicationId));
            
            publicationBean = new ResultPublicationCreationBean();
            publicationBean.fillFromPublication(publication);
            
            if(!publicationBean.getPublicationType().equals(ResultPublicationType.BookPart))
                publicationBean.setActiveSchema("result.publication.create." + publicationBean.getPublicationType().toString());
            else
            {
                publicationBean.setActiveSchema("result.publication.create.BookPart." + publicationBean.getBookPartType());
            }
        }
        else
        {
            //invalid edition
            publicationBean = (ResultPublicationCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        }
        
        request.setAttribute("publicationBean", publicationBean);
        return mapping.findForward("PreparedToEdit");
    }
    
    public ActionForward editPublicationData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ResultPublicationCreationBean publicationBean = (ResultPublicationCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        
        if((publicationBean.getPublicationType().equals(ResultPublicationType.Inproceedings)) || publicationBean.getPublicationType().equals(ResultPublicationType.Proceedings))
        {
        	if((publicationBean.getEvent() == null) && (publicationBean.getCreateEvent() == false))
        	{
        		if(publicationBean.getPublicationType().equals(ResultPublicationType.Inproceedings))
        			publicationBean.setActiveSchema("result.publication.create.InproceedingsAndEvent");
        		else
        			publicationBean.setActiveSchema("result.publication.create.ProceedingsAndEvent");
        		publicationBean.setEventName(new MultiLanguageString(publicationBean.getEventNameAutoComplete()));
        		//default: event is a conference
        		publicationBean.setEventType(EventType.Conference);
        		publicationBean.setCreateEvent(true);
        		RenderUtils.invalidateViewState();
                request.setAttribute("publicationBean", publicationBean);
                
                return mapping.findForward("PreparedToEdit");
        	}
        }
        try{
            ServiceUtils.executeService(getUserView(request), "EditResultPublication", new Object[] { publicationBean});
        }catch(DomainException ex){
            addActionMessage(request,ex.getKey());

            request.setAttribute("publicationBean", publicationBean);
            return mapping.findForward("PreparedToEdit");
        }catch(FenixServiceException ex){
            return listPublications(mapping,form,request,response);
        }        
                
        return prepareViewEditPublication(mapping,form,request,response);
    }

    public ActionForward prepareDeletePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        String publicationId = (String) request.getParameter("publicationId");
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(Integer.valueOf(publicationId));
        request.setAttribute("publication", publication);
        
        request.setAttribute("resultPublicationType", publication.getResultPublicationTypeString());
        
        if(publication.haveResultPublicationRole())
            request.setAttribute("participationsSchema","result.participationsWithRole");

        return mapping.findForward("PreparedToDelete");
    }

    public ActionForward deletePublication(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        Object[] args = { Integer.valueOf(request.getParameter("resultId")) };
        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), "DeleteResultPublication", args);
        } catch (Exception e) {
            return listPublications(mapping,form,request,response);
        }

        return mapping.findForward("PublicationDeleted");
    }

}