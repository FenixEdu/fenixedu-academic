/*
 * Created on 26/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.student.gaugingTests.physics;

import DataBeans.gaugingTests.physics.InfoGaugingTestResult;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.gaugingTests.physics.IGaugingTestResult;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import Util.TipoCurso;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 *  
 */

public class readGaugingTestsResults implements IServico
{

    private static readGaugingTestsResults service = new readGaugingTestsResults();

    /**
	 * The singleton access method of this class.
	 */
    public static readGaugingTestsResults getService()
    {
        return service;
    }
    /**
	 * The constructor of this class.
	 */
    private readGaugingTestsResults()
    {
    }
    /**
	 * The name of the service
	 */
    public final String getNome()
    {
        return "readGaugingTestsResults";
    }

    /**
	 * Executes the service.
	 */

    public InfoGaugingTestResult run(IUserView userView) throws FenixServiceException
    {
        try
        {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentGaugingTestResult persistentGaugingTestResult =
                ps.getIPersistentGaugingTestResult();
            IPessoaPersistente persistentPerson = ps.getIPessoaPersistente();
            IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());

            IPersistentStudent persistentStudent = ps.getIPersistentStudent();

            IStudent student =
                persistentStudent.readByPersonAndDegreeType(person, TipoCurso.LICENCIATURA_OBJ);
            IGaugingTestResult gaugingTestsResult = persistentGaugingTestResult.readByStudent(student);
            if (gaugingTestsResult != null)
            {

                InfoGaugingTestResult infoGaugingTestResult =
                    Cloner.copyIGaugingTestResult2IngoGaugingTestResult(gaugingTestsResult);
                return infoGaugingTestResult;
            }
            else
            {
                return null;
            }

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

    }
}
