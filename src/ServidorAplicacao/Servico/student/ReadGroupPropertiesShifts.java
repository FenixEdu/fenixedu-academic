/*
 * Created on 28/Ago/2003
 *  
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoShift;
import DataBeans.InfoSiteShifts;
import Dominio.GroupProperties;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
                    ITurno shift = (ITurno) shifts.get(i);
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