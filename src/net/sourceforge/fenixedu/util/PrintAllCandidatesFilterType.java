package net.sourceforge.fenixedu.util;
import java.util.List;

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Ricardo Clerigo & Telmo Nabais
 */

public class PrintAllCandidatesFilterType extends FenixUtil {
	
	// filtros iniciais
	private static Object[] filters = {
			new LabelValueBean("Mostrar todos os registos",PrintAllCandidatesFilter.INVALID_FILTER.getName()),
			new LabelValueBean("Especialização",PrintAllCandidatesFilter.FILTERBY_SPECIALIZATION_VALUE.getName()),
			new LabelValueBean("Situação",PrintAllCandidatesFilter.FILTERBY_SITUATION_VALUE.getName()),
			new LabelValueBean("Prentende dar aulas",PrintAllCandidatesFilter.FILTERBY_GIVESCLASSES_VALUE.getName()),
			new LabelValueBean("Não prentende dar aulas",PrintAllCandidatesFilter.FILTERBY_DOESNTGIVESCLASSES_VALUE.getName())
	};

	// filtros a aplicar de especializacao
	private static Object[] specializationFilters = {
			new LabelValueBean("Mestrado",Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE.getName()),
			new LabelValueBean("Especializacao",Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION.getName())
	};
	
	// filtros de situacao
	private static List situationNameFilters = SituationName.toArrayList();
	
	/** retorna a lista dos filtros como labelvaluebeans **/
	public static Object[] getMainFiltersAsList() {
		return filters;
	}
	
	/** retorna a lista dos filtros de especializacao como labelvaluebeans **/
	public static Object[] getSpecializationFiltersAsList() {
		return specializationFilters;
	}

	/** retorna a lista dos filtros de situacao como labelvaluebeans **/
	public static Object[] getSituationNameFiltersAsList() {
		return situationNameFilters.toArray();
	}

	/** retorna o nome do primeiro filtro com base no valor */
	public static String getFilterNameByValue(PrintAllCandidatesFilter filterBy) {
		switch (filterBy) {
		case FILTERBY_SPECIALIZATION_VALUE:
			return "Especialização";
		case FILTERBY_SITUATION_VALUE:
			return "Situação";
		case FILTERBY_DOESNTGIVESCLASSES_VALUE:
			return "Não pretende dar aulas";
		case FILTERBY_GIVESCLASSES_VALUE:
			return"Pretende dar aulas";
		}
		return "Mostrar todos os registos";
	}
}