/*
 * Created on 17/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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

            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            
            persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
            
            persistentShift = persistentSupport.getITurnoPersistente();
            
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
					.readByOID(GroupProperties.class, groupPropertiesCode);
            
            if(groupProperties==null){
            	throw new  ExistingServiceException();
            }

            IShift shift = (IShift) persistentShift
            .readByOID(Shift.class, shiftCode);

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