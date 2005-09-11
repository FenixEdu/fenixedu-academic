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
import net.sourceforge.fenixedu.domain.IPerson;
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
            throws ExcepcaoPersistencia, ExistingServiceException {

        final ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IGrouping grouping = (IGrouping) ps.getIPersistentGrouping().readByOID(Grouping.class,
                groupPropertiesCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final List<IStudentGroup> allStudentsGroups = grouping.getStudentGroups();
        final List<IAttends> attendsGrouping = new ArrayList(grouping.getAttends());
        for (final IStudentGroup studentGroup : allStudentsGroups) {
            for (IAttends attend : studentGroup.getAttends()) {
                attendsGrouping.remove(attend);
            }
        }

        final List<InfoSiteStudentInformation> infoStudentInformationList = new ArrayList<InfoSiteStudentInformation>(attendsGrouping.size());
        for (IAttends attend : attendsGrouping) {
            final IStudent student = attend.getAluno();
            final IPerson person = student.getPerson();
            InfoSiteStudentInformation infoSiteStudentInformation = new InfoSiteStudentInformation();
            infoSiteStudentInformation.setEmail(person.getEmail());
            infoSiteStudentInformation.setName(person.getNome());
            infoSiteStudentInformation.setNumber(student.getNumber());
            infoSiteStudentInformation.setUsername(person.getUsername());
            infoStudentInformationList.add(infoSiteStudentInformation);
        }

        Collections.sort(infoStudentInformationList, new BeanComparator("number"));

        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        infoSiteStudentGroup.setInfoSiteStudentInformationList(infoStudentInformationList);

        final int groupNumber = grouping.findMaxGroupNumber() + 1;
        infoSiteStudentGroup.setNrOfElements(Integer.valueOf(groupNumber));

        return infoSiteStudentGroup;

    }

}
