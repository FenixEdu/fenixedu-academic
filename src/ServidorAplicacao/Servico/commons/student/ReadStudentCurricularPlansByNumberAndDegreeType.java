
/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

public class ReadStudentCurricularPlansByNumberAndDegreeType implements IServico
{

    private static ReadStudentCurricularPlansByNumberAndDegreeType servico =
        new ReadStudentCurricularPlansByNumberAndDegreeType();

    /**
     * The singleton access method of this class.
     **/
    public static ReadStudentCurricularPlansByNumberAndDegreeType getService()
    {
        return servico;
    }

    /**
     * The actor of this class.
     **/
    private ReadStudentCurricularPlansByNumberAndDegreeType()
    {
    }

    /**
     * Returns The Service Name */

    public final String getNome()
    {
        return "ReadStudentCurricularPlansByNumberAndDegreeType";
    }

    public List run(Integer studentNumber, TipoCurso degreeType)
        throws ExcepcaoInexistente, FenixServiceException
    {
        ISuportePersistente sp = null;

        List studentCurricularPlans = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();

            studentCurricularPlans =
                sp.getIStudentCurricularPlanPersistente().readByStudentNumberAndDegreeType(
                    studentNumber,
                    degreeType);

        } catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if ((studentCurricularPlans == null) || (studentCurricularPlans.size() == 0))
        {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlans.iterator();
        List result = new ArrayList();

        while (iterator.hasNext())
        {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
            result.add(
                Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));
        }

        return result;
    }
}