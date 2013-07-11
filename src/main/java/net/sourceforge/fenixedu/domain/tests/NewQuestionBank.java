package net.sourceforge.fenixedu.domain.tests;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class NewQuestionBank extends NewQuestionBank_Base {

    public NewQuestionBank() {
        super();
    }

    public NewQuestionBank(Party owner) {
        this();

        this.setOwner(owner);
    }

    @Override
    public NewQuestionBank getQuestionBank() {
        return this;
    }

    @Override
    public void delete() {
        this.setOwner(null);

        super.delete();
    }

    @Deprecated
    public boolean hasOwner() {
        return getOwner() != null;
    }

}
