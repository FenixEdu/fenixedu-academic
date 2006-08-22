package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditGrantContractRegime extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(InfoObject infoObject, DomainObject domainObject)
            throws ExcepcaoPersistencia {
        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        GrantContractRegime grantContractRegime = (GrantContractRegime) domainObject;
        grantContractRegime.setDateBeginContract(infoGrantContractRegime.getDateBeginContract());
        grantContractRegime.setDateDispatchCC(infoGrantContractRegime.getDateDispatchCC());
        grantContractRegime.setDateDispatchCD(infoGrantContractRegime.getDateDispatchCD());
        grantContractRegime.setDateEndContract(infoGrantContractRegime.getDateEndContract());
        grantContractRegime.setDateSendDispatchCC(infoGrantContractRegime.getDateSendDispatchCC());
        grantContractRegime.setDateSendDispatchCD(infoGrantContractRegime.getDateSendDispatchCD());
        grantContractRegime.setKeyGrantCostCenter(infoGrantContractRegime.getCostCenterKey());
        grantContractRegime.setState(infoGrantContractRegime.getState());

        GrantContract grantContract = rootDomainObject.readGrantContractByOID(infoGrantContractRegime
                .getInfoGrantContract().getIdInternal());
        grantContractRegime.setGrantContract(grantContract);

        if (grantContract.getKeyGrantCostCenter() != null && grantContract.getKeyGrantCostCenter() != 0) {
            GrantCostCenter grantCostCenter = (GrantCostCenter) rootDomainObject
                    .readGrantPaymentEntityByOID(infoGrantContractRegime.getIdInternal());
            grantContractRegime.setGrantCostCenter(grantCostCenter);
        } else {
            grantContractRegime.setGrantCostCenter(null);
        }

    }

    @Override
    protected DomainObject createNewDomainObject(InfoObject infoObject) {
        return new GrantContractRegime();
    }

    @Override
    protected DomainObject readObjectByUnique(InfoObject infoObject) throws ExcepcaoPersistencia {
        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        return rootDomainObject.readGrantContractRegimeByOID(infoGrantContractRegime.getIdInternal());
    }

    public void run(InfoGrantContractRegime infoGrantContractRegime) throws Exception {
        super.run(new Integer(0), infoGrantContractRegime);
    }

    @Override
    protected void doAfterLock(DomainObject domainObjectLocked, InfoObject infoObject)
            throws FenixServiceException, ExcepcaoPersistencia {

        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        if (infoGrantContractRegime.getState().equals(InfoGrantContractRegime.getActiveState())) {
            // Active Contract Regime

            GrantContractRegime grantContractRegime = (GrantContractRegime) domainObjectLocked;
            // Set the correct grant orientation teacher

            GrantContract grantContract = rootDomainObject
                    .readGrantContractByOID(infoGrantContractRegime.getInfoGrantContract()
                            .getIdInternal());

            if (infoGrantContractRegime.getGrantCostCenterInfo() != null
                    && ((infoGrantContractRegime.getGrantCostCenterInfo().getNumber()).trim()).length() > 0) { // ||
                GrantCostCenter grantCostCenter = GrantCostCenter
                        .readGrantCostCenterByNumber(infoGrantContractRegime.getGrantCostCenterInfo()
                                .getNumber());
                if (grantCostCenter == null)
                    throw new GrantOrientationTeacherNotFoundException();
                grantContract.setGrantCostCenter(grantCostCenter);

            } else {
                grantContract.setGrantCostCenter(null);
            }

            grantContractRegime.setGrantCostCenter(grantContract.getGrantCostCenter());

            GrantOrientationTeacher grantOrientationTeacher = grantContract
                    .readActualGrantOrientationTeacher();
            if (grantOrientationTeacher != null) {
                
                // If grantOrientationTeacher is filled in
                // grantContractRegime
                final Teacher teacher;
                if (infoGrantContractRegime.getInfoTeacher() != null) {
                    if (infoGrantContractRegime.getInfoTeacher().getTeacherNumber().equals(
                            grantOrientationTeacher.getOrientationTeacher().getTeacherNumber())) {
                        // Update grant orientation teacher of contract

                        teacher = grantOrientationTeacher.getOrientationTeacher();
                    } else {
                        teacher = Teacher.readByNumber(infoGrantContractRegime.getInfoTeacher()
                                .getTeacherNumber());
                    }

                    grantOrientationTeacher.setOrientationTeacher(teacher);
                }
                grantOrientationTeacher.setBeginDate(infoGrantContractRegime.getDateBeginContract());
                grantOrientationTeacher.setEndDate(infoGrantContractRegime.getDateEndContract());

                grantContractRegime.setTeacher(grantOrientationTeacher.getOrientationTeacher());

            }

            // Set all the others GrantContractRegime that are active to state
            // inactive

            List<GrantContractRegime> activeContractRegime = grantContractRegime.getGrantContract()
                    .readGrantContractRegimeByGrantContractAndState(new Integer(1));
            if (activeContractRegime != null && !activeContractRegime.isEmpty()) {
                // Desactivate the contracts
                for (GrantContractRegime grantContractRegimeTemp : activeContractRegime) {
                    if (!grantContractRegimeTemp.equals(grantContractRegime)) {
                        grantContractRegimeTemp.setState(InfoGrantContractRegime.getInactiveState());
                    }
                }
            }
        }

    }

    protected InfoPerson getInfoPerson(Person person) {
        InfoPerson infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPerson();

            infoPerson.setIdInternal(person.getIdInternal());
            infoPerson.setNome(person.getNome());
            infoPerson.setUsername(person.getUsername());

        }
        return infoPerson;
    }

	@Override
	protected DomainObject readDomainObject(Integer idInternal) {
		return rootDomainObject.readGrantContractRegimeByOID(idInternal);
	}

}
