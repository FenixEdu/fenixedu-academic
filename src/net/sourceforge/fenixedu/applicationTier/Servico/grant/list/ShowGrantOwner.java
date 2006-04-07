/*
 * Created on Jun 21, 2004
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPart;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidyWithContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantOwnerComplete;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;

/**
 * @author Pica
 * @author Barbosa
 */
public class ShowGrantOwner extends Service {

    public ShowGrantOwner() {
    }

    private void buildInfoListGrantOwnerComplete(InfoListGrantOwnerComplete infoListGrantOwnerComplete,
            GrantOwner grantOwner) throws FenixServiceException {
        List listInfoListGrantContracts = new ArrayList();
        try {
            // set grantOwner info
            IPersistentGrantContract persistentGrantContract = persistentSupport
                    .getIPersistentGrantContract();
            infoListGrantOwnerComplete.setInfoGrantOwner(InfoGrantOwnerWithPerson
                    .newInfoFromDomain(grantOwner));
            List grantContractsList = persistentGrantContract.readAllContractsByGrantOwner(grantOwner
                    .getIdInternal());
            Iterator contractsIter = grantContractsList.iterator();
            // set list of qualifications
            List infoQualificationsList = grantOwner.getPerson().getAssociatedQualifications();
            if (infoQualificationsList != null)
                infoListGrantOwnerComplete.setInfoQualifications(infoQualificationsList);
            while (contractsIter.hasNext()) {
                InfoListGrantContract infoListGrantContract = buildInfoListGrantContract((GrantContract) contractsIter
                        .next());
                listInfoListGrantContracts.add(infoListGrantContract);
            }
            Collections.reverse(listInfoListGrantContracts);
            infoListGrantOwnerComplete.setInfoListGrantContracts(listInfoListGrantContracts);
        } catch (Exception e) {
            throw new FenixServiceException();
        }
    }

    private InfoListGrantContract buildInfoListGrantContract(GrantContract grantContract)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoListGrantContract newInfoListGrantContract = new InfoListGrantContract();
        List listInfoListGrantSubsidies = new ArrayList();
        List infoContractRegimes = new ArrayList();
        List infoSubsidyParts = new ArrayList();

        // Set the grant contract
        newInfoListGrantContract.setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                .newInfoFromDomain(grantContract));
        // Set the grant orientation teacher for the contract
        GrantOrientationTeacher grantOrientationTeacher = grantContract
                .readActualGrantOrientationTeacher();
        newInfoListGrantContract.getInfoGrantContract().setGrantOrientationTeacherInfo(
                InfoGrantOrientationTeacherWithTeacherAndGrantContract
                        .newInfoFromDomain(grantOrientationTeacher));
        // Read the contract regimes
        List contractRegimes = grantContract.readGrantContractRegimeByGrantContract();
        Iterator regimesIter = contractRegimes.iterator();
        while (regimesIter.hasNext()) {
            InfoGrantContractRegime newInfoGrantContractRegime = InfoGrantContractRegimeWithTeacherAndContract
                    .newInfoFromDomain((GrantContractRegime) regimesIter.next());
            infoContractRegimes.add(newInfoGrantContractRegime);
        }
        newInfoListGrantContract.setInfoGrantContractRegimes(infoContractRegimes);
        // read the contract subsidies
        List contractSubsidies = grantContract.getAssociatedGrantSubsidies();
        Iterator subsidiesIter = contractSubsidies.iterator();
        while (subsidiesIter.hasNext()) {
            GrantSubsidy grantSubsidy = (GrantSubsidy) subsidiesIter.next();
            InfoListGrantSubsidy newInfoListGrantSubsidy = new InfoListGrantSubsidy();
            InfoGrantSubsidy newInfoGrantSubsidy = InfoGrantSubsidyWithContract
                    .newInfoFromDomain(grantSubsidy);
            newInfoListGrantSubsidy.setInfoGrantSubsidy(newInfoGrantSubsidy);
            // read the subsidy grant parts
            List subsidyParts = grantSubsidy.getAssociatedGrantParts();
            Iterator partsIter = subsidyParts.iterator();
            while (partsIter.hasNext()) {
                InfoGrantPart newInfoGrantPart = InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity
                        .newInfoFromDomain((GrantPart) partsIter.next());
                infoSubsidyParts.add(newInfoGrantPart);
            }
            newInfoListGrantSubsidy.setInfoGrantParts(infoSubsidyParts);
            listInfoListGrantSubsidies.add(newInfoListGrantSubsidy);
        }
        newInfoListGrantContract.setInfoListGrantSubsidys(listInfoListGrantSubsidies);

        return newInfoListGrantContract;
    }

    public InfoListGrantOwnerComplete run(Integer grantOwnerId) throws FenixServiceException,
            ExcepcaoPersistencia {
        InfoListGrantOwnerComplete infoListGrantOwnerComplete = null;

        GrantOwner grantOwner = rootDomainObject.readGrantOwnerByOID(grantOwnerId);
        if (grantOwner != null) {
            infoListGrantOwnerComplete = new InfoListGrantOwnerComplete();
            buildInfoListGrantOwnerComplete(infoListGrantOwnerComplete, grantOwner);
        }

        return infoListGrantOwnerComplete;
    }
}