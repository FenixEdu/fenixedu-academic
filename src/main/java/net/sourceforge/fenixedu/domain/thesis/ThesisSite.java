package net.sourceforge.fenixedu.domain.thesis;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ThesisSite extends ThesisSite_Base {

    public ThesisSite(Thesis thesis) {
        super();
        setThesis(thesis);
    }

    @Override
    public Group getOwner() {
        return NobodyGroup.get();
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
