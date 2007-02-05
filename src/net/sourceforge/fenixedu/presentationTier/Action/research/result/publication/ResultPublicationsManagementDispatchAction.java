package net.sourceforge.fenixedu.presentationTier.Action.research.result.publication;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart.BookPartType;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.ResultsManagementAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultPublicationsManagementDispatchAction extends ResultsManagementAction {

	public ActionForward listPublications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		setRequestAttributesToList(request, getLoggedPerson(request));
		return mapping.findForward("ListPublications");
	}

	public ActionForward showPublication(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);

		setRequestAttributes(request, publication);
		return mapping.findForward("ViewEditPublication");
	}

	public ActionForward showResultForOthers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ResearchResult result = getResultFromRequest(request);
		if(result instanceof ResearchResultPublication) {
			setRequestAttributes(request, (ResearchResultPublication)result);	
		}
		request.setAttribute("result", result);
		return mapping.findForward("ShowResult");
	}
	
	public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		ResultPublicationBean publicationBean = (ResultPublicationBean) getRenderedObject(null);

		if (publicationBean == null) {
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
		ResearchResultPublication publication = null;

		if (getFromRequest(request, "confirm") != null) {
			
			try {
				final Object[] args = { bean };
				publication = (ResearchResultPublication) executeService(request, "CreateResultPublication",
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
		setRequestAttributes(request, publication);
		return mapping.findForward("ViewEditPublication");
		
	}
	
	public ActionForward showAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		
		ResearchResultPublication result = getResearchResultPublication(request);
		result = getResearchResultPublication(request);
		setRequestAttributes(request, result);
        
        request.setAttribute("fileBean", getResultDocumentFileBean(request, result));
        request.setAttribute("unitBean", getResultUnitBean(request, result));
        
		return mapping.findForward("associatingInCreation");
	}

	private ResultDocumentFileSubmissionBean getResultDocumentFileBean(HttpServletRequest request, ResearchResultPublication result) {
		IViewState viewState = RenderUtils.getViewState("editBean");
		ResultDocumentFileSubmissionBean fileBean = (viewState!=null) ? (ResultDocumentFileSubmissionBean) viewState.getMetaObject().getObject() : new ResultDocumentFileSubmissionBean(result);  
		return fileBean;
	}
	
	private ResultUnitAssociationCreationBean getResultUnitBean(HttpServletRequest request, ResearchResultPublication result) {
		IViewState viewState = RenderUtils.getViewState("unitBean");
		ResultUnitAssociationCreationBean unitBean = (viewState!=null) ? (ResultUnitAssociationCreationBean)viewState.getMetaObject().getObject() : new ResultUnitAssociationCreationBean(result);  
		return unitBean;
	}
	
	private ResearchResultPublication getResearchResultPublication(HttpServletRequest request) {
		ResearchResultPublication result;
		String resultId = request.getParameter("resultId");
		if(resultId!=null) {
			result = (ResearchResultPublication) RootDomainObject.readDomainObjectByOID(ResearchResult.class, Integer.valueOf(resultId));
			request.setAttribute("resultId",result.getIdInternal());
		}
		else {
			result = (ResearchResultPublication) RootDomainObject.readDomainObjectByOID(ResearchResult.class, (Integer)request.getAttribute("resultId"));
		}
		return result;
	}
	
	public ActionForward prepareEditData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject(null);

		if (bean == null) {
			ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
			bean = ResultPublicationBean.getBeanToEdit(publication);
			bean.setPerson(getLoggedPerson(request));
		}

		request.setAttribute("publicationBean", bean);
		return mapping.findForward("PreparedToEdit");
	}

	public ActionForward editData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		final ResultPublicationBean bean = (ResultPublicationBean) getRenderedObject(null);
		ResearchResult publicationChanged = ResearchResult.readByOid(bean.getIdInternal());

		if (getFromRequest(request, "confirm") != null) {

			try {
				final Object[] args = { bean };
				publicationChanged = (ResearchResultPublication) executeService(request,
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
		return showPublication(mapping, form, request, response);
	}

	public ActionForward prepareDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
		setRequestAttributes(request, publication);
		
		request.setAttribute("confirm", "yes");
		return mapping.findForward("PreparedToDelete");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		final Integer resultId = getRequestParameterAsInteger(request, "resultId");

		if(getFromRequest(request, "cancel") != null) {
			final ResearchResultPublication publication = (ResearchResultPublication) getResultFromRequest(request);
			setRequestAttributes(request, publication);
			return mapping.findForward("ViewEditPublication");
		}
		if (getFromRequest(request, "confirm") != null) {
			try {
				final Object[] args = { resultId };
				executeService(request, "DeleteResultPublication", args);
			} catch (Exception e) {
				addActionMessage(request, e.getMessage());
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
					final ResearchResultPublication result = (ResearchResultPublication) ResearchResult
							.readByOid(bean.getIdInternal());
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
			return mapping.findForward("PreparedToEdit");
		}
		return mapping.findForward("PreparedToCreate");
	}


	/**
	 * Auxiliary methods
	 */

	private void setRequestAttributes(HttpServletRequest request, ResearchResultPublication publication) {
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

	private void setRequestAttributesToList(HttpServletRequest request, Person person) {
		
		request.setAttribute("books", person.getBooks());
		request.setAttribute("articles", person.getArticles());
		request.setAttribute("inproceedings", person.getInproceedings());
		request.setAttribute("proceedings", person.getProceedings());
		request.setAttribute("theses", person.getTheses());
		request.setAttribute("manuals", person.getManuals());
		request.setAttribute("technicalReports", person.getTechnicalReports());
		request.setAttribute("otherPublications", person.getOtherPublications());
		request.setAttribute("unstructureds", person.getUnstructureds());
		request.setAttribute("inbooks", person.getInbooks());
		request.setAttribute("incollections", person.getIncollections());
		request.setAttribute("person", getLoggedPerson(request));
	}

	
	// TODO: Verifiy if this method is necessary
	/*
	 * private ResultPublicationType getTypeFromRequest(HttpServletRequest
	 * request) { final String typeStr = (String) getFromRequest(request,
	 * "publicationType"); ResultPublicationType type = null; if (typeStr !=
	 * null) { type = ResultPublicationType.valueOf(typeStr); } return type; }
	 */

}