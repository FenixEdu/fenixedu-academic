/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorPersistente.OJB.teacher;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ITeacher;
import Dominio.teacher.Career;
import Dominio.teacher.ProfessionalCareer;
import Dominio.teacher.TeachingCareer;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.teacher.IPersistentCareer;
import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CareerOJB extends ObjectFenixOJB implements IPersistentCareer
{

    /**
	 *  
	 */
    public CareerOJB()
    {
        super();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.IPersistentCareer#readAllByTeacher(Dominio.ITeacher)
	 */
    public List readAllByTeacherAndCareerType(ITeacher teacher, CareerType careerType)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        if (careerType != null)
        {
            if (careerType.equals(CareerType.PROFESSIONAL))
            {
                criteria.addEqualTo("ojbConcreteClass", ProfessionalCareer.class.getName());
            } else
            {
                criteria.addEqualTo("ojbConcreteClass", TeachingCareer.class.getName());
            }
        }
        List careers = queryList(Career.class, criteria);

        return careers;
    }
}
