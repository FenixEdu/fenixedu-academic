/*
 * Created on 20/Out/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *  
 */
public class VerifyStudentGroupWithoutShift implements IServico {

    private static VerifyStudentGroupWithoutShift service = new VerifyStudentGroupWithoutShift();

    /**
     * The singleton access method of this class.
     */
    public static VerifyStudentGroupWithoutShift getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private VerifyStudentGroupWithoutShift() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "VerifyStudentGroupWithoutShift";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode,Integer studentGroupCode, Integer groupPropertiesCode) throws FenixServiceException {

        boolean result = false;
        IPersistentStudentGroup persistentStudentGroup = null;
        IPersistentGroupProperties persistentGroupProperties = null;
		   
        
        try {

            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
        
            
            persistentStudentGroup = ps.getIPersistentStudentGroup();
            
            persistentGroupProperties = ps.getIPersistentGroupProperties();
        
            IGroupProperties groupProperties = (IGroupProperties)persistentGroupProperties.readByOID(
            		GroupProperties.class, groupPropertiesCode);
            
            if(groupProperties == null){
            	throw new ExistingServiceException();
            }
            
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
            		StudentGroup.class, studentGroupCode);
            
            if(studentGroup == null){
            	throw new InvalidSituationServiceException();
            }
            
            if(studentGroup.getShift()==null && studentGroup.getAttendsSet().getGroupProperties().getShiftType()!=null){
            	result = true;
            }
            
            
                        
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        
        return result;

    }
}



