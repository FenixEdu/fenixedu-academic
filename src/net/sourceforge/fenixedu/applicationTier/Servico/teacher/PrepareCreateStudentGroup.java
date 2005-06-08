/*
 * Created on 12/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author ansr and scpo
 *  
 */
public class PrepareCreateStudentGroup implements IServico {

    private static PrepareCreateStudentGroup service = new PrepareCreateStudentGroup();

    /**
     * The singleton access method of this class.
     */
    public static PrepareCreateStudentGroup getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private PrepareCreateStudentGroup() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "PrepareCreateStudentGroup";
    }

    /**
     * Executes the service.
     */

    public ISiteComponent run(Integer executionCourseCode,
            Integer groupPropertiesCode) throws FenixServiceException {

        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;

        List frequentas = new ArrayList();

        List infoStudentInformationList = new ArrayList();
        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        Integer groupNumber = null;
        IGroupProperties groupProperties;
        try {

            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
            persistentStudentGroupAttend = ps
                    .getIPersistentStudentGroupAttend();

             groupProperties = (IGroupProperties) ps
                    .getIPersistentGroupProperties().readByOID(
                            GroupProperties.class, groupPropertiesCode);

             if(groupProperties == null){
            	throw new ExistingServiceException();
             }
             
            frequentas.addAll(groupProperties.getAttendsSet().getAttends());
            
            
            IAttendsSet attendsSet = groupProperties.getAttendsSet();
            

            List allStudentsGroups = attendsSet.getStudentGroups();
            
            groupNumber = new Integer(1);
            
            if(allStudentsGroups!=null){
            	if (allStudentsGroups.size() != 0) {
            		Collections.sort(allStudentsGroups, new BeanComparator(
            		"groupNumber"));
            		Integer lastGroupNumber = ((IStudentGroup) allStudentsGroups
            				.get(allStudentsGroups.size() - 1)).getGroupNumber();
            		groupNumber = new Integer(lastGroupNumber.intValue() + 1);
            	}

            	Iterator iterator = allStudentsGroups.iterator();
            	List allStudentGroupAttend;
            
            	while (iterator.hasNext()) {
                    IStudentGroup studentGroup = (IStudentGroup) iterator.next();
            		allStudentGroupAttend =  studentGroup.getStudentGroupAttends();
            
            		Iterator iterator2 = allStudentGroupAttend.iterator();
            		IAttends frequenta = null;
            		while (iterator2.hasNext()) {
            			frequenta = ((IStudentGroupAttend) iterator2.next())
						.getAttend();
            			if (frequentas.contains(frequenta)) {
            				frequentas.remove(frequenta);
            			}
            		}
            	}
            }
            
            IStudent student = null;
            Iterator iterator3 = frequentas.iterator();

            while (iterator3.hasNext()) {
                student = ((IAttends) iterator3.next()).getAluno();
                InfoSiteStudentInformation infoSiteStudentInformation = new InfoSiteStudentInformation();

                infoSiteStudentInformation.setEmail(student.getPerson()
                        .getEmail());
                infoSiteStudentInformation.setName(student.getPerson()
                        .getNome());
                infoSiteStudentInformation.setNumber(student.getNumber());
                infoSiteStudentInformation.setUsername(student.getPerson()
                        .getUsername());
                infoStudentInformationList.add(infoSiteStudentInformation);
            }

            
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        Collections.sort(infoStudentInformationList, new BeanComparator(
                "number"));

        infoSiteStudentGroup
                .setInfoSiteStudentInformationList(infoStudentInformationList);
        infoSiteStudentGroup.setNrOfElements(groupNumber);
        
        return infoSiteStudentGroup;

    }
}



