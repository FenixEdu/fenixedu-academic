package Dominio;

import constants.assiduousness.Constants;

/**
 * 
 * @author Fernanda Quitério e Tania Pousão
 */
public class SuporteStrategyHorarios {
    private static SuporteStrategyHorarios _instance = null;

    private SuporteStrategyHorarios() {
    }

    public static SuporteStrategyHorarios getInstance() {
        if (_instance == null) {
            _instance = new SuporteStrategyHorarios();
        }
        return _instance;
    }

    public IStrategyHorarios callStrategy(String modalidade) {
        if (modalidade.equals(Constants.DESFASADO)) {
            return (new Desfasado());
        }
        if (modalidade.equals(Constants.ESPECIFICO)) {
            return (new Especifico());
        }
        if (modalidade.equals(Constants.FLEXIVEL)) {
            return (new Flexivel());
        }
        if (modalidade.equals(Constants.ISENCAOHORARIO)) {
            return (new IsencaoHorario());
        }
        if (modalidade.equals(Constants.JORNADACONTINUA)) {
            return (new JornadaContinua());
        }
        if (modalidade.equals(Constants.MEIOTEMPO)) {
            return (new MeioTempo());
        }
        if (modalidade.equals(Constants.TURNOS)) {
            return (new Turnos());
        }
        return null;
    } /* callStrategy */
}