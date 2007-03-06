/*
 * Created on 12/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author ansr and scpo
 * 
 */
public class PrepareCreateStudentGroup extends Service {

    public ISiteComponent run(Integer executionCourseCode, Integer groupPropertiesCode)
            throws ExcepcaoPersistencia, ExistingServiceException {

        final Grouping grouping = rootDomainObject.readGroupingByOID(
                groupPropertiesCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final List<StudentGroup> allStudentsGroups = grouping.getStudentGroups();
        final List<Attends> attendsGrouping = new ArrayList(grouping.getAttends());
        for (final StudentGroup studentGroup : allStudentsGroups) {
            for (Attends attend : studentGroup.getAttends()) {
                attendsGrouping.remove(attend);
            }
        }

        final List<InfoSiteStudentInformation> infoStudentInformationList = new ArrayList<InfoSiteStudentInformation>(attendsGrouping.size());
        for (Attends attend : attendsGrouping) {
            final Registration registration = attend.getRegistration();
            final Person person = registration.getPerson();
            InfoSiteStudentInformation infoSiteStudentInformation = new InfoSiteStudentInformation();
            infoSiteStudentInformation.setEmail(person.getEmail());
            infoSiteStudentInformation.setName(person.getNome());
            infoSiteStudentInformation.setNumber(registration.getNumber());
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
