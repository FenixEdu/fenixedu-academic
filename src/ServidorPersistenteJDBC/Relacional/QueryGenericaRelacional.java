/*
 * QueryGenericaRelacional.java
 *  
 */

package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * classe que executa operacoes de leitura e escrita de registos a partir da estrutura das mesmas
 * 
 * @author Ivo Brandão
 */
public class QueryGenericaRelacional
{

    /** construtor */
    public QueryGenericaRelacional()
    {
    }

    /**
	 * gera as clausulas Where com a chave de uma Query; usado por queryLeitura, queryApagar,
	 * queryAlteracao
	 */
    private String clausulasWhere(Hashtable chave)
    {
        Iterator iteradorChaves;
        int contadorChaves = 0;
        String clausulas = new String();

        clausulas = clausulas.concat("WHERE ");

        iteradorChaves = chave.entrySet().iterator();
        while (iteradorChaves.hasNext())
        {
            contadorChaves++;
            Entry entrada = (Entry) iteradorChaves.next();
            String nomeChave = entrada.getKey().toString();
            clausulas = clausulas.concat(nomeChave);
            clausulas = clausulas.concat(" = ? AND ");
        }
        clausulas = clausulas.substring(0, clausulas.length() - 5);
        return clausulas;
    }

    /** produz um PreparedStatement para a query de leitura, usado pelo metodo ler */
    private PreparedStatement queryLeitura(String nomeTabela, Hashtable chave) throws Exception
    {
        Iterator iteradorChaves;
        int contadorChaves = 0;
        String query = new String();
        PreparedStatement querySQL;

        query = query.concat("SELECT * FROM ");
        query = query.concat(nomeTabela);
        query = query.concat(" ");
        query = query.concat(clausulasWhere(chave));

        querySQL = UtilRelacional.prepararComando(query);

        //acertar valores para a chave primaria
        iteradorChaves = chave.entrySet().iterator();
        while (iteradorChaves.hasNext())
        {
            contadorChaves++;
            Entry entrada = (Entry) iteradorChaves.next();
            querySQL.setObject(contadorChaves, entrada.getValue());
        }

        return querySQL;
    }

    /** produz um PreparedStatement para a query de escrita, usado pelo metodo escrever */
    private PreparedStatement queryEscrita(String nomeTabela, Hashtable atributos, Hashtable valores)
        throws Exception
    {

        Iterator iteradorAtributos;
        String query = new String();
        PreparedStatement querySQL;
        String nomesAtributos = new String();
        String valoresAtributos = new String();
        int contadorAtributos = 0;

        query = query.concat("INSERT INTO ");
        query = query.concat(nomeTabela);

        //construir simultaneamente pares atributo - valor
        iteradorAtributos = atributos.entrySet().iterator();
        while (iteradorAtributos.hasNext())
        {
            Entry atributo = (Entry) iteradorAtributos.next();
            String nomeAtributo = atributo.getKey().toString();

            nomesAtributos = nomesAtributos.concat(nomeAtributo);
            nomesAtributos = nomesAtributos.concat(",");
            valoresAtributos = valoresAtributos.concat("?,");
        }
        nomesAtributos = nomesAtributos.substring(0, nomesAtributos.length() - 1);
        valoresAtributos = valoresAtributos.substring(0, valoresAtributos.length() - 1);

        query = query.concat(" (");
        query = query.concat(nomesAtributos);
        query = query.concat(")");
        query = query.concat(" VALUES (");
        query = query.concat(valoresAtributos);
        query = query.concat(")");

        querySQL = UtilRelacional.prepararComando(query);

        //acertar valores para os atributos
        iteradorAtributos = atributos.entrySet().iterator();
        while (iteradorAtributos.hasNext())
        {
            Entry atributo = (Entry) iteradorAtributos.next();
            String nomeAtributo = atributo.getKey().toString();
            Object valorAtributo = valores.get(nomeAtributo);
            contadorAtributos++;

            querySQL.setObject(contadorAtributos, valorAtributo);
        }

        return querySQL;
    }

    /** produz um PreparedStatement para a query de alteracao, usado pelo metodo alterar */
    private PreparedStatement queryAlteracao(
        String nomeTabela,
        Hashtable atributos,
        Hashtable valores,
        Hashtable chave)
        throws Exception
    {

        Iterator iteradorAtributos;
        Iterator iteradorChaves;
        String query = new String();
        PreparedStatement querySQL;
        String nomesAtributos = new String();
        int contadorAtributos = 0;
        int contadorChaves = 0;

        query = query.concat("UPDATE ");
        query = query.concat(nomeTabela);

        //construir simultaneamente pares atributo - valor
        iteradorAtributos = atributos.entrySet().iterator();
        while (iteradorAtributos.hasNext())
        {
            Entry atributo = (Entry) iteradorAtributos.next();
            String nomeAtributo = atributo.getKey().toString();

            nomesAtributos = nomesAtributos.concat(nomeAtributo);
            nomesAtributos = nomesAtributos.concat(" = ?,");
        }
        nomesAtributos = nomesAtributos.substring(0, nomesAtributos.length() - 1);

        query = query.concat(" SET ");
        query = query.concat(nomesAtributos);

        query = query.concat(" ");
        query = query.concat(clausulasWhere(chave));

        System.out.println("QueryGenericaRelacional.queryAlteracao:QUERY - " + query);

        querySQL = UtilRelacional.prepararComando(query);

        //acertar valores para os atributos
        iteradorAtributos = atributos.entrySet().iterator();
        while (iteradorAtributos.hasNext())
        {
            Entry atributo = (Entry) iteradorAtributos.next();
            String nomeAtributo = atributo.getKey().toString();
            Object valorAtributo = valores.get(nomeAtributo);
            contadorAtributos++;

            querySQL.setObject(contadorAtributos, valorAtributo);
        }

        //acertar valores para as chaves
        iteradorChaves = chave.entrySet().iterator();
        while (iteradorChaves.hasNext())
        {
            Entry elementoChave = (Entry) iteradorChaves.next();
            String nomeChave = elementoChave.getKey().toString();
            Object valorChave = chave.get(nomeChave);
            contadorChaves++;

            querySQL.setObject(contadorAtributos + contadorChaves, valorChave);
        }

        return querySQL;
    }

    /** produz um PreparedStatement para a query de eliminacao, usado pelo metodo apagar */
    private PreparedStatement queryApagar(String nomeTabela, Hashtable chave) throws Exception
    {
        Iterator iteradorChaves;
        String query = new String();
        PreparedStatement querySQL;
        int contadorChaves = 0;

        query = query.concat("DELETE FROM ");
        query = query.concat(nomeTabela);

        query = query.concat(" ");
        query = query.concat(clausulasWhere(chave));

        querySQL = UtilRelacional.prepararComando(query);

        //acertar valores para as chaves
        iteradorChaves = chave.entrySet().iterator();
        while (iteradorChaves.hasNext())
        {
            Entry elementoChave = (Entry) iteradorChaves.next();
            String nomeChave = elementoChave.getKey().toString();
            Object valorChave = chave.get(nomeChave);
            contadorChaves++;

            querySQL.setObject(contadorChaves, valorChave);
        }

        return querySQL;
    }
    /** constroi uma tabela de dispersao com os valores para os atributos, usado pelo metodo ler */
    private Hashtable constroiResultadoLeitura(Hashtable atributos, ResultSet resultadoSQL)
        throws Exception
    {
        Iterator iteradorAtributos = null;
        Hashtable resultado = null;

        iteradorAtributos = atributos.entrySet().iterator();
        if (resultadoSQL.next())
        {
            resultado = new Hashtable();

            while (iteradorAtributos.hasNext())
            {
                Entry atributo = (Entry) iteradorAtributos.next();
                String nomeAtributo = atributo.getKey().toString();
                Object valorAtributo = resultadoSQL.getObject(nomeAtributo);

                resultado.put(nomeAtributo, valorAtributo);
            }
        }
        return resultado;
    }

    /** metodo generico para implementar a leitura de um registo */
    public Hashtable ler(String nomeTabela, Hashtable chave, Hashtable atributos)
    {
        PreparedStatement querySQL = null;
        ResultSet resultadoSQL = null;
        Hashtable resultado = null;

        try
        {
            querySQL = queryLeitura(nomeTabela, chave);

            resultadoSQL = querySQL.executeQuery();

            resultado = constroiResultadoLeitura(atributos, resultadoSQL);

            querySQL.close();
        }
        catch (Exception e)
        {
            System.out.println("QueryGenericaRelacional.ler: " + e.toString());
        }
        return resultado;
    }

    /** metodo generico para implementar a escrita de um registo */
    public Boolean escrever(String nomeTabela, Hashtable atributos, Hashtable valores)
    {
        PreparedStatement querySQL = null;
        Boolean resultado = new Boolean(false);

        try
        {
            querySQL = queryEscrita(nomeTabela, atributos, valores);

            querySQL.executeUpdate();
            querySQL.close();

            resultado = new Boolean(true);

        }
        catch (Exception e)
        {
            System.out.println("QueryGenericaRelacional.escrever: " + e.toString());
        }
        return resultado;
    }

    /** metodo generico para implementar a alteracao de um registo */
    public Boolean alterar(String nomeTabela, Hashtable atributos, Hashtable valores, Hashtable chave)
    {
        PreparedStatement querySQL = null;
        Boolean resultado = new Boolean(false);

        try
        {
            querySQL = queryAlteracao(nomeTabela, atributos, valores, chave);

            querySQL.executeUpdate();
            querySQL.close();

            resultado = new Boolean(true);

        }
        catch (Exception e)
        {
            System.out.println("QueryGenericaRelacional.alterar: " + e.toString());
        }
        return resultado;
    }

    /** metodo generico para implementar a eliminacao de um registo */
    public Boolean apagar(String nomeTabela, Hashtable chave)
    {
        PreparedStatement querySQL = null;
        Boolean resultado = new Boolean(false);

        try
        {
            querySQL = queryApagar(nomeTabela, chave);

            querySQL.executeUpdate();
            querySQL.close();

            resultado = new Boolean(true);

        }
        catch (Exception e)
        {
            System.out.println("QueryGenericaRelacional.apagar: " + e.toString());
        }
        return resultado;
    }

}
