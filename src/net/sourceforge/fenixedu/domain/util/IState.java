package net.sourceforge.fenixedu.domain.util;

public interface IState {
    public void nextState();
    
    public void checkConditionsToForward();

}
