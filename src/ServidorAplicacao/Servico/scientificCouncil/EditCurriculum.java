/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.scientificCouncil;

import Dominio.Curriculum;
import Dominio.ICurriculum;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 *  
 */
public class EditCurriculum implements IServico {

    private static EditCurriculum _servico = new EditCurriculum();

    /**
     * The actor of this class.
     */

    private EditCurriculum() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "EditCurriculum";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static EditCurriculum getService() {
        return _servico;
    }

    public Boolean run(Integer curriculumId, String program, String programEn,
            String operacionalObjectives, String operacionalObjectivesEn, String generalObjectives,
            String generalObjectivesEn, Boolean basic) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            ICurriculum curriculum = (ICurriculum) persistentCurriculum.readByOID(Curriculum.class,
                    curriculumId, true);
            if (curriculum.getCurricularCourse().getBasic().equals(basic)) {
                curriculum.setProgram(program);
                curriculum.setProgramEn(programEn);
                curriculum.setOperacionalObjectives(operacionalObjectives);
                curriculum.setOperacionalObjectivesEn(operacionalObjectivesEn);
                curriculum.setGeneralObjectives(generalObjectives);
                curriculum.setGeneralObjectivesEn(generalObjectivesEn);
                return new Boolean(true);
            }
            return new Boolean(false);

            //TODO: KEEP HISTORY OF CURRICULAR INFORMATION

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}