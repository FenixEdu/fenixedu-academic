package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.util.List;

import net.sourceforge.fenixedu.domain.Regime;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IRegimePersistente {
    public boolean alterarRegime(Regime regime);

    public boolean apagarRegime(String designacao);

    public boolean escreverRegime(Regime regime);

    public List lerDesignacaoRegimes(List listaIdRegimes);

    public Regime lerRegime(String designacao);

    public Regime lerRegime(int codigoInterno);

    public List lerRegimes();
}