/*
 * IPersistentExam.java Created on 2003/03/19
 */

package ServidorPersistente;

import java.util.List;

import Dominio.IEvaluation;
import Dominio.IFrequenta;
import Dominio.IMark;

/**
 * @author Angela
 */
public interface IPersistentMark extends IPersistentObject
{

    public List readAll() throws ExcepcaoPersistencia;

    public IMark readBy(IEvaluation evaluation, IFrequenta attend) throws ExcepcaoPersistencia;

    public List readBy(IFrequenta attend) throws ExcepcaoPersistencia;

    public List readBy(IEvaluation evaluation) throws ExcepcaoPersistencia;

    public List readBy(IEvaluation evaluation, boolean published) throws ExcepcaoPersistencia;

    public void delete(IMark mark) throws ExcepcaoPersistencia;

    public void deleteByEvaluation(IEvaluation evaluation) throws ExcepcaoPersistencia;
}