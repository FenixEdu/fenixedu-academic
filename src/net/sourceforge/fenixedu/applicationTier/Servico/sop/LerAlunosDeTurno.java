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
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerAlunosDeTurno implements IService {

    public Object run(ShiftKey keyTurno) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ITurnoPersistente persistentShift = sp.getITurnoPersistente();
        IShift shift = persistentShift.readByNameAndExecutionCourse(keyTurno.getShiftName(), keyTurno
                .getInfoExecutionCourse().getIdInternal());

        final List<IStudent> alunos = sp.getITurnoAlunoPersistente().readByShift(shift.getIdInternal());

        List<InfoStudent> infoAlunos = new ArrayList<InfoStudent>(alunos.size());
        for (IStudent elem : alunos) {
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
