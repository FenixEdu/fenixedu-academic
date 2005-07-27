package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.util.List;

import net.sourceforge.fenixedu.domain.StatusAssiduidade;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public interface IStatusAssiduidadePersistente {
    public StatusAssiduidade lerStatus(int codigoInterno);

    public StatusAssiduidade lerStatus(String designacao);

    public StatusAssiduidade lerStatus(String sigla, String designacao);

    public List lerTodosStatus();

}