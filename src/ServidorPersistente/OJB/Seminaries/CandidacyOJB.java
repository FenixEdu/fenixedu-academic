/*
 * Created on 29/Jul/2003, 10:29:18
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.ojb.broker.query.Criteria;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import Dominio.Seminaries.ICaseStudyChoice;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 29/Jul/2003, 10:29:18
 * 
 */
public class CandidacyOJB extends ObjectFenixOJB implements IPersistentSeminaryCandidacy
{
    public ICandidacy readByName(String name) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        return (ICandidacy) super.queryObject(Candidacy.class, criteria);
    }
    public List readByStudentID(Integer id) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student_id_internal", id);
        return super.queryList(Candidacy.class, criteria);
    }
	public List readByStudentIDAndSeminaryID(Integer studentID,Integer seminaryID) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("student_id_internal", studentID);
		criteria.addEqualTo("seminary_id_internal", seminaryID);		
		return super.queryList(Candidacy.class, criteria);
	}    
    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return super.queryList(Candidacy.class, criteria);
    }
    public void delete(ICandidacy candidacy) throws ExcepcaoPersistencia
    {
        super.deleteByOID(Candidacy.class, candidacy.getIdInternal());
    }
    public List readByUserInput(
        Integer modalityID,
        Integer seminaryID,
        Integer themeID,
        Integer case1Id,
        Integer case2Id,
        Integer case3Id,
        Integer case4Id,
        Integer case5Id,
        Integer curricularCourseID,
        Integer degreeID,
        Boolean approved)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        if (modalityID.intValue() != -1)
            criteria.addEqualTo("seminary_modality_id_internal", modalityID);
        if (seminaryID.intValue() != -1)
            criteria.addEqualTo("seminary_id_internal", seminaryID);
        if (curricularCourseID.intValue() != -1)
            criteria.addEqualTo("curricular_course_id_internal", curricularCourseID);
        if (themeID.intValue() != -1)
        {
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
        if (degreeID.intValue() != -1)
        {
            Criteria criteriaDegree = new Criteria();
            criteriaDegree.addEqualTo("key_degree_curricular_plan", degreeID);
            List curricularCoursesInDegree = super.queryList(CurricularCourse.class, criteriaDegree);
            List curricularCoursesIds = new LinkedList();
            for (Iterator iter = curricularCoursesInDegree.iterator(); iter.hasNext();)
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                curricularCoursesIds.add(curricularCourse.getIdInternal());
            }
            criteria.addIn("curricular_course_id_internal", curricularCoursesIds);
        }
        criteria.addOrderBy("seminary_modality_id_internal", true);
        criteria.addOrderBy("student_id_internal", true);
        List candidacies = super.queryList(Candidacy.class, criteria);
        List filteredCandidacies = new LinkedList();
        for (Iterator iterator = candidacies.iterator(); iterator.hasNext();)
        {
            boolean case1 = true, case2 = true, case3 = true, case4 = true, case5 = true;
            ICandidacy candidacy = (ICandidacy) iterator.next();
            if (case1Id.intValue() != -1)
                case1 =
                    ((ICaseStudyChoice) candidacy.getCaseStudyChoices().get(0))
                        .getCaseStudyIdInternal()
                        .intValue()
                        == case1Id.intValue();
            if (case2Id.intValue() != -1)
                case2 =
                    ((ICaseStudyChoice) candidacy.getCaseStudyChoices().get(1))
                        .getCaseStudyIdInternal()
                        .intValue()
                        == case2Id.intValue();
            if (case3Id.intValue() != -1)
                case3 =
                    ((ICaseStudyChoice) candidacy.getCaseStudyChoices().get(2))
                        .getCaseStudyIdInternal()
                        .intValue()
                        == case3Id.intValue();
            if (case4Id.intValue() != -1)
                case4 =
                    ((ICaseStudyChoice) candidacy.getCaseStudyChoices().get(3))
                        .getCaseStudyIdInternal()
                        .intValue()
                        == case4Id.intValue();
            if (case5Id.intValue() != -1)
                case5 =
                    ((ICaseStudyChoice) candidacy.getCaseStudyChoices().get(4))
                        .getCaseStudyIdInternal()
                        .intValue()
                        == case5Id.intValue();
            if (case1 && case2 && case3 && case4 && case5)
                filteredCandidacies.add(candidacy);
        }
        return filteredCandidacies;
    }
}
