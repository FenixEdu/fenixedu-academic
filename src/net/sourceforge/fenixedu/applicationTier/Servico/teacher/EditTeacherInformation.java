/*
 * Created on 17/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOrientation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoPublicationsNumber;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoServiceProviderRegime;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;
import net.sourceforge.fenixedu.util.beanUtils.FenixPropertyUtils;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditTeacherInformation implements IService {
    /**
     *  
     */
    public EditTeacherInformation() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "EditTeacherInformation";
    }

    /**
     * Executes the service.
     */
    public Boolean run(InfoServiceProviderRegime infoServiceProviderRegime,
            InfoWeeklyOcupation infoWeeklyOcupation, List infoOrientations, List infoPublicationsNumbers)
            throws FenixServiceException {
        try {
            Date date = Calendar.getInstance().getTime();
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher pteacher = sp.getIPersistentTeacher();
            ITeacher teacher = (ITeacher) pteacher.readByOID(Teacher.class,infoServiceProviderRegime.getInfoTeacher().getIdInternal());
            
            IPersistentServiceProviderRegime persistentServiceProviderRegime = sp
                    .getIPersistentServiceProviderRegime();
            Integer serviceProviderRegimeId = infoServiceProviderRegime.getIdInternal();
            IServiceProviderRegime serviceProviderRegimeToLock = (IServiceProviderRegime) persistentServiceProviderRegime
                    .readByOID(ServiceProviderRegime.class, serviceProviderRegimeId, true);
            IServiceProviderRegime newServiceProviderRegime = Cloner
                    .copyInfoServiceProviderRegime2IServiceProviderRegime(infoServiceProviderRegime);

            if (serviceProviderRegimeToLock == null) {
                serviceProviderRegimeToLock = new ServiceProviderRegime();
                persistentServiceProviderRegime.simpleLockWrite(serviceProviderRegimeToLock);
                serviceProviderRegimeToLock.setTeacher(teacher);
            }
            //Integer ackOptLock = serviceProviderRegimeToLock.getAckOptLock();
            FenixPropertyUtils.copyProperties(serviceProviderRegimeToLock, newServiceProviderRegime);
            //PropertyUtils.copyProperties(serviceProviderRegimeToLock, newServiceProviderRegime);
            //serviceProviderRegimeToLock.setAckOptLock(ackOptLock);
            serviceProviderRegimeToLock.setLastModificationDate(date);

            IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
            Integer weeklyOcupationId = infoWeeklyOcupation.getIdInternal();
            IWeeklyOcupation weeklyOcupationToLock = (IWeeklyOcupation) persistentWeeklyOcupation
                    .readByOID(WeeklyOcupation.class, weeklyOcupationId, true);
            IWeeklyOcupation newWeeklyOcupation = Cloner
                    .copyInfoWeeklyOcupation2IWeeklyOcupation(infoWeeklyOcupation);

            if (weeklyOcupationToLock == null) {
                weeklyOcupationToLock = new WeeklyOcupation();
                persistentWeeklyOcupation.simpleLockWrite(weeklyOcupationToLock);
                weeklyOcupationToLock.setTeacher(teacher);
            }
            //ackOptLock = weeklyOcupationToLock.getAckOptLock();
            FenixPropertyUtils.copyProperties(weeklyOcupationToLock, newWeeklyOcupation);
            //PropertyUtils.copyProperties(weeklyOcupationToLock, newWeeklyOcupation);
            //weeklyOcupationToLock.setAckOptLock(ackOptLock);
            weeklyOcupationToLock.setLastModificationDate(date);
            
            IPersistentOrientation persistentOrientation = sp.getIPersistentOrientation();
            Iterator iter = infoOrientations.iterator();
            while (iter.hasNext()) {
                InfoOrientation infoOrientation = (InfoOrientation) iter.next();
                Integer orientationId = infoOrientation.getIdInternal();
                IOrientation orientationToLock = (IOrientation) persistentOrientation.readByOID(
                        Orientation.class, orientationId, true);

                IOrientation newOrientation = Cloner.copyInfoOrientation2IOrientation(infoOrientation);

                if (orientationToLock == null) {
                    orientationToLock = new Orientation();
                    persistentOrientation.simpleLockWrite(orientationToLock);
                    orientationToLock.setTeacher(teacher);
                }
                //ackOptLock = orientationToLock.getAckOptLock();
                FenixPropertyUtils.copyProperties(orientationToLock, newOrientation);
                //PropertyUtils.copyProperties(orientationToLock, newOrientation);
                //orientationToLock.setAckOptLock(ackOptLock);
                orientationToLock.setLastModificationDate(date);
            }

            IPersistentPublicationsNumber persistentPublicationsNumber = sp
                    .getIPersistentPublicationsNumber();
            iter = infoPublicationsNumbers.iterator();
            while (iter.hasNext()) {
                InfoPublicationsNumber infoPublicationsNumber = (InfoPublicationsNumber) iter.next();
                Integer publicationsNumberId = infoPublicationsNumber.getIdInternal();
                IPublicationsNumber publicationsNumberToLock = (IPublicationsNumber) persistentPublicationsNumber
                        .readByOID(PublicationsNumber.class, publicationsNumberId, true);
                IPublicationsNumber newPublicationsNumber = Cloner
                        .copyInfoPublicationsNumber2IPublicationsNumber(infoPublicationsNumber);

                if (publicationsNumberToLock == null) {
                    publicationsNumberToLock = new PublicationsNumber();
                    persistentPublicationsNumber.simpleLockWrite(publicationsNumberToLock);
                    publicationsNumberToLock.setTeacher(teacher);
                }
                //ackOptLock = publicationsNumberToLock.getAckOptLock();
                FenixPropertyUtils.copyProperties(publicationsNumberToLock, newPublicationsNumber);
                //PropertyUtils.copyProperties(publicationsNumberToLock, newPublicationsNumber);
                //publicationsNumberToLock.setAckOptLock(ackOptLock);
                publicationsNumberToLock.setLastModificationDate(date);
            }
            // TODO: faltam os cargos de gestão

            return new Boolean(true);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
    }
}