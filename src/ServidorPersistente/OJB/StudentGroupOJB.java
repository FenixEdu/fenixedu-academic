/*
 * Created on 12/Mai/2003
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IAttendsSet;
import Dominio.IStudentGroup;
import Dominio.IShift;
import Dominio.StudentGroup;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;

/**
 * @author asnr and scpo
 *  
 */
public class StudentGroupOJB extends ObjectFenixOJB implements IPersistentStudentGroup
{

    public IStudentGroup readStudentGroupByAttendsSetAndGroupNumber(
        IAttendsSet attendsSet,
        Integer studentGroupNumber)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("attendsSet.idInternal", attendsSet.getIdInternal());
        criteria.addEqualTo("groupNumber", studentGroupNumber);

        return (IStudentGroup) queryObject(StudentGroup.class, criteria);
    }

    public List readAllStudentGroupByAttendsSet(IAttendsSet attendsSet)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("attendsSet.idInternal", attendsSet.getIdInternal());

        return queryList(StudentGroup.class, criteria);
    }

    public List readAllStudentGroupByAttendsSetAndShift(
        IAttendsSet attendsSet,
        IShift shift)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("attendsSet.idInternal", attendsSet.getIdInternal());
        criteria.addEqualTo("shift.idInternal", shift.getIdInternal());

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


    public List readAllStudentGroupByShift(IShift shift) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        criteria.addEqualTo("shift.idInternal", shift.getIdInternal());

        return queryList(StudentGroup.class, criteria);
    }

    

}
