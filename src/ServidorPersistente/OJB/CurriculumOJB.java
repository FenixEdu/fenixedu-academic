/*
 * CurriculumOJB.java
 * 
 * Created on 6 de Janeiro de 2003, 20:44
 */
package ServidorPersistente.OJB;
/**
 * @author João Mota
 */
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Curriculum;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.exceptions.ExistingPersistentException;
/**
 * @author EP 15
 */
public class CurriculumOJB extends ObjectFenixOJB implements IPersistentCurriculum
{
    public ICurriculum readCurriculumByCurricularCourse(ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyCurricularCourse", curricularCourse.getIdInternal());
        criteria.addOrderBy("lastModificationDate", false);
        return (ICurriculum) queryObject(Curriculum.class, criteria);
    }
    public List readCurriculumHistoryByCurricularCourse(ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyCurricularCourse", curricularCourse.getIdInternal());
        return queryList(Curriculum.class, criteria);
    }
    public ICurriculum readCurriculumByCurricularCourseAndExecutionYear(
        ICurricularCourse curricularCourse,
        IExecutionYear executionYear)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyCurricularCourse", curricularCourse.getIdInternal());
        criteria.addLessOrEqualThan("lastModificationDate", executionYear.getEndDate());
        criteria.addOrderBy("lastModificationDate", false);
        return (ICurriculum) queryObject(Curriculum.class, criteria);
    }
    public void lockWrite(ICurriculum curriculum)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {
        ICurriculum curriculumFromDB = null;
        // If there is nothing to write, simply return.
        if (curriculum == null)
            return;
        // Read curriculum from database.
        curriculumFromDB = this.readCurriculumByCurricularCourse(curriculum.getCurricularCourse());
        // If curriculum is not in database, then write it.
        if (curriculumFromDB == null)
            super.lockWrite(curriculum);
        // else If the curriculum is mapped to the database, then write any
		// existing changes.
        else if (
            (curriculum instanceof Curriculum)
                && ((Curriculum) curriculumFromDB).getIdInternal().equals(
                    ((Curriculum) curriculum).getIdInternal()))
        {
            super.lockWrite(curriculum);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }
    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(Curriculum.class, new Criteria());
    }
    public void delete(ICurriculum curriculum) throws ExcepcaoPersistencia
    {
        super.delete(curriculum);
    }
    
}
