/*
 * Created on 28/Jul/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGroupProperties;
import DataBeans.util.Cloner;
import Dominio.AttendInAttendsSet;
import Dominio.AttendsSet;
import Dominio.ExecutionCourse;
import Dominio.GroupPropertiesExecutionCourse;
import Dominio.IAttendInAttendsSet;
import Dominio.IAttendsSet;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IGroupPropertiesExecutionCourse;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAttendInAttendsSet;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentGroupPropertiesExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.ProposalState;

/**
 * @author asnr and scpo
 *  
 */

public class CreateGroupProperties implements IService {

    /**
     * The constructor of this class.
     */
    public CreateGroupProperties() {
    }

     
    /**
     * Executes the service.
     */
    public boolean run(Integer executionCourseCode,
            InfoGroupProperties infoGroupProperties)
            throws FenixServiceException {

        IExecutionCourse executionCourse = null;
        try {
        	
            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                    .getIPersistentExecutionCourse();
            IPersistentGroupProperties persistentGroupProperties = persistentSupport
					.getIPersistentGroupProperties();        
            IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = 
            		persistentSupport.getIPersistentGroupPropertiesExecutionCourse();
            IPersistentAttendsSet persistentAttendsSet = persistentSupport
            		.getIPersistentAttendsSet();
            IPersistentAttendInAttendsSet persistentAttendInAttendsSet = persistentSupport
					.getIPersistentAttendInAttendsSet();
            IFrequentaPersistente persistentFrequenta = persistentSupport.getIFrequentaPersistente();
    		
            
            executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, executionCourseCode);
            
            
            
            // Checks if already exists a groupProperties with the same name, related with the executionCourse
            
            if(executionCourse.getGroupPropertiesByName(infoGroupProperties.getName())!=null){
            	throw new ExistingServiceException();
            }
            //

            
            List attends = new ArrayList();
            List attendInAttendsSetList = new ArrayList();
            attends=persistentFrequenta.readByExecutionCourse(executionCourse);
            
            


            IGroupProperties newGroupProperties = Cloner
                    .copyInfoGroupProperties2IGroupProperties(infoGroupProperties);
            persistentGroupProperties.simpleLockWrite(newGroupProperties);
            
            
            
            
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse;
            groupPropertiesExecutionCourse = new GroupPropertiesExecutionCourse(newGroupProperties,executionCourse);
            persistentGroupPropertiesExecutionCourse.simpleLockWrite(groupPropertiesExecutionCourse);
            groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(1)));
            
            IAttendsSet attendsSet;
            attendsSet = new AttendsSet(executionCourse.getNome());
            persistentAttendsSet.simpleLockWrite(attendsSet);
            
            IAttendInAttendsSet attendInAttendsSet;
            Iterator iterAttends = attends.iterator();
            while(iterAttends.hasNext()){
            	IFrequenta frequenta = (IFrequenta)iterAttends.next();
            	
            	attendInAttendsSet = new AttendInAttendsSet(frequenta,attendsSet);
            	persistentAttendInAttendsSet.simpleLockWrite(attendInAttendsSet);
            	attendInAttendsSetList.add(attendInAttendsSet);
            	frequenta.addAttendInAttendsSet(attendInAttendsSet);
            }
            attendsSet.setGroupProperties(newGroupProperties);
            attendsSet.setAttendInAttendsSet(attendInAttendsSetList);
            attendsSet.setStudentGroups(new ArrayList());
            
            
            newGroupProperties.setAttendsSet(attendsSet);
            newGroupProperties.addGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
            executionCourse.addGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);

            
        }  catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}