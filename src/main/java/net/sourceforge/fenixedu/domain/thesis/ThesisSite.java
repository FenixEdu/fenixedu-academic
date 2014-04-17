package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ThesisSite extends ThesisSite_Base {

    public ThesisSite(Thesis thesis) {
        super();
        setThesis(thesis);
    }

    @Override
    public IGroup getOwner() {
        return new NoOneGroup();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString().with(MultiLanguageString.pt, String.valueOf(getThesis().getExternalId()));
    }

    @Override
    public void delete() {
        setThesis(null);
        super.delete();

    }

    @Deprecated
    public boolean hasThesis() {
        return getThesis() != null;
    }

}
