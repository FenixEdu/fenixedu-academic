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
public class CheckIfStudentGroupShiftIsCorrect implements IServico {

    private static CheckIfStudentGroupShiftIsCorrect service = new CheckIfStudentGroupShiftIsCorrect();

    /**
     * The singleton access method of this class.
     */
    public static CheckIfStudentGroupShiftIsCorrect getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private CheckIfStudentGroupShiftIsCorrect() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "CheckIfStudentGroupShiftIsCorrect";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode,Integer studentGroupCode, String shiftCodeString) throws FenixServiceException {

    	try{
    		
    		IPersistentStudentGroup persistentStudentGroup = null;

            ISuportePersistente ps = SuportePersistenteOJB.getInstance();

            persistentStudentGroup = ps.getIPersistentStudentGroup();
            
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
            		StudentGroup.class, studentGroupCode);
            
            if(studentGroup == null){
            	throw new ExistingServiceException();
            }
            
            System.out.println("CheckIfStudentGroupShiftIsCorrect--->shiftCodeString" + shiftCodeString);
            
            Integer shiftCode = null;
            if(shiftCodeString!=null){
            	shiftCode = new Integer(shiftCodeString);
            }
            
            if(studentGroup.getShift() == null){
            	if(shiftCode != null) throw new InvalidSituationServiceException();
            }
            else{
            	if(studentGroup.getShift().getIdInternal().intValue() != shiftCode.intValue()){
            		throw new InvalidSituationServiceException();
            	}
            }
            
                        
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}
