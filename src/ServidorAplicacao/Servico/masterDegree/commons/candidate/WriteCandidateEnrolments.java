package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import Dominio.CandidateEnrolment;
import Dominio.CurricularCourse;
import Dominio.ICandidateEnrolment;
import Dominio.ICurricularCourse;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class WriteCandidateEnrolments implements IServico
{

    private static WriteCandidateEnrolments servico = new WriteCandidateEnrolments();

    /**
     * The singleton access method of this class.
     **/
    public static WriteCandidateEnrolments getService()
    {
        return servico;
    }

    /**
     * The actor of this class.
     **/
    private WriteCandidateEnrolments()
    {
    }

    /**
     * Returns The Service Name */

    public final String getNome()
    {
        return "WriteCandidateEnrolments";
    }

    public void run(
        Integer[] selection,
        Integer candidateID,
        Double credits,
        String givenCreditsRemarks)
        throws FenixServiceException
    {
        try
        {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IMasterDegreeCandidate mdcTemp = new MasterDegreeCandidate();
            mdcTemp.setIdInternal(candidateID);
            IMasterDegreeCandidate masterDegreeCandidate =
                (IMasterDegreeCandidate) sp.getIPersistentMasterDegreeCandidate().readByOId(
                    mdcTemp,
                    true);

            if (masterDegreeCandidate == null)
            {
                throw new NonExistingServiceException();
            }

            masterDegreeCandidate.setGivenCredits(credits);

            if (credits.floatValue() != 0)
            {
                masterDegreeCandidate.setGivenCreditsRemarks(givenCreditsRemarks);
            }

            // Clean the Enrolment Information
            sp.getIPersistentCandidateEnrolment().deleteAllByCandidateID(masterDegreeCandidate);

            sp.confirmarTransaccao();
            sp.iniciarTransaccao();

            for (int i = 0; i < selection.length; i++)
            {
//                ICurricularCourseScope curricularCourseScopeTemp = new CurricularCourseScope();
//                curricularCourseScopeTemp.setIdInternal(selection[i]);
//                ICurricularCourseScope curricularCourseScope =
//                    (ICurricularCourseScope) sp.getIPersistentCurricularCourseScope().readByOId(
//                        curricularCourseScopeTemp,
//                        false);
//
//                if (curricularCourseScope == null)
//                {
//                    throw new NonExistingServiceException();
//                }
				
				ICurricularCourse curricularCourseTemp = new CurricularCourse();
				curricularCourseTemp.setIdInternal(selection[i]);
				ICurricularCourse curricularCourse =
					(ICurricularCourse) sp.getIPersistentCurricularCourse().readByOId(
						curricularCourseTemp,
						false);

				if (curricularCourse == null)
				{
					throw new NonExistingServiceException();
				}

                ICandidateEnrolment candidateEnrolment = new CandidateEnrolment();

                candidateEnrolment.setMasterDegreeCandidate(masterDegreeCandidate);
                //candidateEnrolment.setCurricularCourseScope(curricularCourseScope);
				candidateEnrolment.setCurricularCourse(curricularCourse);

                try
                {
                    sp.getIPersistentCandidateEnrolment().write(candidateEnrolment);
                }
                catch (ExcepcaoPersistencia e)
                {
                    throw new ExcepcaoPersistencia();
                }

            }

        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }
}
