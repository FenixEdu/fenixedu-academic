package Dominio;

/**
 * @author Tânia Pousão
 *  
 */
public class FinalEvaluation extends Evaluation implements IFinalEvaluation
{

    public boolean equals(Object obj)
    {
        if (obj instanceof FinalEvaluation)
        {
			FinalEvaluation finalEvaluationObj = (FinalEvaluation)obj;
            return this.getIdInternal().equals(finalEvaluationObj.getIdInternal());
        }

        return false;
    }

}
