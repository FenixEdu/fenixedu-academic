package ServidorPersistenteJDBC;

import ServidorPersistenteJDBC.Relacional.CartaoRelacional;
import ServidorPersistenteJDBC.Relacional.CentroCustoRelacional;
import ServidorPersistenteJDBC.Relacional.FeriadoRelacional;
import ServidorPersistenteJDBC.Relacional.FeriasRelacional;
import ServidorPersistenteJDBC.Relacional.FuncNaoDocenteRelacional;
import ServidorPersistenteJDBC.Relacional.FuncionarioRelacional;
import ServidorPersistenteJDBC.Relacional.HorarioRelacional;
import ServidorPersistenteJDBC.Relacional.HorarioTipoRelacional;
import ServidorPersistenteJDBC.Relacional.JustificacaoRelacional;
import ServidorPersistenteJDBC.Relacional.MarcacaoPontoRelacional;
import ServidorPersistenteJDBC.Relacional.ModalidadeRelacional;
import ServidorPersistenteJDBC.Relacional.ParamFeriasRelacional;
import ServidorPersistenteJDBC.Relacional.ParamJustificacaoRelacional;
import ServidorPersistenteJDBC.Relacional.ParamRegularizacaoRelacional;
import ServidorPersistenteJDBC.Relacional.PeriodoFeriasRelacional;
import ServidorPersistenteJDBC.Relacional.PessoaRelacional;
import ServidorPersistenteJDBC.Relacional.RegimeRelacional;
import ServidorPersistenteJDBC.Relacional.RegularizacaoMarcacaoPontoRelacional;
import ServidorPersistenteJDBC.Relacional.StatusAssiduidadeRelacional;
import ServidorPersistenteJDBC.Relacional.UnidadeMarcacaoRelacional;
import ServidorPersistenteJDBC.Relacional.UtilRelacional;
import constants.assiduousness.Constants;

public class SuportePersistente {
    private static SuportePersistente _instance = null;

    private SuportePersistente(String filename) {
        //		UtilRelacional.inicializarBaseDados(filename);
    }

    /**
     * 
     * @deprecated
     */
    public void apagarDados() {
        //		try {
        //			iniciarTransaccao();
        //			try {
        //				UtilRelacional.limparTabelas();
        //			} catch (Exception e) {
        //				cancelarTransaccao();
        //			}
        //			confirmarTransaccao();
        //		} catch (Exception e) {
        //			System.out.println("SuportePersistente.apagarDados: " +
        // e.toString());
        //		}
    }

    public int ultimoIdGerado() {
        int ultimoIdGerado = 0;
        try {
            ultimoIdGerado = UtilRelacional.ultimoIdGerado();
        } catch (Exception e) {
            System.out.println("SuportePersistente.ultimoIdGerado: " + e.toString());
        }
        return ultimoIdGerado;
    }

    public void iniciarTransaccao() throws Exception {
        UtilRelacional.iniciarTransaccao();
    }

    public void confirmarTransaccao() throws Exception {
        UtilRelacional.confirmarTransaccao();
    }

    public void cancelarTransaccao() throws Exception {
        UtilRelacional.cancelarTransaccao();
    }

    public static synchronized SuportePersistente getInstance() {
        if (_instance == null) {
            _instance = new SuportePersistente(Constants.CONFIG_SERVIDORPERSISTENTE);
        }
        return _instance;
    }

    public IPessoaPersistente iPessoaPersistente() {
        return new PessoaRelacional();
    }

    public IFuncionarioPersistente iFuncionarioPersistente() {
        return new FuncionarioRelacional();
    }

    public IFuncNaoDocentePersistente iFuncNaoDocentePersistente() {
        return new FuncNaoDocenteRelacional();
    }

    public IRegimePersistente iRegimePersistente() {
        return new RegimeRelacional();
    }

    public IModalidadePersistente iModalidadePersistente() {
        return new ModalidadeRelacional();
    }

    public IHorarioPersistente iHorarioPersistente() {
        return new HorarioRelacional();
    }

    public IHorarioTipoPersistente iHorarioTipoPersistente() {
        return new HorarioTipoRelacional();
    }

    public IMarcacaoPontoPersistente iMarcacaoPontoPersistente() {
        return new MarcacaoPontoRelacional();
    }

    public IUnidadeMarcacaoPersistente iUnidadeMarcacaoPersistente() {
        return new UnidadeMarcacaoRelacional();
    }

    public ICartaoPersistente iCartaoPersistente() {
        return new CartaoRelacional();
    }

    public IFeriasPersistente iFeriasPersistente() {
        return new FeriasRelacional();
    }

    public IPeriodoFeriasPersistente iPeriodoFeriasPersistente() {
        return new PeriodoFeriasRelacional();
    }

    public IParamFeriasPersistente iParanFeriasPersistente() {
        return new ParamFeriasRelacional();
    }

    public IParamJustificacaoPersistente iParamJustificacaoPersistente() {
        return new ParamJustificacaoRelacional();
    }

    public IJustificacaoPersistente iJustificacaoPersistente() {
        return new JustificacaoRelacional();
    }

    public IParamRegularizacaoPersistente iParamRegularizacaoPersistente() {
        return new ParamRegularizacaoRelacional();
    }

    public IRegularizacaoMarcacaoPontoPersistente iRegularizacaoMarcacaoPontoPersistente() {
        return new RegularizacaoMarcacaoPontoRelacional();
    }

    public IFeriadoPersistente iFeriadoPersistente() {
        return new FeriadoRelacional();
    }

    public ICentroCustoPersistente iCentroCustoPersistente() {
        return new CentroCustoRelacional();
    }

    public IStatusAssiduidadePersistente iStatusAssiduidadePersistente() {
        return new StatusAssiduidadeRelacional();
    }
}