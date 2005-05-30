/*
 * Created on 28/Jul/2003, 15:30:12
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudy;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 15:30:12
 *  
 */
public class CaseStudyOJB extends PersistentObjectOJB implements IPersistentSeminaryCaseStudy {

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return super.queryList(CaseStudy.class, criteria);
    }

    public List readByThemeID(Integer id) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("seminary_theme_Id_Internal", id);
        return super.queryList(CaseStudy.class, criteria);
    }

}