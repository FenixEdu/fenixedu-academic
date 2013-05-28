package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DepartmentCreditsPool extends DepartmentCreditsPool_Base {

    public DepartmentCreditsPool() {
        super();
    }

    public DepartmentCreditsPool(Department department, ExecutionYear executionYear, BigDecimal creditsPool) {
        super();
        if (department == null || executionYear == null) {
            throw new DomainException("arguments can't be null");
        }
        setDepartment(department);
        setExecutionYear(executionYear);
        if (creditsPool == null) {
            creditsPool = BigDecimal.ZERO;
        }
        setCreditsPool(creditsPool);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public static DepartmentCreditsPool getDepartmentCreditsPool(Department department, ExecutionYear executionYear) {
        for (DepartmentCreditsPool departmentCreditsPool : executionYear.getDepartmentCreditsPools()) {
            if (departmentCreditsPool.getDepartment().equals(department)) {
                return departmentCreditsPool;
            }
        }
        return null;
    }
}
