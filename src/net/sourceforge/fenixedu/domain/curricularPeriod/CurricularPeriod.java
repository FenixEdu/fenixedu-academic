package net.sourceforge.fenixedu.domain.curricularPeriod;

import java.util.Arrays;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.CurricularPeriodLabelFormatter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 * 
 */
public class CurricularPeriod extends CurricularPeriod_Base {

    public CurricularPeriod(CurricularPeriodType curricularPeriodType) {
        super();
        setPeriodType(curricularPeriodType);
    }

    public CurricularPeriod(CurricularPeriodType curricularPeriodType, Integer order,
            CurricularPeriod parent) {
        super();
        setPeriodType(curricularPeriodType);
        setOrder(order);
        setParent(parent);

    }

    public CurricularPeriod getChildByOrder(Integer order) {

        for (CurricularPeriod curricularPeriod : getChilds()) {
            if (curricularPeriod.getOrder().equals(order)) {
                return curricularPeriod;
            }
        }

        return null;
    }

    private CurricularPeriod findChild(CurricularPeriodType periodType, Integer order) {

        for (CurricularPeriod curricularPeriod : getChilds()) {
            if (curricularPeriod.getOrder().equals(order) && curricularPeriod.getPeriodType() == periodType) {
                return curricularPeriod;
            }
        }

        return null;
    }

    public CurricularPeriod getCurricularPeriod(CurricularPeriodInfoDTO... curricularPeriodsPaths) {

        validatePath(curricularPeriodsPaths);

        CurricularPeriod curricularPeriod = this;

        for (CurricularPeriodInfoDTO path : curricularPeriodsPaths) {
            curricularPeriod = (CurricularPeriod) curricularPeriod.findChild(path.getPeriodType(), path
                    .getOrder());

            if (curricularPeriod == null) {
                return null;
            }
        }

        return curricularPeriod;
    }

    public CurricularPeriod addCurricularPeriod(CurricularPeriodInfoDTO... curricularPeriodsPaths) {

        validatePath(curricularPeriodsPaths);

        CurricularPeriod curricularPeriod = null;
        CurricularPeriod curricularPeriodParent = this;

        for (CurricularPeriodInfoDTO path : curricularPeriodsPaths) {
            curricularPeriod = (CurricularPeriod) curricularPeriodParent.findChild(path.getPeriodType(),
                    path.getOrder());

            if (curricularPeriod == null) {
                curricularPeriod = new CurricularPeriod(path.getPeriodType(), path.getOrder(),
                        curricularPeriodParent);
            }

            curricularPeriodParent = curricularPeriod;
        }

        return curricularPeriod;
    }

    public Integer getOrderByType(CurricularPeriodType curricularPeriodType) {

        Integer resultOrder = null;

        if (this.getPeriodType() == curricularPeriodType) {
            resultOrder = this.getOrder();
        } else if (this.getParent() != null
                && this.getParent().getPeriodType().getWeight() > this.getPeriodType().getWeight()) {
            resultOrder = ((CurricularPeriod) this.getParent()).getOrderByType(curricularPeriodType);
        }

        return resultOrder;
    }
    
    private void validatePath(CurricularPeriodInfoDTO... curricularPeriodsPaths) {

        Arrays.sort(curricularPeriodsPaths, new Comparator<CurricularPeriodInfoDTO>() {
            public int compare(CurricularPeriodInfoDTO c1, CurricularPeriodInfoDTO c2) {
                if (c1.getPeriodType().getWeight() > c2.getPeriodType().getWeight()) {
                    return -1;
                } else if (c1.getPeriodType().getWeight() < c2.getPeriodType().getWeight()) {
                    return 1;
                }
                throw new DomainException("error.pathShouldNotHaveSameTypePeriods");
            }

        });
    }

    public void delete(){
        
        getContexts().clear();
        removeDegreeCurricularPlan();
        
        for (CurricularPeriod child : getChilds()) {
            child.delete();
        }

        deleteDomainObject();
        
    }

    public String getLabel() {
        return CurricularPeriodLabelFormatter.getLabel(this);
    }
    
    public String getFullLabel() {
        return CurricularPeriodLabelFormatter.getFullLabel(this);
    }
}
