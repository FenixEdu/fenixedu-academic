/*
 * ICursoPersistente.java
 * 
 * Created on 31 de Outubro de 2002, 15:48
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author rpfi
 * @author jpvl
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.util.TipoCurso;

public interface ICursoPersistente extends IPersistentObject {
    IDegree readBySigla(String sigla) throws ExcepcaoPersistencia;

    IDegree readByIdInternal(Integer idInternal) throws ExcepcaoPersistencia;

    void delete(IDegree degree) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readAllByDegreeType(TipoCurso degreeType) throws ExcepcaoPersistencia;

    public IDegree readByNameAndDegreeType(String name, TipoCurso degreeType) throws ExcepcaoPersistencia;
}