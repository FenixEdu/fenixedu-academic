package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.util.List;

import net.sourceforge.fenixedu.domain.Modalidade;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IModalidadePersistente {
    public boolean alterarModalidade(Modalidade modalidade);

    public boolean apagarModalidade(String designacao);

    public boolean escreverModalidade(Modalidade modalidade);

    public Modalidade lerModalidade(String designacao);

    public Modalidade lerModalidade(int codigoInterno);

    public List lerModalidades();
}