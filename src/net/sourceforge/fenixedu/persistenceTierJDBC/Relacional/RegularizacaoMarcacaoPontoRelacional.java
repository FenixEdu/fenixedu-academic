package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.domain.RegularizacaoMarcacaoPonto;
import net.sourceforge.fenixedu.persistenceTierJDBC.IRegularizacaoMarcacaoPontoPersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class RegularizacaoMarcacaoPontoRelacional implements IRegularizacaoMarcacaoPontoPersistente {

    public boolean alterarRegularizacaoMarcacaoPonto(RegularizacaoMarcacaoPonto regularizacao) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("UPDATE ass_REGULARIZACAO_MARCACAO SET " + "codigoInterno = ? , "
                            + "chaveMarcacaoPonto = ? , " + "chaveParamRegularizacao = ? , "
                            + "quem = ? , " + "quando = ? " + "WHERE codigoInterno = ? ");

            sql.setInt(1, regularizacao.getCodigoInterno());
            sql.setInt(2, regularizacao.getChaveMarcacaoPonto());
            sql.setInt(3, regularizacao.getChaveParamRegularizacao());
            sql.setInt(4, regularizacao.getQuem());
            if (regularizacao.getQuando() != null) {
                sql.setTimestamp(5, new Timestamp((regularizacao.getQuando()).getTime()));
            } else {
                sql.setTimestamp(5, null);
            }
            sql.setInt(6, regularizacao.getCodigoInterno());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out
                    .println("RegularizacaoMarcacaoPontoRelacional.alterarRegularizacaoMarcacaoPonto: "
                            + e.toString());
        }
        return resultado;
    } /* alterarRegularizacaoMarcacaoPonto */

    public boolean apagarRegularizacaoMarcacaoPonto(int chaveMarcacaoPonto) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("DELETE FROM ass_REGULARIZACAO_MARCACAO "
                            + "WHERE chaveMarcacaoPonto=?");

            sql.setInt(1, chaveMarcacaoPonto);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("RegularizacaoMarcacaoPontoRelacional.apagarRegularizacaoMarcacaoPonto: "
                    + e.toString());
        }
        return resultado;
    } /* apagarRegularizacaoMarcacaoPonto */

    public boolean apagarRegularizacoesMarcacoesPonto() {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("DELETE FROM ass_REGULARIZACAO_MARCACAO");
            sql.executeUpdate();
            sql.close();
            /*
             * sql = UtilRelacional.prepararComando("ALTER TABLE
             * ass_REGULARIZACAO_MARCACAO auto_increment=1");
             * sql.executeUpdate();
             */
            resultado = true;
        } catch (Exception e) {
            System.out
                    .println("RegularizacaoMarcacaoPontoRelacional.apagarRegularizacoesMarcacoesPonto: "
                            + e.toString());
        }
        return resultado;
    } /* apagarRegularizacoesMarcacoesPonto */

    public boolean escreverRegularizacaoMarcacaoPonto(RegularizacaoMarcacaoPonto regularizacao) {
        boolean resultado = false;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("INSERT INTO ass_REGULARIZACAO_MARCACAO "
                            + "VALUES (?, ?, ?, ?, ?)");

            sql.setInt(1, regularizacao.getCodigoInterno());
            sql.setInt(2, regularizacao.getChaveMarcacaoPonto());
            sql.setInt(3, regularizacao.getChaveParamRegularizacao());
            sql.setInt(4, regularizacao.getQuem());
            if (regularizacao.getQuando() != null) {
                sql.setTimestamp(5, new Timestamp((regularizacao.getQuando()).getTime()));
            } else {
                sql.setTimestamp(5, null);
            }
            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out
                    .println("RegularizacaoMarcacaoPontoRelacional.escreverRegularizacaoMarcacaoPonto: "
                            + e.toString());
        }
        return resultado;
    } /* escreverRegularizacaoMarcacaoPonto */

    public boolean escreverRegularizacoesMarcacoesPonto(List listaRegularizacoes) {
        boolean resultado = false;

        ListIterator iterador = listaRegularizacoes.listIterator();
        RegularizacaoMarcacaoPonto regularizacao = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("INSERT INTO ass_REGULARIZACAO_MARCACAO VALUES (?, ?, ?, ?, ?)");

            while (iterador.hasNext()) {
                regularizacao = (RegularizacaoMarcacaoPonto) iterador.next();

                sql.setInt(1, regularizacao.getCodigoInterno());
                sql.setInt(2, regularizacao.getChaveMarcacaoPonto());
                sql.setInt(3, regularizacao.getChaveParamRegularizacao());
                sql.setInt(4, regularizacao.getQuem());
                if (regularizacao.getQuando() != null) {
                    sql.setTimestamp(5, new Timestamp((regularizacao.getQuando()).getTime()));
                } else {
                    sql.setTimestamp(5, null);
                }
                sql.executeUpdate();
            }
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out
                    .println("RegularizacaoMarcacaoPontoRelacional.escreverRegularizacoesMarcacoesPonto: "
                            + e.toString());
        }

        return resultado;

    } /* escreverRegularizacoesMarcacoesPonto */

    public boolean existeRegularizacaoMarcacaoPonto(RegularizacaoMarcacaoPonto regularizacao) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_REGULARIZACAO_MARCACAO "
                            + "WHERE chaveMarcacaoPonto = ? " + "AND chaveParamRegularizacao = ? "
                            + "AND quem = ? " + "AND quando = ?");

            sql.setInt(1, regularizacao.getChaveMarcacaoPonto());
            sql.setInt(2, regularizacao.getChaveParamRegularizacao());
            sql.setInt(3, regularizacao.getQuem());
            if (regularizacao.getQuando() != null) {
                sql.setTimestamp(4, new Timestamp((regularizacao.getQuando()).getTime()));
            } else {
                sql.setTimestamp(4, null);
            }
            ResultSet resultadoQuery = sql.executeQuery();
            if (resultadoQuery.next()) {
                resultado = true;
            }
            sql.close();
        } catch (Exception e) {
            System.out
                    .println("RegularizacaoMarcacaoPontoRelacional.existeRegularizacaoMarcacaoPonto : "
                            + e.toString());
            e.printStackTrace();
        }

        return resultado;

    } /* existeRegularizacaoMarcacaoPonto */

    public RegularizacaoMarcacaoPonto lerRegularizacaoMarcacaoPonto(int chaveMarcacaoPonto) {
        RegularizacaoMarcacaoPonto regularizacao = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_REGULARIZACAO_MARCACAO "
                            + "WHERE chaveMarcacaoPonto=?");

            sql.setInt(1, chaveMarcacaoPonto);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                regularizacao = new RegularizacaoMarcacaoPonto(resultado.getInt("codigoInterno"),
                        resultado.getInt("chaveMarcacaoPonto"), resultado
                                .getInt("chaveParamRegularizacao"), resultado.getInt("quem"), Timestamp
                                .valueOf(resultado.getString("quando")));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("RegularizacaoMarcacaoPontoRelacional.lerRegularizacaoMarcacaoPonto: "
                    + e.toString());
        }

        return regularizacao;

    } /* lerRegularizacaoMarcacaoPonto */

    public List lerTodasRegularizacoes() {
        List regularizacoes = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_REGULARIZACAO_MARCACAO");

            ResultSet resultado = sql.executeQuery();
            regularizacoes = new ArrayList();
            while (resultado.next()) {
                regularizacoes.add(new RegularizacaoMarcacaoPonto(resultado.getInt("codigoInterno"),
                        resultado.getInt("chaveMarcacaoPonto"), resultado
                                .getInt("chaveParamRegularizacao"), resultado.getInt("quem"), Timestamp
                                .valueOf(resultado.getString("quando"))));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("RegularizacaoMarcacaoPontoRelacional.lerTodasRegularizacoes: "
                    + e.toString());
        }
        return regularizacoes;

    } /* lerTodasRegularizacoes */
}