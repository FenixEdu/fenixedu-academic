/*
 * Created on 26/Ago/2003
 *

 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteStudentInformation;
import DataBeans.InfoStudentGroupAttend;
import DataBeans.InfoStudentGroupAttendWithAllUntilPersons;
import DataBeans.InfoStudentGroupWithAllUntilLessons;
import Dominio.IAttendsSet;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */
public class ReadStudentGroupInformation implements IServico {

    private static ReadStudentGroupInformation _servico = new ReadStudentGroupInformation();

    /**
     * The singleton access method of this class.
     */
    public static ReadStudentGroupInformation getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadStudentGroupInformation() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadStudentGroupInformation";
    }

    public ISiteComponent run(Integer studentGroupCode)
            throws FenixServiceException {

        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
        List studentGroupAttendInformationList = null;
        List studentGroupAttendList = null;
        IStudentGroup studentGroup = null;
        IGroupProperties groupProperties = null;
        IAttendsSet attendsSet = null;
        
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup()
                    .readByOID(StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
                return null;
            }
            
            studentGroupAttendList = sp.getIPersistentStudentGroupAttend()
                    .readAllByStudentGroup(studentGroup);

        	attendsSet = studentGroup.getAttendsSet();
        	
        	groupProperties = attendsSet.getGroupProperties();
            
            
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();

            throw new FenixServiceException(
                    "error.impossibleReadStudentGroupInformation");
        }

        
        studentGroupAttendInformationList = new ArrayList(
                studentGroupAttendList.size());
        Iterator iter = studentGroupAttendList.iterator();
        InfoSiteStudentInformation infoSiteStudentInformation = null;
        InfoStudentGroupAttend infoStudentGroupAttend = null;

        while (iter.hasNext()) {
            infoSiteStudentInformation = new InfoSiteStudentInformation();

            infoStudentGroupAttend = InfoStudentGroupAttendWithAllUntilPersons.newInfoFromDomain((IStudentGroupAttend) iter.next());
                
            infoSiteStudentInformation.setNumber(infoStudentGroupAttend
                    .getInfoAttend().getAluno().getNumber());

            infoSiteStudentInformation.setName(infoStudentGroupAttend
                    .getInfoAttend().getAluno().getInfoPerson().getNome());

            infoSiteStudentInformation.setEmail(infoStudentGroupAttend
                    .getInfoAttend().getAluno().getInfoPerson().getEmail());

            infoSiteStudentInformation.setUsername(infoStudentGroupAttend
                    .getInfoAttend().getAluno().getInfoPerson().getUsername());

            studentGroupAttendInformationList.add(infoSiteStudentInformation);

        }

        Collections.sort(studentGroupAttendInformationList, new BeanComparator(
                "number"));
        infoSiteStudentGroup
                .setInfoSiteStudentInformationList(studentGroupAttendInformationList);
        infoSiteStudentGroup.setInfoStudentGroup(InfoStudentGroupWithAllUntilLessons.newInfoFromDomain(studentGroup));
        
 
        if (groupProperties.getMaximumCapacity() != null) {

            int vagas = groupProperties.getMaximumCapacity().intValue()
                    - studentGroupAttendList.size();

            infoSiteStudentGroup.setNrOfElements(new Integer(vagas));
        } else
            infoSiteStudentGroup.setNrOfElements("Sem limite");

        
        return infoSiteStudentGroup;
    }
}