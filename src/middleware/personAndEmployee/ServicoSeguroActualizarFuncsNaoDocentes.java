package middleware.personAndEmployee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import middleware.RoleFunctions;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.FuncNaoDocente;
import Dominio.Funcionario;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.ITeacher;
import Dominio.Teacher;
import Util.RoleType;

/**
 * @author  Ivo Brandão
 */
public class ServicoSeguroActualizarFuncsNaoDocentes
{

    private static String ficheiro = null;
    private static String delimitador;
    private static Hashtable estrutura;
    private static Collection ordem;
    private static Collection lista;

    /** Construtor */
    public ServicoSeguroActualizarFuncsNaoDocentes(String[] args)
    {
        ficheiro = args[0];
        delimitador = new String(";");

        /* Inicializar Hashtable com atributos a recuperar do ficheiro de texto requeridos */
        estrutura = new Hashtable();
        estrutura.put("numeroDocumentoIdentificacao", new Object()); //String
        estrutura.put("tipoDocumentoIdentificacao", new Object()); //int
        estrutura.put("numeroMecanografico", new Object()); //String

        /* Inicializar Collection com ordem dos atributos a recuperar do ficheiro de texto */
        ordem = new ArrayList();
        ordem.add("numeroDocumentoIdentificacao");
        ordem.add("tipoDocumentoIdentificacao");
        ordem.add("numeroMecanografico");
    }

    /** Executa a actualizacao da tabela FuncNaoDocente na Base de Dados */
    public static void main(String[] args) throws Exception
    {
        new ServicoSeguroActualizarFuncsNaoDocentes(args);
        LeituraFicheiroFuncNaoDocente servicoLeitura = new LeituraFicheiroFuncNaoDocente();
        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

        broker.clearCache();

        lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

        System.out.println("Migrating " + lista.size() + " Funcionarios Nao Docentes ... ");

        int newEmployees = 0;
        int newRoles = 0;

        List persons = new ArrayList();
        FuncNaoDocente funcNaoDocente = null;
        Integer numeroMecanografico = null;

        /* ciclo para percorrer a Collection de Funcionarios */
        /* algoritmo */

        /* Procurar chaveFuncionario correspondente e criar funcNaoDocente */
        Iterator iteradorNovo = lista.iterator();

        broker.beginTransaction();
        while (iteradorNovo.hasNext())
        {
            try
            {
                Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

                numeroMecanografico =
                    new Integer((String) instanciaTemporaria.get("numeroMecanografico"));

                Criteria criteria = new Criteria();
                Query query = null;

                // Check if a Employee exists	
                criteria = new Criteria();
                query = null;

                criteria.addEqualTo("numeroMecanografico", numeroMecanografico);
                query = new QueryByCriteria(Funcionario.class, criteria);
                List resultFuncionario = (List) broker.getCollectionByQuery(query);

                if (resultFuncionario.size() == 0)
                {
                    throw new Exception("Error Reading Existing Employee");
                }

                Funcionario funcionario = (Funcionario) resultFuncionario.get(0);

                criteria = new Criteria();
                query = null;

                criteria.addEqualTo("chaveFuncionario", new Integer(funcionario.getCodigoInterno()));
                query = new QueryByCriteria(FuncNaoDocente.class, criteria);
                List resultFuncNaoDocente = (List) broker.getCollectionByQuery(query);

                if (resultFuncNaoDocente.size() == 0)
                {
                    funcNaoDocente = new FuncNaoDocente();
                    funcNaoDocente.setFuncionario(funcionario);
                    broker.store(funcNaoDocente);
                    newEmployees++;

                    IPersonRole personRole =
                        RoleFunctions.readPersonRole(funcionario.getPerson(), RoleType.EMPLOYEE, broker);
                    if (personRole == null)
                    {
                        RoleFunctions.giveRole(funcionario.getPerson(), RoleType.EMPLOYEE, broker);
                        newRoles++;
                    }
                } else if (resultFuncNaoDocente.size() > 0)
                {
                    funcNaoDocente = (FuncNaoDocente) resultFuncNaoDocente.get(0);
                    if (!funcNaoDocente.getFuncionario().equals(funcionario))
                    {
                        funcNaoDocente.setFuncionario(funcionario);
                    }
                    broker.store(funcNaoDocente);
                }

                persons.add(funcNaoDocente.getFuncionario().getPerson());
            } catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Error migrating employee " + numeroMecanografico + "\n");
                continue;
                //broker.commitTransaction();
                //throw new Exception("Error migrating employee " + numeroMecanografico + "\n" + e);
            }
        }
        broker.commitTransaction();

        broker.beginTransaction();
        try
        {
            Iterator iterator = persons.listIterator();
            while (iterator.hasNext())
            {
                IPessoa pessoa = (IPessoa) iterator.next();

                Criteria criteria = new Criteria();
                Query query = null;

                criteria.addEqualTo("keyPerson", pessoa.getIdInternal());
                query = new QueryByCriteria(Teacher.class, criteria);
                List resultTeacher = (List) broker.getCollectionByQuery(query);
                Iterator iterator2 = resultTeacher.listIterator();
                while (iterator2.hasNext())
                {
                    ITeacher teacher = (ITeacher) iterator2.next();
                    broker.delete(teacher);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace(System.out);
        }

        broker.commitTransaction();

        System.out.println("New Funcionarios Nao Docentes added : " + newEmployees);
        System.out.println("New Roles added : " + newRoles);
        System.out.println("  Done !");
    }
}