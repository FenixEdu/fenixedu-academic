/*
 * ApagarTurma.java
 *
 * Created on 27 de Outubro de 2002, 18:13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço ApagarTurma.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

public class ApagarTurma implements IServico {

    private static ApagarTurma _servico = new ApagarTurma();

    /**
     * The singleton access method of this class.
     */
    public static ApagarTurma getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ApagarTurma() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ApagarTurma";
    }

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {

        
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                infoClass.getIdInternal());
        sp.getITurmaPersistente().simpleLockWrite(schoolClass);
        final List shifts = schoolClass.getAssociatedShifts();
        final List shiftsToRemove = new ArrayList(shifts);
        shifts.removeAll(shiftsToRemove);

        sp.getITurmaPersistente().delete(schoolClass);

        return new Boolean(true);
    }

}