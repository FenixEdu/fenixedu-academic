/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.AttendInAttendsSet;
import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteAttendsSetMembers implements IService {

    public boolean run(Integer executionCourseCode, Integer attendsSetCode, List studentUsernames)
            throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentAttendsSet persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = persistentSupport
                .getIPersistentAttendInAttendsSet();
        IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSupport
                .getIPersistentStudentGroupAttend();

        IAttendsSet attendsSet = (IAttendsSet) persistentAttendsSet.readByOID(AttendsSet.class,
                attendsSetCode);

        if (attendsSet == null) {
            throw new ExistingServiceException();
        }

        IGroupProperties groupProperties = attendsSet.getGroupProperties();

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentsUserNamesInAttendsSet(studentUsernames, groupProperties)) {
            throw new InvalidSituationServiceException();
        }

        Iterator iterator = studentUsernames.iterator();
        while (iterator.hasNext()) {
            String username = (String) iterator.next();
            List attendInAttendsSetList = attendsSet.getAttendInAttendsSet();
            Iterator iterAttendInAttendsSet = attendInAttendsSetList.iterator();
            IAttendInAttendsSet attendInAttendsSet = null;
            boolean found1 = false;
            while (iterAttendInAttendsSet.hasNext() && !found1) {
                attendInAttendsSet = (IAttendInAttendsSet) iterAttendInAttendsSet.next();
                if (attendInAttendsSet.getAttend().getAluno().getPerson().getUsername().equals(username)) {
                    found1 = true;
                }
            }

            IAttends attend = attendInAttendsSet.getAttend();

            boolean found = false;
            Iterator iterStudentsGroups = attendsSet.getStudentGroups().iterator();
            while (iterStudentsGroups.hasNext() && !found) {

                IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend.readBy(
                        (IStudentGroup) iterStudentsGroups.next(), attend);
                if (oldStudentGroupAttend != null) {
                    persistentStudentGroupAttend.delete(oldStudentGroupAttend);
                    found = true;
                }
            }

            attendsSet.removeAttendInAttendsSet(attendInAttendsSet);
            attend.removeAttendInAttendsSet(attendInAttendsSet);
            persistentAttendInAttendsSet.deleteByOID(AttendInAttendsSet.class, attendInAttendsSet
                    .getIdInternal());

        }

        return true;
    }
}