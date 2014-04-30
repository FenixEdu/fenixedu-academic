package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class PersistentCerimonyInquiryGroup extends PersistentCerimonyInquiryGroup_Base {
    protected PersistentCerimonyInquiryGroup(CerimonyInquiry cerimonyInquiry) {
        super();
        setCerimonyInquiry(cerimonyInquiry);
    }

    @Override
    public Group toGroup() {
        return CerimonyInquiryGroup.get(getCerimonyInquiry());
    }

    @Override
    protected void gc() {
        setCerimonyInquiry(null);
        super.gc();
    }

    public static PersistentCerimonyInquiryGroup getInstance(CerimonyInquiry cerimonyInquiry) {
        PersistentCerimonyInquiryGroup instance = cerimonyInquiry.getGroup();
        return instance != null ? instance : create(cerimonyInquiry);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentCerimonyInquiryGroup create(CerimonyInquiry cerimonyInquiry) {
        PersistentCerimonyInquiryGroup instance = cerimonyInquiry.getGroup();
        return instance != null ? instance : new PersistentCerimonyInquiryGroup(cerimonyInquiry);
    }

}
