/*
 * Created on 24/Ago/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IAttendsSet;
import Dominio.IAttends;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
             ISuportePersistente sp = SuportePersistenteOJB.getInstance();
             IPersistentAttendsSet persistentAttendsSet = sp.getIPersistentAttendsSet();
             IPersistentStudent persistentStudent = sp.getIPersistentStudent();
             
             IAttendsSet attendsSet = (IAttendsSet) persistentAttendsSet.readByOID(Dominio.AttendsSet.class,attendsSetId);
             
             
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
