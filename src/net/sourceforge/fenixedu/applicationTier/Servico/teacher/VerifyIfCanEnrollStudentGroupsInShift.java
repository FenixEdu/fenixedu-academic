/*
 * Created on 13/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author joaosa & rmalo
 *  
 */
public class VerifyIfCanEnrollStudentGroupsInShift implements IServico {

    private static VerifyIfCanEnrollStudentGroupsInShift service = new VerifyIfCanEnrollStudentGroupsInShift();

    /**
     * The singleton access method of this class.
     */
    public static VerifyIfCanEnrollStudentGroupsInShift getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private VerifyIfCanEnrollStudentGroupsInShift() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "VerifyIfCanEnrollStudentGroupsInShift";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer groupPropertiesCode, Integer shiftCode) throws FenixServiceException {
    	
    	boolean result = true;
		
        try {

            ITurnoPersistente persistentShift = null;
            IPersistentGroupProperties persistentGroupProperties = null;

            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentShift = ps.getITurnoPersistente();            
            persistentGroupProperties = ps.getIPersistentGroupProperties();
        
            IGroupProperties groupProperties = (IGroupProperties)persistentGroupProperties.readByOID(
            		GroupProperties.class, groupPropertiesCode);
            
            if(groupProperties == null){
            	throw new ExistingServiceException();
            }
            
            IShift shift = (IShift) persistentShift.readByOID(
            		Shift.class, shiftCode);
 
            if(groupProperties.getShiftType().getTipo().intValue() != shift.getTipo().getTipo().intValue()){
            	return result = false;
            }
                
        	List studentGroups = groupProperties.getAttendsSet().getStudentGroups();
			
        	List studentGroupsAux = getStudentGroupsByShift(groupProperties, shift); 
			
        	if(studentGroups.size() == studentGroupsAux.size()){
        		return result = false;
        	}
                        
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        
        return result;
    }
    
	private List getStudentGroupsByShift(IGroupProperties groupProperties, IShift shift){
		List result = new ArrayList();
		List studentGroups = groupProperties.getAttendsSet().getStudentGroupsWithShift();
		Iterator iterStudentGroups = studentGroups.iterator();
		while(iterStudentGroups.hasNext()){
			IStudentGroup studentGroup = (IStudentGroup)iterStudentGroups.next();
			if(studentGroup.getShift().equals(shift)){
				result.add(studentGroup);
			}
		}
		return result;
	}
	
	}



