/*
 * Created on 11/Nov/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *  
 */

public class EnrollStudentGroupShift implements IService {

    /**
     * The constructor of this class.
     */
    public EnrollStudentGroupShift() {
    }

    /**
     * Executes the service.
     */

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode,Integer groupPropertiesCode,
            Integer newShiftCode) throws FenixServiceException {
System.out.println("1");
        ITurnoPersistente persistentShift = null;
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
            
            persistentShift = persistentSupport.getITurnoPersistente();
            ITurno shift = (ITurno) persistentShift.readByOID(Turno.class,
                    newShiftCode);
            
            if (shift == null) {
                throw new InvalidSituationServiceException();
            }
            
            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup
                    .readByOID(StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
                throw new InvalidArgumentsServiceException();
            }

            if(groupProperties.getShiftType() == null || 
            	   (groupProperties.getShiftType().getTipo().intValue() != shift.getTipo().getTipo().intValue())){
            	throw new InvalidChangeServiceException();
            }
            
            persistentStudentGroup.simpleLockWrite(studentGroup);
            studentGroup.setShift(shift);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        System.out.println("2");
        return new Boolean(true);
    }
}