/*
 * Created on 20/Out/2004
 *
 */
package ServidorAplicacao.Servico.student;

import Dominio.IStudentGroup;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *  
 */
public class VerifyGroupPropertiesAndStudentGroupWithoutShift implements IServico {

    private static VerifyGroupPropertiesAndStudentGroupWithoutShift _servico = new VerifyGroupPropertiesAndStudentGroupWithoutShift();

    /**
     * The singleton access method of this class.
     */
    public static VerifyGroupPropertiesAndStudentGroupWithoutShift getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private VerifyGroupPropertiesAndStudentGroupWithoutShift() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "VerifyGroupPropertiesAndStudentGroupWithoutShift";
    }

    public boolean run(Integer studentGroupCode,String username)throws FenixServiceException {
    	 boolean result = false;
         IPersistentStudentGroup persistentStudentGroup = null;
         
         try {

             ISuportePersistente ps = SuportePersistenteOJB.getInstance();
         
             
             persistentStudentGroup = ps.getIPersistentStudentGroup();
             
             IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
             		StudentGroup.class, studentGroupCode);
             
             if(studentGroup.getShift()==null && studentGroup.getAttendsSet().getGroupProperties().getShiftType()!=null){
             	result = true;
             }
             
             
                         
         } catch (ExcepcaoPersistencia excepcaoPersistencia) {
             throw new FenixServiceException(excepcaoPersistencia.getMessage());
         }
         
         return result;

     }
 }



