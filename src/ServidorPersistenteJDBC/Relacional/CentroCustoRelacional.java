package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Dominio.CentroCusto;
import ServidorPersistenteJDBC.ICentroCustoPersistente;
/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class CentroCustoRelacional implements ICentroCustoPersistente
{

    public boolean alterarCentroCusto(CentroCusto centroCusto)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "UPDATE ass_CENTRO_CUSTO SET "
                        + "codigoInterno = ? , "
                        + "sigla = ? , "
                        + "departamento = ? ,"
                        + "seccao1 = ? ,"
                        + "seccao2 = ? "
                        + "WHERE codigoInterno = ? ");

            sql.setInt(1, centroCusto.getCodigoInterno());
            sql.setString(2, centroCusto.getSigla());
            sql.setString(3, centroCusto.getDepartamento());
            sql.setString(4, centroCusto.getSeccao1());
            sql.setString(5, centroCusto.getSeccao2());
            sql.setInt(6, centroCusto.getCodigoInterno());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("CentroCustoRelacional.alterarCentroCusto: " + e.toString());
        }
        return resultado;
    } /* alterarCentroCusto */

    public boolean escreverCentroCusto(CentroCusto centroCusto)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("INSERT INTO ass_CENTRO_CUSTO VALUES (?, ?, ?, ?, ?, 1)");

            sql.setInt(1, centroCusto.getCodigoInterno());
            sql.setString(2, centroCusto.getSigla());
            sql.setString(3, centroCusto.getDepartamento());
            sql.setString(4, centroCusto.getSeccao1());
            sql.setString(5, centroCusto.getSeccao2());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("CentroCustoRelacional.escreverCentroCusto: " + e.toString());
        }

        return resultado;

    } /* escreverCentroCusto */

    public CentroCusto lerCentroCusto(String sigla)
    {
        CentroCusto centroCusto = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM ass_CENTRO_CUSTO WHERE sigla = ?");

            sql.setString(1, sigla);

            ResultSet resultado = sql.executeQuery();

            if (resultado.next())
            {
                centroCusto =
                    new CentroCusto(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("departamento"),
                        resultado.getString("seccao1"),
                        resultado.getString("seccao2"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("CentroCustoRelacional.lerCentroCusto: " + e.toString());
        }
        return centroCusto;

    } /* lerCentroCusto */

    public CentroCusto lerCentroCusto(int codigoInterno)
    {
        CentroCusto centroCusto = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM ass_CENTRO_CUSTO WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                centroCusto =
                    new CentroCusto(
                        resultado.getInt("codigoInterno"),
                        resultado.getString("sigla"),
                        resultado.getString("departamento"),
                        resultado.getString("seccao1"),
                        resultado.getString("seccao2"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("CentroCustoRelacional.lerCentroCusto: " + e.toString());
        }
        return centroCusto;

    } /* lerCentroCusto */

    public ArrayList lerTodosCentrosCusto()
    {
        ArrayList listaCentrosCusto = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM ass_CENTRO_CUSTO ORDER BY sigla");
            ResultSet resultadoQuery = sql.executeQuery();

            listaCentrosCusto = new ArrayList();
            while (resultadoQuery.next())
            {
                listaCentrosCusto.add(
                    new CentroCusto(
                        resultadoQuery.getInt("codigoInterno"),
                        resultadoQuery.getString("sigla"),
                        resultadoQuery.getString("departamento"),
                        resultadoQuery.getString("seccao1"),
                        resultadoQuery.getString("seccao2")));
            }
            sql.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("CentroCustoRelacional.lerTodosCentrosCusto: " + e.toString());
        }
        return listaCentrosCusto;

    } /* lerTodosCentrosCusto */
}