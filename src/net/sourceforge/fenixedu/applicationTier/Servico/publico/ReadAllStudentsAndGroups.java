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
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
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
public class ReadAllStudentsAndGroups extends Service {

    public InfoSiteStudentsAndGroups run(Integer groupingId) throws FenixServiceException,
            ExcepcaoPersistencia {
        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

        Grouping grouping = rootDomainObject.readGroupingByOID(groupingId);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        List infoSiteStudentsAndGroupsList = new ArrayList();
        List studentGroups = getAllStudentGroups(grouping);
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

                infoSiteStudentInformation.setNumber(attend.getRegistration()
                        .getNumber());

                infoSiteStudentInformation.setName(attend.getRegistration().getPerson().getName());

                infoSiteStudentInformation.setUsername((attend.getRegistration().getPerson().getUsername()));

                infoSiteStudentInformation.setEmail(attend.getRegistration().getPerson().getEmail());

                infoSiteStudentAndGroup.setInfoSiteStudentInformation(infoSiteStudentInformation);

                infoSiteStudentsAndGroupsList.add(infoSiteStudentAndGroup);
            }
        }

        Collections.sort(infoSiteStudentsAndGroupsList, new BeanComparator(
                "infoSiteStudentInformation.number"));

        infoSiteStudentsAndGroups.setInfoSiteStudentsAndGroupsList(infoSiteStudentsAndGroupsList);
        infoSiteStudentsAndGroups.setInfoGrouping(InfoGrouping.newInfoFromDomain(grouping));

        return infoSiteStudentsAndGroups;
    }

    private List getAllStudentGroups(Grouping groupProperties) {
        List result = new ArrayList();
        List studentGroups = groupProperties.getStudentGroups();
        result.addAll(studentGroups);
        return result;
    }
}
