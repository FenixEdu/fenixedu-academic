/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 */

public class PrepareEditAttendsSetMembers implements IServico{

    private static PrepareEditAttendsSetMembers service = new PrepareEditAttendsSetMembers();

    /**
     * The singleton access method of this class.
     */
    public static PrepareEditAttendsSetMembers getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private PrepareEditAttendsSetMembers() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "PrepareEditAttendsSetMembers";
    }

    /**
     * Executes the service.
     */

    public List run(Integer executionCourseCode, Integer groupPropertiesCode, Integer attendsSetCode)
            throws FenixServiceException {

        IFrequentaPersistente persistentAttend = null;
        IPersistentAttendsSet persistentAttendsSet = null;
        IPersistentGroupProperties persistentGroupProperties = null;
        List frequentasPossiveis = new ArrayList();
        List infoStudentList = new ArrayList();
        List frequentasStudentNumbersInsert = new ArrayList();

        try {

            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            persistentAttend = ps.getIFrequentaPersistente();
            persistentGroupProperties = ps.getIPersistentGroupProperties();
            persistentAttendsSet = ps.getIPersistentAttendsSet();

            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties.
													readByOID(GroupProperties.class,groupPropertiesCode);
            
            
            List frequentasAttendsSet = groupProperties.getAttendsSet().getAttends();            
            
            List allGroupPropertiesExecutionCourse = groupProperties.getGroupPropertiesExecutionCourse();            
            
            
            Iterator iterator = allGroupPropertiesExecutionCourse.iterator();
            while (iterator.hasNext()) {

            	IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse)iterator.next(); 
            	
            	if(groupPropertiesExecutionCourse.getProposalState().getState().intValue()==1
            		|| groupPropertiesExecutionCourse.getProposalState().getState().intValue()==2){
                IExecutionCourse executionCourse = groupPropertiesExecutionCourse.getExecutionCourse();
                List allExecutionCourseAttends = persistentAttend.readByExecutionCourse(executionCourse);
                frequentasPossiveis.addAll(allExecutionCourseAttends);
                }
            }
            
            
            Iterator iterator2 = frequentasAttendsSet.iterator();
            IAttends frequenta = null;
            while (iterator2.hasNext()) {
            	frequenta = (IAttends)iterator2.next();
            	frequentasStudentNumbersInsert.add(frequenta.getAluno().getNumber());
            	
            	if (frequentasPossiveis.contains(frequenta)) {
            		frequentasPossiveis.remove(frequenta);
            	}
            }
            
            // Delete duplicate students
            
            List frequentasInsert = new ArrayList();
            Iterator iteratorFreqPoss = frequentasPossiveis.iterator();
            while (iteratorFreqPoss.hasNext()) {
            	frequenta = (IAttends)iteratorFreqPoss.next();
            	if(!frequentasStudentNumbersInsert.contains(frequenta.getAluno().getNumber())){
            		
            		frequentasStudentNumbersInsert.add(frequenta.getAluno().getNumber());
                    frequentasInsert.add(frequenta);
            	}
            }
            
            
            IStudent student = null;
            Iterator iterator3 = frequentasInsert.iterator();

            while (iterator3.hasNext()) {

                student = ((IAttends) iterator3.next()).getAluno();
                infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return infoStudentList;

    }
}

