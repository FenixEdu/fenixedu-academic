package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import Dominio.Regime;
import ServidorPersistenteJDBC.IRegimePersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class RegimeRelacional implements IRegimePersistente {

    public boolean alterarRegime(Regime regime) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("UPDATE ass_REGIME SET "
                    + "codigoInterno = ? , " + "designacao = ? " + "WHERE designacao = ? ");

            sql.setInt(1, regime.getCodigoInterno());
            sql.setString(2, regime.getDesignacao());
            sql.setString(3, regime.getDesignacao());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("RegimeRelacional.alterarRegime: " + e.toString());
        }
        return resultado;
    } /* alterarRegime */

    public boolean apagarRegime(String designacao) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("DELETE FROM ass_REGIME WHERE designacao = ?");

            sql.setString(1, designacao);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("RegimeRelacional.apagarRegime: " + e.toString());
        }
        return resultado;
    } /* apagarRegime */

    public boolean escreverRegime(Regime regime) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("INSERT INTO ass_REGIME VALUES (?, ?)");

            sql.setInt(1, regime.getCodigoInterno());
            sql.setString(2, regime.getDesignacao());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("RegimeRelacional.escreverRegime: " + e.toString());
        }
        return resultado;
    } /* escreverRegime */

    public List lerDesignacaoRegimes(List listaIdRegimes) {
        List regimes = null;
        PreparedStatement sql;
        ResultSet resultado;
        int elemento;
        ListIterator iterLista = listaIdRegimes.listIterator();
        regimes = new ArrayList();

        try {
            while (iterLista.hasNext()) {
                elemento = ((Integer) (iterLista.next())).intValue();
                sql = UtilRelacional
                        .prepararComando("SELECT designacao FROM ass_REGIME WHERE codigoInterno = ?");

                sql.setInt(1, elemento);

                resultado = sql.executeQuery();
                if (resultado.next()) {
                    regimes.add(new String(resultado.getString(1)));
                }
                sql.close();
            }
        } catch (Exception e) {
            System.out.println("RegimeRelacional.lerDesignacaoRegimes" + e.toString());
        }
        return regimes;
    } /* lerDesignacaoRegimes */

    public Regime lerRegime(String designacao) {
        Regime regime = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_REGIME WHERE designacao = ?");

            sql.setString(1, designacao);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                regime = new Regime(resultado.getInt("codigoInterno"), resultado.getString("designacao"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("RegimeRelacional.lerRegime: " + e.toString());
        }
        return regime;
    } /* lerRegime */

    public Regime lerRegime(int codigoInterno) {
        Regime regime = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_REGIME WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                regime = new Regime(resultado.getInt("codigoInterno"), resultado.getString("designacao"));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("RegimeRelacional.lerRegime" + e.toString());
        }
        return regime;
    } /* lerRegime */

    public List lerRegimes() {
        List regimes = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_REGIME");

            ResultSet resultado = sql.executeQuery();
            regimes = new ArrayList();
            while (resultado.next()) {
                regimes.add(new String(resultado.getString(2)));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("RegimeRelacional.lerRegimes" + e.toString());
            return null;
        }
        return regimes;
    } /* lerRegimes */
}