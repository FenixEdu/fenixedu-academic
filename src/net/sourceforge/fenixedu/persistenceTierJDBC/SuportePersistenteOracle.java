package net.sourceforge.fenixedu.persistenceTierJDBC;

import net.sourceforge.fenixedu.constants.assiduousness.Constants;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.CartaoRelacionalOracle;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.FuncionarioRelacionalOracle;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.HorarioRelacionalOracle;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.JustificacaoRelacionalOracle;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.MarcacaoPontoRelacionalOracle;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.RegularizacaoMarcacaoPontoRelacionalOracle;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.UtilRelacionalOracle;

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