/*
 * Created on 04/Sep/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
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
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAttendInAttendsSet;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *
 */

public class DeleteAllAttendsSetMembers implements IServico {

    private static  DeleteAllAttendsSetMembers service = new DeleteAllAttendsSetMembers();

    /**
     * The singleton access method of this class.
     */
    public static DeleteAllAttendsSetMembers getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private DeleteAllAttendsSetMembers() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "DeleteAllAttendsSetMembers";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer objectCode, Integer attendsSetCode) throws FenixServiceException {

        
        IFrequentaPersistente persistentAttend = null;
        IPersistentAttendsSet persistentAttendsSet = null;
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = null;
        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        

        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
            persistentAttendInAttendsSet = persistentSupport.getIPersistentAttendInAttendsSet();
            persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();
            IAttendsSet attendsSet = (IAttendsSet) persistentAttendsSet
										.readByOID(AttendsSet.class, attendsSetCode);
            
            if (attendsSet == null) {
                throw new ExistingServiceException();
            }
            
            List attendsSetElements = new ArrayList();
            attendsSetElements.addAll(attendsSet.getAttendInAttendsSet());
            Iterator iterator = attendsSetElements.iterator();
            while (iterator.hasNext()) {
            	IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet)iterator.next();
            	IFrequenta frequenta = (IFrequenta)attendInAttendsSet.getAttend();

            		boolean found = false;
                    Iterator iterStudentsGroups = attendsSet.getStudentGroups().iterator();
                    while (iterStudentsGroups.hasNext() && !found) {
                            
                    	IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend
    														.readBy((IStudentGroup)iterStudentsGroups.next(), frequenta);
                    	if (oldStudentGroupAttend != null) {
                    		persistentStudentGroupAttend.delete(oldStudentGroupAttend);
                    		found = true;
                    	} 
                    }            
                    frequenta.removeAttendInAttendsSet(attendInAttendsSet);
            		attendsSet.removeAttendInAttendsSet(attendInAttendsSet);
            		persistentAttendInAttendsSet.delete(attendInAttendsSet);
            }            
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}