/*
 * LerAula.java
 *
 * Created on December 16th, 2002, 1:58
 */

package ServidorAplicacao.Servico.student;

/**
 * Serviço LerAluno.
 *
 * @author tfc130
 **/
import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


public class ReadStudentByUsername implements IServico {

  private static ReadStudentByUsername _servico = new ReadStudentByUsername();
  /**
   * The singleton access method of this class.
   **/
  public static ReadStudentByUsername getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ReadStudentByUsername() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ReadStudentByUsername";
  }

  public Object run(String username) {

    InfoStudent infoStudent = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      IStudent student = sp.getIPersistentStudent().readByUsername(username);

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