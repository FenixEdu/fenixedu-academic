/*
 * Created on 28/Mai/2003
 * 
 * To change the template for this generated file go to Window>Preferences>Java>Code Generation>Code and
 * Comments
 */
package middleware.posgrad;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourseScope;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class MigrateDisciplinaArea2FenixScopes
{

    PersistenceBroker broker = null;

    public MigrateDisciplinaArea2FenixScopes()
    {
        broker = PersistenceBrokerFactory.defaultPersistenceBroker();
    }

    public static void main(String args[]) throws Exception
    {
        MigrateDisciplinaArea2FenixScopes migrateDisciplina2Fenix =
            new MigrateDisciplinaArea2FenixScopes();

        //		migrateDisciplina2Fenix.broker.beginTransaction();
        //		migrateDisciplina2Fenix.broker.clearCache();
        migrateDisciplina2Fenix.migrateDisciplinaArea2FenixScopes();

        //		migrateDisciplina2Fenix.broker.commitTransaction();
    }

    public void migrateDisciplinaArea2FenixScopes() throws Exception
    {
        Posgrad_disc_area discArea = null;
        List result = null;
        Query query = null;
        Criteria criteria = null;
        Branch branch = null;
        int scopesWritten = 0;
        int discAreasIgnored = 0;

        try
        {
            System.out.print("Reading PosGrad Disciplina Area ...");

            broker.beginTransaction();
            broker.clearCache();

            List discAreasPG = getDisciplinaArea();

            broker.commitTransaction();

            System.out.println("  Done !");

            System.out.println(
                "Migrating " + discAreasPG.size() + " PosGrad Disciplina Area to Fenix Scopes ...");
            Iterator iterator = discAreasPG.iterator();
            while (iterator.hasNext())
            {
                discArea = (Posgrad_disc_area) iterator.next();

                broker.beginTransaction();
                broker.clearCache();

                // Get the Area Cientifica

                criteria = new Criteria();
                criteria.addEqualTo(
                    "codigointerno",
                    new Integer(String.valueOf(discArea.getCodigoareacientifica())));
                query = new QueryByCriteria(Posgrad_area_cientifica.class, criteria);
                result = (List) broker.getCollectionByQuery(query);

                if (result.size() == 0)
                {
                    throw new Exception(
                        "Error Reading Area Cientifica (" + discArea.getCodigointerno() + ")");
                }

                Posgrad_area_cientifica areaCientifica = (Posgrad_area_cientifica) result.get(0);

                // Skip unwanted Areas Cientificas
                if ((areaCientifica.getCodigocursomestrado() == 15)
                    || (areaCientifica.getCodigocursomestrado() == 31)
                    || (areaCientifica.getCodigocursomestrado() == 50))
                {
                    discAreasIgnored++;
                    broker.commitTransaction();
                    continue;
                }

                if (areaCientifica.getNome().equals("DISCIPLINAS DE ESCOLHA LIVRE")
                    || areaCientifica.getNome().equals("DISCIPLINAS PROPEDÊUTICAS"))
                {

                    // Read the Curso Mestrado

                    criteria = new Criteria();
                    criteria.addEqualTo(
                        "codigoInterno",
                        new Integer(String.valueOf(areaCientifica.getCodigocursomestrado())));
                    query = new QueryByCriteria(Posgrad_curso_mestrado.class, criteria);
                    result = (List) broker.getCollectionByQuery(query);

                    if (result.size() == 0)
                    {
                        throw new Exception(
                            "Error Reading Curso Mestrado (" + discArea.getCodigointerno() + ")");
                    }

                    Posgrad_curso_mestrado posgrad_curso_mestrado =
                        (Posgrad_curso_mestrado) result.get(0);

                    // Get the Degree

                    criteria = new Criteria();
                    criteria.addEqualTo("nome", posgrad_curso_mestrado.getNomemestrado());
                    criteria.addEqualTo("tipoCurso", new Integer(TipoCurso.MESTRADO));
                    query = new QueryByCriteria(Curso.class, criteria);
                    result = (List) broker.getCollectionByQuery(query);

                    if (result.size() == 0)
                    {
                        throw new Exception(
                            "Error Reading Degree (" + discArea.getCodigointerno() + ")");
                    }

                    Curso degree = (Curso) result.get(0);

                    // Get the Degree Curricular Plan

                    criteria = new Criteria();
                    criteria.addEqualTo("degreeKey", degree.getIdInternal());
                    query = new QueryByCriteria(DegreeCurricularPlan.class, criteria);
                    result = (List) broker.getCollectionByQuery(query);

                    if (result.size() == 0)
                    {
                        throw new Exception(
                            "Error Reading Degree Curricular Plan ("
                                + discArea.getCodigointerno()
                                + ")");
                    }

                    DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) result.get(0);

                    // Get the Branch

                    criteria = new Criteria();
                    criteria.addEqualTo("code", "");
                    criteria.addEqualTo("name", "");
                    criteria.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
                    query = new QueryByCriteria(Branch.class, criteria);
                    result = (List) broker.getCollectionByQuery(query);

                    if (result.size() == 0)
                    {
                        throw new Exception(
                            "Error Reading Branch (Curricular Plan Key: "
                                + degreeCurricularPlan.getIdInternal()
                                + ")");
                    }

                    branch = (Branch) result.get(0);

                } else
                {

                    // Get the Corresponding Branch

                    criteria = new Criteria();
                    criteria.addEqualTo(
                        "internalID",
                        new Integer(String.valueOf(areaCientifica.getCodigoInternoRamo())));
                    query = new QueryByCriteria(Branch.class, criteria);
                    result = (List) broker.getCollectionByQuery(query);

                    if (result.size() == 0)
                    {
                        throw new Exception(
                            "Error Reading Branch (" + discArea.getCodigointerno() + ")");
                    }

                    branch = (Branch) result.get(0);

                }
                // Get the Disciplina

                criteria = new Criteria();
                criteria.addEqualTo(
                    "codigointerno",
                    new Integer(String.valueOf(discArea.getCodigodisciplina())));
                query = new QueryByCriteria(Posgrad_disciplina.class, criteria);
                result = (List) broker.getCollectionByQuery(query);

                if (result.size() == 0)
                {
                    throw new Exception(
                        "Error Reading Disciplina (" + discArea.getCodigointerno() + ")");
                }

                Posgrad_disciplina posgrad_disciplina = (Posgrad_disciplina) result.get(0);

                // Skip unwanted Disciplinas
                if ((posgrad_disciplina.getNome().indexOf("CRÉDITOS") != -1)
                    || (posgrad_disciplina.getNome().indexOf("CURRICULAR") != -1))
                {
                    discAreasIgnored++;
                    broker.commitTransaction();
                    continue;
                }

                // Get the Corresponding Curricular Course

                criteria = new Criteria();
                criteria.addEqualTo(
                    "idInternal",
                    new Integer(String.valueOf(posgrad_disciplina.getCodigoCurricularCourse())));
                query = new QueryByCriteria(CurricularCourse.class, criteria);
                result = (List) broker.getCollectionByQuery(query);

                if (result.size() == 0)
                {
                    throw new Exception(
                        "Error Reading Curricular Course (" + discArea.getCodigointerno() + ")");
                }

                CurricularCourse curricularCourse = (CurricularCourse) result.get(0);

                // Get the Curricular Semesters
                criteria = new Criteria();
                criteria.addEqualTo("curricularYearKey", new Integer(1));
                criteria.addEqualTo("semester", new Integer(1));

                //				criteria.addOrCriteria(criteria2);

                query = new QueryByCriteria(CurricularSemester.class, criteria);
                result = (List) broker.getCollectionByQuery(query);

                if (result.size() != 1)
                {
                    throw new Exception("Error Reading Curricular Semester");
                }

                //				Iterator iteratorCurricularSemester = result.iterator();
                //				while(iteratorCurricularSemester.hasNext()) {

                // Create the Scope
                ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
                curricularCourseScope.setBranch(branch);
                curricularCourseScope.setCurricularCourse(curricularCourse);
                
                //			TODO: add sets for begin and end dates and ectsCredits
                scopesWritten++;
                broker.store(curricularCourseScope);
                //				}
                broker.commitTransaction();
            }
            System.out.println(
                scopesWritten + " Scopes written for " + discAreasPG.size() + " Disciplina Area PG");
            System.out.println(" Scopes ignored " + discAreasIgnored);
            System.out.println("  Done !");
        } catch (Exception e)
        {
            System.out.println();
            throw new Exception("Error Migrating Disciplina Area " + discArea, e);

        }
    }

    private List getDisciplinaArea() throws Exception
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("anoLectivo", "2002/2003");
        Query query = new QueryByCriteria(Posgrad_disc_area.class, criteria);
        return (List) broker.getCollectionByQuery(query);
    }

}
