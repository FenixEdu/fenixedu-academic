/*
 * Created on 17/Jan/2005
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa and rmalo
 *  
 */

public class EditStudentGroupsShift implements IService {

    /**
     * The constructor of this class.
     */
    public EditStudentGroupsShift() {
    }

    /**
     * Executes the service.
     */

    public Boolean run(Integer executionCourseCode,Integer groupPropertiesCode, Integer shiftCode,
            List studentGroupsCodes) throws FenixServiceException {

        IPersistentGroupProperties persistentGroupProperties = null;
        ITurnoPersistente persistentShift = null;
        IPersistentStudentGroup persistentStudentGroup = null;
        
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            
            persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
            
            persistentShift = persistentSupport.getITurnoPersistente();
            
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
					.readByOID(GroupProperties.class, groupPropertiesCode);
            
            if(groupProperties==null){
            	throw new  ExistingServiceException();
            }

            ITurno shift = (ITurno) persistentShift
            .readByOID(Turno.class, shiftCode);

            if(shift==null){
            	throw new  InvalidChangeServiceException();
            }
            
            if(groupProperties.getShiftType() == null || groupProperties.getShiftType().getTipo().intValue() != shift.getTipo().getTipo().intValue()){
            	throw new NonValidChangeServiceException();
            }
           
            List studentGroups = new ArrayList();
            Iterator iterStudentGroupsCodes = studentGroupsCodes.iterator();
            while(iterStudentGroupsCodes.hasNext()){
            	Integer studentGroupCode = (Integer)iterStudentGroupsCodes.next();
            	IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup
                 .readByOID(StudentGroup.class, studentGroupCode);
            	if(studentGroup == null){
            		throw new  InvalidSituationServiceException();
            	}
            	studentGroups.add(studentGroup);
            }
            
            Iterator iterStudentGroups = studentGroups.iterator();
            while(iterStudentGroups.hasNext()){
            	IStudentGroup studentGroup = (IStudentGroup)iterStudentGroups.next();
            	if(!studentGroup.getAttendsSet().getGroupProperties().equals(groupProperties)){
            		throw new  InvalidArgumentsServiceException();
            	}
            }
            
            Iterator iterStudentGroupsToEnroll = studentGroups.iterator();

            while (iterStudentGroupsToEnroll.hasNext()) {
            	IStudentGroup studentGroup = (IStudentGroup)iterStudentGroupsToEnroll.next();
            	persistentStudentGroup.simpleLockWrite(studentGroup);
                studentGroup.setShift(shift);
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return new Boolean(true);
    }
}