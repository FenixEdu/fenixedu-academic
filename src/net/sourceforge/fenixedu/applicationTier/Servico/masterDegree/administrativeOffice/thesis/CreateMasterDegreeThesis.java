package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.GuiderAlreadyChosenServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class CreateMasterDegreeThesis implements IService {

    public void run(IUserView userView, InfoStudentCurricularPlan infoStudentCurricularPlan,
            String dissertationTitle, List infoTeacherGuiders, List infoTeacherAssistentGuiders,
            List infoExternalPersonExternalGuiders, List infoExternalPersonExternalAssistentGuiders)
            throws FenixServiceException {

        try {

            //	check duplicate guiders and assistent guiders
            for (Iterator iter = infoTeacherGuiders.iterator(); iter.hasNext();) {
                InfoTeacher guider = (InfoTeacher) iter.next();

                for (Iterator iterator = infoTeacherAssistentGuiders.iterator(); iterator.hasNext();) {
                    InfoTeacher assistentGuider = (InfoTeacher) iterator.next();
                    if (assistentGuider.getIdInternal().equals(guider.getIdInternal())) {
                        throw new GuiderAlreadyChosenServiceException(
                                "error.exception.masterDegree.guiderAlreadyChosen");
                    }
                }
            }

            // check duplicate external guiders and external assistent guiders
            for (Iterator iter = infoExternalPersonExternalGuiders.iterator(); iter.hasNext();) {
                InfoExternalPerson externalGuider = (InfoExternalPerson) iter.next();

                for (Iterator iterator = infoExternalPersonExternalAssistentGuiders.iterator(); iterator
                        .hasNext();) {
                    InfoExternalPerson externalAssistentGuider = (InfoExternalPerson) iterator.next();
                    if (externalAssistentGuider.getIdInternal().equals(externalGuider.getIdInternal())) {
                        throw new GuiderAlreadyChosenServiceException(
                                "error.exception.masterDegree.externalGuiderAlreadyChosen");
                    }
                }
            }

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudentCurricularPlan studentCurricularPlan = Cloner
                    .copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

            IMasterDegreeThesis storedMasterDegreeThesis = sp.getIPersistentMasterDegreeThesis()
                    .readByStudentCurricularPlan(studentCurricularPlan);
            if (storedMasterDegreeThesis != null)
                throw new ExistingServiceException(
                        "error.exception.masterDegree.existingMasterDegreeThesis");

            IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = sp
                    .getIPersistentMasterDegreeThesisDataVersion().readActiveByDissertationTitle(
                            dissertationTitle);
            if (storedMasterDegreeThesisDataVersion != null)
                if (!storedMasterDegreeThesisDataVersion.getMasterDegreeThesis()
                        .getStudentCurricularPlan().equals(studentCurricularPlan))
                    throw new ExistingServiceException(
                            "error.exception.masterDegree.dissertationTitleAlreadyChosen");

            IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = sp.getIPersistentEmployee().readByPerson(
                    person.getIdInternal().intValue());

            IMasterDegreeThesis masterDegreeThesis = new MasterDegreeThesis(studentCurricularPlan);
            sp.getIPersistentMasterDegreeThesis().simpleLockWrite(masterDegreeThesis);

            //write data version
            IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = new MasterDegreeThesisDataVersion(
                    masterDegreeThesis, employee, dissertationTitle,
                    new Timestamp(new Date().getTime()), new State(State.ACTIVE));
            List guiders = Cloner.copyListInfoTeacher2ListITeacher(infoTeacherGuiders);
            List assistentGuiders = Cloner.copyListInfoTeacher2ListITeacher(infoTeacherAssistentGuiders);
            List externalGuiders = Cloner
                    .copyListInfoExternalPerson2ListIExternalPerson(infoExternalPersonExternalGuiders);
            List externalAssistentGuiders = Cloner
                    .copyListInfoExternalPerson2ListIExternalPerson(infoExternalPersonExternalAssistentGuiders);
            masterDegreeThesisDataVersion.setGuiders(guiders);
            masterDegreeThesisDataVersion.setAssistentGuiders(assistentGuiders);
            masterDegreeThesisDataVersion.setExternalGuiders(externalGuiders);
            masterDegreeThesisDataVersion.setExternalAssistentGuiders(externalAssistentGuiders);
            sp.getIPersistentMasterDegreeThesisDataVersion().simpleLockWrite(
                    masterDegreeThesisDataVersion);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }
}