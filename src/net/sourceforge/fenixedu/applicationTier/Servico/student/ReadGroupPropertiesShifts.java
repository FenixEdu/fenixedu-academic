/*
 * Created on 28/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */
public class ReadGroupPropertiesShifts implements IServico {

    private static ReadGroupPropertiesShifts service = new ReadGroupPropertiesShifts();

    /**
     * The singleton access method of this class.
     */
    public static ReadGroupPropertiesShifts getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadGroupPropertiesShifts() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "ReadGroupPropertiesShifts";
    }

    /**
     * Executes the service.
     */
    public InfoSiteShifts run(Integer groupPropertiesCode, Integer studentGroupCode)
            throws FenixServiceException {
    	
    	InfoSiteShifts infoSiteShifts = new InfoSiteShifts();
        List infoShifts = new ArrayList();
        IGroupProperties groupProperties = null;
        boolean result = false;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentGroup studentGroup = null;
            groupProperties = (IGroupProperties) sp
			.getIPersistentGroupProperties().readByOID(
					GroupProperties.class, groupPropertiesCode);
            if(groupProperties == null){
            	throw new ExistingServiceException();
            }
            if (studentGroupCode != null) {

                studentGroup = (IStudentGroup) sp.getIPersistentStudentGroup().readByOID(
                        StudentGroup.class, studentGroupCode);

                if (studentGroup == null) {
                	throw new InvalidSituationServiceException();
                }
                
                infoSiteShifts.setOldShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
            }
            
            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);
        	
        	if(strategy.checkHasShift(groupProperties)){
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            
            List executionCourses = new ArrayList();
            executionCourses = groupProperties.getExecutionCourses();
            
            Iterator iterExecutionCourses = executionCourses.iterator();
            List executionCourseShifts = new ArrayList();
            while(iterExecutionCourses.hasNext()){
            
               List someShifts = persistentShift.readByExecutionCourse(
               		(IExecutionCourse)iterExecutionCourses.next());

               executionCourseShifts.addAll(someShifts);
            }
            
            List shifts = strategy.checkShiftsType(groupProperties,
                    executionCourseShifts);
            if (shifts == null || shifts.isEmpty()) {

            } else {

                for (int i = 0; i < shifts.size(); i++) {
                    IShift shift = (IShift) shifts.get(i);
                    result = strategy.checkNumberOfGroups(groupProperties,
                            shift);
                    
                    if (result) {
                    	infoShifts.add(InfoShift.newInfoFromDomain(shift));
                    }
                }
            }
        	}
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        
        infoSiteShifts.setShifts(infoShifts);
        return infoSiteShifts;
        
    }

}