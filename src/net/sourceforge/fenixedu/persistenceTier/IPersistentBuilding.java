package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author Luis Cruz
 * 
 */
import java.util.List;

public interface IPersistentBuilding extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;
   
}