/*
 * Created on May 14, 2004
 */
package ServidorApresentacao.Action.publication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoObject;
import DataBeans.SiteView;
import DataBeans.publication.InfoAuthor;
import DataBeans.publication.InfoPublication;
import DataBeans.publication.InfoSiteAttributes;
import DataBeans.util.Cloner;
import Dominio.publication.IAuthor;
import Dominio.publication.IPublication;
import Dominio.publication.IPublicationType;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.framework.CRUDActionByOID;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.mapping.framework.CRUDMapping;
import constants.publication.PublicationConstants;

/**
 * @author TJBF
 * @author PFON
 */
public class InsertPublicationAction extends CRUDActionByOID {
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
	 *      DataBeans.InfoObject, org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */

	public ActionForward prepareEdit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);
	
		ActionForward actionForward = null;
		DynaActionForm dynaForm = (DynaActionForm) form;
		Integer publicationTypeId =
			new Integer(request.getParameter("publicationTypeId"));

		Integer idInternal = new Integer(request.getParameter("idInternal"));

		if (session != null) {

			Object[] argsToRead = { idInternal, userView };

			List authors =
				(List) ServiceUtils.executeService(
					userView,
					"ReadAuthorsByPublicationId",
					argsToRead);

			Object[] argsReadAuthorByKeyPerson = { userView };
			IAuthor author =
				(IAuthor) ServiceUtils.executeService(
					userView,
					"ReadAuthorByKeyPerson",
					argsReadAuthorByKeyPerson);

			InfoAuthor infoAuthor = Cloner.copyIAuthor2InfoAuthor(author);
			List allInfoAuthors = copyAuthorsToInfoAuthors(authors);

			List infoAuthors = removeInfoAuthor(allInfoAuthors, infoAuthor);

			Object[] args = { userView.getUtilizador(), publicationTypeId };
			List requiredAttributes =
				(List) ServiceUtils.executeService(
					userView,
					"ReadRequiredAttributes",
					args);

			List nonRequiredAttributes =
				(List) ServiceUtils.executeService(
					userView,
					"ReadNonRequiredAttributes",
					args);

			List subTypeList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadPublicationSubtypes",
					args);

			List formatList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadPublicationFormats",
					args);

			List monthList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadPublicationMonths",
					args);

			List scopeList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadPublicationScopes",
					args);

			InfoSiteAttributes bodyComponent = new InfoSiteAttributes();
			bodyComponent.setInfoRequiredAttributes(requiredAttributes);
			bodyComponent.setInfoNonRequiredAttributes(nonRequiredAttributes);

			SiteView siteView = new SiteView(bodyComponent);

			request.setAttribute("infoAuthorsList", infoAuthors);
			request.setAttribute("siteView", siteView);
			request.setAttribute("subTypeList", subTypeList);
			request.setAttribute("formatList", formatList);
			request.setAttribute("monthList", monthList);
			request.setAttribute("scopeList", scopeList);
			dynaForm.set("infoPublicationTypeId", publicationTypeId);

		}

		actionForward = super.prepareEdit(mapping, form, request, response);
		
		return actionForward;
	}

	protected void populateFormFromInfoObject(
		ActionMapping mapping,
		InfoObject infoObject,
		ActionForm form,
		HttpServletRequest request)
		throws FenixActionException {

		super.populateFormFromInfoObject(mapping, infoObject, form, request);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateInfoObjectFromForm(org.apache.struts.action.ActionForm,
	 *      ServidorApresentacao.mapping.framework.CRUDMapping)
	 */
	protected InfoObject populateInfoObjectFromForm(
		ActionForm form,
		CRUDMapping mapping,
		HttpServletRequest request)
		throws FenixActionException {
		try {

			HttpSession session = request.getSession(false);

			IUserView userView = SessionUtils.getUserView(request);
			InfoPublication infoPublication =
				(InfoPublication) super.populateInfoObjectFromForm(
					form,
					mapping);

			DynaActionForm dynaForm = (DynaActionForm) form;

			Integer keyTeacher = (Integer) dynaForm.get("teacherId");

			Integer publicationType = PublicationConstants.CIENTIFIC;

			Integer keyPublicationType =
				(Integer) dynaForm.get("infoPublicationTypeId");

			String subType = (String) dynaForm.get("subtype");

			String typePublication = (String) dynaForm.get("typePublication");

			String[] list = (String[]) dynaForm.get("authorsIds");
			List newList = Arrays.asList(list);
			List authorsIds = new ArrayList();
			authorsIds.addAll(newList);

			if (typePublication.equalsIgnoreCase(PublicationConstants.DIDATIC_STRING)) {
				publicationType = PublicationConstants.DIDATIC;
			}

			List infoAuthors = readAuthorsToInsert(authorsIds, userView);

			infoPublication.setInfoPublicationAuthors(infoAuthors);
			infoPublication.setKeyPublicationType(keyPublicationType);

			infoPublication.setSubType(subType);

			infoPublication.setDidatic(publicationType);

			return infoPublication;
		} catch (Exception e) {
			throw new FenixActionException(e.getMessage());
		}
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		CRUDMapping crudMapping = (CRUDMapping) mapping;

		InfoObject infoObject =
			populateInfoObjectFromForm(form, crudMapping, request);
		Object[] args =
			getEditServiceArguments(form, crudMapping, infoObject, request);

		DynaActionForm dynaForm = (DynaActionForm) form;
		String typePublication = (String) dynaForm.get("typePublication");

		//TODO ver s os campos obrigatorios veem preenchidos

		boolean contains = false;
		//TODO ver s a publicacao ja existe...no caso de fazer F5
		if (infoObject.getIdInternal().intValue() == PublicationConstants.ZERO_VALUE) {
			contains = verifyIfPublicationExists(request, infoObject);
		} //por os erros no request

		if (contains) {
			sendErrors(request, "existing", "message.publication.alreadyInserted");
			return crudMapping.findForward("Unsuccessfull-edit");
		}

		ServiceUtils.executeService(
			SessionUtils.getUserView(request),
			crudMapping.getEditService(),
			args);
		return crudMapping.findForward("successfull-edit");
	}

	public List readAuthorsToInsert(List authorsIds, IUserView userView)
		throws FenixServiceException {

		List newAuthorsIds = new ArrayList();
		Iterator iteratorIds = authorsIds.iterator();

		while (iteratorIds.hasNext()) {
			String idString = (String) iteratorIds.next();
			newAuthorsIds.add(new Integer(idString));
		}

		Object[] args1 = { userView };

		IAuthor author =
			(IAuthor) ServiceUtils.executeService(
				userView,
				"ReadAuthorByKeyPerson",
				args1);

		Object[] args = { newAuthorsIds };
		List authors =
			(List) ServiceUtils.executeService(
				userView,
				"ReadAuthorsToInsert",
				args);

		authors.add(author);

		return copyAuthorsToInfoAuthors(authors);
	}

	public List copyAuthorsToInfoAuthors(List authors) {

		List infoAuthors =
			(List) CollectionUtils.collect(authors, new Transformer() {
			public Object transform(Object o) {
				IAuthor author = (IAuthor) o;
				return Cloner.copyIAuthor2InfoAuthor(author);
			}
		});

		return infoAuthors;
	}

	public List removeInfoAuthor(List infoAuthors, InfoAuthor infoAuthor) {
		List infoAuthorsRemoved = new ArrayList();

		Iterator iterator = infoAuthors.iterator();

		while (iterator.hasNext()) {
			InfoAuthor infoAuthorAux = (InfoAuthor) iterator.next();
			if (!infoAuthor
				.getIdInternal()
				.equals(infoAuthorAux.getIdInternal())) {
				infoAuthorsRemoved.add(infoAuthorAux);
			}
		}

		return infoAuthorsRemoved;
	}

	/**
	 * @param request
	 * @param infoObject
	 * @return
	 */
	private boolean verifyIfPublicationExists(
		HttpServletRequest request,
		InfoObject infoObject)
		throws FenixServiceException {
		// TODO Auto-generated method stub

		HttpSession session = request.getSession(false);

		IUserView userView = SessionUtils.getUserView(request);
		Object[] args1 = { userView };

		IAuthor author =
			(IAuthor) ServiceUtils.executeService(
				userView,
				"ReadAuthorByKeyPerson",
				args1);

		List publications = author.getAuthorPublications();

		IPublication publication =
			Cloner.copyInfoPublication2IPublication(
				(InfoPublication) infoObject);

		Integer keyPublicationTypeId =
			(Integer) ((InfoPublication) infoObject).getKeyPublicationType();

		publication.setKeyPublicationType(keyPublicationTypeId);

		Iterator iteratorPublications = publications.iterator();
		Boolean contains = Boolean.FALSE;

		Object[] args2 = { keyPublicationTypeId };

		IPublicationType publicationType =
			(IPublicationType) ServiceUtils.executeService(
					userView,
					"ReadPublicationType",
					args2);
		
		publication.setType(publicationType);
		
		IPublication publicationToCompare2 = publication;
		publicationToCompare2.setPublicationString(publication.toString());
		
		
		while (iteratorPublications.hasNext()) {
			IPublication publicationToCompare =
				(IPublication) iteratorPublications.next();
			IPublication publicationToCompare3 = publicationToCompare;
			publicationToCompare3.setPublicationString(publicationToCompare.toString());
			if (publicationToCompare3.equals(publicationToCompare2)) {
				contains = Boolean.TRUE;
			}
		}
		return contains.booleanValue();
	}

	private void sendErrors(
		HttpServletRequest request,
		String arg0,
		String arg1) {
		ActionErrors errors = new ActionErrors();
		errors.add(arg0, new ActionError(arg1));
		saveErrors(request, errors);
	}
}
