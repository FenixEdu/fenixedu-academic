/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.scientificCouncil;

import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 *  
 */
public class InsertCurriculum implements IServico {

    private static InsertCurriculum _servico = new InsertCurriculum();

    /**
     * The actor of this class.
     */

    private InsertCurriculum() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "InsertCurriculum";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static InsertCurriculum getService() {
        return _servico;
    }

    public Boolean run(Integer curricularCourseId, String program,
            String programEn, String operacionalObjectives,
            String operacionalObjectivesEn, String generalObjectives,
            String generalObjectivesEn, Boolean basic)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurriculum persistentCurriculum = sp
                    .getIPersistentCurriculum();
            ICurriculum curriculum = new Curriculum();
            IPersistentCurricularCourse persistentCurricularCourse = sp
                    .getIPersistentCurricularCourse();
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseId);
            if (curricularCourse == null) {
                throw new InvalidArgumentsServiceException();
            }
            ICurriculum curriculumFromDB = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourse);
            if (curriculumFromDB != null) {
                throw new InvalidArgumentsServiceException();
            }
            if (curricularCourse.getBasic().equals(basic)) {
                persistentCurriculum.simpleLockWrite(curriculum);
                curriculum.setCurricularCourse(curricularCourse);
                curriculum.setProgram(program);
                curriculum.setProgramEn(programEn);
                curriculum.setOperacionalObjectives(operacionalObjectives);
                curriculum.setOperacionalObjectivesEn(operacionalObjectivesEn);
                curriculum.setGeneralObjectives(generalObjectives);
                curriculum.setGeneralObjectivesEn(generalObjectivesEn);
                return new Boolean(true);
            }

            return new Boolean(false);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}