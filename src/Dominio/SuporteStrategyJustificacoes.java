package Dominio;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class SuporteStrategyJustificacoes {
    private static SuporteStrategyJustificacoes _instance = null;

    private SuporteStrategyJustificacoes() {
    }

    public static SuporteStrategyJustificacoes getInstance() {
        if (_instance == null) {
            _instance = new SuporteStrategyJustificacoes();
        }
        return _instance;
    }

    public IStrategyJustificacoes callStrategy(String tipoJustificacao) {
        if (tipoJustificacao.equals(new String("OC"))) {
            return (new Ocorrencia());
        }
        if (tipoJustificacao.equals(new String("JH"))) {
            return (new JustificacaoHoras());
        }
        if (tipoJustificacao.equals(new String("SALD"))) {
            return (new Saldo());
        }
        return null;
    } /* callStrategy */
}