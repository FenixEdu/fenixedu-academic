package net.sourceforge.fenixedu.domain.curricularRules;

public abstract class Rule extends Rule_Base {

    @Override
    public boolean isLeaf() {
	return true;
    }
}
