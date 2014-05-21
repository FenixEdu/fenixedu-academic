package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry;

import org.fenixedu.bennu.core.groups.Group;

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
        return singleton(() -> Optional.ofNullable(cerimonyInquiry.getGroup()), () -> new PersistentCerimonyInquiryGroup(
                cerimonyInquiry));
    }
}
