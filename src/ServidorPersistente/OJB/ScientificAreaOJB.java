/*
 * Created on 18/Dez/2003
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IScientificArea;
import Dominio.ScientificArea;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentScientificArea;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ScientificAreaOJB extends ObjectFenixOJB implements IPersistentScientificArea
{

    public ScientificAreaOJB()
    {
    }

    public IScientificArea readByName(String name) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        return (IScientificArea) queryObject(ScientificArea.class, criteria);
    }

    public void lockWrite(IScientificArea scientificAreaToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IScientificArea scientificAreaFromDB = null;

        // If there is nothing to write, simply return.
        if (scientificAreaToWrite == null)
        {
            return;
        }

        // Read branch from database.
        scientificAreaFromDB = this.readByName(scientificAreaToWrite.getName());

        // If branch is not in database, then write it.
        if (scientificAreaFromDB == null)
        {
            super.lockWrite(scientificAreaToWrite);
            // else If the branch is mapped to the database, then write any existing changes.
        }
        else 
        	if (
            (scientificAreaToWrite instanceof ScientificArea)
                && ((ScientificArea) scientificAreaFromDB).getIdInternal().equals(
                    ((ScientificArea) scientificAreaToWrite).getIdInternal()))
        {
            super.lockWrite(scientificAreaToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public void deleteAll() throws ExcepcaoPersistencia
    {
        try
        {
            String oqlQuery = "select all from " + ScientificArea.class.getName();
            super.deleteAll(oqlQuery);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

}
