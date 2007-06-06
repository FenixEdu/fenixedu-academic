package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResearchUnitFunctionalities extends UnitFunctionalities {

	private static final int PAGE_SIZE = 20;
	
	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.findForward("ShowUnitFunctionalities");
	}

	
	public ActionForward configurePublicationCollaborators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		return mapping.findForward("editPublicationCollaborators");
	}
	
	public ActionForward preparePublications(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		ResultPublicationBean publicationBean = ResultPublicationBean.getBeanToCreate(ResultPublicationType.getDefaultType()); 
		Unit unit = getUnit(request);
		publicationBean.setUnit(unit);
		request.setAttribute("bean",publicationBean);
		putPublicationsInRequest(request,(ResearchUnit)unit);
		request.setAttribute("publications", unit.getResearchResultPublications());
		return mapping.findForward("publications");
	}
	
	@Override
	protected PersistentGroupMembers getGroup(HttpServletRequest request) {
		String persistedGroupID = request.getParameter("groupId");
		PersistentGroupMembers group = null;
		if (persistedGroupID != null) {
			group = (PersistentGroupMembers) RootDomainObject.readDomainObjectByOID(
					PersistentGroupMembers.class, Integer.valueOf(persistedGroupID));
		}
		return group;
	}

	@Override
	protected Unit getUnit(HttpServletRequest request) {
		String unitID = request.getParameter("unitId");
		ResearchUnit unit = null;
		if (unitID != null) {
			unit = (ResearchUnit) RootDomainObject.readDomainObjectByOID(ResearchUnit.class, Integer
					.valueOf(unitID));
		}
		return unit;
	}
	
	@Override
	protected UnitFile getUnitFile(HttpServletRequest request) {
		String fid = request.getParameter("fid");
		UnitFile file = (UnitFile) RootDomainObject.readDomainObjectByOID(UnitFile.class, Integer
				.valueOf(fid));
		return file;
	}
	
	@Override
	protected Integer getPageSize() {
		return PAGE_SIZE;
	}
	
	@Override
	protected PersistentGroupMembersBean getNewPersistentGroupBean(HttpServletRequest request) {
		PersistentGroupMembersBean bean = new PersistentGroupMembersBean(getUnit(request));
		bean.setType(PersistentGroupMembersType.UNIT_GROUP);
		return bean;
	}
	
	private void putPublicationsInRequest(HttpServletRequest request, ResearchUnit unit) {
		request.setAttribute("books", ResearchResultPublication.sort(unit.getBooks()));
		request.setAttribute("national-articles", ResearchResultPublication.sort(unit
			.getArticles(ScopeType.NATIONAL)));
		request.setAttribute("international-articles", ResearchResultPublication.sort(unit
			.getArticles(ScopeType.INTERNATIONAL)));
		request.setAttribute("national-inproceedings", ResearchResultPublication.sort(unit
			.getInproceedings(ScopeType.NATIONAL)));
		request.setAttribute("international-inproceedings", ResearchResultPublication.sort(unit
			.getInproceedings(ScopeType.INTERNATIONAL)));
		request.setAttribute("proceedings", ResearchResultPublication.sort(unit.getProceedings()));
		request.setAttribute("theses", ResearchResultPublication.sort(unit.getTheses()));
		request.setAttribute("manuals", ResearchResultPublication.sort(unit.getManuals()));
		request
			.setAttribute("technicalReports", ResearchResultPublication
				.sort(unit.getTechnicalReports()));
		request.setAttribute("otherPublications", ResearchResultPublication.sort(unit
			.getOtherPublications()));
		request.setAttribute("unstructureds", ResearchResultPublication.sort(unit.getUnstructureds()));
		request.setAttribute("inbooks", ResearchResultPublication.sort(unit.getInbooks()));
		
	}
}
