/*
 * Created on 21/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.Guide;
import Dominio.IEmployee;
import Dominio.IGuide;
import Dominio.IPessoa;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.IReimbursementGuideSituation;
import Dominio.reimbursementGuide.ReimbursementGuide;
import Dominio.reimbursementGuide.ReimbursementGuideSituation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidReimbursementValueServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidReimbursementValueSumServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.guide.IPersistentReimbursementGuide;
import Util.ReimbursementGuideState;
import Util.SituationOfGuide;
import Util.State;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 * 
 * <br>
 * <strong>Description:</strong><br>
 * This service creates a reimbursement guide and associates it with a payment guide.
 * It also creates the reimbursement guide situation and sets it to the ISSUED state.
 * If any problem occurs during the execution of the service a FenixServiceException 
 * is thrown. If the payment guide doesn't have a PAYED state active an 
 * InvalidGuideSituationServiceException is thrown. If the value of the reimbursement
 * exceeds the total of the payment guide an InvalidReimbursementValueServiceException
 * is thrown and if the sum of all the reimbursements associated with the payment guide
 * exceeds the total an  InvalidReimbursementValueSumServiceException is thrown.
 * <br>
 * The service also generates the number of the new reimbursement guide using a sequential 
 * method.   
 *
 * 
 */
public class CreateReimbursementGuide implements IServico
{

    private static CreateReimbursementGuide servico = new CreateReimbursementGuide();

    /**
     * The singleton access method of this class.
     **/
    public static CreateReimbursementGuide getService()
    {
        return servico;
    }

    /**
     * The actor of this class.
     **/
    private CreateReimbursementGuide()
    {
    }

    /**
     * Returns The Service Name */

    public final String getNome()
    {
        return "CreateReimbursementGuide";
    }
    /**
     *  @throws FenixServiceException, InvalidReimbursementValueServiceException,
     *  InvalidGuideSituationServiceException, InvalidReimbursementValueSumServiceException
     */

    public void run(Integer guideId, String justification, Double value, IUserView userView)
        throws FenixServiceException
    {
        try
        {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentGuide persistentGuide = ps.getIPersistentGuide();
            IPersistentReimbursementGuide persistentReimbursementGuide =
                ps.getIPersistentReimbursementGuide();
            IGuide guide = (IGuide) persistentGuide.readByOId(new Guide(guideId), false);
            Double total = guide.getTotal();
            if (total.doubleValue() < value.doubleValue())
            {
                throw new InvalidReimbursementValueServiceException();
            }
            List reimbursementGuides = persistentReimbursementGuide.readByGuide(guide);
            if (reimbursementGuides != null)
            {
                Double valueSum = sumReimbursementGuidesValue(reimbursementGuides);
                if (valueSum.doubleValue() + value.doubleValue() > total.doubleValue())
                {
                    throw new InvalidReimbursementValueSumServiceException();
                }
            }

            if (!guide.getActiveSituation().getSituation().equals(SituationOfGuide.PAYED_TYPE))
            {
                throw new InvalidGuideSituationServiceException();
            }
            Integer reimbursementGuideNumber =
                persistentReimbursementGuide.generateReimbursementGuideNumber();

            IReimbursementGuide reimbursementGuide = new ReimbursementGuide();
            persistentReimbursementGuide.lockWrite(reimbursementGuide);

            reimbursementGuide.setCreationDate(Calendar.getInstance());
            reimbursementGuide.setJustification(justification);
            reimbursementGuide.setNumber(reimbursementGuideNumber);
            reimbursementGuide.setGuide(guide);
            reimbursementGuide.setValue(value);

            IPersistentEmployee persistentEmployee = ps.getIPersistentEmployee();
            IPessoaPersistente persistentPerson = ps.getIPessoaPersistente();
            IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = persistentEmployee.readByPerson(person);

            IReimbursementGuideSituation reimbursementGuideSituation = new ReimbursementGuideSituation();
            persistentReimbursementGuide.lockWrite(reimbursementGuideSituation);

            reimbursementGuideSituation.setEmployee(employee);
            reimbursementGuideSituation.setModificationDate(Calendar.getInstance());
            reimbursementGuideSituation.setReimbursementGuide(reimbursementGuide);
            reimbursementGuideSituation.setReimbursementGuideState(ReimbursementGuideState.ISSUED);
            reimbursementGuideSituation.setRemarks(justification);
            reimbursementGuideSituation.setState(new State(State.ACTIVE));

        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

    /**
     * @param reimbursementGuides
     * @return
     * 
     * Only sums the reimbursement guide's values of those that were not annulled
     */
    private Double sumReimbursementGuidesValue(List reimbursementGuides)
    {
        Iterator iter = reimbursementGuides.iterator();
        Double sum = new Double(0);
        while (iter.hasNext())
        {
            IReimbursementGuide reimbursementGuide = (IReimbursementGuide) iter.next();
            if (!reimbursementGuide
                .getActiveReimbursementGuideSituation()
                .getReimbursementGuideState()
                .equals(ReimbursementGuideState.ANNULLED))
            {
                sum = new Double(sum.doubleValue() + reimbursementGuide.getValue().doubleValue());
            }
        }
        return sum;
    }

}
