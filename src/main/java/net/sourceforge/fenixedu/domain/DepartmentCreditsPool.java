package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DepartmentCreditsPool extends DepartmentCreditsPool_Base {

    public DepartmentCreditsPool() {
        super();
    }

    public DepartmentCreditsPool(Department department, ExecutionYear executionYear, BigDecimal originalCreditsPool,
            BigDecimal creditsPool) {
        super();
        if (department == null || executionYear == null) {
            throw new DomainException("arguments can't be null");
        }
        setDepartment(department);
        setExecutionYear(executionYear);
        if (originalCreditsPool == null) {
            originalCreditsPool = BigDecimal.ZERO;
        }
        setOriginalCreditsPool(originalCreditsPool);
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
    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasOriginalCreditsPool() {
        return getOriginalCreditsPool() != null;
    }

    @Deprecated
    public boolean hasCreditsPool() {
        return getCreditsPool() != null;
    }

    @Deprecated
    public boolean hasDepartment() {
        return getDepartment() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
