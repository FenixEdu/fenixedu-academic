/*
 * Created on 04/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.AttendInAttendsSet;
import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroupAttend;
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

public class DeleteAllAttendsSetMembers implements IService {

    public boolean run(Integer objectCode, Integer attendsSetCode) throws FenixServiceException,
            ExcepcaoPersistencia {
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

        List attendsSetElements = new ArrayList();
        attendsSetElements.addAll(attendsSet.getAttendInAttendsSet());
        Iterator iterator = attendsSetElements.iterator();
        while (iterator.hasNext()) {
            IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet) iterator.next();
            IAttends frequenta = attendInAttendsSet.getAttend();

            boolean found = false;
            Iterator iterStudentsGroups = attendsSet.getStudentGroups().iterator();
            while (iterStudentsGroups.hasNext() && !found) {

                IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend
                        .readByStudentGroupAndAttend(((IStudentGroup) iterStudentsGroups.next())
                                .getIdInternal(), frequenta.getIdInternal());
                if (oldStudentGroupAttend != null) {

                    oldStudentGroupAttend.getStudentGroup().getStudentGroupAttends().remove(
                            oldStudentGroupAttend);
                    oldStudentGroupAttend.setStudentGroup(null);
                    oldStudentGroupAttend.getAttend().getStudentGroupAttends().remove(
                            oldStudentGroupAttend);
                    oldStudentGroupAttend.setAttend(null);

                    persistentStudentGroupAttend.deleteByOID(StudentGroupAttend.class,
                            oldStudentGroupAttend.getIdInternal());
                    found = true;
                }
            }
            frequenta.removeAttendInAttendsSet(attendInAttendsSet);
            attendsSet.removeAttendInAttendsSet(attendInAttendsSet);
            persistentAttendInAttendsSet.deleteByOID(AttendInAttendsSet.class, attendInAttendsSet
                    .getIdInternal());
        }

        return true;
    }
}