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

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAlunosDeTurno implements IService {

    public Object run(ShiftKey keyTurno) throws ExcepcaoPersistencia {

        List alunos = null;
        List infoAlunos = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(keyTurno
                .getInfoExecutionCourse());

        ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        IShift shift = persistentShift.readByNameAndExecutionCourse(keyTurno.getShiftName(), executionCourse.getIdInternal());

        alunos = sp.getITurnoAlunoPersistente().readByShift(shift.getIdInternal());

        
        infoAlunos = new ArrayList(alunos.size());
        for (final Iterator iterator = alunos.iterator(); iterator.hasNext(); ) {
            IStudent elem = (IStudent) iterator.next();
            InfoPerson infoPessoa = new InfoPerson();
            infoPessoa.setNome(elem.getPerson().getNome());
            infoPessoa.setUsername(elem.getPerson().getUsername());
            infoPessoa.setPassword(elem.getPerson().getPassword());
            infoPessoa.setEmail(elem.getPerson().getEmail());
            infoAlunos.add(new InfoStudent(elem.getNumber(), elem.getState(), infoPessoa, elem
                    .getDegreeType()));
        }

        return infoAlunos;
    }

}
