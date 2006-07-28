package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageMaterialSpaceOccupationsDA extends FenixDispatchAction {

    public ActionForward showMaterialSpaceOccupations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        readAndSetSpaceInformation(request);
        return mapping.findForward("showMaterialSpaceOccupations");
    }

    public ActionForward prepareInsertMaterialOccupation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
        
        SpaceInformation spaceInformation = readAndSetSpaceInformation(request);
        readAndSetUnitsTree(request, spaceInformation);
        return mapping.findForward("insertMaterialOccupation");
    }

    private void readAndSetUnitsTree(HttpServletRequest request, SpaceInformation spaceInformation) throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
        String path = "/SpaceManager/manageSpaceResponsibility.do?method=manageResponsabilityInterval";
        String unitsTree = UnitUtils.getInstitutionTree(request, spaceInformation, "spaceInformationID", path);        
        request.setAttribute("unitsList", unitsTree);
    }

    private SpaceInformation readAndSetSpaceInformation(HttpServletRequest request) {
        final SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
        return spaceInformation;
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString = request.getParameterMap().containsKey(
                "spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
                .getAttribute("spaceInformationID");
        final Integer spaceInformationID = spaceInformationIDString != null ? Integer
                .valueOf(spaceInformationIDString) : null;
        return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }  
}
