/*
 * Created on 29/Jul/2003, 10:29:18
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICandidacy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 29/Jul/2003, 10:29:18
 *  
 */
public class CandidacyOJB extends PersistentObjectOJB implements IPersistentSeminaryCandidacy {

    public List readByStudentID(Integer id) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student_id_internal", id);
        return super.queryList(Candidacy.class, criteria);
    }

    public List readByStudentIDAndSeminaryID(Integer studentID, Integer seminaryID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student_id_internal", studentID);
        criteria.addEqualTo("seminary_id_internal", seminaryID);
        return super.queryList(Candidacy.class, criteria);
    }

    public List readByUserInput(Integer modalityID, Integer seminaryID, Integer themeID,
            Integer case1Id, Integer case2Id, Integer case3Id, Integer case4Id, Integer case5Id,
            Integer curricularCourseID, Integer degreeID, Boolean approved) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        if (modalityID.intValue() != -1)
            criteria.addEqualTo("seminary_modality_id_internal", modalityID);
        if (seminaryID.intValue() != -1)
            criteria.addEqualTo("seminary_id_internal", seminaryID);
        if (curricularCourseID.intValue() != -1)
            criteria.addEqualTo("curricular_course_id_internal", curricularCourseID);
        if (themeID.intValue() != -1) {
            Criteria orCriteria = new Criteria();
            Criteria themeCriteria = new Criteria();
            Criteria completeCriteria = new Criteria();
            themeCriteria.addEqualTo("seminary_theme_id_internal", themeID);
            completeCriteria.addEqualTo("seminary_modality_id_internal", new Integer(1));
            orCriteria.addOrCriteria(themeCriteria);
            orCriteria.addOrCriteria(completeCriteria);
            criteria.addAndCriteria(orCriteria);

        }
        if (approved != null)
            criteria.addEqualTo("approved", approved);
        if (degreeID.intValue() != -1) {
            Criteria criteriaDegree = new Criteria();
            criteriaDegree.addEqualTo("key_degree_curricular_plan", degreeID);
            List curricularCoursesInDegree = super.queryList(CurricularCourse.class, criteriaDegree);
            List curricularCoursesIds = new LinkedList();
            for (Iterator iter = curricularCoursesInDegree.iterator(); iter.hasNext();) {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                curricularCoursesIds.add(curricularCourse.getIdInternal());
            }
            criteria.addIn("curricular_course_id_internal", curricularCoursesIds);
        }
        QueryByCriteria queryByCriteria = new QueryByCriteria(Candidacy.class, criteria);
        queryByCriteria.addOrderBy("seminary_modality_id_internal", true);
        queryByCriteria.addOrderBy("student_id_internal", true);

        List candidacies = super.queryList(queryByCriteria);
        List filteredCandidacies = new LinkedList();
        for (Iterator iterator = candidacies.iterator(); iterator.hasNext();) {
            boolean case1 = true, case2 = true, case3 = true, case4 = true, case5 = true;
            ICandidacy candidacy = (ICandidacy) iterator.next();
            if (case1Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 0)
                case1 = candidacy.getCaseStudyChoices().get(0).getCaseStudy()
                        .getIdInternal().intValue() == case1Id.intValue();
            if (case2Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 1)
                case2 = candidacy.getCaseStudyChoices().get(1).getCaseStudy()
                        .getIdInternal().intValue() == case2Id.intValue();
            if (case3Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 2)
                case3 = candidacy.getCaseStudyChoices().get(2).getCaseStudy()
                        .getIdInternal().intValue() == case3Id.intValue();
            if (case4Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 3)
                case4 = candidacy.getCaseStudyChoices().get(3).getCaseStudy()
                        .getIdInternal().intValue() == case4Id.intValue();
            if (case5Id.intValue() != -1 && candidacy.getCaseStudyChoices().size() > 4)
                case5 = candidacy.getCaseStudyChoices().get(4).getCaseStudy()
                        .getIdInternal().intValue() == case5Id.intValue();
            if (case1 && case2 && case3 && case4 && case5)
                filteredCandidacies.add(candidacy);
        }
        return filteredCandidacies;
    }
}