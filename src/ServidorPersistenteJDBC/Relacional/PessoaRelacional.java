package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import Dominio.Pessoa;
import ServidorPersistenteJDBC.IPessoaPersistente;
import Util.DataIndisponivel;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Ivo Brandão
 */
public class PessoaRelacional implements IPessoaPersistente
{

    /** Cria uma nova instância de PessoaRelacional */
    public PessoaRelacional()
    {
    }

    /**
	 * Altera um registo de Pessoa
	 * 
	 * @return true se sucedeu, false se ocorreu uma excepcao
	 */
    public boolean alterarPessoa(Pessoa pessoa)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "UPDATE PERSON SET "
                        + "ID_INTERNAL = ? , "
                        + "DOCUMENT_ID_NUMBER = ? , "
                        + "EMISSION_LOCATION_OF_DOCUMENT_ID = ? , "
                        + "EMISSION_DATE_OF_DOCUMENT_ID = ? , "
                        + "EXPERATION_DATE_OF_DOCUMENT_ID = ? , "
                        + "NAME = ? , "
                        + "DATE_OF_BIRTH = ? , "
                        + "NAME_OF_FATHER = ? , "
                        + "NAME_OF_MOTHER = ? , "
                        + "NATIONALITY = ? , "
                        + "PARISH_OF_BIRTH = ? , "
                        + "DISTRICT_SUBDIVISION_OF_BIRTH = ? , "
                        + "DISTRICT_OF_BIRTH = ? , "
                        + "ADDRESS = ? , "
                        + "AREA = ? , "
                        + "AREA_CODE = ? , "
                        + "AREA_OF_AREA_CODE = ? , "
                        + "PARISH_OF_RESIDENCE = ? , "
                        + "DISTRICT_SUBDIVISION_OF_RESIDENCE = ? , "
                        + "DISTRICT_OF_RESIDENCE = ? , "
                        + "PHONE = ? , "
                        + "MOBILE = ? , "
                        + "EMAIL = ? , "
                        + "WEB_ADRDRESS = ? , "
                        + "SOCIAL_SECURITY_NUMBER = ? , "
                        + "PROFESSION = ? , "
                        + "USERNAME = ? , "
                        + "PASSWD = ? , "
                        + "KEY_COUNTRY = ? , "
                        + "FISCAL_CODE = ? , "
                        + "TYPE_ID_DOCUMENT = ? , "
                        + "SEX = ? , "
                        + "MARITAL_STATUS = ? "
                        + "WHERE ID_INTERNAL = ? ");

            sql.setInt(1, pessoa.getIdInternal().intValue());
            sql.setString(2, pessoa.getNumeroDocumentoIdentificacao());
            sql.setString(3, pessoa.getLocalEmissaoDocumentoIdentificacao());
            if (pessoa.getDataEmissaoDocumentoIdentificacao() != null)
            {
                sql.setDate(
                    4,
                    new java.sql.Date(pessoa.getDataEmissaoDocumentoIdentificacao().getTime()));
            }
            else
            {
                sql.setDate(4, null);
            }
            if (pessoa.getDataValidadeDocumentoIdentificacao() != null)
            {
                sql.setDate(
                    5,
                    new java.sql.Date(pessoa.getDataValidadeDocumentoIdentificacao().getTime()));
            }
            else
            {
                sql.setDate(5, null);
            }
            sql.setString(6, pessoa.getNome());
            if (pessoa.getNascimento() != null)
            {
                sql.setDate(7, new java.sql.Date(pessoa.getNascimento().getTime()));
            }
            else
            {
                sql.setDate(7, null);
            }
            sql.setString(8, pessoa.getNomePai());
            sql.setString(9, pessoa.getNomeMae());
            sql.setString(10, pessoa.getNacionalidade());
            sql.setString(11, pessoa.getFreguesiaNaturalidade());
            sql.setString(12, pessoa.getConcelhoNaturalidade());
            sql.setString(13, pessoa.getDistritoNaturalidade());
            sql.setString(14, pessoa.getMorada());
            sql.setString(15, pessoa.getLocalidade());
            sql.setString(16, pessoa.getCodigoPostal());
            sql.setString(17, pessoa.getLocalidadeCodigoPostal());
            sql.setString(18, pessoa.getFreguesiaMorada());
            sql.setString(19, pessoa.getConcelhoMorada());
            sql.setString(20, pessoa.getDistritoMorada());
            sql.setString(21, pessoa.getTelefone());
            sql.setString(22, pessoa.getTelemovel());
            sql.setString(23, pessoa.getEmail());
            sql.setString(24, pessoa.getEnderecoWeb());
            sql.setString(25, pessoa.getNumContribuinte());
            sql.setString(26, pessoa.getProfissao());
            sql.setString(27, pessoa.getUsername());
            sql.setString(28, pessoa.getPassword());
            if (pessoa.getChavePais() != null)
            {
                sql.setInt(29, pessoa.getChavePais().intValue());
            }
            else
            {
                sql.setInt(29, 0);
            }
            sql.setString(30, pessoa.getCodigoFiscal());
            sql.setInt(31, pessoa.getTipoDocumentoIdentificacao().getTipo().intValue());
            sql.setInt(32, pessoa.getSexo().getSexo().intValue());
            sql.setInt(33, pessoa.getEstadoCivil().getEstadoCivil().intValue());
            sql.setInt(34, pessoa.getIdInternal().intValue());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (/* java.sql.SQL */
            Exception SQLe)
        {
            System.out.println("PessoaRelacional.alterarPessoa: " + SQLe.toString());
        }
        return resultado;

    }

    /**
	 * Apaga um registo de Pessoa
	 * 
	 * @return true se sucedeu, false se ocorreu uma excepcao
	 */
    public boolean apagarPessoa(int codigoInterno)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("DELETE FROM PERSON WHERE ID_INTERNAL = ?");

            sql.setInt(1, codigoInterno);

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (/* SQL */
            Exception SQLe)
        {
            System.out.println("PessoaRelacional.apagarPessoa: " + SQLe.toString());
        }
        return resultado;

    }

    public boolean escreverPapelPessoa(Pessoa pessoa, int chaveRole)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("INSERT INTO PERSON_ROLE " + "VALUES (?, ?, ?)");

            sql.setInt(1, 0);
            sql.setInt(2, chaveRole);
            sql.setInt(3, pessoa.getIdInternal().intValue());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (/* SQL */
            Exception SQLe)
        {
            SQLe.printStackTrace();
            System.out.println("PessoaRelacional.escreverPapelPessoa: " + SQLe.toString());
        }
        finally
        {
            return resultado;
        }
    }

    /**
	 * Escreve um registo de Pessoa
	 * 
	 * @return true se sucedeu, false se ocorreu uma excepcao
	 */
    public boolean escreverPessoa(Pessoa pessoa)
    {
        boolean resultado = false;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "INSERT INTO PERSON "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, MD5(?), ?, ?, ?, ?, ?)");

            sql.setInt(1, pessoa.getIdInternal().intValue());
            sql.setString(2, pessoa.getNumeroDocumentoIdentificacao());
            sql.setString(3, pessoa.getLocalEmissaoDocumentoIdentificacao());
            if (pessoa.getDataEmissaoDocumentoIdentificacao() != null)
            {
                sql.setDate(
                    4,
                    new java.sql.Date(pessoa.getDataEmissaoDocumentoIdentificacao().getTime()));
            }
            else
            {
                sql.setDate(4, null);
            }
            if (pessoa.getDataValidadeDocumentoIdentificacao() != null)
            {
                sql.setDate(
                    5,
                    new java.sql.Date(pessoa.getDataValidadeDocumentoIdentificacao().getTime()));
            }
            else
            {
                sql.setDate(5, null);
            }
            sql.setString(6, pessoa.getNome());
            if (pessoa.getNascimento() != null)
            {
                sql.setDate(7, new java.sql.Date(pessoa.getNascimento().getTime()));
            }
            else
            {
                sql.setDate(7, null);
            }
            sql.setString(8, pessoa.getNomePai());
            sql.setString(9, pessoa.getNomeMae());
            sql.setString(10, pessoa.getNacionalidade());
            sql.setString(11, pessoa.getFreguesiaNaturalidade());
            sql.setString(12, pessoa.getConcelhoNaturalidade());
            sql.setString(13, pessoa.getDistritoNaturalidade());
            sql.setString(14, pessoa.getMorada());
            sql.setString(15, pessoa.getLocalidade());
            sql.setString(16, pessoa.getCodigoPostal());
            sql.setString(17, pessoa.getLocalidadeCodigoPostal());
            sql.setString(18, pessoa.getFreguesiaMorada());
            sql.setString(19, pessoa.getConcelhoMorada());
            sql.setString(20, pessoa.getDistritoMorada());
            sql.setString(21, pessoa.getTelefone());
            sql.setString(22, pessoa.getTelemovel());
            sql.setString(23, pessoa.getEmail());
            sql.setString(24, pessoa.getEnderecoWeb());
            sql.setString(25, pessoa.getNumContribuinte());
            sql.setString(26, pessoa.getProfissao());
            sql.setString(27, pessoa.getUsername());
            sql.setString(28, pessoa.getPassword());
            if (pessoa.getChavePais() != null)
            {
                sql.setInt(29, pessoa.getChavePais().intValue());
            }
            else
            {
                sql.setInt(29, 0);
            }
            sql.setString(30, pessoa.getCodigoFiscal());
            sql.setInt(31, pessoa.getTipoDocumentoIdentificacao().getTipo().intValue());
            sql.setInt(32, pessoa.getSexo().getSexo().intValue());
            sql.setInt(33, pessoa.getEstadoCivil().getEstadoCivil().intValue());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        }
        catch (/* SQL */
            Exception SQLe)
        {
            System.out.println("PessoaRelacional.escreverPessoa: " + SQLe.toString());
        }
        return resultado;

    }

    /**
	 * Le os cargos de Pessoa com a chave primaria
	 * 
	 * @return lista de cargos se sucedeu, null caso contrario
	 */
    public ArrayList lerCargos(int chavePessoa)
    {
        ArrayList listaCargos = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT ass_CARGO.cargo FROM ass_CARGO "
                        + "LEFT JOIN ass_PESSOA_CARGO ON ass_CARGO.chaveCargo=ass_PESSOA_CARGO.chaveCargo "
                        + "WHERE ass_PESSOA_CARGO.chavePessoa = ?");
            sql.setInt(1, chavePessoa);

            ResultSet resultado = sql.executeQuery();
            listaCargos = new ArrayList();
            while (resultado.next())
            {
                listaCargos.add(resultado.getString("cargo"));
            }
            sql.close();
        }
        catch (SQLException SQLe)
        {
            System.out.println("PessoaRelacional.lerCargos: " + SQLe.toString());
        }
        finally
        {
            return listaCargos;
        }
    } /* lerCargos */

    /** Le todos os roles associado à Pessoa */
    public ArrayList lerPapelPessoa(int codigoInterno)
    {
        ArrayList listaRoles = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM PERSON_ROLE WHERE KEY_PERSON = ?");
            sql.setInt(1, codigoInterno);

            ResultSet resultadoQuery = sql.executeQuery();

            //query para buscar os roles
            PreparedStatement sql2 =
                UtilRelacional.prepararComando("SELECT * FROM ROLE WHERE ID_INTERNAL = ?");
            while (resultadoQuery.next())
            {
                sql2.setInt(1, resultadoQuery.getInt("KEY_ROLE"));

                ResultSet resultadoQuery2 = sql2.executeQuery();

                listaRoles = new ArrayList();
                while (resultadoQuery2.next())
                {
                    listaRoles.add(new Integer(resultadoQuery2.getInt("ID_INTERNAL")));
                }
            }
            sql2.close();
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("PessoaRelacional.lerPapelPessoa: " + e.toString());
            return null;
        }
        return listaRoles;

    } /* lerPapelPessoa */

    /**
	 * Le um registo de Pessoa com a chave primaria
	 * 
	 * @return Pessoa se sucedeu, null caso contrario
	 */
    public Pessoa lerPessoa(int codigoInterno)
    {
        Pessoa pessoa = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM PERSON WHERE ID_INTERNAL = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                pessoa = constroiPessoa(resultado);
            }
            sql.close();
        }
        catch (/* SQL */
            Exception SQLe)
        {
            System.out.println("PessoaRelacional.lerPessoa: " + SQLe.toString());
        }
        return pessoa;

    }

    /**
	 * Le um registo de Pessoa com o username
	 * 
	 * @return Pessoa se sucedeu, null caso contrario
	 */
    public Pessoa lerPessoa(String username)
    {
        Pessoa pessoa = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM PERSON WHERE USERNAME = ?");

            sql.setString(1, username);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                pessoa = constroiPessoa(resultado);
            }
            sql.close();
        }
        catch (/* SQL */
            Exception SQLe)
        {
            System.out.println("PessoaRelacional.lerPessoa: " + SQLe.toString());
        }
        return pessoa;

    }

    /**
	 * Le um registo de Pessoa atraves do Documento de Identificacao
	 * 
	 * @return Pessoa se sucedeu, null caso contrario
	 */
    public Pessoa lerPessoa(String numeroDocumentoIdentificacao, int tipoDocumentoIdentificacao)
    {
        Pessoa pessoa = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando(
                    "SELECT * FROM PERSON WHERE DOCUMENT_ID_NUMBER = ? " + "AND TYPE_ID_DOCUMENT = ?");

            sql.setString(1, numeroDocumentoIdentificacao);
            sql.setInt(2, tipoDocumentoIdentificacao);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next())
            {
                pessoa = constroiPessoa(resultado);
            }
            sql.close();
        }
        catch (/* SQL */
            Exception SQLe)
        {
            System.out.println("PessoaRelacional.lerPessoa: " + SQLe.toString());
        }
        return pessoa;

    }

    public ArrayList lerPessoasPorCargo(String cargo)
    {
        ArrayList listaPessoas = null;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT * FROM ass_CARGO WHERE cargo = ?");
            sql.setString(1, cargo);
            ResultSet resultadoQuery = sql.executeQuery();

            if (resultadoQuery.next())
            {
                PreparedStatement sql2 =
                    UtilRelacional.prepararComando(
                        "SELECT * FROM ass_PESSOA_CARGO WHERE chaveCargo = ?");
                sql2.setInt(1, resultadoQuery.getInt("chaveCargo"));
                ResultSet resultadoQuery2 = sql2.executeQuery();

                sql.close();
                sql = UtilRelacional.prepararComando("SELECT * FROM PERSON WHERE ID_INTERNAL = ?");

                listaPessoas = new ArrayList();
                while (resultadoQuery2.next())
                {
                    sql.setInt(1, resultadoQuery2.getInt("chavePessoa"));
                    resultadoQuery = sql.executeQuery();

                    if (resultadoQuery.next())
                    {
                        listaPessoas.add(constroiPessoa(resultadoQuery));
                    }
                }
                sql.close();
                sql2.close();
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("PessoaRelacional.lerPessoasPorCargo: " + e.toString());
            return null;
        }
        return listaPessoas;

    } /* lerPessoasPorCargo */

    public ArrayList lerTodasPessoas()
    {
        ArrayList listaPessoas = null;

        try
        {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM PERSON ");
            ResultSet resultado = sql.executeQuery();

            listaPessoas = new ArrayList();
            while (resultado.next())
            {
                listaPessoas.add(constroiPessoa(resultado));
            }
            sql.close();
        }
        catch (Exception e)
        {
            System.out.println("PessoaRelacional.lerTodasPessoas: " + e.toString());
            return null;
        }
        return listaPessoas;

    } /* lerTodasPessoas */

    /**
	 * Retorna o ultimo codigoInterno utilizado
	 * 
	 * @return codigoInterno se sucedeu, 0 caso contrario
	 */
    public int ultimoCodigoInterno()
    {
        int ultimo = 0;

        try
        {
            PreparedStatement sql =
                UtilRelacional.prepararComando("SELECT max(ID_INTERNAL) FROM PERSON");

            ResultSet resultado = sql.executeQuery();

            if (resultado.next())
                ultimo = resultado.getInt(1);

            sql.close();
        }
        catch (/* SQL */
            Exception SQLe)
        {
            System.out.println("PessoaRelacional.ultimoCodigoInterno: " + SQLe.toString());
        }
        return ultimo;

    }

    /**
	 * Constroi um registo de Pessoa a partir de um ResultSet
	 * 
	 * @return Pessoa se sucedeu, null caso contrario
	 */
    private Pessoa constroiPessoa(ResultSet resultado)
    {
        Pessoa pessoa = null;

        try
        {
            Date emissionDate = null;
            if (resultado.getString("EMISSION_DATE_OF_DOCUMENT_ID") != null)
            {
                emissionDate =
                    converteData(
                        java.sql.Date.valueOf(resultado.getString("EMISSION_DATE_OF_DOCUMENT_ID")));
            }
            Date experationDate = null;
            if (resultado.getString("EXPERATION_DATE_OF_DOCUMENT_ID") != null)
            {
                experationDate =
                    converteData(
                        java.sql.Date.valueOf(resultado.getString("EXPERATION_DATE_OF_DOCUMENT_ID")));
            }
            Date birthDate = null;
            if (resultado.getString("DATE_OF_BIRTH") != null)
            {
                birthDate = converteData(java.sql.Date.valueOf(resultado.getString("DATE_OF_BIRTH")));
            }
            pessoa =
                new Pessoa(
                    new Integer(resultado.getInt("ID_INTERNAL")),
                    resultado.getString("DOCUMENT_ID_NUMBER"),
                    new TipoDocumentoIdentificacao(resultado.getInt("TYPE_ID_DOCUMENT")),
                    resultado.getString("EMISSION_LOCATION_OF_DOCUMENT_ID"),
                    emissionDate,
                    experationDate,
                    resultado.getString("NAME"),
                    new Sexo(resultado.getInt("SEX")),
                    new EstadoCivil(resultado.getInt("MARITAL_STATUS")),
                    birthDate,
                    resultado.getString("NAME_OF_FATHER"),
                    resultado.getString("NAME_OF_MOTHER"),
                    resultado.getString("NATIONALITY"),
                    resultado.getString("PARISH_OF_BIRTH"),
                    resultado.getString("DISTRICT_SUBDIVISION_OF_BIRTH"),
                    resultado.getString("DISTRICT_OF_BIRTH"),
                    resultado.getString("ADDRESS"),
                    resultado.getString("AREA"),
                    resultado.getString("AREA_CODE"),
                    resultado.getString("AREA_OF_AREA_CODE"),
                    resultado.getString("PARISH_OF_RESIDENCE"),
                    resultado.getString("DISTRICT_SUBDIVISION_OF_RESIDENCE"),
                    resultado.getString("DISTRICT_OF_RESIDENCE"),
                    resultado.getString("PHONE"),
                    resultado.getString("MOBILE"),
                    resultado.getString("EMAIL"),
                    resultado.getString("WEB_ADRDRESS"),
                    resultado.getString("SOCIAL_SECURITY_NUMBER"),
                    resultado.getString("PROFESSION"),
                    resultado.getString("USERNAME"),
                    resultado.getString("PASSWD"),
                    resultado.getString("FISCAL_CODE"));
        }
        catch (Exception e)
        {
            System.out.println("PessoaRelacional.constroiPessoa: " + e.toString());
        }
        return pessoa;

    }

    /**
	 * Verifica se se trata de uma data invalida
	 * 
	 * @return java.util.Date ou Util.DataIndisponivel
	 */
    private java.util.Date converteData(java.util.Date dataNaoConvertida)
    {
        java.util.Date resultado = null;

        if (DataIndisponivel.isDataIndisponivel(dataNaoConvertida))
        {
            resultado = new DataIndisponivel();
        }
        else
        {
            resultado = dataNaoConvertida;
        }

        return resultado;
    }

    public ArrayList lerTodosCargos()
    {
        ArrayList cargos = null;
        try
        {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_CARGO");
            ResultSet resultado = sql.executeQuery();

            cargos = new ArrayList();
            while (resultado.next())
                cargos.add(new String(resultado.getString(2)));
        }
        catch (SQLException SQLe)
        {
            System.out.println("ServicoSeguroLerCargos.execute" + SQLe.toString());
        }
        return cargos;
    }
}