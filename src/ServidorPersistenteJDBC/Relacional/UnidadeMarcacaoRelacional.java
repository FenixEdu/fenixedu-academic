package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Dominio.UnidadeMarcacao;
import ServidorPersistenteJDBC.IUnidadeMarcacaoPersistente;

/**
 * @author Fernanda Quitério & Tânia Pousão
 */
public class UnidadeMarcacaoRelacional implements IUnidadeMarcacaoPersistente
{

    public boolean alterarUnidadeMarcacao(UnidadeMarcacao unidadeMarcacao)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "UPDATE ass_UNIDADE_MARCACAO SET "
                        + "codigoInterno = ? , "
                        + "sigla = ? , "
                        + "descricao = ? "
                        + "id = ? "
                        + "WHERE sigla = ? ");

            sql.setInt(1, unidadeMarcacao.getCodigoInterno());
            sql.setString(2, unidadeMarcacao.getSigla());
            sql.setString(3, unidadeMarcacao.getDescricao());
            sql.setInt(4, unidadeMarcacao.getID());
            sql.setString(5, unidadeMarcacao.getSigla());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("UnidadeMarcacaoRelacional.alterarUnidadeMarcacao: " + e.toString());
        }

        return resultado;

    } /* alterarUnidadeMarcacao */

    public boolean apagarUnidadeMarcacao(String sigla)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("DELETE FROM ass_UNIDADE_MARCACAO WHERE sigla = ?");

            sql.setString(1, sigla);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("UnidadeMarcacaoRelacional.apagarUnidadeMarcacao: " + e.toString());
        }

        return resultado;

    } /* apagarUnidadeMarcacao */

    public boolean escreverUnidadeMarcacao(UnidadeMarcacao unidadeMarcacao)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("INSERT INTO ass_UNIDADE_MARCACAO VALUES (?, ?, ?, ?)");

            sql.setInt(1, unidadeMarcacao.getCodigoInterno());
            sql.setString(2, unidadeMarcacao.getSigla());
            sql.setString(3, unidadeMarcacao.getDescricao());
            sql.setInt(4, unidadeMarcacao.getID());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("UnidadeMarcacaoRelacional.escreverUnidadeMarcacao: " + e.toString());
        }

        return resultado;

    } /* escreverUnidadeMarcacao */

    public UnidadeMarcacao lerUnidadeMarcacao(String sigla)
    {
        UnidadeMarcacao unidadeMarcacao = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM ass_UNIDADE_MARCACAO WHERE sigla = ?");

            sql.setString(1, sigla);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                unidadeMarcacao =
                    new UnidadeMarcacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getInt("id"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("UnidadeMarcacaoRelacional.lerUnidadeMarcacao: " + e.toString());
        }

        return unidadeMarcacao;

    } /* lerUnidadeMarcacao */

    public UnidadeMarcacao lerUnidadeMarcacao(int codigoInterno)
    {
        UnidadeMarcacao unidadeMarcacao = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_UNIDADE_MARCACAO WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                unidadeMarcacao =
                    new UnidadeMarcacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getInt("id"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("UnidadeMarcacaoRelacional.lerUnidadeMarcacao: " + e.toString());
        }

        return unidadeMarcacao;

    } /* lerUnidadeMarcacao */

    public UnidadeMarcacao lerUnidadeMarcacaoPorDescricao(String descricao)
    {
        UnidadeMarcacao unidadeMarcacao = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM ass_UNIDADE_MARCACAO WHERE descricao = ?");

            sql.setString(1, descricao);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                unidadeMarcacao =
                    new UnidadeMarcacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getInt("id"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println(
                "UnidadeMarcacaoRelacional.lerUnidadeMarcacaoPorDescricao: " + e.toString());
        }

        return unidadeMarcacao;

    } /* lerUnidadeMarcacaoPorDescricao */

    public UnidadeMarcacao lerUnidadeMarcacaoPorID(int id)
    {
        UnidadeMarcacao unidadeMarcacao = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM ass_UNIDADE_MARCACAO WHERE id = ?");

            sql.setInt(1, id);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                unidadeMarcacao =
                    new UnidadeMarcacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getInt("id"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("UnidadeMarcacaoRelacional.lerUnidadeMarcacaoPorID: " + e.toString());
        }

        return unidadeMarcacao;

    } /* lerUnidadeMarcacaoPorID */

    public ArrayList lerUnidadesMarcacao()
    {
        ArrayList unidades = null;

        try
        {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_UNIDADE_MARCACAO");

            ResultSet resultado = sql.executeQuery();
            unidades = new ArrayList();
            while (resultado.next())
            {
                unidades.add(
                    new UnidadeMarcacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getInt("id")));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("UnidadeMarcacaoRelacional.lerUnidadesMarcacao" + e.toString());
        }

        return unidades;

    } /* lerUnidadesMarcacao */
}