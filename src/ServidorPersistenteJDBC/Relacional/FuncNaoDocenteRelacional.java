package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Dominio.FuncNaoDocente;
import ServidorPersistenteJDBC.IFuncNaoDocentePersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class FuncNaoDocenteRelacional implements IFuncNaoDocentePersistente
{

    public boolean alterarFuncNaoDocente(FuncNaoDocente funcionario)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "UPDATE ass_FUNC_NAO_DOCENTE SET "
                        + "codigoInterno = ? , "
                        + "chaveFuncionario = ? "
                        + "WHERE codigoInterno = ? ");

            sql.setInt(1, funcionario.getCodigoInterno());
            sql.setInt(2, funcionario.getChaveFuncionario());
            sql.setInt(3, funcionario.getCodigoInterno());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("FuncNaoDocenteRelacional.alterarFuncNaoDocente: " + e.toString());
        }
        return resultado;
    } /* alterarFuncNaoDocente */

    public boolean apagarFuncNaoDocente(int chaveFuncionario)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "DELETE FROM ass_FUNC_NAO_DOCENTE " + "WHERE chaveFuncionario = ?");

            sql.setInt(1, chaveFuncionario);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("FuncNaoDocenteRelacional.apagarFuncNaoDocente: " + e.toString());
        }
        return resultado;
    } /* apagarFuncNaoDocente */

    public boolean escreverFuncNaoDocente(FuncNaoDocente funcionario)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("INSERT INTO ass_FUNC_NAO_DOCENTE " + "VALUES (?, ?, 1)");

            sql.setInt(1, funcionario.getCodigoInterno());
            sql.setInt(2, funcionario.getChaveFuncionario());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (Exception e)
        {
            System.out.println("FuncNaoDocenteRelacional.escreverFuncNaoDocente: " + e.toString());
        }
        return resultado;
    } /* escreverFuncNaoDocente */

    public FuncNaoDocente lerFuncNaoDocente(int codigoInterno)
    {
        FuncNaoDocente funcionario = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_FUNC_NAO_DOCENTE " + "WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                funcionario =
                    new FuncNaoDocente(
                        resultado.getInt("codigoInterno"),
                        resultado.getInt("chaveFuncionario"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("FuncNaoDocenteRelacional.lerFuncNaoDocente" + e.toString());
        }
        return funcionario;
    } /* lerFuncNaoDocente */

    public FuncNaoDocente lerFuncNaoDocentePorFuncionario(int chaveFuncionario)
    {
        FuncNaoDocente funcionario = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM ass_FUNC_NAO_DOCENTE " + "WHERE chaveFuncionario = ?");

            sql.setInt(1, chaveFuncionario);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                funcionario =
                    new FuncNaoDocente(
                        resultado.getInt("codigoInterno"),
                        resultado.getInt("chaveFuncionario"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println(
                "FuncNaoDocenteRelacional.lerFuncNaoDocentePorFuncionario" + e.toString());
        }
        return funcionario;
    } /* lerFuncNaoDocentePorFuncionario */

    public FuncNaoDocente lerFuncNaoDocentePorNumMecanografico(int numMecanografico)
    {
        FuncNaoDocente funcionario = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
            sql.setInt(1, numMecanografico);
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
                    "SELECT * FROM ass_FUNC_NAO_DOCENTE WHERE chaveFuncionario = ?");
            sql.setInt(1, chaveFuncionario);

            resultado = sql.executeQuery();
            if (resultado.next())
            {
                funcionario =
                    new FuncNaoDocente(
                        resultado.getInt("codigoInterno"),
                        resultado.getInt("chaveFuncionario"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println(
                "FuncNaoDocenteRelacional.lerFuncNaoDocentePorNumMecanografico: " + e.toString());
        }
        return funcionario;
    }

    public FuncNaoDocente lerFuncNaoDocentePorPessoa(int chavePessoa)
    {
        FuncNaoDocente funcionario = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE chavePessoa = ?");
            sql.setInt(1, chavePessoa);
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
                    "SELECT * FROM ass_FUNC_NAO_DOCENTE WHERE chaveFuncionario = ?");
            sql.setInt(1, chaveFuncionario);

            resultado = sql.executeQuery();
            if (resultado.next())
            {
                funcionario =
                    new FuncNaoDocente(
                        resultado.getInt("codigoInterno"),
                        resultado.getInt("chaveFuncionario"));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("FuncNaoDocenteRelacional.lerFuncNaoDocentePorPessoa: " + e.toString());
        }
        return funcionario;
    } /* lerFuncNaoDocentePorPessoa */

    public int ultimoCodigoInterno()
    {
        int ultimo = 0;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT max(codigoInterno) FROM ass_FUNC_NAO_DOCENTE");

            ResultSet resultado = sql.executeQuery();

            if (resultado.next())
                ultimo = resultado.getInt(1);

            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("FuncNaoDocenteRelacional.ultimoCodigoInterno: " + e.toString());
        }
        return ultimo;
    } /* ultimoCodigoInterno */
}