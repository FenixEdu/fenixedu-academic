/*
 * CursoOJB.java
 * 
 * Created on 1 de Novembro de 2002, 12:37
 */

package ServidorPersistente.OJB;

/**
 * @author rpfi
 * @author jpvl
 */

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Curso;
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import Util.TipoCurso;

public class CursoOJB extends ObjectFenixOJB implements ICursoPersistente
{

    /** Creates a new instance of CursoOJB */
    public CursoOJB()
    {
    }

    public ICurso readBySigla(String sigla) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("sigla", sigla);
        return (ICurso) queryObject(Curso.class, criteria);
    }

    public ICurso readByIdInternal(Integer idInternal) throws ExcepcaoPersistencia
    {
        return (ICurso) readByOId(new Curso(idInternal), false);
    }

    public void delete(ICurso degree) throws ExcepcaoPersistencia
    {
        super.delete(degree);
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(Curso.class, criteria);
    }

    public List readAllByDegreeType(TipoCurso degreeType) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("tipoCurso", degreeType);
        criteria.addOrderBy("nome", true);
        return queryList(Curso.class, criteria);
    }

    public ICurso readByNameAndDegreeType(String name, TipoCurso degreeType) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("nome", name);
        criteria.addEqualTo("tipoCurso", degreeType);
        return (ICurso) queryObject(Curso.class, criteria);
    }

}
