/*
 * CursoOJB.java
 * 
 * Created on 1 de Novembro de 2002, 12:37
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author rpfi
 * @author jpvl
 */

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;

import org.apache.ojb.broker.query.Criteria;

public class CursoOJB extends PersistentObjectOJB implements ICursoPersistente {

    /** Creates a new instance of CursoOJB */
    public CursoOJB() {
    }

    public IDegree readBySigla(String sigla) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("sigla", sigla);
        return (IDegree) queryObject(Degree.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Degree.class, criteria);
    }

    public List readAllByDegreeType(DegreeType degreeType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("tipoCurso", degreeType);
        return queryList(Degree.class, criteria, "nome", true);
    }
}