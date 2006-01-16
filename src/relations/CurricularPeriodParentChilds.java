package relations;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CurricularPeriodParentChilds extends CurricularPeriodParentChilds_Base {

    public static void add(CurricularPeriod child, CurricularPeriod parent) {

        if (child.getPeriodType().getWeight() >= parent.getPeriodType().getWeight()) {
            throw new DomainException("error.childTypeGreaterThanParentType");
        }

        float childsWeight = child.getPeriodType().getWeight();
        for (CurricularPeriod period : parent.getChilds()) {
            childsWeight += period.getPeriodType().getWeight();
        }

        if (childsWeight > parent.getPeriodType().getWeight()) {
            throw new DomainException("error.childWeightOutOfLimit");
        }

        // re-order childs
        Integer order = child.getOrder();
        if (order == null) {
            child.setOrder(parent.getChildsCount() + 1);
        } else {
            if (parent.getChildByOrder(order) != null) {
                throw new DomainException("error.childAlreadyExists");
            }
        }

        CurricularPeriodParentChilds_Base.add(child, parent);

    }

}
