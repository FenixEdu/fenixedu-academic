/*
 * ICursoPersistente.java
 *
 * Created on 31 de Outubro de 2002, 15:48
 */

package ServidorPersistente;

/**
 *
 * @author  rpfi
 */

import java.util.List;

import Dominio.ICurso;
import ServidorPersistente.exceptions.ExistingPersistentException;

public interface ICursoPersistente extends IPersistentObject {
    ICurso readBySigla(String sigla) throws ExcepcaoPersistencia;
	ICurso readByIdInternal(Integer idInternal) throws ExcepcaoPersistencia;
    void lockWrite(ICurso degree) throws ExcepcaoPersistencia, ExistingPersistentException;
    String delete(ICurso degree) throws ExcepcaoPersistencia;
    void deleteAll() throws ExcepcaoPersistencia;
    public List readAll() throws ExcepcaoPersistencia;
}
