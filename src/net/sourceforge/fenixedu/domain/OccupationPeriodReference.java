package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import com.google.common.base.Function;

public class OccupationPeriodReference extends OccupationPeriodReference_Base {

	public static Function<OccupationPeriodReference, OccupationPeriod> FUNCTION_TO_PERIOD =
			new Function<OccupationPeriodReference, OccupationPeriod>() {

				@Override
				public OccupationPeriod apply(OccupationPeriodReference reference) {
					return reference.getOccupationPeriod();
				}

			};

	public static Function<OccupationPeriodReference, ExecutionDegree> FUNCTION_TO_DEGREE =
			new Function<OccupationPeriodReference, ExecutionDegree>() {

				@Override
				public ExecutionDegree apply(OccupationPeriodReference reference) {
					return reference.getExecutionDegree();
				}

			};

	private OccupationPeriodReference() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public OccupationPeriodReference(OccupationPeriod period, ExecutionDegree degree, OccupationPeriodType type,
			Integer semester, CurricularYearList curricularYears) {
		this();
		if (period == null || degree == null) {
			throw new DomainException("exception.null.arguments");
		}
		setOccupationPeriod(period);
		setExecutionDegree(degree);
		setPeriodType(type);
		setSemester(semester);
		setCurricularYears(curricularYears);
	}

	public void delete() {
		removeOccupationPeriod();
		removeExecutionDegree();
		removeRootDomainObject();

		deleteDomainObject();
	}

	public String getCurricularYearsString() {
		CurricularYearList years = getCurricularYears();
		if (years == null) {
			return "-1";
		}

		List<Integer> yearList = years.getYears();

		StringBuilder returnStr = new StringBuilder();

		for (Integer year : yearList) {
			if (returnStr.length() > 0) {
				returnStr.append(",");
			}

			returnStr.append(year);
		}

		return returnStr.toString();
	}

	public String getCurricularYearsPresentationString() {
		CurricularYearList years = getCurricularYears();
		if (years == null || years.hasAll()) {
			return "Todos os anos";
		}

		List<Integer> yearList = years.getYears();

		StringBuilder returnStr = new StringBuilder();

		for (Integer year : yearList) {
			if (returnStr.length() > 0) {
				returnStr.append(", ");
			}

			returnStr.append(year + "º");
		}

		if (yearList.size() > 1) {
			returnStr.append(" Anos");
		} else {
			returnStr.append(" Ano");
		}

		return returnStr.toString();
	}

}
