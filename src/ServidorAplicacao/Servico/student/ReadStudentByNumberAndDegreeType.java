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
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;


public class ReadStudentByNumberAndDegreeType implements IServico {

  private static ReadStudentByNumberAndDegreeType _servico = new ReadStudentByNumberAndDegreeType();
  /**
   * The singleton access method of this class.
   **/
  public static ReadStudentByNumberAndDegreeType getService() {
    return _servico;
  }

  /**
   * The actor of this class.
   **/
  private ReadStudentByNumberAndDegreeType() { }

  /**
   * Devolve o nome do servico
   **/
  public final String getNome() {
    return "ReadStudentByNumberAndDegreeType";
  }

  // FIXME: We have to read the student by type also !!

  public Object run(Integer number, TipoCurso degreeType) throws FenixServiceException {

    InfoStudent infoStudent = null;

    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      //////////////////////////////////////////////////////////////////////////////////////////////////////////
      // Isto não é para ficar assim. Está assim temporariamente até se saber como é feita de facto a distinção
      // dos aluno, referente ao tipo, a partir da página de login.
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////
      IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number, degreeType);

      if (student != null) {

		infoStudent = Cloner.copyIStudent2InfoStudent(student);
      }
      
    } catch (ExcepcaoPersistencia ex) {
      ex.printStackTrace();
      throw new FenixServiceException(ex);
    }

    return infoStudent;
  }

}