package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço RemoverTurno
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class RemoverTurno implements IService {

    public Object run(final InfoShift infoShift, final InfoClass infoClass) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();

        final IShift shift = (IShift) persistentShift.readByOID(Shift.class, infoShift.getIdInternal());
        if (shift == null) {
            return Boolean.FALSE;
        }
        final ISchoolClass schoolClass = (ISchoolClass) CollectionUtils.find(shift.getAssociatedClasses(), new Predicate() {
            public boolean evaluate(Object arg0) {
                final ISchoolClass schoolClass = (ISchoolClass) arg0;
                return schoolClass.getIdInternal().equals(infoClass.getIdInternal());
            }
        });
        if (schoolClass == null) {
            return Boolean.FALSE;
        }

        persistentShift.simpleLockWrite(shift);
        shift.getAssociatedClasses().remove(schoolClass);
        schoolClass.getAssociatedShifts().remove(shift);

        return Boolean.TRUE;
    }

}