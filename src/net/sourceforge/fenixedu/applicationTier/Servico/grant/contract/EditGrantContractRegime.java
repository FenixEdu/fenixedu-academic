package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;

public class EditGrantContractRegime extends EditDomainObjectService {

    @Override
    protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject,
            IDomainObject domainObject) throws ExcepcaoPersistencia {
        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        IGrantContractRegime grantContractRegime = (IGrantContractRegime) domainObject;
        grantContractRegime.setDateBeginContract(infoGrantContractRegime.getDateBeginContract());
        grantContractRegime.setDateDispatchCC(infoGrantContractRegime.getDateDispatchCC());
        grantContractRegime.setDateDispatchCD(infoGrantContractRegime.getDateDispatchCD());
        grantContractRegime.setDateEndContract(infoGrantContractRegime.getDateEndContract());
        grantContractRegime.setDateSendDispatchCC(infoGrantContractRegime.getDateSendDispatchCC());
        grantContractRegime.setDateSendDispatchCD(infoGrantContractRegime.getDateSendDispatchCD());
        grantContractRegime.setCostCenterKey(infoGrantContractRegime.getCostCenterKey());
        grantContractRegime.setState(infoGrantContractRegime.getState());

        IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
        IGrantContract grantContract = (IGrantContract) persistentGrantContract.readByOID(
                GrantContract.class, infoGrantContractRegime.getInfoGrantContract().getIdInternal());
        grantContractRegime.setGrantContract(grantContract);

        if (grantContract.getCostCenterKey() != null
                && grantContract.getCostCenterKey() != new Integer(0)) {
            IPersistentGrantCostCenter persistentGrantCostCenter = sp.getIPersistentGrantCostCenter();
            IGrantCostCenter grantCostCenter = (IGrantCostCenter) persistentGrantCostCenter.readByOID(
                    GrantCostCenter.class, infoGrantContractRegime.getIdInternal());
            grantContractRegime.setGrantCostCenter(grantCostCenter);
        } else {
            grantContractRegime.setGrantCostCenter(null);
        }

    }

    @Override
    protected IDomainObject createNewDomainObject(InfoObject infoObject) {
        return DomainFactory.makeGrantContractRegime();
    }

    @Override
    protected Class getDomainObjectClass() {
        return GrantContractRegime.class;
    }

    @Override
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentGrantContractRegime();
    }

    @Override
    protected IDomainObject readObjectByUnique(InfoObject infoObject, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentGrantContractRegime persistentGrantContractRegime = sp
                .getIPersistentGrantContractRegime();
        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        return persistentGrantContractRegime.readByOID(GrantContractRegime.class,
                infoGrantContractRegime.getIdInternal());
    }

    public void run(InfoGrantContractRegime infoGrantContractRegime) throws Exception {
        super.run(new Integer(0), infoGrantContractRegime);
    }

    @Override
    protected void doAfterLock(IDomainObject domainObjectLocked, InfoObject infoObject,
            ISuportePersistente sp) throws FenixServiceException, ExcepcaoPersistencia {

        InfoGrantContractRegime infoGrantContractRegime = (InfoGrantContractRegime) infoObject;
        if (infoGrantContractRegime.getState().equals(InfoGrantContractRegime.getActiveState())) {
            // Active Contract Regime

            IGrantContractRegime grantContractRegime = (IGrantContractRegime) domainObjectLocked;
            // Set the correct grant orientation teacher

            IPersistentGrantOrientationTeacher persistentGrantOrientationTeacher = sp
                    .getIPersistentGrantOrientationTeacher();
            IPersistentGrantCostCenter pGrantCostCenter = sp.getIPersistentGrantCostCenter();
            IPersistentTeacher pTeacher = sp.getIPersistentTeacher();

            IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
            IGrantContract grantContract = (IGrantContract) persistentGrantContract.readByOID(
                    GrantContract.class, infoGrantContractRegime.getInfoGrantContract().getIdInternal());

            if (infoGrantContractRegime.getGrantCostCenterInfo() != null
                    && ((infoGrantContractRegime.getGrantCostCenterInfo().getNumber()).trim()).length() > 0) { // ||
                IGrantCostCenter grantCostCenter = pGrantCostCenter
                        .readGrantCostCenterByNumber(infoGrantContractRegime.getGrantCostCenterInfo()
                                .getNumber());
                if (grantCostCenter == null)
                    throw new GrantOrientationTeacherNotFoundException();
                grantContract.setGrantCostCenter(grantCostCenter);

            } else {
                grantContract.setGrantCostCenter(null);
            }

            grantContractRegime.setGrantCostCenter(grantContract.getGrantCostCenter());

            IGrantOrientationTeacher grantOrientationTeacher = persistentGrantOrientationTeacher
                    .readActualGrantOrientationTeacherByContract(grantContract.getIdInternal(),
                            new Integer(0));
            if (grantOrientationTeacher != null) {

                persistentGrantOrientationTeacher.simpleLockWrite(grantOrientationTeacher);
                InfoTeacher infoTeacher = new InfoTeacher();
                // If grantOrientationTeacher is filled in
                // grantContractRegime
                final ITeacher teacher;
                if (infoGrantContractRegime.getInfoTeacher() != null) {
                    if (infoGrantContractRegime.getInfoTeacher().getTeacherNumber().equals(
                            grantOrientationTeacher.getOrientationTeacher().getTeacherNumber())) {
                        // Update grant orientation teacher of contract
                        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

                        teacher = teacherDAO.readByNumber(grantOrientationTeacher
                                .getOrientationTeacher().getTeacherNumber());
                        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
                        IPerson person = (IPerson) persistentPerson.readByOID(Person.class, teacher
                                .getPerson().getIdInternal());
                        infoTeacher.setTeacherNumber(teacher.getTeacherNumber());
                        InfoPerson infoPerson = new InfoPerson();
                        infoPerson = getInfoPerson(person);
                        infoTeacher.setInfoPerson(infoPerson);
                    } else {
                        teacher = pTeacher.readByNumber(infoGrantContractRegime.getInfoTeacher()
                                .getTeacherNumber());
                        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
                        IPerson person = (IPerson) persistentPerson.readByOID(Person.class, teacher
                                .getPerson().getIdInternal());
                        infoTeacher.setTeacherNumber(teacher.getTeacherNumber());
                        InfoPerson infoPerson = new InfoPerson();
                        infoPerson = getInfoPerson(person);
                        infoTeacher.setInfoPerson(infoPerson);
                    }

                    grantOrientationTeacher.setOrientationTeacher(teacher);
                }
                grantOrientationTeacher.setBeginDate(infoGrantContractRegime.getDateBeginContract());
                grantOrientationTeacher.setEndDate(infoGrantContractRegime.getDateEndContract());

                grantContractRegime.setTeacher(grantOrientationTeacher.getOrientationTeacher());

            }

            // Set all the others GrantContractRegime that are active to state
            // inactive

            IPersistentGrantContractRegime persistentGrantContractRegime = sp
                    .getIPersistentGrantContractRegime();
            List<IGrantContractRegime> activeContractRegime = persistentGrantContractRegime
                    .readGrantContractRegimeByGrantContractAndState(grantContractRegime
                            .getGrantContract().getIdInternal(), new Integer(1));
            if (activeContractRegime != null && !activeContractRegime.isEmpty()) {
                // Desactivate the contracts
                for (IGrantContractRegime grantContractRegimeTemp : activeContractRegime) {
                    if (!grantContractRegimeTemp.equals(grantContractRegime)) {
                        grantContractRegimeTemp.setState(InfoGrantContractRegime.getInactiveState());
                    }
                }
            }
        }

    }

    protected InfoPerson getInfoPerson(IPerson person) {
        InfoPerson infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPerson();

            infoPerson.setIdInternal(person.getIdInternal());
            infoPerson.setNome(person.getNome());
            infoPerson.setUsername(person.getUsername());

        }
        return infoPerson;
    }

}
