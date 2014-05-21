package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentThesisReadersGroup extends PersistentThesisReadersGroup_Base {
    protected PersistentThesisReadersGroup(Thesis thesis) {
        super();
        setThesis(thesis);
    }

    @Override
    public Group toGroup() {
        return ThesisReadersGroup.get(getThesis());
    }

    @Override
    protected void gc() {
        setThesis(null);
        super.gc();
    }

    public static PersistentThesisReadersGroup getInstance(Thesis thesis) {
        return singleton(() -> Optional.ofNullable(thesis.getReaders()), () -> new PersistentThesisReadersGroup(thesis));
    }
}
