package ServidorPersistenteFiltroPessoa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import middleware.dataClean.personFilter.Concelho;
import middleware.dataClean.personFilter.Distrito;
import middleware.dataClean.personFilter.Freguesia;
import middleware.dataClean.personFilter.Morada;
import middleware.dataClean.personFilter.Pais;

public class ServidorAbrv
{
    public ServidorAbrv()
    {
    }

    /*
	 * constroi hashmap em que a key é a chave de um distrito e o object é um arraylist com todos os
	 * concelhos para esse distrito
	 */
    public HashMap lerMapConcelhos()
    {
        ResultSet resultado = null;
        HashMap mapConcelhos = new HashMap();
        int chaveConc, chaveDist;
        String design;
        Integer index;
        Concelho novoConcelho;
        ArrayList al = new ArrayList();
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM Concelho");
            resultado = sql.executeQuery();

            while (resultado.next())
            {
                chaveConc = resultado.getInt(1);
                chaveDist = resultado.getInt(2);
                design = resultado.getString(3);

                index = new Integer(chaveDist);
                novoConcelho = new Concelho(chaveConc, chaveDist, design);
                if (mapConcelhos.containsKey(index))
                {
                    al = (ArrayList) mapConcelhos.get(index);
                    al.add(novoConcelho);
                }
                else
                {
                    al = new ArrayList();
                    al.add(novoConcelho);
                    mapConcelhos.put(index, al);
                }
            }
            sql.close();
        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a ler Map Concelhos" + SQLe.toString());
            mapConcelhos = null;
        }
        return mapConcelhos;
    }

    /*
	 * constroi hashmap em que a key é a chave de um concelho e o object é um arraylist com todas as
	 * freguesias para esse concelho
	 */

    public HashMap lerMapFreguesias()
    {
        ResultSet resultado = null;
        HashMap mapFreguesias = new HashMap();
        int chaveFreg, chaveConc, chaveDistr;
        String design;
        Integer index;
        Freguesia novaFreguesia;
        ArrayList al = new ArrayList();
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM Freguesia");
            resultado = sql.executeQuery();

            while (resultado.next())
            {
                chaveFreg = resultado.getInt(1);
                chaveDistr = resultado.getInt(2);
                chaveConc = resultado.getInt(3);
                design = resultado.getString(4);

                index = new Integer(chaveConc);
                novaFreguesia = new Freguesia(chaveFreg, chaveDistr, chaveConc, design);
                if (mapFreguesias.containsKey(index))
                {
                    al = (ArrayList) mapFreguesias.get(index);
                    al.add(novaFreguesia);
                }
                else
                {
                    al = new ArrayList();
                    al.add(novaFreguesia);
                    mapFreguesias.put(index, al);
                }
            }
            sql.close();
        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a ler Map Distritos" + SQLe.toString());
            mapFreguesias = null;
        }
        return mapFreguesias;
    }

    /*
	 * constroi hashmap em que a key é a chave de um distrito e o object é um arraylist com todas as
	 * freguesias desse distrito
	 */

    public HashMap lerMapDistrFreguesias()
    {
        ResultSet resultado = null;
        HashMap mapFreguesias = new HashMap();
        int chaveFreg, chaveConc, chaveDistr;
        String design;
        Integer index;
        Freguesia novaFreguesia;
        ArrayList al = new ArrayList();
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM Freguesia");
            resultado = sql.executeQuery();

            while (resultado.next())
            {
                chaveFreg = resultado.getInt(1);
                chaveDistr = resultado.getInt(2);
                chaveConc = resultado.getInt(3);
                design = resultado.getString(4);

                index = new Integer(chaveDistr);
                novaFreguesia = new Freguesia(chaveFreg, chaveDistr, chaveConc, design);
                if (mapFreguesias.containsKey(index))
                {
                    al = (ArrayList) mapFreguesias.get(index);
                    al.add(novaFreguesia);
                }
                else
                {
                    al = new ArrayList();
                    al.add(novaFreguesia);
                    mapFreguesias.put(index, al);
                }
            }
            sql.close();
        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a ler Map Distritos-Freguesias" + SQLe.toString());
            mapFreguesias = null;
        }
        return mapFreguesias;
    }

    public ArrayList lerFreguesias()
    {
        ResultSet resultado = null;
        ArrayList listaFreguesias = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM Freguesia");

            resultado = sql.executeQuery();
            listaFreguesias = new ArrayList();

            while (resultado.next())
            {
                listaFreguesias.add(
                    new Freguesia(
                        resultado.getInt(1),
                        resultado.getInt(2),
                        resultado.getInt(3),
                        resultado.getString(4)));
            }
            sql.close();
        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a ler Freguesias" + SQLe.toString());
            listaFreguesias = null;
        }
        return listaFreguesias;
    }

    public ArrayList lerConcelhos()
    {
        ResultSet resultado = null;
        ArrayList listaConcelhos = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM Concelho");

            resultado = sql.executeQuery();
            listaConcelhos = new ArrayList();

            while (resultado.next())
            {
                listaConcelhos.add(
                    new Concelho(resultado.getInt(1), resultado.getInt(2), resultado.getString(3)));
            }
            sql.close();
        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a ler Distritos" + SQLe.toString());
            listaConcelhos = null;
        }
        return listaConcelhos;
    }

    public ArrayList lerDistritos()
    {
        ResultSet resultado = null;
        ArrayList listaDistritos = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM Distrito");

            resultado = sql.executeQuery();
            listaDistritos = new ArrayList();

            while (resultado.next())
            {
                listaDistritos.add(new Distrito(resultado.getInt(1), resultado.getString(2)));
            }
            sql.close();
        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a ler Distritos" + SQLe.toString());
            listaDistritos = null;
        }
        return listaDistritos;
    }

    public ArrayList lerTopWords()
    {
        ResultSet resultado = null;
        ArrayList listaPalavras = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM TopWords");

            resultado = sql.executeQuery();
            listaPalavras = new ArrayList();

            while (resultado.next())
            {
                listaPalavras.add(resultado.getString(1));
            }
            sql.close();

        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro Ler Top Words" + SQLe.toString());
            listaPalavras = null;
        }
        return listaPalavras;
    }

    public HashMap lerAbreviaturas()
    {
        ResultSet resultado = null;
        HashMap listaAbrv = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM Abreviaturas");

            resultado = sql.executeQuery();
            listaAbrv = new HashMap();

            while (resultado.next())
            {
                listaAbrv.put(resultado.getString(1), resultado.getString(2));
            }
            sql.close();

        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro em Ler Abreviaturas" + SQLe.toString());
            listaAbrv = null;
        }
        return listaAbrv;
    }

    public HashMap lerPaisesExpansao()
    {
        ResultSet resultado = null;
        HashMap listaAbrv = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM paisExpansao");

            resultado = sql.executeQuery();
            listaAbrv = new HashMap();

            while (resultado.next())
            {
                listaAbrv.put(resultado.getString(1), resultado.getString(2));
            }
            sql.close();

        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro em Ler Paises Conversão" + SQLe.toString());
            listaAbrv = null;
        }
        return listaAbrv;
    }

    public HashMap lerConversaoLocalDistrito()
    {
        ResultSet resultado = null;
        HashMap listaAbrv = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM conversaoLocalDistrito");

            resultado = sql.executeQuery();
            listaAbrv = new HashMap();

            while (resultado.next())
            {
                listaAbrv.put(resultado.getString(1), resultado.getString(2));
            }
            sql.close();

        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro em Ler Conversão Local -> Distrito" + SQLe.toString());
            listaAbrv = null;
        }
        return listaAbrv;
    }

    public HashMap lerConversaoCidadePais()
    {
        ResultSet resultado = null;
        HashMap listaAbrv = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM conversaoCidadePais");

            resultado = sql.executeQuery();
            listaAbrv = new HashMap();

            while (resultado.next())
            {
                listaAbrv.put(resultado.getString(1), resultado.getString(2));
            }
            sql.close();

        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a Ler conversaoCidadePais" + SQLe.toString());
            listaAbrv = null;
        }
        return listaAbrv;
    }

    public HashMap lerConcelhoExpansao()
    {
        ResultSet resultado = null;
        HashMap listaAbrv = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM concelhoExpansao");

            resultado = sql.executeQuery();
            listaAbrv = new HashMap();

            while (resultado.next())
            {
                listaAbrv.put(resultado.getString(1), resultado.getString(2));
            }
            sql.close();

        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a Ler concelho Expansao" + SQLe.toString());
            listaAbrv = null;
        }
        return listaAbrv;
    }

    public HashMap lerFreguesiaExpansao()
    {
        ResultSet resultado = null;
        HashMap listaAbrv = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM freguesiaExpansao");

            resultado = sql.executeQuery();
            listaAbrv = new HashMap();

            while (resultado.next())
            {
                listaAbrv.put(resultado.getString(1), resultado.getString(2));
            }
            sql.close();

        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a Ler freguesia Expansao" + SQLe.toString());
            listaAbrv = null;
        }
        return listaAbrv;
    }

    public HashMap lerConversaoLocalConcelho()
    {
        ResultSet resultado = null;
        HashMap listaAbrv = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM conversaoLocalConcelho");

            resultado = sql.executeQuery();
            listaAbrv = new HashMap();

            while (resultado.next())
            {
                listaAbrv.put(resultado.getString(1), resultado.getString(2));
            }
            sql.close();

        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a Ler conversaoLocalConcelho" + SQLe.toString());
            listaAbrv = null;
        }
        return listaAbrv;
    }

    public ArrayList lerTodasMoradas()
    {
        ResultSet resultado = null;
        ArrayList listaMoradas = null;
        try
        {
            PreparedStatement sql =
                DB.prepararComando(
                    "SELECT codigoInterno, nacionalidade, distritoNaturalidade, concelhoNaturalidade, freguesiaNaturalidade FROM pessoa order by codigoInterno");
            //localidade, codigoPostal1, codigoPostal2

            resultado = sql.executeQuery();
            listaMoradas = new ArrayList();

            while (resultado.next())
            {
                listaMoradas.add(
                    new Morada(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4),
                        resultado.getString(5)));
            }
            sql.close();

        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a carregar moradas" + SQLe.toString());
            listaMoradas = null;
        }
        return listaMoradas;
    }

    public ArrayList lerPaises()
    {
        ResultSet resultado = null;
        ArrayList listaPaises = null;
        try
        {
            PreparedStatement sql = DB.prepararComando("SELECT * FROM PaisNaturalidade");

            resultado = sql.executeQuery();
            listaPaises = new ArrayList();

            while (resultado.next())
            {
                listaPaises.add(new Pais(resultado.getInt(1), resultado.getString(2)));
            }
            sql.close();
        }
        catch (SQLException SQLe)
        {
            System.out.println("Erro a ler Paises" + SQLe.toString());
            listaPaises = null;
        }
        return listaPaises;
    }

}