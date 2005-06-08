/*
 * Created on 17/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */

public class PrepareEditStudentGroupMembers implements IService {

    public List run(Integer executionCourseCode, Integer studentGroupCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        List frequentas = new ArrayList();
        List infoStudentList = new ArrayList();

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentStudentGroupAttend = ps.getIPersistentStudentGroupAttend();

        IStudentGroup studentGroup = (IStudentGroup) ps.getIPersistentStudentGroup().readByOID(
                StudentGroup.class, studentGroupCode);

        frequentas.addAll(studentGroup.getAttendsSet().getAttends());

        List allStudentsGroups = studentGroup.getAttendsSet().getStudentGroups();

        List allStudentGroupAttend = null;

        Iterator iterator = allStudentsGroups.iterator();
        while (iterator.hasNext()) {
            IStudentGroup studentGroup2 = (IStudentGroup) iterator.next();
            allStudentGroupAttend = studentGroup2.getStudentGroupAttends();

            Iterator iterator2 = allStudentGroupAttend.iterator();
            IAttends frequenta = null;
            while (iterator2.hasNext()) {
                frequenta = ((IStudentGroupAttend) iterator2.next()).getAttend();
                if (frequentas.contains(frequenta)) {
                    frequentas.remove(frequenta);
                }
            }
        }

        IStudent student = null;
        Iterator iterator3 = frequentas.iterator();

        while (iterator3.hasNext()) {

            student = ((IAttends) iterator3.next()).getAluno();
            infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));
        }
        return infoStudentList;

    }
}
