package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * @author Fernanda Quitério 25/06/2003
 */
public interface IPersistentEvaluation extends IPersistentObject {
    public List readAll() throws ExcepcaoPersistencia;

}