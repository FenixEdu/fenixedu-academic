/*
 * Created on 7/Out/2004
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GroupProperties;
import Dominio.IAttendsSet;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *  
 */

public class CreateStudentGroupWithoutShift implements IService {

    private IPersistentStudentGroup persistentStudentGroup = null;

    /**
     * The constructor of this class.
     */
    public CreateStudentGroupWithoutShift() {
    }

    private void checkIfStudentGroupExists(Integer groupNumber,
            IGroupProperties groupProperties) throws FenixServiceException {

        IStudentGroup studentGroup = null;

        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();
            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();

            studentGroup = persistentStudentGroup
                    .readStudentGroupByAttendsSetAndGroupNumber(
                            groupProperties.getAttendsSet(), groupNumber);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        if (studentGroup != null)
            throw new ExistingServiceException();
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer groupNumber,
            Integer groupPropertiesCode,List studentCodes)
            throws FenixServiceException {

        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        IPersistentGroupProperties persistentGroupProperites = null;
        IPersistentStudent persistentStudent = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudentGroup persistentStudentGroup = null;
        IPersistentAttendsSet persistentAttendsSet = null;
        
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentGroupProperites = persistentSupport
                    .getIPersistentGroupProperties();
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperites
                    .readByOID(GroupProperties.class, groupPropertiesCode);

            persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
            
            checkIfStudentGroupExists(groupNumber, groupProperties);

            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            IAttendsSet attendsSet = groupProperties.getAttendsSet();
            IStudentGroup newStudentGroup = new StudentGroup(groupNumber,attendsSet);
            persistentStudentGroup.simpleLockWrite(newStudentGroup);
            //persistentAttendsSet.simpleLockWrite(attendsSet);
            attendsSet.addStudentGroup(newStudentGroup);
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentStudentGroupAttend = persistentSupport
                    .getIPersistentStudentGroupAttend();

            List allStudentGroup = persistentStudentGroup
                    .readAllStudentGroupByAttendsSet(groupProperties.getAttendsSet());

            Iterator iterGroups = allStudentGroup.iterator();

            while (iterGroups.hasNext()) {
                IStudentGroup existingStudentGroup = (IStudentGroup) iterGroups
                        .next();
                IStudentGroupAttend newStudentGroupAttend = null;
                Iterator iterator = studentCodes.iterator();

                while (iterator.hasNext()) {
                    IStudent student = persistentStudent
                            .readByUsername((String) iterator.next());

		            
                    Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();
		            boolean found = false;
		            IFrequenta attend = null;
		            while(iterAttends.hasNext() && !found){
		            	attend = (IFrequenta)iterAttends.next();
		            	System.out.println("1-" + attend.getAluno().getNumber());
		            	if (attend.getAluno().equals(student)) {
		            		found=true;
		            		}
		            	else
		            	{
		            		attend = null;
		            	}
		            }
                    
		            if(attend==null){
	                	throw new InvalidArgumentsServiceException();
	                }
                   
                    newStudentGroupAttend = persistentStudentGroupAttend
                            .readBy(existingStudentGroup, attend);

                    if (newStudentGroupAttend != null)
                        throw new InvalidSituationServiceException();

                }
            }

            Iterator iter = studentCodes.iterator();

            while (iter.hasNext()) {

                IStudent student = persistentStudent
                        .readByUsername((String) iter.next());


                
                
                
                Iterator iterAttends = groupProperties.getAttendsSet().getAttends().iterator();
	            boolean found = false;
	            IFrequenta attend = null;
	            while(iterAttends.hasNext() && !found){
	            	attend = (IFrequenta)iterAttends.next();
	            	System.out.println("2-" + attend.getAluno().getNumber());
	            	if (attend.getAluno().equals(student)) {
	            		found=true;
	            		}
	            	else
	            	{
	            		attend = null;
	            	}
	            }

                if(attend==null){
                	throw new InvalidArgumentsServiceException();
                }
                		
                IStudentGroupAttend notExistingSGAttend = new StudentGroupAttend(
                        newStudentGroup, attend);

                persistentStudentGroupAttend
                        .simpleLockWrite(notExistingSGAttend);
                
                
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;

    }
    
}
