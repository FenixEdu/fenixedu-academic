
/*
 * Created on 3/Dez/2003
 *
 */
 
package middleware.studentMigration.ileecDataMigration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAreaEspecializacaoIleec;
import middleware.middlewareDomain.MWAreaSecundariaIleec;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWDisciplinaIleec;
import middleware.middlewareDomain.MWGrupoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaEspecializacaoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaSecundariaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWGrupoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseGroup;
import Dominio.IBranch;
import Dominio.ICurricularCourseGroup;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;
import Util.DegreeCurricularPlanState;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class migrateILeecGroupsData
{

	private static int totalBranchesUpdated = 0;
	private static int totalCurricularCourseGroupsCreated = 0;
	private static int totalCurricularCourseGroupsScopesCreated = 0;
	private static Integer ileecDegreeCode = new Integer(14);

	public static void main(String[] args)
	{
		try
		{
			System.out.println("[INFO] Running migrateILeecGroupsData script");

			PersistentMiddlewareSupportOJB persistentMiddlewareSupportOJB =
				PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWGrupoIleec pMWGroups =
				persistentMiddlewareSupportOJB.getIPersistentMWGruposILeec();
			IPersistentMWAreaEspecializacaoIleec pMWAreaEspecializacao =
				persistentMiddlewareSupportOJB.getIPersistentMWAreasEspecializacaoIleec();
			IPersistentMWAreaSecundariaIleec pMWAreaSecundaria =
				persistentMiddlewareSupportOJB.getIPersistentMWAreaSecundariaIleec();

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentBranch pBranch = sp.getIPersistentBranch();

			IPersistentCurricularCourseGroup pccg = sp.getIPersistentCurricularCourseGroup();

			IDegreeCurricularPlan dcp = null;

			sp.iniciarTransaccao();

			System.out.println("[INFO] Reading ILeec Groups...");
			List groups = pMWGroups.readAll();
			Iterator groupsIterator = groups.iterator();

			System.out.println(
				"[INFO] Total number of CurricularCourseGroups to create [" + groups.size() + "].");
			String nomeArea;
			Integer credits;

			while (groupsIterator.hasNext())
			{
				MWGrupoIleec mwgl = (MWGrupoIleec) groupsIterator.next();

				Integer areaId = mwgl.getIdAreaEspecializacao();
				AreaType areaType;

				if (areaId.intValue() != 0)
				{
					MWAreaEspecializacaoIleec mwae =
						pMWAreaEspecializacao.readSpecializationAreaById(areaId);

					nomeArea = mwae.getNome();

					areaType = AreaType.SPECIALIZATION_OBJ;
					credits = mwae.getMaxCreditos();

				}
				else
				{
					areaId = mwgl.getIdAreaSecundaria();
					MWAreaSecundariaIleec mwas = pMWAreaSecundaria.readSecondaryAreaById(areaId);
					nomeArea = mwas.getNome();

					areaType = AreaType.SECONDARY_OBJ;
					credits = mwas.getMaxCreditos();
				}

				dcp = getDegreeCurricularPlan(ileecDegreeCode, sp);

				IBranch branch = pBranch.readByDegreeCurricularPlanAndBranchName(dcp, nomeArea);

				setBranchCredits(sp, branch, areaType, credits);

				ICurricularCourseGroup ccg = new CurricularCourseGroup();
				ccg.setBranch(branch);
				ccg.setMaximumCredits(mwgl.getMaxCreditos());
				ccg.setMinimumCredits(mwgl.getMinCreditos());
				ccg.setAreaType(areaType);

				ccg.setIdInternal(mwgl.getIdGrupo());
				pccg.lockWrite(ccg);
				totalCurricularCourseGroupsCreated++;
			}
			sp.confirmarTransaccao();

			createRelationBetweenScopesAndGroups();

			System.out.println("[INFO] DONE!");
			System.out.println("[INFO] Total Branches updated: [" + totalBranchesUpdated + "].");
			System.out.println(
				"[INFO] Total CurricularCourseGroups created: ["
					+ totalCurricularCourseGroupsCreated
					+ "].");
			System.out.println(
				"[INFO] Total relations between CCGroups and CCScopes created: ["
					+ totalCurricularCourseGroupsScopesCreated
					+ "].");

		}
		catch (ExcepcaoPersistencia ex)
		{
			ex.printStackTrace();

		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	private static void createRelationBetweenScopesAndGroups()
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourseGroup pCCGroup = sp.getIPersistentCurricularCourseGroup();

			sp.iniciarTransaccao();
			PersistentMiddlewareSupportOJB pmws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWGrupoIleec pMWGroups = pmws.getIPersistentMWGruposILeec();

			List listIleecGroups = pMWGroups.readAll();
			Iterator iterator = listIleecGroups.iterator();

			while (iterator.hasNext())
			{
				MWGrupoIleec mwg = (MWGrupoIleec) iterator.next();
				List listCurricularCourses =
					getFenixCurricularCoursesWithDegreeCurricularPlan(mwg, sp, pmws);

//				List listCurricularCourseScopes = getCurricularCourseScopes(listCurricularCourses, sp);

				ICurricularCourseGroup curricularCourseGroup = new CurricularCourseGroup();
				curricularCourseGroup.setIdInternal(mwg.getIdGrupo());
				ICurricularCourseGroup fenixGroup = (ICurricularCourseGroup) pCCGroup.readByOId(curricularCourseGroup, true);
				//fenixGroup.getCurricularCourseScopes().clear();
				//fenixGroup.getCurricularCourseScopes().addAll(listCurricularCourseScopes);

				fenixGroup.setCurricularCourses(listCurricularCourses);
				totalCurricularCourseGroupsScopesCreated += listCurricularCourses.size();
			}

			sp.confirmarTransaccao();

		}
		catch (Throwable e)
		{
			System.out.println(
				"[ERROR] Creating the relations between the curricular course scopes and the curricular course groups");
			e.printStackTrace();
		}

	}

	/**
	* @param degreeCode
	* @param fenixPersistentSuport
	* @return
	* @throws Throwable
	*/
	private static IDegreeCurricularPlan getDegreeCurricularPlan(
		Integer degreeCode,
		ISuportePersistente fenixPersistentSuport)
		throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation =
			mws.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
			fenixPersistentSuport.getIPersistentDegreeCurricularPlan();
		MWDegreeTranslation mwDegreeTranslation =
			persistentMWDegreeTranslation.readByDegreeCode(degreeCode);
		if (mwDegreeTranslation != null)
		{
			ICurso degree = mwDegreeTranslation.getDegree();
			List result =
				persistentDegreeCurricularPlan.readByDegreeAndState(
					degree,
					DegreeCurricularPlanState.ACTIVE_OBJ);
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) result.get(0);
			return degreeCurricularPlan;
		}
		else
		{
			return null;
		}
	}
	/**
	 * 
	 * @param sp
	 * @param branch
	 * @param areaType
	 * @param credits
	 * @throws ExcepcaoPersistencia
	 */
	private static void setBranchCredits(
		ISuportePersistente sp,
		IBranch branch,
		AreaType areaType,
		Integer credits)
		throws ExcepcaoPersistencia
	{
		IPersistentBranch pb = sp.getIPersistentBranch();
		if (areaType.equals(AreaType.SPECIALIZATION_OBJ))
			branch.setSpecializationCredits(credits);
		else
			branch.setSecondaryCredits(credits);
		totalBranchesUpdated++;
		pb.lockWrite(branch);
	}

	/**
	 * 
	 * @param groupIleec
	 * @param dcp
	 * @param sp
	 * @param pmwsOJB
	 * @return
	 */
	private static List getFenixCurricularCoursesWithDegreeCurricularPlan(
		MWGrupoIleec groupIleec,
		ISuportePersistente sp,
		PersistentMiddlewareSupportOJB pmwsOJB)
	{
		ArrayList listCurricularCourses = new ArrayList();

		IDegreeCurricularPlan dcp = null;
		try
		{
			dcp = getDegreeCurricularPlan(ileecDegreeCode, sp);
		}
		catch (Throwable e)
		{
			System.out.println("[ERROR] Could not obtain ILEEC degree curricular plan");
			e.printStackTrace();
		}
		IPersistentCurricularCourse pCurricularCourse = sp.getIPersistentCurricularCourse();
		IPersistentMWDisciplinaIleec pCourseIleec = pmwsOJB.getIPersistentMWDisciplinaIleec();

		List listCoursesIleec = null;
		try
		{
			listCoursesIleec = pCourseIleec.readByGroup(groupIleec);
		}
		catch (ExcepcaoPersistencia e1)
		{
			System.out.println("[ERROR] An ExcepcaoPersistencia occured when obtaining ILEEC groups");
			e1.printStackTrace();
		}
		catch (PersistentMiddlewareSupportException e1)
		{
			System.out.println(
				"[ERROR] An PersistentMiddlewareSupportException occured when obtaining ILEEC groups");
			e1.printStackTrace();
		}

		Iterator iterator = listCoursesIleec.iterator();
		while (iterator.hasNext())
		{
			MWDisciplinaIleec mwCourseIleec = (MWDisciplinaIleec) iterator.next();

			List listCC = null;
			try
			{
				listCC =
					pCurricularCourse.readbyCourseCodeAndDegreeCurricularPlan(
						mwCourseIleec.getCodigoDisciplina(),
						dcp);
			}
			catch (ExcepcaoPersistencia e2)
			{
				System.out.println("[ERROR] An ExcepcaoPersistencia occured when obtaining curricular course");
				e2.printStackTrace();
			}
			if (listCC.size() > 1)
			{
				System.out.println(
					"[ERROR] Duplicate entry with code "
						+ mwCourseIleec.getCodigoDisciplina()
						+ " and DegreeCurricularPlan  "
						+ dcp);
			}

			if (!listCC.isEmpty())
			{
				CurricularCourse curricularCourse = (CurricularCourse) listCC.get(0);
				listCurricularCourses.add(curricularCourse);
			}

		}

		return listCurricularCourses;
	}

	/**
	 * 
	 * @param listCurricularCourses
	 * @param sp
	 * @return
	 */
	public static List getCurricularCourseScopes(List listCurricularCourses, ISuportePersistente sp)
	{

		ArrayList listCurricularCourseScopes = new ArrayList();
		IPersistentCurricularCourseScope pCCScope = sp.getIPersistentCurricularCourseScope();
		try
		{

			Iterator iterator = listCurricularCourses.iterator();
			while (iterator.hasNext())
			{
				CurricularCourse curricularCourse = (CurricularCourse) iterator.next();
				listCurricularCourseScopes.addAll(
					pCCScope.readCurricularCourseScopesByCurricularCourse(curricularCourse));
			}

		}
		catch (ExcepcaoPersistencia e)
		{
			System.out.println(
				"[ERROR] An ExcepcaoPersistencia occured in reading the Curricular Courses Scopes");
			e.printStackTrace();
		}

		return listCurricularCourseScopes;
	}

}
