/*
 * IPersistentExam.java Created on 2003/03/19
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IMark;

/**
 * @author Angela
 */
public interface IPersistentMark extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public IMark readBy(IEvaluation evaluation, IAttends attend) throws ExcepcaoPersistencia;

    public List readBy(IAttends attend) throws ExcepcaoPersistencia;

    public List readBy(IEvaluation evaluation) throws ExcepcaoPersistencia;

    public List readBy(IEvaluation evaluation, boolean published) throws ExcepcaoPersistencia;

    public void delete(IMark mark) throws ExcepcaoPersistencia;

    public void deleteByEvaluation(IEvaluation evaluation) throws ExcepcaoPersistencia;
}