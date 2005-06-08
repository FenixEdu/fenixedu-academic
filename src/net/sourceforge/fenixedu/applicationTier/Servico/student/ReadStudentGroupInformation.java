/*
 * Created on 26/Ago/2003
 *

 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupAttend;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupAttendWithAllUntilPersons;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupWithAllUntilLessons;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */
public class ReadStudentGroupInformation implements IService {

    public ISiteComponent run(Integer studentGroupCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        List studentGroupAttendInformationList = null;
        List studentGroupAttendList = null;
        IStudentGroup studentGroup = null;
        IGroupProperties groupProperties = null;
        IAttendsSet attendsSet = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOID(StudentGroup.class,
                studentGroupCode);

        if (studentGroup == null) {
            return null;
        }

        studentGroupAttendList = studentGroup.getStudentGroupAttends();

        attendsSet = studentGroup.getAttendsSet();

        groupProperties = attendsSet.getGroupProperties();

        studentGroupAttendInformationList = new ArrayList(studentGroupAttendList.size());
        Iterator iter = studentGroupAttendList.iterator();
        InfoSiteStudentInformation infoSiteStudentInformation = null;
        InfoStudentGroupAttend infoStudentGroupAttend = null;

        while (iter.hasNext()) {
            infoSiteStudentInformation = new InfoSiteStudentInformation();

            infoStudentGroupAttend = InfoStudentGroupAttendWithAllUntilPersons
                    .newInfoFromDomain((IStudentGroupAttend) iter.next());

            infoSiteStudentInformation.setNumber(infoStudentGroupAttend.getInfoAttend().getAluno()
                    .getNumber());

            infoSiteStudentInformation.setName(infoStudentGroupAttend.getInfoAttend().getAluno()
                    .getInfoPerson().getNome());

            infoSiteStudentInformation.setEmail(infoStudentGroupAttend.getInfoAttend().getAluno()
                    .getInfoPerson().getEmail());

            infoSiteStudentInformation.setUsername(infoStudentGroupAttend.getInfoAttend().getAluno()
                    .getInfoPerson().getUsername());

            studentGroupAttendInformationList.add(infoSiteStudentInformation);

        }

        Collections.sort(studentGroupAttendInformationList, new BeanComparator("number"));
        infoSiteStudentGroup.setInfoSiteStudentInformationList(studentGroupAttendInformationList);
        infoSiteStudentGroup.setInfoStudentGroup(InfoStudentGroupWithAllUntilLessons
                .newInfoFromDomain(studentGroup));

        if (groupProperties.getMaximumCapacity() != null) {

            int vagas = groupProperties.getMaximumCapacity().intValue() - studentGroupAttendList.size();

            infoSiteStudentGroup.setNrOfElements(new Integer(vagas));
        } else
            infoSiteStudentGroup.setNrOfElements("Sem limite");

        return infoSiteStudentGroup;
    }
}