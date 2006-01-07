/*
 * Created on Jan 20, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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

        GuideEntry guideEntry = (GuideEntry) sp.getIPersistentGuideEntry().readByOID(GuideEntry.class,
                guideEntryID);

        Guide guide = guideEntry.getGuide();
        Student student = sp.getIPersistentStudent().readByPersonAndDegreeType(guide.getPerson().getIdInternal(),
                DegreeType.MASTER_DEGREE);
        GratuitySituation gratuitySituation = sp.getIPersistentGratuitySituation()
                .readGratuitySituationByExecutionDegreeAndStudent(guide.getExecutionDegree().getIdInternal(), student.getIdInternal());
        PersonAccount personAccount = sp.getIPersistentPersonAccount().readByPerson(guide.getPerson().getIdInternal());
        
        if(personAccount == null){
            personAccount = DomainFactory.makePersonAccount(guide.getPerson());
        }
        
        Person responsible = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

        Double value = new Double(guideEntry.getPrice().doubleValue()
                * guideEntry.getQuantity().intValue());

        DomainFactory.makeGratuityTransaction(value, new Timestamp(Calendar
                .getInstance().getTimeInMillis()), guideEntry.getDescription(), guide.getPaymentType(),
                TransactionType.GRATUITY_ADHOC_PAYMENT, Boolean.FALSE, responsible, personAccount,
                guideEntry, gratuitySituation);

        // Update GratuitySituation
        Double remainingValue = gratuitySituation.getRemainingValue();
        gratuitySituation.setRemainingValue(new Double(remainingValue.doubleValue()
                - value.doubleValue()));

    }

}
