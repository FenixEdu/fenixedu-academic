/*
 * Created on 12/Nov/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.IShift;
import Dominio.StudentGroup;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa and rmalo
 *  
 */

public class UnEnrollStudentGroupShift implements IService {

    /**
     * The constructor of this class.
     */
    public UnEnrollStudentGroupShift() {
    }

    /**
     * Executes the service.
     */

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode,Integer groupPropertiesCode) throws FenixServiceException {

        IPersistentStudentGroup persistentStudentGroup = null;
        IPersistentGroupProperties persistentGroupProperties = null;

        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
            
            IGroupProperties groupProperties = (IGroupProperties)persistentGroupProperties.readByOID(
            		GroupProperties.class, groupPropertiesCode);
            
            if(groupProperties == null){
            	throw new ExistingServiceException();
            }
            
            
            persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
            
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup
                    .readByOID(StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
                throw new InvalidArgumentsServiceException();
            }

            if(!(studentGroup.getShift() != null && groupProperties.getShiftType() == null) || studentGroup.getShift() == null){
            	throw new InvalidChangeServiceException();
            }
            
            IShift shift = null;
            persistentStudentGroup.simpleLockWrite(studentGroup);
            studentGroup.setShift(shift);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return new Boolean(true);
    }
}