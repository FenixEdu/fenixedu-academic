/*
 * IPersistentExam.java Created on 2003/03/19
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Mark;

/**
 * @author Angela
 */
public interface IPersistentMark extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public Mark readBy(Evaluation evaluation, Attends attend) throws ExcepcaoPersistencia;

    public List readBy(Attends attend) throws ExcepcaoPersistencia;

    public List readBy(Evaluation evaluation) throws ExcepcaoPersistencia;

    public List readBy(Evaluation evaluation, boolean published) throws ExcepcaoPersistencia;

    public void delete(Mark mark) throws ExcepcaoPersistencia;

    public void deleteByEvaluation(Evaluation evaluation) throws ExcepcaoPersistencia;
}