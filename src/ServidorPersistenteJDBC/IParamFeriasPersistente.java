package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.ParamFerias;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public interface IParamFeriasPersistente {
    public boolean alterarParamFerias(ParamFerias tipoFerias);

    public boolean apagarParamFerias(int codigoInterno);

    public boolean apagarParamFeriasPorSigla(String sigla);

    public boolean escreverParamFerias(ParamFerias tipoFerias);

    public ParamFerias lerParamFerias(int codigoInterno);

    public ParamFerias lerParamFeriasPorSigla(String sigla);

    public List lerTodosParamFerias();
}