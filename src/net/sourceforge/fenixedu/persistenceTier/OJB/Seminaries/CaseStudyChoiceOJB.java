/*
 * Created on 29/Jul/2003, 15:34:22
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudyChoice;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudyChoice;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 29/Jul/2003, 15:34:22
 *  
 */
public class CaseStudyChoiceOJB extends PersistentObjectOJB implements
        IPersistentSeminaryCaseStudyChoice {
    public List readByCandidacyIdInternal(Integer id) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("candidacy_id_internal", id);
        return super.queryList(CaseStudyChoice.class, criteria);
    }

    public List readByCaseStudyIdInternal(Integer id) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("casestudy_id_internal", id);
        return super.queryList(CaseStudyChoice.class, criteria);
    }

    public ICaseStudyChoice readByCaseStudyIdInternalAndCandidacyIdInternal(Integer idCaseStudy,
            Integer idCandidacy) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("casestudy_id_internal", idCaseStudy);
        criteria.addEqualTo("candidacy_id_internal", idCandidacy);
        return (ICaseStudyChoice) super.queryObject(CaseStudyChoice.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return super.queryList(CaseStudyChoice.class, criteria);
    }

    public void delete(ICaseStudyChoice choice) throws ExcepcaoPersistencia {
        super.delete(choice);
    }
}