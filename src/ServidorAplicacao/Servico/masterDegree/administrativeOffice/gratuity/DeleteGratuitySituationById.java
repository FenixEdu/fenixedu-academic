/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;


/**
 * @author Tânia Pousão
 *
 */
public class DeleteGratuitySituationById implements IServico
{

    private static DeleteGratuitySituationById servico = new DeleteGratuitySituationById();

    /**
	 * The singleton access method of this class.
	 */
    public static DeleteGratuitySituationById getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private DeleteGratuitySituationById()
    {
    }

    /**
	 * Returns The Service Name
	 */

    public final String getNome()
    {
        return "DeleteGratuitySituationById";
    }

    public Boolean run(Integer gratuitySituationID) throws FenixServiceException
    {

		return Boolean.FALSE;
    }
}
