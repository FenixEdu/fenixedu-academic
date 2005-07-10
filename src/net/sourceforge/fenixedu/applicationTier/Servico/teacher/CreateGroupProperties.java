/*
 * Created on 28/Jul/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProperties;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.AttendInAttendsSet;
import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 *  
 */

public class CreateGroupProperties implements IService {

    public boolean run(Integer executionCourseCode, InfoGroupProperties infoGroupProperties)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();
        IPersistentGroupProperties persistentGroupProperties = persistentSupport
                .getIPersistentGroupProperties();
        IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = persistentSupport
                .getIPersistentGroupPropertiesExecutionCourse();
        IPersistentAttendsSet persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = persistentSupport
                .getIPersistentAttendInAttendsSet();
        IFrequentaPersistente persistentFrequenta = persistentSupport.getIFrequentaPersistente();

        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseCode);

        // Checks if already exists a groupProperties with the same name,
        // related with the executionCourse
        if (executionCourse.getGroupPropertiesByName(infoGroupProperties.getName()) != null) {
            throw new ExistingServiceException();
        }
        List attends = new ArrayList();
        attends = persistentFrequenta.readByExecutionCourse(executionCourseCode);

        IGroupProperties newGroupProperties = Cloner
                .copyInfoGroupProperties2IGroupProperties(infoGroupProperties);
        persistentGroupProperties.simpleLockWrite(newGroupProperties);

        IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse;
        groupPropertiesExecutionCourse = new GroupPropertiesExecutionCourse(newGroupProperties,
                executionCourse);
        persistentGroupPropertiesExecutionCourse.simpleLockWrite(groupPropertiesExecutionCourse);
        groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(1)));

        IAttendsSet attendsSet;
        attendsSet = new AttendsSet();
        attendsSet.setName(executionCourse.getNome());
        persistentAttendsSet.simpleLockWrite(attendsSet);

        IAttendInAttendsSet attendInAttendsSet;
        Iterator iterAttends = attends.iterator();
        while (iterAttends.hasNext()) {
            IAttends frequenta = (IAttends) iterAttends.next();

            attendInAttendsSet = new AttendInAttendsSet(frequenta, attendsSet);
            persistentAttendInAttendsSet.simpleLockWrite(attendInAttendsSet);
            attendsSet.addAttendInAttendsSet(attendInAttendsSet);
            frequenta.addAttendInAttendsSet(attendInAttendsSet);
        }
        attendsSet.setGroupProperties(newGroupProperties);

        newGroupProperties.setAttendsSet(attendsSet);
        newGroupProperties.addGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
        executionCourse.addGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);

        return true;
    }
}
