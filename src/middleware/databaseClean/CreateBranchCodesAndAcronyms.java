/*
 * Created on 13/Nov/2003
 */
package middleware.databaseClean;

import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import DataBeans.InfoBranch;
import Dominio.Branch;
import Dominio.IBranch;
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.TipoCurso;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class CreateBranchCodesAndAcronyms
{


    public static void main(String[] args)
    {

        IBranch branch = null;
        String branchCode = null;

        System.out.println("Running CreateBranchCodesAndAcronyms script");
        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
        broker.clearCache();
        broker.beginTransaction();

        Criteria criteria = new Criteria();
        Query query = new QueryByCriteria(Branch.class, criteria);
        List branchList = (List) broker.getCollectionByQuery(query);

        Iterator iterator = branchList.iterator();

        try
        {
            while (iterator.hasNext())
            {

                branch = (Branch) iterator.next();

                if (branch.getName().equals(""))
                {
                    branch.setCode("");
                }
                else
                {
                    if (branchCodeIsInteger(branch))
                        branch = createAndSetBranchAcronym(branch);
                    else
                    {
                        branchCode = generateBranchCode(branch);
                        branch.setAcronym(branch.getCode());
                        branch.setCode(branchCode);
                    }

                }
                broker.store(branch);
                
            }
        }
        catch (PersistenceBrokerException pbe)
        {
            System.out.println("A PersistenceBrokerException has occured when writing branch: ");
            System.out.println(branch);
            pbe.printStackTrace();
        }

        broker.commitTransaction();
        broker.close();
        System.out.println("createBranchCodesAndAcronyms script was runned sucessfuly");
    }

    private static IBranch createAndSetBranchAcronym(IBranch branch)
    {
        InfoBranch infoBranch = new InfoBranch(branch.getName(), branch.getCode());
        String branchAcronym = infoBranch.getPrettyCode();
        branch.setAcronym(branchAcronym);
        return branch;
    }

    private static boolean branchCodeIsInteger(IBranch branch)
    {

        String branchCode = branch.getCode();
        try
        {
            Integer.parseInt(branchCode);
            return true;
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
    }

    private static String generateBranchCode(IBranch branch)
    {
        String code = "";

        ICurso degree = branch.getDegreeCurricularPlan().getDegree();

        if (degree.getTipoCurso().equals(TipoCurso.MESTRADO_OBJ))
            return branch.getCode();
        
        try
        {
            IPersistentMiddlewareSupport pms = PersistentMiddlewareSupportOJB.getInstance();
            IPersistentMWDegreeTranslation pmwdt = pms.getIPersistentMWDegreeTranslation();
			IPersistentMWBranch pmwb = pms.getIPersistentMWBranch();

            MWDegreeTranslation mwDegreeTranslation = pmwdt.readByDegree(degree);

            Integer mwDegreeCode = mwDegreeTranslation.getDegreeCode();
            code += mwDegreeCode.toString();

            MWBranch mwBranch = pmwb.readByDegreeCodeAndBranchName(mwDegreeCode,branch.getName());

            if (mwBranch == null)
                code += "00";
            else
            {
                code += mwBranch.getBranchcode();
                code += mwBranch.getDescription();
            }
        }

        catch (PersistentMiddlewareSupportException pmse)
        {
            System.out.println(
                "An PersistentMiddlewareSupportException occured in method generateBranchCode");
            System.out.println(branch);
        }
        catch (ExcepcaoPersistencia ep)
        {
            System.out.println("An ExcepcaoPersistencia occured in method generateBranchCode");
            System.out.println(branch);
        }
        return code;
    }
}
