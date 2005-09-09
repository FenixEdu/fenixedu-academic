/*
 * Created on 13/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa and rmalo
 * 
 */

public class EnrollGroupShift implements IService {

    public boolean run(Integer studentGroupCode, Integer groupPropertiesCode, Integer newShiftCode,
            String username) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentGrouping persistentGroupProperties = persistentGroupProperties = persistentSupport
                .getIPersistentGrouping();
        IGrouping groupProperties = (IGrouping) persistentGroupProperties.readByOID(Grouping.class,
                groupPropertiesCode);
        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        IPersistentStudentGroup persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
        IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
                StudentGroup.class, studentGroupCode);
        if (studentGroup == null)
            throw new InvalidArgumentsServiceException();

        ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();
        IShift shift = (IShift) persistentShift.readByOID(Shift.class, newShiftCode);
        if (groupProperties.getShiftType() == null || studentGroup.getShift() != null
                || (!groupProperties.getShiftType().equals(shift.getTipo()))) {
            throw new InvalidStudentNumberServiceException();
        }

        IStudent student = persistentSupport.getIPersistentStudent().readByUsername(username);

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentInGrouping(groupProperties, username)) {
            throw new NotAuthorizedException();
        }

        if (!checkStudentInStudentGroup(student, studentGroup)) {
            throw new InvalidSituationServiceException();
        }

        boolean result = strategy.checkNumberOfGroups(groupProperties, shift);
        if (!result) {
            throw new InvalidChangeServiceException();
        }
        persistentStudentGroup.simpleLockWrite(studentGroup);
        studentGroup.setShift(shift);
        return true;
    }

    private boolean checkStudentInStudentGroup(IStudent student, IStudentGroup studentGroup)
            throws FenixServiceException {

        for (final IAttends attend : studentGroup.getAttends()) {
            if (attend.getAluno() == student) {
                return true;
            }
        }
        return false;
    }
}