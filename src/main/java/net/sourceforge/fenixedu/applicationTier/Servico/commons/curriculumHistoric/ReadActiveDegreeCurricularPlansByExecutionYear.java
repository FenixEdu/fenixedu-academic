/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.AcademicCurriculumsViewAuthorization;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.OperatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ResourceAllocationManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveDegreeCurricularPlansByExecutionYear {

    protected List run(Integer executionYearID) throws FenixServiceException {
        ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(executionYearID);

        List executionDegrees = null;
        if (executionYear != null) {
            executionDegrees = executionYear.getExecutionDegrees();
        }

        if (executionDegrees == null) {
            throw new FenixServiceException("nullDegree");
        }

        List infoDegreeCurricularPlans = (List) CollectionUtils.collect(executionDegrees, new Transformer() {
            @Override
            public Object transform(Object obj) {
                ExecutionDegree cursoExecucao = (ExecutionDegree) obj;
                DegreeCurricularPlan degreeCurricularPlan = cursoExecucao.getDegreeCurricularPlan();
                return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
            }
        });

        return infoDegreeCurricularPlans;
    }

    // Service Invokers migrated from Berserk

    private static final ReadActiveDegreeCurricularPlansByExecutionYear serviceInstance =
            new ReadActiveDegreeCurricularPlansByExecutionYear();

    @Service
    public static List runReadActiveDegreeCurricularPlansByExecutionYear(Integer executionYearID) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionYearID);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionYearID);
            } catch (NotAuthorizedException ex2) {
                try {
                    AcademicCurriculumsViewAuthorization.instance.execute();
                    return serviceInstance.run(executionYearID);
                } catch (NotAuthorizedException ex3) {
                    try {
                        ResourceAllocationManagerAuthorizationFilter.instance.execute();
                        return serviceInstance.run(executionYearID);
                    } catch (NotAuthorizedException ex4) {
                        try {
                            GEPAuthorizationFilter.instance.execute();
                            return serviceInstance.run(executionYearID);
                        } catch (NotAuthorizedException ex5) {
                            try {
                                DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                                EmployeeAuthorizationFilter.instance.execute();
                                return serviceInstance.run(executionYearID);
                            } catch (NotAuthorizedException ex6) {
                                try {
                                    OperatorAuthorizationFilter.instance.execute();
                                    return serviceInstance.run(executionYearID);
                                } catch (NotAuthorizedException ex7) {
                                    throw ex7;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}