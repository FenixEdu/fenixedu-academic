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

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteStudentsWithoutGroup;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IAttendsSet;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
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
public class ReadStudentsWithoutGroup implements IServico {

    private static ReadStudentsWithoutGroup service = new ReadStudentsWithoutGroup();

    /**
     * The singleton access method of this class.
     */
    public static ReadStudentsWithoutGroup getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadStudentsWithoutGroup() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "ReadStudentsWithoutGroup";
    }

    /**
     * Executes the service.
     */

    public ISiteComponent run(Integer groupPropertiesCode, String username)
            throws FenixServiceException {

        List frequentas = new ArrayList();
        IStudent userStudent = null;
        List infoStudentList = null;
        InfoSiteStudentsWithoutGroup infoSiteStudentsWithoutGroup = new InfoSiteStudentsWithoutGroup();
        try {

            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = ps.getIPersistentStudent();
            IFrequentaPersistente persistentAttend = ps
                    .getIFrequentaPersistente();
            IPersistentStudentGroup persistentStudentGroup = ps
                    .getIPersistentStudentGroup();
            IPersistentStudentGroupAttend persistentStudentGroupAttend = ps
                    .getIPersistentStudentGroupAttend();

            IGroupProperties groupProperties = (IGroupProperties) ps
                    .getIPersistentGroupProperties().readByOID(
                            GroupProperties.class, groupPropertiesCode);

            if(groupProperties == null){
            	throw new ExistingServiceException();
             }
            

            IAttendsSet attendsSet = (IAttendsSet)groupProperties.getAttendsSet();
            

            List allStudentsGroups = attendsSet.getStudentGroups();
            
            
           
            Integer groupNumber = new Integer(1);
            if (allStudentsGroups.size() != 0) {
                Collections.sort(allStudentsGroups, new BeanComparator(
                        "groupNumber"));
                Integer lastGroupNumber = ((IStudentGroup) allStudentsGroups
                        .get(allStudentsGroups.size() - 1)).getGroupNumber();
                groupNumber = new Integer(lastGroupNumber.intValue() + 1);

            }

            IStudentGroup newStudentGroup = persistentStudentGroup
                    .readStudentGroupByAttendsSetAndGroupNumber(
                            groupProperties.getAttendsSet(), groupNumber);

            if (newStudentGroup != null)
                throw new FenixServiceException();

            infoSiteStudentsWithoutGroup.setGroupNumber(groupNumber);

            if (groupProperties.getEnrolmentPolicy().equals(
                    new EnrolmentGroupPolicyType(2))) {
                return infoSiteStudentsWithoutGroup;
            }

            frequentas = groupProperties.getAttendsSet().getAttends();

            userStudent = persistentStudent.readByUsername(username);

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
                if (!student.equals(userStudent))
                    infoStudentList.add(Cloner
                            .copyIStudent2InfoStudent(student));
            }

            infoSiteStudentsWithoutGroup.setInfoStudentList(infoStudentList);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return infoSiteStudentsWithoutGroup;
    }
}