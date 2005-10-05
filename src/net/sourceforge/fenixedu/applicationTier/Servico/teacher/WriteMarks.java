package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class WriteMarks implements IService {

    public void run(final Integer evaluationOID, final Map<Integer, String> marks)
            throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentEvaluation persistentEvaluation = persistentSupport.getIPersistentEvaluation();
        final IFrequentaPersistente persistentAttends = persistentSupport.getIFrequentaPersistente();

        final IEvaluation evaluation = (IEvaluation) persistentEvaluation.readByOID(Evaluation.class, evaluationOID);
        for (final Entry<Integer, String> entry : marks.entrySet()) {
            final Integer attendsOID = entry.getKey();
            final String markValue = entry.getValue();

            final IMark mark = findExistingMark(evaluation.getMarks(), attendsOID);
            if (mark == null) {
                final IAttends attends = (IAttends) persistentAttends.readByOID(Attends.class, attendsOID);
                DomainFactory.makeMark(attends, evaluation, markValue);
            } else {
                mark.setMark(markValue);
            }
        }
   }

    private IMark findExistingMark(final List<IMark> marks, final Integer attendsOID) {
        for (final IMark mark : marks) {
            final IAttends attends = mark.getAttend();
            if (attends.getIdInternal().equals(attendsOID)) {
                return mark;
            }
        }
        return null;
    }

}