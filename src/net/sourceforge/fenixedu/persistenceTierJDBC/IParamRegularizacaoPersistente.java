package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.util.List;

import net.sourceforge.fenixedu.domain.ParamRegularizacao;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IParamRegularizacaoPersistente {
    public boolean alterarParamRegularizacao(ParamRegularizacao paramRegularizacao);

    public boolean apagarParamRegularizacao(String sigla);

    public boolean escreverParamRegularizacao(ParamRegularizacao paramRegularizacao);

    public ParamRegularizacao lerParamRegularizacao(int chaveParamRegularizacao);

    public ParamRegularizacao lerParamRegularizacao(String sigla);

    public List lerTodasParamRegularizacoes();
}