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
import Dominio.Role;
import Dominio.Teacher;
import Util.LeituraFicheiroFuncDocente;
import Util.RoleType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ServicoSeguroActualizarFuncsDocentes
{

    private static String ficheiro = null;
    private static String delimitador;
    private static Hashtable estrutura;
    private static Collection ordem;
    private static Collection lista;

    /** Construtor */
    public ServicoSeguroActualizarFuncsDocentes(String[] args)
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
        new ServicoSeguroActualizarFuncsDocentes(args);
        LeituraFicheiroFuncDocente servicoLeitura = new LeituraFicheiroFuncDocente();
        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
        broker.clearCache();

        lista = servicoLeitura.lerFicheiro(ficheiro, delimitador, estrutura, ordem);

        /* ciclo para percorrer a Collection de Funcionarios */
        /* algoritmo */
        System.out.println("A converter " + lista.size() + " Docentes ... ");
        int newTeachers = 0;
        int newRoles = 0;

        List persons = new ArrayList();
        /* Procurar chaveFuncionario correspondente e criar funcNaoDocente */
        Iterator iteradorNovo = lista.iterator();
        Integer numeroMecanografico = null;

        broker.beginTransaction();
        while (iteradorNovo.hasNext())
        {
            try
            {

                Hashtable instanciaTemporaria = (Hashtable) iteradorNovo.next();

                numeroMecanografico =
                    new Integer((String) instanciaTemporaria.get("numeroMecanografico"));

                //Read The Employee
                Criteria criteria = new Criteria();
                Query query = null;
                Funcionario funcionario = getFuncionario(broker, numeroMecanografico, criteria);

                // Check if Teacher Exists
                criteria = new Criteria();
                query = null;
                criteria.addEqualTo("teacherNumber", numeroMecanografico);
                //criteria.addEqualTo("keyPerson", person.getIdInternal());
                query = new QueryByCriteria(Teacher.class, criteria);
                List resultTeacher = (List) broker.getCollectionByQuery(query);

                if (funcionario == null && resultTeacher.size() == 0)
                {
                    throw new Exception(
                        "Não encontro o funcionário para o professor " + numeroMecanografico);
                }
                if (funcionario == null && resultTeacher.size() != 0)
                {
                    throw new Exception(
                        "Erro ao Ler o Funcionario "
                            + numeroMecanografico
                            + " do docente "
                            + ((ITeacher) resultTeacher.get(0)).getTeacherNumber());
                }

                ITeacher teacher = null;
                if (resultTeacher.size() == 0)
                {
                    teacher = new Teacher();
                    teacher.setPerson(funcionario.getPerson());
                    teacher.setTeacherNumber(numeroMecanografico);

                    newTeachers++;
                } else if (resultTeacher.size() == 1)
                {
                    teacher = (ITeacher) resultTeacher.get(0);
                    if ((teacher.getPerson() == null)
                        || (!teacher.getPerson().equals(funcionario.getPerson())))
                    {
                        teacher.setPerson(funcionario.getPerson());
                    }
                } else
                {
                    System.out.println(
                        "\nMais que um professor com o numero mecanografico 	"
                            + numeroMecanografico
                            + "\n");
                    continue;
                }
                broker.store(teacher);

                IPersonRole personRole =
                    RoleFunctions.readPersonRole(teacher.getPerson(), RoleType.TEACHER, broker);
                if (personRole == null)
                {
                    criteria = new Criteria();
                    criteria.addEqualTo("roleType", RoleType.TEACHER);

                    query = new QueryByCriteria(Role.class, criteria);
                    List result = (List) broker.getCollectionByQuery(query);

                    if (result.size() == 0)
                    {
                        throw new Exception("Unknown Role !!!");
                    } else
                    {
                        teacher.getPerson().getPersonRoles().add(result.get(0));
                    }
                    newRoles++;
                }

                teacher.getPerson().setUsername("D" + numeroMecanografico);
                broker.store(teacher.getPerson());

                persons.add(teacher.getPerson());
            } catch (Exception e)
            {
                System.out.println("\nError Migrating Employee " + numeroMecanografico + "\n");
                e.printStackTrace(System.out);
            }
        }
        broker.commitTransaction();

        broker.beginTransaction();
        Iterator iterator = persons.listIterator();
        while (iterator.hasNext())
        {
            IPessoa pessoa = (IPessoa) iterator.next();

            Criteria criteria = new Criteria();
            Query query = null;

            criteria.addEqualTo("funcionario.person.idInternal", pessoa.getIdInternal());
            query = new QueryByCriteria(FuncNaoDocente.class, criteria);
            List resultfuncionario = (List) broker.getCollectionByQuery(query);

            Iterator iterator2 = resultfuncionario.listIterator();
            while (iterator2.hasNext())
            {
                FuncNaoDocente funcNaoDocente = (FuncNaoDocente) iterator2.next();
                broker.delete(funcNaoDocente);
            }
        }
        broker.commitTransaction();

        System.out.println("New Teachers added : " + newTeachers);
        System.out.println("New Roles added : " + newRoles);
        System.out.println("  Done !");

    }

    private static Funcionario getFuncionario(
        PersistenceBroker broker,
        Integer numeroMecanografico,
        Criteria criteria)
        throws Exception
    {
        Query query;
        criteria.addEqualTo("numeroMecanografico", numeroMecanografico);
        query = new QueryByCriteria(Funcionario.class, criteria);
        List resultFuncionario = (List) broker.getCollectionByQuery(query);

        if (resultFuncionario.size() == 0)
        {
            return null;
            //throw new Exception("Error Reading Existing Employee " + numeroMecanografico);
        }
        return (Funcionario) resultFuncionario.get(0);
    }
}