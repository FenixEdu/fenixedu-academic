/*
 * Created on 12/Mai/2003
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import Dominio.StudentGroup;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;

/**
 * @author asnr and scpo
 *  
 */
public class StudentGroupOJB extends ObjectFenixOJB implements IPersistentStudentGroup
{

    public IStudentGroup readStudentGroupByGroupPropertiesAndGroupNumber(
        IGroupProperties groupProperties,
        Integer studentGroupNumber)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyGroupProperties", groupProperties.getIdInternal());
        criteria.addEqualTo("groupNumber", studentGroupNumber);

        return (IStudentGroup) queryObject(StudentGroup.class, criteria);
    }

    public List readAllStudentGroupByGroupProperties(IGroupProperties groupProperties)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyGroupProperties", groupProperties.getIdInternal());

        return queryList(StudentGroup.class, criteria);
    }

    public List readAllStudentGroupByGroupPropertiesAndShift(
        IGroupProperties groupProperties,
        ITurno shift)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("keyGroupProperties", groupProperties.getIdInternal());
        criteria.addEqualTo("keyShift", shift.getIdInternal());

        return queryList(StudentGroup.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia
    {

        return queryList(StudentGroup.class, new Criteria());
    }

    

    public void delete(IStudentGroup studentGroup) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(studentGroup);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    

}
