/*
 * Created on Jan 20, 2005
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GuideEntry;
import Dominio.IGratuitySituation;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IPersonAccount;
import Dominio.IPerson;
import Dominio.IStudent;
import Dominio.transactions.GratuityTransaction;
import Dominio.transactions.IGratuityTransaction;
import ServidorAplicacao.IUserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;
import Util.transactions.TransactionType;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateGratuityTransaction implements IService {

    public void run(Integer guideEntryID, IUserView userView) throws ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IGuideEntry guideEntry = (IGuideEntry) sp.getIPersistentGuideEntry().readByOID(GuideEntry.class,
                guideEntryID);

        IGuide guide = guideEntry.getGuide();
        IStudent student = sp.getIPersistentStudent().readByPersonAndDegreeType(guide.getPerson(),
                TipoCurso.MESTRADO_OBJ);
        IGratuitySituation gratuitySituation = sp.getIPersistentGratuitySituation()
                .readGratuitySituationByExecutionDegreeAndStudent(guide.getExecutionDegree(), student);
        IPersonAccount personAccount = sp.getIPersistentPersonAccount().readByPerson(guide.getPerson());
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
