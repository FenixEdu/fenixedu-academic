package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ListIterator;

import Dominio.Justificacao;
import Dominio.ParamJustificacao;
import ServidorPersistenteJDBC.IParamJustificacaoPersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class ParamJustificacaoRelacional implements IParamJustificacaoPersistente
{
    public boolean alterarParamJustificacao(ParamJustificacao paramJustificacao)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "UPDATE ass_PARAM_JUSTIFICACAO SET "
                        + "codigoInterno = ? , "
                        + "sigla = ? , "
                        + "descricao = ? , "
                        + "tipo = ? , "
                        + "tipoDias = ? , "
                        + "grupo = ? , "
                        + "quem = ? , "
                        + "quando = ? , "
                        + "WHERE codigoInterno = ? ");

            sql.setInt(1, paramJustificacao.getCodigoInterno());
            sql.setString(2, paramJustificacao.getSigla());
            sql.setString(3, paramJustificacao.getDescricao());
            sql.setString(4, paramJustificacao.getTipo());
            sql.setString(5, paramJustificacao.getTipoDias());
            sql.setString(6, paramJustificacao.getGrupo());
            sql.setInt(7, paramJustificacao.getQuem());
            if (paramJustificacao.getQuando() != null)
            {
                sql.setTimestamp(8, new Timestamp((paramJustificacao.getQuando()).getTime()));
            }
            else
            {
                sql.setTimestamp(8, null);
            }
            sql.setInt(9, paramJustificacao.getCodigoInterno());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("ParamJustificacaoRelacional.alterarParamJustificacao: " + e.toString());
        }
        return resultado;
    } /* alterarParamJustificacao */

    public boolean apagarParamJustificacao(String sigla)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "DELETE FROM ass_PARAM_JUSTIFICACAO " + "WHERE sigla = ?");

            sql.setString(1, sigla);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("ParamJustificacaoRelacional.apagarParamJustificacao: " + e.toString());
        }
        return resultado;
    } /* apagarParamJustificacao */

    public boolean escreverParamJustificacao(ParamJustificacao paramJustificacao)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "INSERT INTO ass_PARAM_JUSTIFICACAO " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            sql.setInt(1, paramJustificacao.getCodigoInterno());
            sql.setString(2, paramJustificacao.getSigla());
            sql.setString(3, paramJustificacao.getDescricao());
            sql.setString(4, paramJustificacao.getTipo());
            sql.setString(5, paramJustificacao.getTipoDias());
            sql.setString(6, paramJustificacao.getGrupo());
            sql.setInt(7, paramJustificacao.getQuem());
            if (paramJustificacao.getQuando() != null)
            {
                sql.setTimestamp(8, new Timestamp((paramJustificacao.getQuando()).getTime()));
            }
            else
            {
                sql.setTimestamp(8, null);
            }

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("ParamJustificacaoRelacional.escreverParamJustificacao: " + e.toString());
        }
        return resultado;
    } /* escreverParamJustificacao */

    public ArrayList lerGruposParamJustificacoes()
    {
        ArrayList listaGruposParamJustificacoes = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT DISTINCT grupo FROM ass_PARAM_JUSTIFICACAO");
            ResultSet resultado = sql.executeQuery();
            listaGruposParamJustificacoes = new ArrayList();
            while (resultado.next())
            {
                String grupo = new String();
                if (resultado.getString("grupo") != null)
                {
                    grupo = resultado.getString("grupo");
                }
                listaGruposParamJustificacoes.add(grupo);

            }
            sql.close();

        }
        catch (Exception e)
        {
            System.out.println(
                "ParamJustificacaoRelacional.lerGruposParamJustificacoes: " + e.toString());
        }
        return listaGruposParamJustificacoes;
    } /* lerGruposParamJustificacoes */

    public ParamJustificacao lerParamJustificacao(int codigoInterno)
    {
        ParamJustificacao paramJustificacao = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_PARAM_JUSTIFICACAO " + "WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                paramJustificacao =
                    new ParamJustificacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getString("tipo"),
                        resultado.getString("tipoDias"),
                        resultado.getString("grupo"),
                        resultado.getInt("quem"),
                        Timestamp.valueOf(resultado.getString("quando")));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("ParamJustificacaoRelacional.lerParamJustificacao: " + e.toString());
        }
        return paramJustificacao;
    } /* lerParamJustificacao */

    public ParamJustificacao lerParamJustificacao(String sigla)
    {
        ParamJustificacao paramJustificacao = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_PARAM_JUSTIFICACAO " + "WHERE sigla = ?");

            sql.setString(1, sigla);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                paramJustificacao =
                    new ParamJustificacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getString("tipo"),
                        resultado.getString("tipoDias"),
                        resultado.getString("grupo"),
                        resultado.getInt("quem"),
                        Timestamp.valueOf(resultado.getString("quando")));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("ParamJustificacaoRelacional.lerParamJustificacao: " + e.toString());
        }
        return paramJustificacao;
    } /* lerParamJustificacao */

    public ArrayList lerParamJustificacoes(ArrayList listaJustificacoes)
    {
        ParamJustificacao paramJustificacao = null;
        Justificacao justificacao = null;
        ArrayList listaParamJustificacoes = null;

        try
        {
            if (listaJustificacoes != null)
            {
                ListIterator iterador = listaJustificacoes.listIterator();
                listaParamJustificacoes = new ArrayList();
                PreparedStatement sql =
                    UtilRelacional.prepararComando(
                        "SELECT * FROM ass_PARAM_JUSTIFICACAO " + "WHERE codigoInterno = ?");
                ResultSet resultado = null;
                while (iterador.hasNext())
                {
                    justificacao = (Justificacao) iterador.next();

                    sql.setInt(1, justificacao.getChaveParamJustificacao());
                    resultado = sql.executeQuery();
                    if (resultado.next())
                    {
                        paramJustificacao =
                            new ParamJustificacao(
                                resultado.getInt("codigoInterno"),
                                resultado.getString("sigla"),
                                resultado.getString("descricao"),
                                resultado.getString("tipo"),
                                resultado.getString("tipoDias"),
                                resultado.getString("grupo"),
                                resultado.getInt("quem"),
                                Timestamp.valueOf(resultado.getString("quando")));
                    }
                    else
                    {
                        sql.close();
                        throw new Exception();
                    }
                    listaParamJustificacoes.add(paramJustificacao);
                }
                sql.close();
            }
        }
        catch (Exception e)
        {
            System.out.println("ParamJustificacaoRelacional.lerParamJustificacoes: " + e.toString());
        }
        return listaParamJustificacoes;
    } /* lerParamJustificacoes */

    public ArrayList lerSiglasJustificacao()
    {
        ArrayList listaJustificacoes = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT sigla FROM ass_PARAM_JUSTIFICACAO");

            ResultSet resultado = sql.executeQuery();
            listaJustificacoes = new ArrayList();
            while (resultado.next())
            {
                listaJustificacoes.add(new String(resultado.getString("sigla")));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("ParamJustificacaoRelacional.lerSiglasJustificacao: " + e.toString());
        }
        return listaJustificacoes;
    } /* lerSiglasJustificacao */

    public ArrayList lerTipoDiasParamJustificacoes()
    {
        ArrayList listaTipoDiasParamJustificacoes = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT DISTINCT tipoDias FROM ass_PARAM_JUSTIFICACAO");
            ResultSet resultado = sql.executeQuery();
            listaTipoDiasParamJustificacoes = new ArrayList();
            while (resultado.next())
            {
                listaTipoDiasParamJustificacoes.add(new String(resultado.getString("tipoDias")));
            }
            sql.close();

        }
        catch (Exception e)
        {
            System.out.println(
                "ParamJustificacaoRelacional.lerTipoDiasParamJustificacoes: " + e.toString());
        }
        return listaTipoDiasParamJustificacoes;
    } /* lerTipoDiasParamJustificacoes */

    public ArrayList lerTiposParamJustificacoes()
    {
        ArrayList listaTiposParamJustificacoes = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT DISTINCT tipo FROM ass_PARAM_JUSTIFICACAO");
            ResultSet resultado = sql.executeQuery();
            listaTiposParamJustificacoes = new ArrayList();
            while (resultado.next())
            {
                listaTiposParamJustificacoes.add(new String(resultado.getString("tipo")));
            }
            sql.close();

        }
        catch (Exception e)
        {
            System.out.println(
                "ParamJustificacaoRelacional.lerTiposParamJustificacoes: " + e.toString());
        }
        return listaTiposParamJustificacoes;
    } /* lerTiposParamJustificacoes */

    public ArrayList lerTodasParamJustificacoes(String ordem)
    {
        ArrayList listaJustificacoes = null;
        String query = null;

        try
        {
            if (ordem.equals("descendente"))
            {
                query = new String("SELECT * FROM ass_PARAM_JUSTIFICACAO ORDER BY sigla DESC");
            }
            else
            {
                query = new String("SELECT * FROM ass_PARAM_JUSTIFICACAO");
            }
            PreparedStatement sql = UtilRelacional.prepararComando(query);
            ResultSet resultado = sql.executeQuery();
            listaJustificacoes = new ArrayList();
            while (resultado.next())
            {
                listaJustificacoes.add(
                    new ParamJustificacao(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("descricao"),
                        resultado.getString("tipo"),
                        resultado.getString("tipoDias"),
                        resultado.getString("grupo"),
                        resultado.getInt("quem"),
                        Timestamp.valueOf(resultado.getString("quando"))));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println(
                "ParamJustificacaoRelacional.lerTodasParamJustificacoes: " + e.toString());
        }
        return listaJustificacoes;
    } /* lerTodasParamJustificacoes */
}