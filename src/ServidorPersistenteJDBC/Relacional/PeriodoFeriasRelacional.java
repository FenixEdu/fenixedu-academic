package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import Dominio.PeriodoFerias;
import ServidorPersistenteJDBC.IPeriodoFeriasPersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class PeriodoFeriasRelacional implements IPeriodoFeriasPersistente
{

    public boolean alterarPeriodoFerias(PeriodoFerias periodoFerias)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "UPDATE ass_PERIODO_FERIAS SET "
                        + "codigoInterno = ? , "
                        + "chaveFuncionario = ? , "
                        + "dataInicio = ? , "
                        + "dataFim = ? , "
                        + "numDiasUteis = ? , "
                        + "tipoFerias = ? , "
                        + "quem = ? , "
                        + "quando = ? , "
                        + "WHERE codigoInterno = ? ");

            sql.setInt(1, periodoFerias.getCodigoInterno());
            sql.setInt(2, periodoFerias.getChaveFuncionario());
            if (periodoFerias.getDataInicio() != null)
            {
                sql.setDate(3, new java.sql.Date((periodoFerias.getDataInicio()).getTime()));
            }
            else
            {
                sql.setDate(3, null);
            }
            if (periodoFerias.getDataFim() != null)
            {
                sql.setDate(4, new java.sql.Date((periodoFerias.getDataFim()).getTime()));
            }
            else
            {
                sql.setDate(4, null);
            }
            sql.setInt(5, periodoFerias.getNumDiasUteis());
            sql.setInt(6, periodoFerias.getTipoFerias());
            sql.setInt(7, periodoFerias.getQuem());
            if (periodoFerias.getQuando() != null)
            {
                sql.setTimestamp(8, new Timestamp((periodoFerias.getQuando()).getTime()));
            }
            else
            {
                sql.setTimestamp(8, null);
            }
            sql.setInt(9, periodoFerias.getCodigoInterno());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("PeriodoFeriasRelacional.alterarPeriodoFerias: " + e.toString());
        }
        return resultado;
    } /* alterarPeriodoFerias */

    public boolean apagarPeriodoFerias(int codigoInterno)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("DELETE FROM ass_PERIODO_FERIAS WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("PeriodoFeriasRelacional.apagarPeriodoFerias: " + e.toString());
        }
        return resultado;
    } /* apagarPeriodoFerias */

    public boolean escreverPeriodoFerias(PeriodoFerias periodoFerias)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "INSERT INTO ass_PERIODO_FERIAS VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            sql.setInt(1, periodoFerias.getCodigoInterno());
            sql.setInt(2, periodoFerias.getChaveFuncionario());
            if (periodoFerias.getDataInicio() != null)
            {
                sql.setDate(3, new java.sql.Date((periodoFerias.getDataInicio()).getTime()));
            }
            else
            {
                sql.setDate(3, null);
            }
            if (periodoFerias.getDataFim() != null)
            {
                sql.setDate(4, new java.sql.Date((periodoFerias.getDataFim()).getTime()));
            }
            else
            {
                sql.setDate(4, null);
            }
            sql.setInt(5, periodoFerias.getNumDiasUteis());
            sql.setInt(6, periodoFerias.getTipoFerias());
            sql.setInt(7, periodoFerias.getQuem());
            if (periodoFerias.getQuando() != null)
            {
                sql.setTimestamp(8, new Timestamp((periodoFerias.getQuando()).getTime()));
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
            System.out.println("PeriodoFeriasRelacional.escreverPeriodoFerias: " + e.toString());
        }
        return resultado;
    } /* escreverPeriodoFerias */

    public ArrayList historicoFeriasPorFuncionario(int numFuncionario)
    {
        ArrayList listaFerias = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
            sql.setInt(1, numFuncionario);
            ResultSet resultado = sql.executeQuery();
            int chaveFuncionario = 0;
            if (resultado.next())
            {
                chaveFuncionario = resultado.getInt("codigoInterno");
            }
            else
            {
                sql.close();
                return null;
            }
            sql.close();

            sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_PERIODO_FERIAS WHERE chaveFuncionario = ?");
            sql.setInt(1, chaveFuncionario);

            resultado = sql.executeQuery();
            listaFerias = new ArrayList();
            java.util.Date dataFim = null;
            while (resultado.next())
            {
                if (resultado.getString("dataFim") != null)
                {
                    dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
                }
                listaFerias.add(
                    new PeriodoFerias(
                        resultado.getInt("codigoInterno"),
                        resultado.getInt("chaveFuncionario"),
                        java.sql.Date.valueOf(resultado.getString("dataInicio")),
                        dataFim,
                        resultado.getInt("numDiasUteis"),
                        resultado.getInt("tipoFerias"),
                        resultado.getInt("quem"),
                        Timestamp.valueOf(resultado.getString("quando"))));
            }
        }
        catch (Exception e)
        {
            System.out.println("PeriodoFeriasRelacional.historicoFeriasPorFuncionario: " + e.toString());
        }
        return listaFerias;
    } /* historicoFeriasPorFuncionario */

    public PeriodoFerias lerPeriodoFerias(int codigoInterno)
    {
        PeriodoFerias periodoFerias = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_PERIODO_FERIAS " + "WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                java.util.Date dataFim = null;
                if (resultado.getString("dataFim") != null)
                {
                    dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
                }

                periodoFerias =
                    new PeriodoFerias(
                        resultado.getInt("codigoInterno"),
                        resultado.getInt("chaveFuncionario"),
                        java.sql.Date.valueOf(resultado.getString("dataInicio")),
                        dataFim,
                        resultado.getInt("numDiasUteis"),
                        resultado.getInt("tipoFerias"),
                        resultado.getInt("quem"),
                        Timestamp.valueOf(resultado.getString("quando")));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("PeriodoFeriasRelacional.lerPeriodoFerias: " + e.toString());
        }
        return periodoFerias;
    } /* lerPeriodoFerias */

    public ArrayList lerFuncionariosComFerias(Timestamp dataFerias)
    {
        ArrayList listaFuncionarios = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT chaveFuncionario "
                        + "FROM ass_PERIODO_FERIAS WHERE ? BETWEEN dataInicio AND dataFim ");
            sql.setDate(1, new java.sql.Date(dataFerias.getTime()));
            ResultSet resultado = sql.executeQuery();

            listaFuncionarios = new ArrayList();
            PreparedStatement sql2 =
                UtilRelacional.prepararComando(
                    "SELECT numeroMecanografico FROM ass_FUNCIONARIO " + "WHERE codigoInterno = ?");
            ResultSet resultado2 = null;
            while (resultado.next())
            {
                sql2.setInt(1, resultado.getInt("chaveFuncionario"));
                resultado2 = sql.executeQuery();
                if (resultado2.next())
                {
                    listaFuncionarios.add(new Integer(resultado2.getInt("numeroFuncionario")));
                }
            }
            sql2.close();
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("PeriodoFeriasRelacional.lerFuncionariosComFerias: " + e.toString());
        }
        return listaFuncionarios;
    } /* lerFuncionariosComFerias */

    public ArrayList lerFeriasPorTipo(String tipoFerias)
    {
        ArrayList listaFerias = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT codigoInterno FROM ass_TIPO_FERIAS WHERE sigla = ?");
            sql.setString(1, tipoFerias);
            ResultSet resultado = sql.executeQuery();
            int chaveTipoFerias = 0;
            if (resultado.next())
            {
                chaveTipoFerias = resultado.getInt("codigoInterno");
            }
            else
            {
                sql.close();
                return null;
            }
            sql.close();

            sql =
                UtilRelacional.prepararComando("SELECT * FROM ass_PERIODO_FERIAS WHERE tipoFerias = ?");
            sql.setInt(1, chaveTipoFerias);

            resultado = sql.executeQuery();
            listaFerias = new ArrayList();
            java.util.Date dataFim = null;
            while (resultado.next())
            {
                if (resultado.getString("dataFim") != null)
                {
                    dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
                }
                listaFerias.add(
                    new PeriodoFerias(
                        resultado.getInt("codigoInterno"),
                        resultado.getInt("chaveFuncionario"),
                        java.sql.Date.valueOf(resultado.getString("dataInicio")),
                        dataFim,
                        resultado.getInt("numDiasUteis"),
                        resultado.getInt("tipoFerias"),
                        resultado.getInt("quem"),
                        Timestamp.valueOf(resultado.getString("quando"))));
            }
        }
        catch (Exception e)
        {
            System.out.println("PeriodoFeriasRelacional.lerFeriasPorTipo: " + e.toString());
        }
        return listaFerias;
    } /* lerFeriasPorTipo */
}