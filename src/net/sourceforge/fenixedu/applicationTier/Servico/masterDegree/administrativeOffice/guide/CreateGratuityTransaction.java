/*
 * Created on Jan 20, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.IGratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateGratuityTransaction implements IService {

    public void run(Integer guideEntryID, IUserView userView) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IGuideEntry guideEntry = (IGuideEntry) sp.getIPersistentGuideEntry().readByOID(GuideEntry.class,
                guideEntryID);

        IGuide guide = guideEntry.getGuide();
        IStudent student = sp.getIPersistentStudent().readByPersonAndDegreeType(guide.getPerson(),
                DegreeType.MASTER_DEGREE);
        IGratuitySituation gratuitySituation = sp.getIPersistentGratuitySituation()
                .readGratuitySituationByExecutionDegreeAndStudent(guide.getExecutionDegree().getIdInternal(), student.getIdInternal());
        IPersonAccount personAccount = sp.getIPersistentPersonAccount().readByPerson(guide.getPerson().getIdInternal());
        IPerson responsible = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

        Double value = new Double(guideEntry.getPrice().doubleValue()
                * guideEntry.getQuantity().intValue());

        IGratuityTransaction gratuityTransaction = new GratuityTransaction(value, new Timestamp(Calendar
                .getInstance().getTimeInMillis()), guideEntry.getDescription(), guide.getPaymentType(),
                TransactionType.GRATUITY_ADHOC_PAYMENT, Boolean.FALSE, responsible, personAccount,
                guideEntry, gratuitySituation);

        sp.getIPersistentGratuityTransaction().lockWrite(gratuityTransaction);

        // Update GratuitySituation
        sp.getIPersistentGratuitySituation().lockWrite(gratuitySituation);

        Double remainingValue = gratuitySituation.getRemainingValue();

        gratuitySituation.setRemainingValue(new Double(remainingValue.doubleValue()
                - value.doubleValue()));

    }

}
