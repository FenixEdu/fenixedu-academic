package ServidorPersistenteJDBC;

import ServidorPersistenteJDBC.Relacional.CartaoRelacionalOracle;
import ServidorPersistenteJDBC.Relacional.FuncionarioRelacionalOracle;
import ServidorPersistenteJDBC.Relacional.HorarioRelacionalOracle;
import ServidorPersistenteJDBC.Relacional.JustificacaoRelacionalOracle;
import ServidorPersistenteJDBC.Relacional.MarcacaoPontoRelacionalOracle;
import ServidorPersistenteJDBC.Relacional.RegularizacaoMarcacaoPontoRelacionalOracle;
import ServidorPersistenteJDBC.Relacional.UtilRelacionalOracle;
import constants.assiduousness.Constants;

/**
 * 
 * @author Nanda & Tânia
 */
public class SuportePersistenteOracle {
    private static SuportePersistenteOracle _instance = null;

    private SuportePersistenteOracle(String filename) {
        UtilRelacionalOracle.inicializarBaseDados(filename);
    }

    /**
     * 
     * @deprecated
     */
    public void apagarDados() {
        //    try {
        //      iniciarTransaccao();
        //      try {
        //        UtilRelacionalOracle.limparTabelas();
        //      } catch (Exception e) {
        //        cancelarTransaccao();
        //      }
        //      confirmarTransaccao();
        //    } catch (Exception e) {
        //      System.out.println("SuportePersistenteOracle.apagarDados: " +
        // e.toString());
        //    }
    }

    public int ultimoIdGerado() {
        int ultimoIdGerado = 0;
        try {
            ultimoIdGerado = UtilRelacionalOracle.ultimoIdGerado();
        } catch (Exception e) {
            System.out.println("SuportePersistenteOracle.ultimoIdGerado: " + e.toString());
        }
        return ultimoIdGerado;
    }

    public void iniciarTransaccao() throws Exception {
        UtilRelacionalOracle.iniciarTransaccao();
    }

    public void confirmarTransaccao() throws Exception {
        UtilRelacionalOracle.confirmarTransaccao();
    }

    public void cancelarTransaccao() throws Exception {
        UtilRelacionalOracle.cancelarTransaccao();
    }

    public static synchronized SuportePersistenteOracle getInstance() {
        if (_instance == null) {
            _instance = new SuportePersistenteOracle(Constants.CONFIG_SERVIDORPERSISTENTE_ORACLE);
        }
        return _instance;
    }

    public IFuncionarioPersistente iFuncionarioPersistente() {
        return new FuncionarioRelacionalOracle();
    }

    public IMarcacaoPontoPersistente iMarcacaoPontoPersistente() {
        return new MarcacaoPontoRelacionalOracle();
    }

    public IRegularizacaoMarcacaoPontoPersistente iRegularizacaoMarcacaoPontoPersistente() {
        return new RegularizacaoMarcacaoPontoRelacionalOracle();
    }

    public IHorarioPersistente iHorarioPersistente() {
        return new HorarioRelacionalOracle();
    }

    public ICartaoPersistente iCartaoPersistente() {
        return new CartaoRelacionalOracle();
    }

    public IJustificacaoPersistente iJustificacaoPersistente() {
        return new JustificacaoRelacionalOracle();
    }
}