package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RegularizacaoMarcacaoPonto;
import net.sourceforge.fenixedu.persistenceTierJDBC.IRegularizacaoMarcacaoPontoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistenteOracle;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class RegularizacaoMarcacaoPontoRelacionalOracle implements
        IRegularizacaoMarcacaoPontoPersistente {

    public boolean alterarRegularizacaoMarcacaoPonto(RegularizacaoMarcacaoPonto regularizacao) {
        boolean resultado = false;
        return resultado;
    } /* alterarRegularizacaoMarcacaoPonto */

    public boolean apagarRegularizacaoMarcacaoPonto(int chaveMarcacaoPonto) {
        boolean resultado = false;
        return resultado;
    } /* apagarRegularizacaoMarcacaoPonto */

    public boolean apagarRegularizacoesMarcacoesPonto() {
        boolean resultado = false;
        return resultado;
    } /* apagarRegularizacoesMarcacoesPonto */

    public boolean escreverRegularizacaoMarcacaoPonto(RegularizacaoMarcacaoPonto regularizacao) {
        boolean resultado = false;
        return resultado;
    } /* escreverRegularizacaoMarcacaoPonto */

    public boolean escreverRegularizacoesMarcacoesPonto(List listaRegularizacoes) {
        boolean resultado = false;
        return resultado;
    } /* escreverRegularizacoesMarcacoesPonto */

    public boolean existeRegularizacaoMarcacaoPonto(RegularizacaoMarcacaoPonto regularizacao) {
        boolean resultado = false;
        return resultado;
    } /* existeRegularizacaoMarcacaoPonto */

    public RegularizacaoMarcacaoPonto lerRegularizacaoMarcacaoPonto(int chaveMarcacaoPonto) {
        return null;
    } /* lerRegularizacaoMarcacaoPonto */

    public List lerTodasRegularizacoes() {
        //ORACLE: metodo utiliza a BD do teleponto para o carregamento das
        // regularizacoes de marcacoes
        // de ponto
        List regularizacoes = null;

        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
            try {
                PreparedStatement sql = UtilRelacionalOracle.prepararComando("SELECT * FROM ASS_MARREG");
                ResultSet resultado = sql.executeQuery();

                regularizacoes = new ArrayList();
                // funcionario que fez a regularizacao
                PreparedStatement sql2 = UtilRelacionalOracle
                        .prepararComando("SELECT ASS_MARWHO, ASS_MARWHEN FROM ASS_MARCAS WHERE ASS_MARSEQ = ?");
                ResultSet resultado2 = null;

                // chaveParamRegularizacao
                PreparedStatement sql3 = UtilRelacional
                        .prepararComando("SELECT codigoInterno FROM ass_PARAM_REGULARIZACAO WHERE sigla = ?");
                ResultSet resultado3 = null;

                Timestamp quando = null;
                while (resultado.next()) {
                    // obtem o funcionario que fez a regularizacao
                    sql2.setInt(1, resultado.getInt("ASS_MARREGMARCAS"));
                    resultado2 = sql2.executeQuery();
                    // obtem a chave da parametrizacao de regularizacao
                    sql3.setString(1, resultado.getString("ASS_MARREGREGUL"));
                    resultado3 = sql3.executeQuery();

                    if (resultado2.next() && resultado3.next()) {
                        if (resultado2.getString("ASS_MARWHEN") != null) {
                            quando = Timestamp.valueOf(resultado2.getString("ASS_MARWHEN"));
                        }
                        regularizacoes.add(new RegularizacaoMarcacaoPonto(0,
                        //resultado.getInt("ASS_MARREGMARCAS"),
                                resultado.getInt("ASS_MARREGMARCAS"),
                                resultado3.getInt("codigoInterno"), resultado2.getInt("ASS_MARWHO"),
                                quando));

                        quando = null;
                    }
                }
                sql2.close();
                sql.close();
            } catch (SQLException SQLe) {
                SuportePersistenteOracle.getInstance().cancelarTransaccao();
                System.out.println("RegularizacaoMarcacaoPontoRelacionalOracle.lerTodasRegularizacoes: "
                        + SQLe.toString());
                return null;
            }
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            System.out.println("RegularizacaoMarcacaoPontoRelacionalOracle.lerTodasRegularizacoes: "
                    + e.toString());
        }
        return regularizacoes;

    } /* lerTodasRegularizacoes */
}