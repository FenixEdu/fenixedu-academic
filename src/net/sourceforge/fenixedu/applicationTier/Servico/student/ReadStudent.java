/*
 * LerAula.java
 *
 * Created on December 16th, 2002, 1:58
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * Serviço LerAluno.
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoCurso;

public class ReadStudent implements IServico {

    private static ReadStudent _servico = new ReadStudent();

    /**
     * The singleton access method of this class.
     */
    public static ReadStudent getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadStudent() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadStudent";
    }

    // FIXME: We have to read the student by type also !!

    public Object run(Integer number) {

        InfoStudent infoStudent = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            //////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Isto não é para ficar assim. Está assim temporariamente até se
            // saber como é feita de facto a distinção
            // dos aluno, referente ao tipo, a partir da página de login.
            //////////////////////////////////////////////////////////////////////////////////////////////////////////
            IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                    new TipoCurso(TipoCurso.LICENCIATURA));

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

                infoStudent = new InfoStudent(student.getNumber(), student.getState(), infoPerson,
                        student.getDegreeType());
                infoStudent.setIdInternal(student.getIdInternal());
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return infoStudent;
    }

}