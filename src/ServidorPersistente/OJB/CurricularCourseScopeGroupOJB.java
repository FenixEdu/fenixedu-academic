/*
 * Created on 9/Dez/2003
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularCourseGroup;
import Dominio.CurricularCourseScopeGroup;
import Dominio.ICurricularCourseGroup;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularCourseScopeGroup;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScopeGroup;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class CurricularCourseScopeGroupOJB
    extends ObjectFenixOJB
    implements IPersistentCurricularCourseScopeGroup
{
    public CurricularCourseScopeGroupOJB()
    {
    }

    public void delete(ICurricularCourseScopeGroup ccGroup) throws ExcepcaoPersistencia
    {
        super.delete(ccGroup);
    }

    public void deleteAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + CurricularCourseScopeGroup.class.getName();
            super.deleteAll(oqlQuery);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public void lockWrite(ICurricularCourseScopeGroup curricularCourseScopeGroupToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        ICurricularCourseScopeGroup curricularCourseScopeGroupFromDB = null;

        // If there is nothing to write, simply return.
        if (curricularCourseScopeGroupToWrite == null)
        {
            return;
        }

        // Read curricular course group from database.
        curricularCourseScopeGroupFromDB =
            this.readByCurricularCouseGroupAndCurricularCourseScope(
                curricularCourseScopeGroupToWrite.getKeyCurricularCourseGroup(),
                curricularCourseScopeGroupToWrite.getKeyCurricularCourseScope());

        // If curricular course group is not in database, then write it.
        if (curricularCourseScopeGroupFromDB == null)
        {
            super.lockWrite(curricularCourseScopeGroupToWrite);
            // else If the curricular course group is mapped to the database, then write any existing changes.
        }
        else if (
            (curricularCourseScopeGroupToWrite instanceof CurricularCourseGroup)
                && ((CurricularCourseGroup) curricularCourseScopeGroupFromDB).getIdInternal().equals(
                    ((CurricularCourseGroup) curricularCourseScopeGroupToWrite).getIdInternal()))
        {
            super.lockWrite(curricularCourseScopeGroupToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public ICurricularCourseScopeGroup readByCurricularCouseGroupIdAndCurricularCourseScopeId(
        ICurricularCourseGroup ccGroupId,
        ICurricularCourseScope ccScopeId)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyCurricularCourseGroup", ccGroup.getIdInternal());
        crit.addEqualTo("keyCurricularCourseScope", ccScope.getIdInternal());
        return (ICurricularCourseScopeGroup) queryObject(CurricularCourseScopeGroup.class, crit);
    }
}
