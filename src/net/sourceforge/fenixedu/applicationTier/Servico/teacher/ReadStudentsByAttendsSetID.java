/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

/**
 * @author joaosa & rmalo
 */

public class ReadStudentsByAttendsSetID implements IServico{

	private static ReadStudentsByAttendsSetID _servico = new ReadStudentsByAttendsSetID();
         /**
          * The singleton access method of this class.
          **/
         public static ReadStudentsByAttendsSetID getService() {
           return _servico;
         }

         /**
          * The actor of this class.
          **/
         private ReadStudentsByAttendsSetID() { }

         /**
          * Devolve o nome do servico
          **/
         public final String getNome() {
           return "teacher.ReadStudentsByAttendsSetID";
         }

         public List run(Integer executionCourseId, Integer groupPropertiesId, Integer attendsSetId) throws FenixServiceException {

           List infoStudents = new LinkedList();


           try {
             ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
             IPersistentAttendsSet persistentAttendsSet = sp.getIPersistentAttendsSet();
             
             IAttendsSet attendsSet = (IAttendsSet) persistentAttendsSet.readByOID(AttendsSet.class,attendsSetId);
             
             
             for (Iterator iter= attendsSet.getAttends().iterator(); iter.hasNext();)
			{
				IAttends attend= (IAttends) iter.next();
				IStudent student = attend.getAluno();
                infoStudents.add(Cloner.copyIStudent2InfoStudent(student));                
			}
           
        
           } catch (ExcepcaoPersistencia ex) {
             throw new FenixServiceException();
           }
           return infoStudents;    
         }

}
