/*
 * Created on 17/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author asnr and scpo
 */
public class EditGrouping {

    protected List run(String executionCourseID, InfoGrouping infoGroupProperties) throws FenixServiceException {
        final Grouping grouping = AbstractDomainObject.fromExternalId(infoGroupProperties.getExternalId());
        if (grouping == null) {
            throw new InvalidArgumentsServiceException();
        }

        grouping.edit(infoGroupProperties.getName(), infoGroupProperties.getEnrolmentBeginDay().getTime(), infoGroupProperties
                .getEnrolmentEndDay().getTime(), infoGroupProperties.getEnrolmentPolicy(), infoGroupProperties
                .getGroupMaximumNumber(), infoGroupProperties.getIdealCapacity(), infoGroupProperties.getMaximumCapacity(),
                infoGroupProperties.getMinimumCapacity(), infoGroupProperties.getProjectDescription(), infoGroupProperties
                        .getShiftType(), infoGroupProperties.getAutomaticEnrolment(), infoGroupProperties
                        .getDifferentiatedCapacity(), infoGroupProperties.getInfoShifts());

        return findEditionErrors(grouping);
    }

    private List findEditionErrors(final Grouping grouping) {

        List<Integer> errors = new ArrayList<Integer>();

        Integer groupMaximumError = testGroupMaximumNumber(grouping);
        if (groupMaximumError != 0) {
            errors.add((groupMaximumError));
        }
        errors.addAll(testMaximumAndMininumCapacity(grouping));

        return errors;
    }

    private List<Integer> testMaximumAndMininumCapacity(final Grouping grouping) {
        Integer[] errors = { 0, 0 };
        List<Integer> result = new ArrayList();

        if (grouping.getMaximumCapacity() == null && grouping.getMinimumCapacity() == null) {
            return result;
        }
        for (final StudentGroup studentGroup : grouping.getStudentGroups()) {
            if (grouping.getMaximumCapacity() != null && studentGroup.getAttendsCount() > grouping.getMaximumCapacity()) {
                errors[0] = -2;
            }
            if (grouping.getMinimumCapacity() != null && studentGroup.getAttendsCount() < grouping.getMinimumCapacity()) {
                errors[1] = -3;
            }
            if (errors[0] != 0 && errors[1] != 0) {
                break;
            }
        }
        if (errors[0] != 0) {
            result.add(errors[0]);
        }
        if (errors[1] != 0) {
            result.add(errors[1]);
        }
        return result;
    }

    private Integer testGroupMaximumNumber(final Grouping grouping) {
        if (grouping.getGroupMaximumNumber() != null) {
            for (final StudentGroup studentGroup : grouping.getStudentGroups()) {
                Integer groupCapacity;
                if (studentGroup.getShift() != null) {
                    if (grouping.getDifferentiatedCapacity()) {
                        groupCapacity = studentGroup.getShift().getShiftGroupingProperties().getCapacity();
                    } else {
                        groupCapacity = grouping.getGroupMaximumNumber();
                    }
                    if (studentGroup.getShift().getAssociatedStudentGroups(grouping).size() > groupCapacity) {
                        return -1;
                    }
                } else if (!grouping.getDifferentiatedCapacity()
                        && grouping.getStudentGroupsCount() > grouping.getGroupMaximumNumber()) {
                    return -1;
                }
            }
        }
        return 0;
    }

    // Service Invokers migrated from Berserk

    private static final EditGrouping serviceInstance = new EditGrouping();

    @Service
    public static List runEditGrouping(String executionCourseID, InfoGrouping infoGroupProperties) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID, infoGroupProperties);
    }

}