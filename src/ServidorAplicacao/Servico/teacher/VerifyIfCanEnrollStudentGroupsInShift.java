/*
 * Created on 13/Jan/2005
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.IShift;
import Dominio.Shift;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
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



