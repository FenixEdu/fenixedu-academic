/*
 * Created on 28/Ago/2003, 7:57:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.student;

import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 28/Ago/2003, 7:57:10
 * 
 */
public class ReadStudentById implements IServico
{
    private static ReadStudentById _servico = new ReadStudentById();
      /**
       * The singleton access method of this class.
       **/
      public static ReadStudentById getService() {
        return _servico;
      }

      /**
       * The actor of this class.
       **/
      private ReadStudentById() { }

      /**
       * Devolve o nome do servico
       **/
      public final String getNome() {
        return "student.ReadStudentById";
      }

      public Object run(Integer id) {

        System.out.println("Estou no run !");
        InfoStudent infoStudent = null;

        try {
          ISuportePersistente sp = SuportePersistenteOJB.getInstance();
          IStudent student = (IStudent) sp.getIPersistentStudent().readByOID(Student.class,id);

          if (student != null) {
            InfoPerson infoPerson = new InfoPerson();
            infoPerson.setNome(student.getPerson().getNome());
            infoPerson.setUsername(student.getPerson().getUsername());
            infoPerson.setPassword(student.getPerson().getPassword());
            infoPerson.setDistritoNaturalidade(student.getPerson().getDistritoNaturalidade());
            infoPerson.setNacionalidade(student.getPerson().getNacionalidade());
            infoPerson.setNomePai(student.getPerson().getNomePai());
            infoPerson.setNomeMae(student.getPerson().getNomeMae());
        
            infoStudent =
                new InfoStudent(
                    student.getNumber(),
                    student.getState(),
                    infoPerson,
                    student.getDegreeType());
          
              //by gedl at august 5th, 2003
              infoStudent.setIdInternal(student.getIdInternal());                
          }
        } catch (ExcepcaoPersistencia ex) {
          ex.printStackTrace();
        }

        return infoStudent;
      }

}
