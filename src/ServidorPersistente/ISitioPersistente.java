/*
 * ISitioPersistente.java
 *
 * Created on 25 de Agosto de 2002, 0:53
 */

package ServidorPersistente;

/**
 *
 * @author  ars
 */

import java.util.List;

import Dominio.ISitio;

public interface ISitioPersistente extends IPersistentObject {
    ISitio readByNome(String nome) throws ExcepcaoPersistencia;
    List readAll() throws ExcepcaoPersistencia;
    void lockWrite(ISitio sitio) throws ExcepcaoPersistencia;
    void delete(ISitio sitio) throws ExcepcaoPersistencia;
    void deleteAll() throws ExcepcaoPersistencia;
}
