/*
 * Created on 17/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.IOrientation;
import net.sourceforge.fenixedu.domain.teacher.IPublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.IServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.IWeeklyOcupation;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class EditTeacherInformation implements IService {

    /**
     * Executes the service.
     * 
     * @throws ExcepcaoPersistencia
     */
    public Boolean run(InfoServiceProviderRegime infoServiceProviderRegime,
            InfoWeeklyOcupation infoWeeklyOcupation, List<InfoOrientation> infoOrientations,
            List<InfoPublicationsNumber> infoPublicationsNumbers) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IPersistentTeacher pteacher = sp.getIPersistentTeacher();
        ITeacher teacher = (ITeacher) pteacher.readByOID(Teacher.class, infoServiceProviderRegime
                .getInfoTeacher().getIdInternal());

        // Service Provider Regime
        final IPersistentServiceProviderRegime persistentServiceProviderRegime = sp
                .getIPersistentServiceProviderRegime();
        IServiceProviderRegime serviceProviderRegimeToLock = (IServiceProviderRegime) persistentServiceProviderRegime
                .readByOID(ServiceProviderRegime.class, infoServiceProviderRegime.getIdInternal(), true);

        if (serviceProviderRegimeToLock == null) {
            serviceProviderRegimeToLock = new ServiceProviderRegime();
            persistentServiceProviderRegime.simpleLockWrite(serviceProviderRegimeToLock);
            serviceProviderRegimeToLock.setTeacher(teacher);
        }
        serviceProviderRegimeToLock.setProviderRegimeType(infoServiceProviderRegime
                .getProviderRegimeType());
        serviceProviderRegimeToLock.setLastModificationDate(Calendar.getInstance().getTime());

        // Weekly Ocupation
        final IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
        IWeeklyOcupation weeklyOcupationToLock = (IWeeklyOcupation) persistentWeeklyOcupation.readByOID(
                WeeklyOcupation.class, infoWeeklyOcupation.getIdInternal(), true);

        if (weeklyOcupationToLock == null) {
            weeklyOcupationToLock = new WeeklyOcupation();
            persistentWeeklyOcupation.simpleLockWrite(weeklyOcupationToLock);
            weeklyOcupationToLock.setTeacher(teacher);
        }
        weeklyOcupationToLock.setOther(infoWeeklyOcupation.getOther());
        weeklyOcupationToLock.setLecture(infoWeeklyOcupation.getLecture());
        weeklyOcupationToLock.setManagement(infoWeeklyOcupation.getManagement());
        weeklyOcupationToLock.setResearch(infoWeeklyOcupation.getResearch());
        weeklyOcupationToLock.setSupport(infoWeeklyOcupation.getSupport());
        weeklyOcupationToLock.setLastModificationDate(Calendar.getInstance().getTime());

        // Orientations
        final IPersistentOrientation persistentOrientation = sp.getIPersistentOrientation();
        for (InfoOrientation infoOrientation : infoOrientations) {
            IOrientation orientationToLock = (IOrientation) persistentOrientation.readByOID(
                    Orientation.class, infoOrientation.getIdInternal(), true);

            if (orientationToLock == null) {
                orientationToLock = new Orientation();
                persistentOrientation.simpleLockWrite(orientationToLock);
                orientationToLock.setTeacher(teacher);
            }

            orientationToLock.setDescription(infoOrientation.getDescription());
            orientationToLock.setNumberOfStudents(infoOrientation.getNumberOfStudents());
            orientationToLock.setOrientationType(infoOrientation.getOrientationType());
            orientationToLock.setLastModificationDate(Calendar.getInstance().getTime());
        }

        // Publications Number
        final IPersistentPublicationsNumber persistentPublicationsNumber = sp
                .getIPersistentPublicationsNumber();
        for (InfoPublicationsNumber infoPublicationsNumber : infoPublicationsNumbers) {
            IPublicationsNumber publicationsNumberToLock = (IPublicationsNumber) persistentPublicationsNumber
                    .readByOID(PublicationsNumber.class, infoPublicationsNumber.getIdInternal(), true);

            if (publicationsNumberToLock == null) {
                publicationsNumberToLock = new PublicationsNumber();
                persistentPublicationsNumber.simpleLockWrite(publicationsNumberToLock);
                publicationsNumberToLock.setTeacher(teacher);
            }

            publicationsNumberToLock.setNational(infoPublicationsNumber.getNational());
            publicationsNumberToLock.setInternational(infoPublicationsNumber.getInternational());
            publicationsNumberToLock.setPublicationType(infoPublicationsNumber.getPublicationType());
            publicationsNumberToLock.setLastModificationDate(Calendar.getInstance().getTime());
        }

        // TODO <cargos de gestão>

        return true;
    }

}
