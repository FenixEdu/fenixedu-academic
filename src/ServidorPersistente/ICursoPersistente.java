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

public interface ICursoPersistente extends IPersistentObject {
    ICurso readBySigla(String sigla) throws ExcepcaoPersistencia;
    void lockWrite(ICurso lic) throws ExcepcaoPersistencia;
    void delete(ICurso lic) throws ExcepcaoPersistencia;
    void deleteAll() throws ExcepcaoPersistencia;
    public List readAll() throws ExcepcaoPersistencia;
}
