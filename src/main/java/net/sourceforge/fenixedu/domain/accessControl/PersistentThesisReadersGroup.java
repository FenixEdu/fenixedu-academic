package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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
        PersistentThesisReadersGroup instance = thesis.getReaders();
        return instance != null ? instance : create(thesis);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentThesisReadersGroup create(Thesis thesis) {
        PersistentThesisReadersGroup instance = thesis.getReaders();
        return instance != null ? instance : new PersistentThesisReadersGroup(thesis);
    }
}
