/*
 * Created on 12/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author ansr and scpo
 * 
 */
public class PrepareCreateStudentGroup implements IService {

    public ISiteComponent run(Integer executionCourseCode, Integer groupPropertiesCode)
            throws FenixServiceException {

        List infoStudentInformationList = new ArrayList();
        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        Integer groupNumber = null;
        IGrouping grouping;
        try {

            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

            grouping = (IGrouping) ps.getIPersistentGrouping().readByOID(Grouping.class,
                    groupPropertiesCode);

            if (grouping == null) {
                throw new ExistingServiceException();
            }

            List<IAttends> attendsGrouping = new ArrayList();
            attendsGrouping.addAll(grouping.getAttends());

            List<IStudentGroup> allStudentsGroups = grouping.getStudentGroups();

            groupNumber = new Integer(1);

            if (allStudentsGroups != null) {
                if (allStudentsGroups.size() != 0) {
                    IStudentGroup studentGroup = (IStudentGroup) Collections.max(allStudentsGroups,
                            new BeanComparator("groupNumber"));
                    groupNumber = studentGroup.getGroupNumber() + 1;                            
                }

                List<IAttends> allStudentGroupAttends;
                for(IStudentGroup studentGroup : allStudentsGroups){
                    allStudentGroupAttends = studentGroup.getAttends();
                    for(IAttends attend : allStudentGroupAttends){
                        if(attendsGrouping.contains(attend))
                            attendsGrouping.remove(attend);
                    }                    
                }               
            }

            IStudent student = null;                        
            for(IAttends attend : attendsGrouping){
                student = attend.getAluno();
                InfoSiteStudentInformation infoSiteStudentInformation = new InfoSiteStudentInformation();
                infoSiteStudentInformation.setEmail(student.getPerson().getEmail());
                infoSiteStudentInformation.setName(student.getPerson().getNome());
                infoSiteStudentInformation.setNumber(student.getNumber());
                infoSiteStudentInformation.setUsername(student.getPerson().getUsername());
                infoStudentInformationList.add(infoSiteStudentInformation);
            }          

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        Collections.sort(infoStudentInformationList, new BeanComparator("number"));

        infoSiteStudentGroup.setInfoSiteStudentInformationList(infoStudentInformationList);
        infoSiteStudentGroup.setNrOfElements(groupNumber);

        return infoSiteStudentGroup;

    }
}
