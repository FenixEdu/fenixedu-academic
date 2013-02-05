package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;

/**
 * @author Fernanda Quitério Created on 6/Set/2004
 * 
 */
public class InfoMasterDegreeThesisDataVersionWithGuidersAndResp extends InfoMasterDegreeThesisDataVersionWithGuiders {

    @Override
    public void copyFromDomain(MasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        super.copyFromDomain(masterDegreeThesisDataVersion);
        if (masterDegreeThesisDataVersion != null) {
            setInfoResponsibleEmployee(InfoEmployee.newInfoFromDomain(masterDegreeThesisDataVersion.getResponsibleEmployee()));
        }
    }

    public static InfoMasterDegreeThesisDataVersion newInfoFromDomain(MasterDegreeThesisDataVersion masterDegreeThesisDataVersion) {
        InfoMasterDegreeThesisDataVersionWithGuidersAndResp infoMasterDegreeThesisDataVersionWithGuidersAndResp = null;
        if (masterDegreeThesisDataVersion != null) {
            infoMasterDegreeThesisDataVersionWithGuidersAndResp = new InfoMasterDegreeThesisDataVersionWithGuidersAndResp();
            infoMasterDegreeThesisDataVersionWithGuidersAndResp.copyFromDomain(masterDegreeThesisDataVersion);
        }
        return infoMasterDegreeThesisDataVersionWithGuidersAndResp;
    }
}