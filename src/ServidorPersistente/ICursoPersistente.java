/*
 * ICursoPersistente.java
 * 
 * Created on 31 de Outubro de 2002, 15:48
 */

package ServidorPersistente;

/**
 * @author rpfi
 * @author jpvl
 */

import java.util.List;

import Dominio.ICurso;
import Util.TipoCurso;

public interface ICursoPersistente extends IPersistentObject {
    ICurso readBySigla(String sigla) throws ExcepcaoPersistencia;

    ICurso readByIdInternal(Integer idInternal) throws ExcepcaoPersistencia;

    void delete(ICurso degree) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readAllByDegreeType(TipoCurso degreeType) throws ExcepcaoPersistencia;

    public ICurso readByNameAndDegreeType(String name, TipoCurso degreeType) throws ExcepcaoPersistencia;
}