package net.sourceforge.fenixedu.persistenceTierJDBC;

import net.sourceforge.fenixedu.constants.assiduousness.Constants;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.CartaoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.CentroCustoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.FeriadoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.FeriasRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.FuncNaoDocenteRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.FuncionarioRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.HorarioRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.HorarioTipoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.JustificacaoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.MarcacaoPontoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.ModalidadeRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.ParamFeriasRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.ParamJustificacaoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.ParamRegularizacaoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.PeriodoFeriasRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.PessoaRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.RegimeRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.RegularizacaoMarcacaoPontoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.StatusAssiduidadeRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.UnidadeMarcacaoRelacional;
import net.sourceforge.fenixedu.persistenceTierJDBC.Relacional.UtilRelacional;

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