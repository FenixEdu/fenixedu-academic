package net.sourceforge.fenixedu.domain;

/**
 * @author Tânia Pousão
 *  
 */
public class FinalEvaluation extends FinalEvaluation_Base {

    public FinalEvaluation() {
        this.setOjbConcreteClass(FinalEvaluation.class.getName());
    }

    public boolean equals(Object obj) {
        if (obj instanceof FinalEvaluation) {
            FinalEvaluation finalEvaluationObj = (FinalEvaluation) obj;
            return this.getIdInternal().equals(finalEvaluationObj.getIdInternal());
        }

        return false;
    }

}