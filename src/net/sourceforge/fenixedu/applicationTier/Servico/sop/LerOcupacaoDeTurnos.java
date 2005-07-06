/*
 * LerAlunosDeTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:41
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço LerAlunosDeTurno.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class LerOcupacaoDeTurnos implements IServico {

    private static LerOcupacaoDeTurnos _servico = new LerOcupacaoDeTurnos();

    /**
     * The singleton access method of this class.
     */
    public static LerOcupacaoDeTurnos getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private LerOcupacaoDeTurnos() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "LerOcupacaoDeTurnos";
    }

    public Object run(List infoShiftList) {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            Iterator infoShiftsIterator = infoShiftList.iterator();

            while (infoShiftsIterator.hasNext()) {
                InfoShift infoShift = (InfoShift) infoShiftsIterator.next();

                List alunos = sp.getITurnoAlunoPersistente().readByShift(infoShift.getIdInternal());

                Integer ocupation = new Integer(alunos.size());
                Double percentage = new Double(alunos.size() * 100 / infoShift.getLotacao().intValue());

                infoShift.setOcupation(ocupation);
                infoShift.setPercentage(percentage);

            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoShiftList;
    }

}
