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
import Util.TipoCurso;


public class ReadStudent implements IServico {

  private static ReadStudent _servico = new ReadStudent();
  /**
   * The singleton access method of this class.
   **/
  public static ReadStudent getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ReadStudent() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ReadStudent";
  }

  // FIXME: We have to read the student by type also !!

  public Object run(Integer number) {

    InfoStudent infoStudent = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      //////////////////////////////////////////////////////////////////////////////////////////////////////////
      // Isto não é para ficar assim. Está assim temporariamente até se saber como é feita de facto a distinção
      // dos aluno, referente ao tipo, a partir da página de login.
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
      IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number, new TipoCurso(TipoCurso.LICENCIATURA));

      if (student != null) {
      	InfoPerson infoPerson = new InfoPerson();
      	infoPerson.setNome(student.getPerson().getNome());
      	infoPerson.setUsername(student.getPerson().getUsername());
      	infoPerson.setPassword(student.getPerson().getPassword());
      	infoPerson.setDistritoNaturalidade(student.getPerson().getDistritoNaturalidade());
      	infoPerson.setNacionalidade(student.getPerson().getNacionalidade());
      	infoPerson.setNomePai(student.getPerson().getNomePai());
      	infoPerson.setNomeMae(student.getPerson().getNomeMae());
      	infoPerson.setIdInternal(student.getPerson().getIdInternal());
      	
		infoStudent =
			new InfoStudent(
				student.getNumber(),
				student.getState(),
				infoPerson,
				student.getDegreeType());
		infoStudent.setIdInternal(student.getIdInternal());
      }
      
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
    }

    return infoStudent;
  }

}