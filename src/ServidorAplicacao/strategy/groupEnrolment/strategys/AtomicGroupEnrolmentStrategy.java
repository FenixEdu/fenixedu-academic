/*
 * Created on 24/Jul/2003
 */

package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.List;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */

public class AtomicGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements
        IGroupEnrolmentStrategy {

    public AtomicGroupEnrolmentStrategy() {
    }

    public Integer enrolmentPolicyNewGroup(IGroupProperties groupProperties,
            int numberOfStudentsToEnrole, ITurno shift) {

        if (checkNumberOfGroups(groupProperties, shift)) {
            Integer maximumCapacity = groupProperties.getMaximumCapacity();
            Integer minimumCapacity = groupProperties.getMinimumCapacity();
            Integer nrStudents = new Integer(numberOfStudentsToEnrole);

            if (maximumCapacity == null && minimumCapacity == null)
                return new Integer(1);
            if (minimumCapacity != null) {
                if (nrStudents.compareTo(minimumCapacity) < 0)
                    return new Integer(-2);
            }
            if (maximumCapacity != null) {
                if (nrStudents.compareTo(maximumCapacity) > 0)
                    return new Integer(-3);
            }
        } else
            return new Integer(-1);

        return new Integer(1);

    }

    public boolean checkNumberOfGroupElements(IGroupProperties groupProperties,
            IStudentGroup studentGroup) throws ExcepcaoPersistencia {

        boolean result = false;
        Integer minimumCapacity = groupProperties.getMinimumCapacity();

        if (minimumCapacity == null)
            result = true;
        else {

            List allStudentGroupAttend = new ArrayList();
            try {

                ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
                IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSuport
                        .getIPersistentStudentGroupAttend();

                allStudentGroupAttend = persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);

            } catch (ExcepcaoPersistencia ex) {
                throw ex;
            }

            int numberOfGroupElements = allStudentGroupAttend.size();

            if (numberOfGroupElements > minimumCapacity.intValue())
                result = true;

        }
        return result;
    }
}