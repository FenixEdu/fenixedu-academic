/*
 * Created on 24/Ago/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import Dominio.AttendsSet;
import Dominio.IAttendInAttendsSet;
import Dominio.IAttendsSet;
import Dominio.IFrequenta;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAttendInAttendsSet;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *
 */

public class DeleteAttendsSetMembers implements IServico {

    private static DeleteAttendsSetMembers service = new DeleteAttendsSetMembers();

    /**
     * The singleton access method of this class.
     */
    public static DeleteAttendsSetMembers getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private DeleteAttendsSetMembers() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "DeleteAttendsSetMembers";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer attendsSetCode,
            List studentUsernames) throws FenixServiceException {

        
        IFrequentaPersistente persistentAttend = null;
        IPersistentAttendsSet persistentAttendsSet = null;
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = null;
        IPersistentStudent persistentStudent = null;
        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;

        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
            persistentAttendInAttendsSet = persistentSupport.getIPersistentAttendInAttendsSet();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();

            IAttendsSet attendsSet = (IAttendsSet) persistentAttendsSet
            		.readByOID(AttendsSet.class, attendsSetCode);

            if (attendsSet == null) {
                throw new ExistingServiceException();
            }

            Iterator iterator = studentUsernames.iterator();
            while (iterator.hasNext()) {
            	String username = (String)iterator.next();
            	boolean found1 = false;
            	List attendInAttendsSetList = attendsSet.getAttendInAttendsSet();
				Iterator iterAttendInAttendsSet = attendInAttendsSetList.iterator();
				IAttendInAttendsSet attendInAttendsSet=null;
            	while(iterAttendInAttendsSet.hasNext() && !found1){
            	 	attendInAttendsSet = (IAttendInAttendsSet)iterAttendInAttendsSet.next();
            	 	if(attendInAttendsSet.getAttend().getAluno().getPerson().getUsername().equals(username)){
            	 		found1 = true;
            	 	}
            	 }
                if(found1==false){
                    throw new InvalidSituationServiceException();
                }     
           	    
                IFrequenta attend = attendInAttendsSet.getAttend();
                
                boolean found = false;
                Iterator iterStudentsGroups = attendsSet.getStudentGroups().iterator();
                while (iterStudentsGroups.hasNext() && !found) {
                        
                	IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend
														.readBy((IStudentGroup)iterStudentsGroups.next(), attend);
                	if (oldStudentGroupAttend != null) {
                		persistentStudentGroupAttend.delete(oldStudentGroupAttend);
                		found = true;
                	} 
                }                	

                
                attendsSet.removeAttendInAttendsSet(attendInAttendsSet);
                attend.removeAttendInAttendsSet(attendInAttendsSet);
                persistentAttendInAttendsSet.delete(attendInAttendsSet);
                
            }            
            
            

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}