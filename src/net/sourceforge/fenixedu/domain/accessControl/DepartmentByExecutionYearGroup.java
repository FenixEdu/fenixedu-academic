package net.sourceforge.fenixedu.domain.accessControl;

import java.lang.ref.SoftReference;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;

public abstract class DepartmentByExecutionYearGroup extends LeafGroup {

	private String executionYear;
	private String department;

	public DepartmentByExecutionYearGroup(String executionYearName, String departmentName) {
		this.executionYear = executionYearName;
		this.department = departmentName;
	}

	public DepartmentByExecutionYearGroup(ExecutionYear executionYear, Department department) {
		this(executionYear.getName(), department.getName());
	}

	private transient SoftReference<Department> departmentRef = null;

	public Department getDepartment() {
		Department result = departmentRef == null ? null : departmentRef.get();
		if (result == null) {
			synchronized (department) {
				result = departmentRef == null ? null : departmentRef.get();
				if (result == null) {
					result = Department.readByName(department);
					departmentRef = new SoftReference<Department>(result);
				}
			}
		}
		return result;
	}

	private transient SoftReference<ExecutionYear> executionYearRef = null;

	public ExecutionYear getExecutionYear() {
		ExecutionYear result = executionYearRef == null ? null : executionYearRef.get();
		if (result == null) {
			synchronized (executionYear) {
				result = executionYearRef == null ? null : executionYearRef.get();
				if (result == null) {
					result = ExecutionYear.readExecutionYearByName(this.executionYear);
					executionYearRef = new SoftReference<ExecutionYear>(result);
				}
			}
		}
		return result;
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return new Argument[] { new StaticArgument(getExecutionYear().getYear()), new StaticArgument(getDepartment().getName()) };
	}

	public static abstract class Builder implements GroupBuilder {

		@Override
		public Group build(Object[] arguments) {
			String yearName;
			String departmentName;

			try {
				yearName = (String) arguments[0];
			} catch (ClassCastException e) {
				throw new WrongTypeOfArgumentException(0, String.class, arguments[0].getClass());
			}

			try {
				departmentName = (String) arguments[1];
			} catch (ClassCastException e) {
				throw new WrongTypeOfArgumentException(1, String.class, arguments[1].getClass());
			}

			return buildConcreteGroup(yearName, departmentName);
		}

		protected abstract DepartmentByExecutionYearGroup buildConcreteGroup(String year, String department);

		@Override
		public int getMinArguments() {
			return 2;
		}

		@Override
		public int getMaxArguments() {
			return 2;
		}

	}
}
