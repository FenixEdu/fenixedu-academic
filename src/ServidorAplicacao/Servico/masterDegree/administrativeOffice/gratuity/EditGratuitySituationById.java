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
public class EditGratuitySituationById implements IServico
{

    private static EditGratuitySituationById servico = new EditGratuitySituationById();

    /**
	 * The singleton access method of this class.
	 */
    public static EditGratuitySituationById getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private EditGratuitySituationById()
    {
    }

    /**
	 * Returns The Service Name
	 */

    public final String getNome()
    {
        return "EditGratuitySituationById";
    }

    public Object run(Integer gratuitySituationID) throws FenixServiceException
    {

		return null;
    }
}
