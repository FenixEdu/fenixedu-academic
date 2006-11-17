package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;

public abstract class DepartmentByExecutionYearGroup extends LeafGroup {

    private DomainReference<ExecutionYear> executionYearReference;

    private DomainReference<Department> departmentReference;

    public DepartmentByExecutionYearGroup(ExecutionYear executionYear, Department department) {

        this.executionYearReference = new DomainReference<ExecutionYear>(executionYear);
        this.departmentReference = new DomainReference<Department>(department);

    }

    public Department getDepartment() {
        return this.departmentReference.getObject();
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYearReference.getObject();
    }
    
    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] {
                new StaticArgument(getExecutionYear().getYear()),
                new StaticArgument(getDepartment().getName())
        };
    }
    
    public static abstract class Builder implements GroupBuilder {
        
        public Group build(Object[] arguments) {
            String yearName;
            String departmentName;
            
            try {
                yearName = (String) arguments[0];
            }
            catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(0, String.class, arguments[0].getClass());
            }
            
            try {
                departmentName = (String) arguments[1];
            }
            catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(1, String.class, arguments[1].getClass());
            }
            
            ExecutionYear year = ExecutionYear.readExecutionYearByName(yearName);
            if (year == null) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.departmentByExecutionYear.year.notFound", yearName);
            }
            
            Department department = Department.readByName(departmentName);
            if (department == null) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.departmentByExecutionYear.department.notFound", yearName);
            }
            
            return buildConcreteGroup(year, department);
        }

        protected abstract DepartmentByExecutionYearGroup buildConcreteGroup(ExecutionYear year, Department department);

        public int getMinArguments() {
            return 2;
        }

        public int getMaxArguments() {
            return 2;
        }
        
    }
}
