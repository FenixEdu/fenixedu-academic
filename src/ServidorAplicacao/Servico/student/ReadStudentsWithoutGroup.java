/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ISiteComponent;
import DataBeans.InfoPerson;
import DataBeans.InfoSiteStudentsWithoutGroup;
import DataBeans.InfoStudent;
import Dominio.GroupProperties;
import Dominio.IAttendsSet;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentGroupPolicyType;

/**
 * @author asnr and scpo
 * 
 */
public class ReadStudentsWithoutGroup implements IService {

    public class NewStudentGroupAlreadyExists extends FenixServiceException {}

    public ISiteComponent run(Integer groupPropertiesCode, String username)
            throws FenixServiceException, ExcepcaoPersistencia {

        List frequentas = new ArrayList();
        IStudent userStudent = null;
        List infoStudentList = null;
        InfoSiteStudentsWithoutGroup infoSiteStudentsWithoutGroup = new InfoSiteStudentsWithoutGroup();

        ISuportePersistente ps = SuportePersistenteOJB.getInstance();
        IPersistentStudent persistentStudent = ps.getIPersistentStudent();
        IPersistentStudentGroup persistentStudentGroup = ps.getIPersistentStudentGroup();
        IPersistentStudentGroupAttend persistentStudentGroupAttend = ps
                .getIPersistentStudentGroupAttend();

        IGroupProperties groupProperties = (IGroupProperties) ps.getIPersistentGroupProperties()
                .readByOID(GroupProperties.class, groupPropertiesCode);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        IAttendsSet attendsSet = groupProperties.getAttendsSet();

        List allStudentsGroups = attendsSet.getStudentGroups();

        Integer groupNumber = new Integer(1);
        if (allStudentsGroups.size() != 0) {
            Collections.sort(allStudentsGroups, new BeanComparator("groupNumber"));
            Integer lastGroupNumber = ((IStudentGroup) allStudentsGroups
                    .get(allStudentsGroups.size() - 1)).getGroupNumber();
            groupNumber = new Integer(lastGroupNumber.intValue() + 1);

        }

        IStudentGroup newStudentGroup = persistentStudentGroup
                .readStudentGroupByAttendsSetAndGroupNumber(groupProperties.getAttendsSet(), groupNumber);

        if (newStudentGroup != null) {
            throw new NewStudentGroupAlreadyExists();
        }

        infoSiteStudentsWithoutGroup.setGroupNumber(groupNumber);

        userStudent = persistentStudent.readByUsername(username);
        final InfoStudent infoStudent = getInfoStudentFromStudent(userStudent);
        infoSiteStudentsWithoutGroup.setInfoUserStudent(infoStudent);

        if (groupProperties.getEnrolmentPolicy().equals(new EnrolmentGroupPolicyType(2))) {
            return infoSiteStudentsWithoutGroup;
        }

        frequentas = groupProperties.getAttendsSet().getAttends();

        List allStudentGroupAttend = new ArrayList();
        Iterator iterator = allStudentsGroups.iterator();
        IFrequenta frequenta = null;
        while (iterator.hasNext()) {
            allStudentGroupAttend = persistentStudentGroupAttend
                    .readAllByStudentGroup((IStudentGroup) iterator.next());
            Iterator iter = allStudentGroupAttend.iterator();
            while (iter.hasNext()) {
                frequenta = ((IStudentGroupAttend) iter.next()).getAttend();
                if (frequentas.contains(frequenta)) {
                    frequentas.remove(frequenta);
                }
            }
        }

        IStudent student = null;
        Iterator iterStudent = frequentas.iterator();
        infoStudentList = new ArrayList();
        while (iterStudent.hasNext()) {
            student = ((IFrequenta) iterStudent.next()).getAluno();
            if (!student.equals(userStudent)) {
                final InfoStudent infoStudent2 = getInfoStudentFromStudent(student);
                infoStudentList.add(infoStudent2);
            }
        }

        infoSiteStudentsWithoutGroup.setInfoStudentList(infoStudentList);

        return infoSiteStudentsWithoutGroup;
    }

    protected InfoStudent getInfoStudentFromStudent(IStudent userStudent) {
        final InfoStudent infoStudent = InfoStudent.newInfoFromDomain(userStudent);
        final InfoPerson infoPerson = InfoPerson.newInfoFromDomain(userStudent.getPerson());
        infoStudent.setInfoPerson(infoPerson);
        return infoStudent;
    }
}