/*
 * ApagarTurma.java
 *
 * Created on 27 de Outubro de 2002, 18:13
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço ApagarTurma.
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoClass;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

        
        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        final ITurma schoolClass = (ITurma) sp.getITurmaPersistente().readByOID(Turma.class,
                infoClass.getIdInternal());
        sp.getITurmaPersistente().simpleLockWrite(schoolClass);
        final List shifts = schoolClass.getAssociatedShifts();
        final List shiftsToRemove = new ArrayList(shifts);
        shifts.removeAll(shiftsToRemove);

        sp.getITurmaPersistente().delete(schoolClass);

        return new Boolean(true);
    }

}