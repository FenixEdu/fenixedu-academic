/*
 * Created on 16/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoCurriculum;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadCurriculum implements IServico
{

    private static ReadCurriculum service = new ReadCurriculum();

    /**
    * The singleton access method of this class.
    */
    public static ReadCurriculum getService()
    {
        return service;
    }

    /**
    * The constructor of this class.
    */
    private ReadCurriculum()
    {
    }

    /**
    * Service name
    */
    public final String getNome()
    {
        return "ReadCurriculum";
    }

    /**
    * Executes the service. Returns the current InfoCurriculum.
    */
    public InfoCurriculum run(Integer curricularCourseId) throws FenixServiceException
    {

        ICurricularCourse curricularCourse;
        ICurriculum curriculum;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            curricularCourse =
                (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOId(
                    new CurricularCourse(curricularCourseId),
                    false);
            if (curricularCourse == null)
                throw new NonExistingServiceException();
            curriculum =
                sp.getIPersistentCurriculum().readCurriculumByCurricularCourse(curricularCourse);
        } catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        if (curriculum == null)
            return null;
        InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
        return infoCurriculum;
    }
}