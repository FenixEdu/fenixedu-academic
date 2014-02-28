package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificCommunicationApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ScientificCommunicationApp.class, path = "manage-files", titleKey = "label.manageFiles")
@Mapping(module = "scientificCouncil", path = "/scientificCouncilFiles")
@Forwards(value = { @Forward(name = "uploadFile", path = "/commons/unitFiles/uploadFile.jsp"),
        @Forward(name = "manageFiles", path = "/commons/unitFiles/manageFiles.jsp"),
        @Forward(name = "editUploaders", path = "/commons/PersistentMemberGroups/configureUploaders.jsp"),
        @Forward(name = "managePersistedGroups", path = "/commons/PersistentMemberGroups/managePersistedGroups.jsp"),
        @Forward(name = "editFile", path = "/commons/unitFiles/editFile.jsp"),
        @Forward(name = "editPersistedGroup", path = "/commons/PersistentMemberGroups/editPersistedGroup.jsp"),
        @Forward(name = "createPersistedGroup", path = "/commons/PersistentMemberGroups/createPersistedGroup.jsp") })
public class ManageUnitFilesDA extends UnitFunctionalities {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("module", "scientificCouncil");
        request.setAttribute("functionalityAction", "scientificCouncilFiles");
        return super.execute(mapping, form, request, response);
    }

    @Override
    protected Unit getUnit(HttpServletRequest request) {
        return ScientificCouncilUnit.getScientificCouncilUnit();
    }

}
