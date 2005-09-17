package net.sourceforge.fenixedu.stm;


public interface CommitListener {
    public void beforeCommit(TopLevelTransaction tx);
    public void afterCommit(TopLevelTransaction tx);
}
