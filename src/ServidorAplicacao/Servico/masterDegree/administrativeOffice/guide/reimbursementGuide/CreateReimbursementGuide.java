/*
 * Created on 21/Mar/2003
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.Guide;
import Dominio.GuideEntry;
import Dominio.IEmployee;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IPessoa;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Dominio.reimbursementGuide.IReimbursementGuideSituation;
import Dominio.reimbursementGuide.ReimbursementGuide;
import Dominio.reimbursementGuide.ReimbursementGuideEntry;
import Dominio.reimbursementGuide.ReimbursementGuideSituation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidReimbursementValueServiceException;
import ServidorAplicacao.Servico.exceptions.guide.RequiredJustificationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.IPersistentGuideEntry;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.guide.IPersistentReimbursementGuide;
import ServidorPersistente.guide.IPersistentReimbursementGuideEntry;
import ServidorPersistente.guide.IPersistentReimbursementGuideSituation;
import Util.ReimbursementGuideState;
import Util.SituationOfGuide;
import Util.State;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a><br><strong>Description:</strong>
 *         <br>This service creates a reimbursement guide and associates it with a payment guide. It
 *         also creates the reimbursement guide situation and sets it to the ISSUED state. If any
 *         problem occurs during the execution of the service a FenixServiceException is thrown. If the
 *         payment guide doesn't have a PAYED state active an InvalidGuideSituationServiceException is
 *         thrown. If the value of the reimbursement exceeds the total of the payment guide an
 *         InvalidReimbursementValueServiceException is thrown and if the sum of all the reimbursements
 *         associated with the payment guide exceeds the total an
 *         InvalidReimbursementValueSumServiceException is thrown. <br>The service also generates the
 *         number of the new reimbursement guide using a sequential method.
 */
public class CreateReimbursementGuide implements IService
{

	public CreateReimbursementGuide()
	{
	}

	/**
	 * @throws FenixServiceException,
	 *             InvalidReimbursementValueServiceException, InvalidGuideSituationServiceException,
	 *             InvalidReimbursementValueSumServiceException
	 */

	public Integer run(
		Integer guideId,
		String remarks,
		List infoReimbursementGuideEntries,
		IUserView userView)
		throws FenixServiceException
	{
		try
		{
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();

			IPersistentGuide persistentGuide = ps.getIPersistentGuide();
			IPersistentReimbursementGuide persistentReimbursementGuide =
				ps.getIPersistentReimbursementGuide();
			IPersistentReimbursementGuideSituation persistentReimbursementGuideSituation =
				ps.getIPersistentReimbursementGuideSituation();
			IPersistentReimbursementGuideEntry persistentReimbursementGuideEntry =
				ps.getIPersistentReimbursementGuideEntry();

			IGuide guide = (IGuide) persistentGuide.readByOId(new Guide(guideId), false);

			if (!guide.getActiveSituation().getSituation().equals(SituationOfGuide.PAYED_TYPE))
			{
				throw new InvalidGuideSituationServiceException("error.exception.masterDegree.invalidGuideSituation");
			}

			List reimbursementGuideEntries =
				Cloner.copyListInfoReimbursementGuideEntries2ListIReimbursementGuideEntries(
					infoReimbursementGuideEntries);

			Iterator it = reimbursementGuideEntries.iterator();
			IReimbursementGuideEntry reimbursementGuideEntry = null;

			while (it.hasNext())
			{
				reimbursementGuideEntry = (IReimbursementGuideEntry) it.next();

				if (reimbursementGuideEntry.getJustification().length() == 0)
					throw new RequiredJustificationServiceException("error.exception.masterDegree.requiredJustification");

				if (checkReimbursementGuideEntriesSum(reimbursementGuideEntry, ps) == false)
					throw new InvalidReimbursementValueServiceException("error.exception.masterDegree.invalidReimbursementValue");
			}

			//reimbursement Guide
			Integer reimbursementGuideNumber =
				persistentReimbursementGuide.generateReimbursementGuideNumber();

			IReimbursementGuide reimbursementGuide = new ReimbursementGuide();
			persistentReimbursementGuide.lockWrite(reimbursementGuide);

			reimbursementGuide.setCreationDate(Calendar.getInstance());
			reimbursementGuide.setNumber(reimbursementGuideNumber);
			reimbursementGuide.setGuide(guide);

			//read employee
			IPersistentEmployee persistentEmployee = ps.getIPersistentEmployee();
			IPessoaPersistente persistentPerson = ps.getIPessoaPersistente();
			IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			IEmployee employee = persistentEmployee.readByPerson(person);

			//reimbursement Guide Situation
			IReimbursementGuideSituation reimbursementGuideSituation = new ReimbursementGuideSituation();
			persistentReimbursementGuideSituation.lockWrite(reimbursementGuideSituation);

			reimbursementGuideSituation.setEmployee(employee);
			reimbursementGuideSituation.setModificationDate(Calendar.getInstance());
			reimbursementGuideSituation.setOfficialDate(Calendar.getInstance());
			reimbursementGuideSituation.setReimbursementGuide(reimbursementGuide);
			reimbursementGuideSituation.setReimbursementGuideState(ReimbursementGuideState.ISSUED);
			reimbursementGuideSituation.setRemarks(remarks);
			reimbursementGuideSituation.setState(new State(State.ACTIVE));

			//reimbursement Guide Entries
			it = reimbursementGuideEntries.iterator();

			while (it.hasNext())
			{
				reimbursementGuideEntry = (IReimbursementGuideEntry) it.next();
				reimbursementGuideEntry.setReimbursementGuide(reimbursementGuide);
				persistentReimbursementGuideEntry.lockWrite(reimbursementGuideEntry);
			}

			return reimbursementGuide.getIdInternal();

		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}

	/**
	 * @param newReimbursementGuideEntry
	 * @param suportePersistente
	 * @return true if the sum of existents reeimbursement guide entries of a guide entry with the new
	 *         reimbursement guide entry is less or equal than their guide entry
	 */
	private boolean checkReimbursementGuideEntriesSum(
		IReimbursementGuideEntry newReimbursementGuideEntry,
		ISuportePersistente suportePersistente)
		throws ExcepcaoPersistencia, FenixServiceException
	{
		IPersistentGuideEntry persistentGuideEntry = suportePersistente.getIPersistentGuideEntry();
		IPersistentReimbursementGuideEntry persistentReimbursementGuideEntry =
			suportePersistente.getIPersistentReimbursementGuideEntry();

		IGuideEntry guideEntry = null;
		List reimbursementGuideEntries = null;

		try
		{
			guideEntry =
				(IGuideEntry) persistentGuideEntry.readByOID(
					GuideEntry.class,
					newReimbursementGuideEntry.getGuideEntry().getIdInternal());

			reimbursementGuideEntries = persistentReimbursementGuideEntry.readByGuideEntry(guideEntry);

		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}

		Iterator it = reimbursementGuideEntries.iterator();
		Double sum = new Double(newReimbursementGuideEntry.getValue().doubleValue());

		while (it.hasNext())
		{
			sum =
				new Double(
					sum.doubleValue() + ((ReimbursementGuideEntry) it.next()).getValue().doubleValue());
		}

		Double guideEntryValue =
			new Double(guideEntry.getPrice().doubleValue() * guideEntry.getQuantity().intValue());

		if (sum.doubleValue() > guideEntryValue.doubleValue())
			return false;
		else
			return true;

	}

}
