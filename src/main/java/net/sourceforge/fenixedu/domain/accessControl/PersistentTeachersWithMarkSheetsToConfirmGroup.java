package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.groups.Group;

import com.google.common.base.Supplier;

public class PersistentTeachersWithMarkSheetsToConfirmGroup extends PersistentTeachersWithMarkSheetsToConfirmGroup_Base {
    protected PersistentTeachersWithMarkSheetsToConfirmGroup(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        super();
        init(period, degreeCurricularPlan);
    }

    @Override
    public Group toGroup() {
        return TeachersWithMarkSheetsToConfirmGroup.get(getPeriod(), getDegreeCurricularPlan());
    }

    public static PersistentTeachersWithMarkSheetsToConfirmGroup getInstance(final ExecutionSemester period,
            final DegreeCurricularPlan degreeCurricularPlan) {
        return getInstance(PersistentTeachersWithMarkSheetsToConfirmGroup.class, period, degreeCurricularPlan,
                new Supplier<PersistentTeachersWithMarkSheetsToConfirmGroup>() {
                    @Override
                    public PersistentTeachersWithMarkSheetsToConfirmGroup get() {
                        return new PersistentTeachersWithMarkSheetsToConfirmGroup(period, degreeCurricularPlan);
                    }
                });
    }
}
