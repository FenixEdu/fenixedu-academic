package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class DegreeCurricularPlanOJB extends ObjectFenixOJB implements IPersistentDegreeCurricularPlan
{

    public DegreeCurricularPlanOJB()
    {
    }

    public void write(IDegreeCurricularPlan degreeCurricularPlanToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IDegreeCurricularPlan degreeCurricularPlanFromDB = null;

        // If there is nothing to write, simply return.
        if (degreeCurricularPlanToWrite == null)
            return;

        // Read degreeCurricularPlan from database.
        degreeCurricularPlanFromDB =
            this.readByNameAndDegree(
                degreeCurricularPlanToWrite.getName(),
                degreeCurricularPlanToWrite.getDegree());

        // If degreeCurricularPlan is not in database, then write it.
        if (degreeCurricularPlanFromDB == null)
            super.lockWrite(degreeCurricularPlanToWrite);
        // else If the degreeCurricularPlan is mapped to the database, then
		// write any existing changes.
        else if (
            (degreeCurricularPlanToWrite instanceof DegreeCurricularPlan)
                && ((DegreeCurricularPlan) degreeCurricularPlanFromDB).getIdInternal().equals(
                    ((DegreeCurricularPlan) degreeCurricularPlanToWrite).getIdInternal()))
        {
            super.lockWrite(degreeCurricularPlanToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(DegreeCurricularPlan.class, new Criteria());
    }

    public void deleteDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia
    {

        super.delete(degreeCurricularPlan);

    }

   

    public List readByDegree(ICurso degree) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.nome", degree.getNome());
        criteria.addEqualTo("degree.sigla", degree.getSigla());
        criteria.addEqualTo("degree.tipoCurso", degree.getTipoCurso());
        return queryList(DegreeCurricularPlan.class, criteria);
        
    }

    public List readByDegreeAndState(ICurso degree, DegreeCurricularPlanState state)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.nome", degree.getNome());
        criteria.addEqualTo("degree.sigla", degree.getSigla());
        criteria.addEqualTo("degree.tipoCurso", degree.getTipoCurso());
        criteria.addEqualTo("state", state);

        return queryList(DegreeCurricularPlan.class, criteria);
    }

    public IDegreeCurricularPlan readByNameAndDegree(String name, ICurso degree)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        criteria.addEqualTo("degreeKey", degree.getIdInternal());

        return (IDegreeCurricularPlan) queryObject(DegreeCurricularPlan.class, criteria);
    }

    public List readByDegreeTypeAndState(TipoCurso degreeType, DegreeCurricularPlanState state) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.tipoCurso", degreeType);
        criteria.addEqualTo("state", state);
        return queryList(DegreeCurricularPlan.class, criteria);
    }

}