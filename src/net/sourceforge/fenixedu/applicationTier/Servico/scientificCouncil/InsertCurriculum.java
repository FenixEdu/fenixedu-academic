/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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

    public Boolean run(Integer curricularCourseId, String program, String programEn,
            String operacionalObjectives, String operacionalObjectivesEn, String generalObjectives,
            String generalObjectivesEn, Boolean basic) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            ICurriculum curriculum = new Curriculum();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
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