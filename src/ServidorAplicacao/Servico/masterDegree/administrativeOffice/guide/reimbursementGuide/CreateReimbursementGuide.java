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
import Util.State;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
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
            Integer reimbursementGuideNumber =
                persistentReimbursementGuide.generateReimbursementGuideNumber();

            IReimbursementGuide reimbursementGuide = new ReimbursementGuide();
            persistentReimbursementGuide.lockWrite(reimbursementGuide);

            reimbursementGuide.setCreationDate(Calendar.getInstance());
            reimbursementGuide.setJustification(justification);
            reimbursementGuide.setNumber(reimbursementGuideNumber);
            reimbursementGuide.setPaymentGuide(guide);
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
            reimbursementGuideSituation.setReimbursementGuideState(ReimbursementGuideState.ISSUED_TYPE);
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
     */
    private Double sumReimbursementGuidesValue(List reimbursementGuides)
    {
        Iterator iter = reimbursementGuides.iterator();
        Double sum = new Double(0);
        while (iter.hasNext())
        {
            IReimbursementGuide reimbursementGuide = (IReimbursementGuide) iter.next();
            sum = new Double(sum.doubleValue() + reimbursementGuide.getValue().doubleValue());
        }
        return sum;
    }

}
