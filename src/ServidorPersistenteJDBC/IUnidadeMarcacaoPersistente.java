package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.UnidadeMarcacao;

/**
 * 
 * @author Tania
 */
public interface IUnidadeMarcacaoPersistente {
    public boolean alterarUnidadeMarcacao(UnidadeMarcacao unidadeMarcacao);

    public boolean apagarUnidadeMarcacao(String sigla);

    public boolean escreverUnidadeMarcacao(UnidadeMarcacao unidadeMarcacao);

    public UnidadeMarcacao lerUnidadeMarcacao(int codigoInterno);

    public UnidadeMarcacao lerUnidadeMarcacao(String sigla);

    public UnidadeMarcacao lerUnidadeMarcacaoPorDescricao(String descricao);

    public UnidadeMarcacao lerUnidadeMarcacaoPorID(int id);

    public List lerUnidadesMarcacao();
}