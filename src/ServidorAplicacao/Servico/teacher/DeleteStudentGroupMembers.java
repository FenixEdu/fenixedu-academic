/*
 * Created on 23/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import Dominio.GroupProperties;
import Dominio.IAttendsSet;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 * @author joaosa and rmalo 31/Ago/2004
 */

public class DeleteStudentGroupMembers implements IServico {

    private static DeleteStudentGroupMembers service = new DeleteStudentGroupMembers();

    /**
     * The singleton access method of this class.
     */
    public static DeleteStudentGroupMembers getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private DeleteStudentGroupMembers() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "DeleteStudentGroupMembers";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer studentGroupCode, Integer groupPropertiesCode,
            List studentUsernames) throws FenixServiceException {


        IPersistentStudentGroup persistentStudentGroup = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudent persistentStudent = null;
        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        IPersistentGroupProperties persistentGroupProperties = null;

        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            persistentStudentGroupAttend = persistentSupport
                    .getIPersistentStudentGroupAttend();
            persistentGroupProperties = persistentSupport
				.getIPersistentGroupProperties();
            
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
                    .readByOID(GroupProperties.class, groupPropertiesCode);

           if(groupProperties==null){
           	throw new  InvalidChangeServiceException();
           }
                       
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup
                    .readByOID(StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
                throw new ExistingServiceException();
            }
            
            Iterator iterator = studentUsernames.iterator();

            IAttendsSet attendsSet = studentGroup.getAttendsSet();
            while (iterator.hasNext()) {

                IStudent student = persistentStudent.readByUsername(iterator
                        .next().toString());
                IFrequenta attend = attendsSet.getStudentAttend(student);
                
                 IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend
                        .readBy(studentGroup, attend);
                if (oldStudentGroupAttend != null) {

                    persistentStudentGroupAttend.delete(oldStudentGroupAttend);
                } else {
                    throw new InvalidSituationServiceException();
                }
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}