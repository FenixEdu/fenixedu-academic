/*
 * Created on 10/Fev/2004
 *  
 */
package Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;

/**
 * @author Tânia Pousão
 *  
 */
public class ExecutionDegreesFormat
{
	public static List buildExecutionDegreeLabelValueBean(List executionDegreeList)
	{
		List executionDegreeLabels = new ArrayList();
		Iterator iterator = executionDegreeList.iterator();
		while (iterator.hasNext())
		{
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

			name =
				infoExecutionDegree
					.getInfoDegreeCurricularPlan()
					.getInfoDegree()
					.getTipoCurso()
					.toString()
					+ " em "
					+ name;

			name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
				? " - " + infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
				: "";

			executionDegreeLabels.add(
				new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString()));
		}
		return executionDegreeLabels;
	}
	
	public static List buildExecutionDegreeLabelValueWithNameBean(List executionDegreeList)
	{
		List executionDegreeLabels = new ArrayList();
		Iterator iterator = executionDegreeList.iterator();
		while (iterator.hasNext())
		{
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

			name =
			infoExecutionDegree
			.getInfoDegreeCurricularPlan()
			.getInfoDegree()
			.getTipoCurso()
			.toString()
			+ " em "
			+ name;

			name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
			? " - " + infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
			: "";

			executionDegreeLabels.add(
					new LabelValueBean(name, name + "~" + infoExecutionDegree.getIdInternal().toString()));
		}
		return executionDegreeLabels;
	}

	private static boolean duplicateInfoDegree(
		List executionDegreeList,
		InfoExecutionDegree infoExecutionDegree)
	{
		InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();

		while (iterator.hasNext())
		{
			InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
			if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
				&& !(infoExecutionDegree.equals(infoExecutionDegree2)))
				return true;

		}
		return false;
	}
}
