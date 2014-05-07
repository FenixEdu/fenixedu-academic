package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;
import net.sourceforge.fenixedu.presentationTier.Action.research.ResearcherApplication.ResearcherResearchUnitApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ResearcherResearchUnitApp.class, path = "choose-unit", titleKey = "label.researchUnits")
@Mapping(module = "researcher", path = "/researchUnitFunctionalities")
@Forwards(value = { @Forward(name = "uploadFile", path = "/commons/unitFiles/uploadFile.jsp"),
        @Forward(name = "manageFiles", path = "/commons/unitFiles/manageFiles.jsp"),
        @Forward(name = "editUploaders", path = "/commons/PersistentMemberGroups/configureUploaders.jsp"),
        @Forward(name = "publications", path = "/researcher/researchUnit/listPublications.jsp"),
        @Forward(name = "managePersistedGroups", path = "/researcher/researchUnit/managePersistedGroups.jsp"),
        @Forward(name = "ShowUnitFunctionalities", path = "/researcher/researchUnit/showUnitFunctionalities.jsp"),
        @Forward(name = "editPublicationCollaborators", path = "/researcher/researchUnit/managePublicationCollaborators.jsp"),
        @Forward(name = "editFile", path = "/commons/unitFiles/editFile.jsp"),
        @Forward(name = "editPersistedGroup", path = "/commons/PersistentMemberGroups/editPersistedGroup.jsp"),
        @Forward(name = "createPersistedGroup", path = "/commons/PersistentMemberGroups/createPersistedGroup.jsp"),
        @Forward(name = "chooseUnit", path = "/researcher/researchUnit/chooseUnit.jsp") })
public class ResearchUnitFunctionalities extends UnitFunctionalities {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("functionalityAction", "researchUnitFunctionalities");
        request.setAttribute("module", "researcher");
        return super.execute(mapping, form, request, response);
    }

    @EntryPoint
    public ActionForward chooseUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Unit unit = getUnit(request);
        if (unit != null) {
            return prepare(mapping, form, request, response);
        }
        List<ResearchUnit> units = AccessControl.getPerson().getWorkingResearchUnitsAndParents();
        if (units.size() == 1) {
            request.setAttribute("unit", units.iterator().next());
            return prepare(mapping, form, request, response);
        }
        Collections.sort(units);
        request.setAttribute("units", units);
        return mapping.findForward("chooseUnit");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

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
        request.setAttribute("bean", publicationBean);
        putPublicationsInRequest(request, (ResearchUnit) unit);
        request.setAttribute("publications", unit.getResearchResultPublications());
        String publicationType = request.getParameter("publicationType");
        if (publicationType != null) {
            request.setAttribute("publicationType", publicationType);
        }

        return mapping.findForward("publications");
    }

    private void putPublicationsInRequest(HttpServletRequest request, ResearchUnit unit) {
        request.setAttribute("books", ResearchResultPublication.sort(unit.getBooks()));
        request.setAttribute("national-articles", ResearchResultPublication.sort(unit.getArticles(ScopeType.NATIONAL)));
        request.setAttribute("international-articles", ResearchResultPublication.sort(unit.getArticles(ScopeType.INTERNATIONAL)));
        request.setAttribute("national-inproceedings", ResearchResultPublication.sort(unit.getInproceedings(ScopeType.NATIONAL)));
        request.setAttribute("international-inproceedings",
                ResearchResultPublication.sort(unit.getInproceedings(ScopeType.INTERNATIONAL)));
        request.setAttribute("proceedings", ResearchResultPublication.sort(unit.getProceedings()));
        request.setAttribute("theses", ResearchResultPublication.sort(unit.getTheses()));
        request.setAttribute("manuals", ResearchResultPublication.sort(unit.getManuals()));
        request.setAttribute("technicalReports", ResearchResultPublication.sort(unit.getTechnicalReports()));
        request.setAttribute("otherPublications", ResearchResultPublication.sort(unit.getOtherPublications()));
        request.setAttribute("unstructureds", ResearchResultPublication.sort(unit.getUnstructureds()));
        request.setAttribute("inbooks", ResearchResultPublication.sort(unit.getInbooks()));

    }
}
