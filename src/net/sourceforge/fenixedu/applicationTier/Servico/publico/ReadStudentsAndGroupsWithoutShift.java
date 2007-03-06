/*
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentAndGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author joaosa & rmalo
 * 
 */
public class ReadStudentsAndGroupsWithoutShift extends Service {

    public InfoSiteStudentsAndGroups run(Integer groupPropertiesId) throws FenixServiceException,
            ExcepcaoPersistencia {
        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

        Grouping groupProperties = rootDomainObject.readGroupingByOID(
                groupPropertiesId);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        List infoSiteStudentsAndGroupsList = new ArrayList();
        List studentGroups = getStudentGroupsWithoutShiftByGroupProperties(groupProperties);
        Iterator iterStudentGroups = studentGroups.iterator();
        while (iterStudentGroups.hasNext()) {

            List studentGroupAttendList = new ArrayList();
            StudentGroup studentGroup = (StudentGroup) iterStudentGroups.next();

            studentGroupAttendList = studentGroup.getAttends();

            Iterator iterStudentGroupAttendList = studentGroupAttendList.iterator();
            InfoSiteStudentInformation infoSiteStudentInformation = null;
            InfoSiteStudentAndGroup infoSiteStudentAndGroup = null;
            Attends attend = null;

            while (iterStudentGroupAttendList.hasNext()) {
                infoSiteStudentInformation = new InfoSiteStudentInformation();
                infoSiteStudentAndGroup = new InfoSiteStudentAndGroup();

                attend = (Attends) iterStudentGroupAttendList.next();

                infoSiteStudentAndGroup.setInfoStudentGroup(InfoStudentGroup
                        .newInfoFromDomain(studentGroup));

                infoSiteStudentInformation.setNumber(attend.getRegistration().getNumber());

                infoSiteStudentInformation.setName(attend.getRegistration().getPerson().getNome());

                infoSiteStudentInformation.setEmail(attend.getRegistration().getPerson().getEmail());

                infoSiteStudentAndGroup.setInfoSiteStudentInformation(infoSiteStudentInformation);

                infoSiteStudentsAndGroupsList.add(infoSiteStudentAndGroup);
            }
        }
        Collections.sort(infoSiteStudentsAndGroupsList, new BeanComparator(
                "infoSiteStudentInformation.number"));

        infoSiteStudentsAndGroups.setInfoSiteStudentsAndGroupsList(infoSiteStudentsAndGroupsList);

        return infoSiteStudentsAndGroups;
    }

    private List getStudentGroupsWithoutShiftByGroupProperties(Grouping groupProperties) {
        List result = new ArrayList();
        List studentGroups = groupProperties.getStudentGroupsWithoutShift();
        result.addAll(studentGroups);
        return result;
    }
}
