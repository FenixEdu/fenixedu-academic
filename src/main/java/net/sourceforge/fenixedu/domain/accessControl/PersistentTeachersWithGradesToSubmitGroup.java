package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.groups.Group;

import com.google.common.base.Supplier;

public class PersistentTeachersWithGradesToSubmitGroup extends PersistentTeachersWithGradesToSubmitGroup_Base {
    protected PersistentTeachersWithGradesToSubmitGroup(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        super();
        init(period, degreeCurricularPlan);
    }

    @Override
    public Group toGroup() {
        return TeachersWithGradesToSubmitGroup.get(getPeriod(), getDegreeCurricularPlan());
    }

    public static PersistentTeachersWithGradesToSubmitGroup getInstance(final ExecutionSemester period,
            final DegreeCurricularPlan degreeCurricularPlan) {
        return getInstance(PersistentTeachersWithGradesToSubmitGroup.class, period, degreeCurricularPlan,
                new Supplier<PersistentTeachersWithGradesToSubmitGroup>() {
                    @Override
                    public PersistentTeachersWithGradesToSubmitGroup get() {
                        return new PersistentTeachersWithGradesToSubmitGroup(period, degreeCurricularPlan);
                    }
                });
    }
}
