/*
 * Created on 04/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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
        IPersistentAttendsSet persistentAttendsSet = null;
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = null;
        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        

        try {

            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

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
            	IAttends frequenta = attendInAttendsSet.getAttend();

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