package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import Dominio.ParamRegularizacao;
import ServidorPersistenteJDBC.IParamRegularizacaoPersistente;
/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class ParamRegularizacaoRelacional implements IParamRegularizacaoPersistente
{

    public boolean alterarParamRegularizacao(ParamRegularizacao paramRegularizacao)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "UPDATE ass_PARAM_REGULARIZACAO SET "
                        + "codigoInterno = ? , "
                        + "sigla = ? , "
                        + "descricao = ? , "
                        + "quem = ? , "
                        + "quando = ? , "
                        + "WHERE codigoInterno = ? ");

            sql.setInt(1, paramRegularizacao.getCodigoInterno());
            sql.setString(2, paramRegularizacao.getSigla());
            sql.setString(3, paramRegularizacao.getDescricao());
            sql.setInt(4, paramRegularizacao.getQuem());
            if (paramRegularizacao.getQuando() != null)
            {
                sql.setTimestamp(5, new Timestamp((paramRegularizacao.getQuando()).getTime()));
            }
            else
            {
                sql.setTimestamp(5, null);
            }
            sql.setInt(6, paramRegularizacao.getCodigoInterno());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println(
                "ParamRegularizacaoRelacional.alterarParamRegularizacao: " + e.toString());
        }
        return resultado;
    } /* alterarParamRegularizacao */

    public boolean apagarParamRegularizacao(String sigla)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "DELETE FROM ass_PARAM_REGULARIZACAO " + "WHERE sigla = ?");

            sql.setString(1, sigla);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("ParamRegularizacaoRelacional.apagarParamRegularizacao: " + e.toString());
        }
        return resultado;
    } /* apagarParamRegularizacao */

    public boolean escreverParamRegularizacao(ParamRegularizacao paramRegularizacao)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "INSERT INTO ass_PARAM_REGULARIZACAO " + "VALUES (?, ?, ?, ?, ?)");

            sql.setInt(1, paramRegularizacao.getCodigoInterno());
            sql.setString(2, paramRegularizacao.getSigla());
            sql.setString(3, paramRegularizacao.getDescricao());
            sql.setInt(4, paramRegularizacao.getQuem());
            if (paramRegularizacao.getQuando() != null)
            {
                sql.setTimestamp(5, new Timestamp((paramRegularizacao.getQuando()).getTime()));
            }
            else
            {
                sql.setTimestamp(5, null);
            }

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println(
                "ParamRegularizacaoRelacional.escreverParamRegularizacao: " + e.toString());
        }
        return resultado;
    } /* escreverParamRegularizacao */

    public ParamRegularizacao lerParamRegularizacao(int chaveParamRegularizacao)
    {
        ParamRegularizacao paramRegularizacao = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_PARAM_REGULARIZACAO " + "WHERE codigoInterno = ?");

            sql.setInt(1, chaveParamRegularizacao);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                paramRegularizacao =
                    new ParamRegularizacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getInt("quem"),
                        Timestamp.valueOf(resultado.getString("quando")));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("ParamRegularizacaoRelacional.lerParamRegularizacao: " + e.toString());
        }
        return paramRegularizacao;
    } /* lerParamRegularizacao */

    public ParamRegularizacao lerParamRegularizacao(String sigla)
    {
        ParamRegularizacao paramRegularizacao = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_PARAM_REGULARIZACAO " + "WHERE sigla = ?");

            sql.setString(1, sigla);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                paramRegularizacao =
                    new ParamRegularizacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getInt("quem"),
                        Timestamp.valueOf(resultado.getString("quando")));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("ParamRegularizacaoRelacional.lerParamRegularizacao: " + e.toString());
        }
        return paramRegularizacao;
    } /* lerParamRegularizacao */

    public ArrayList lerTodasParamRegularizacoes()
    {
        ArrayList listaParamRegularizacoes = null;
        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT sigla FROM ass_PARAM_REGULARIZACAO");

            ResultSet resultado = sql.executeQuery();
            listaParamRegularizacoes = new ArrayList();
            while (resultado.next())
            {
                listaParamRegularizacoes.add(new String(resultado.getString("sigla")));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println(
                "ParamRegularizacaoRelacional.lerTodasParamRegularizacoes: " + e.toString());
        }
        return listaParamRegularizacoes;
    } /* lerTodasParamRegularizacoes */
}