package DataBeans;

public class InfoFinalResult extends InfoObject {

    //	Média ponderada
    private String averageWeighted;

    //  Média simples
    private String averageSimple;

    //  Média final
    private String finalAverage;

    //        
    public InfoFinalResult() {
    }

    public InfoFinalResult(String averageWeighted, String averageSimple, String finalAverage) {
        setAverageWeighted(averageWeighted);
        setAverageSimple(averageSimple);
        setFinalAverage(finalAverage);
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoFinalResult) {
            InfoFinalResult d = (InfoFinalResult) obj;
            result = (getAverageWeighted().equals(d.getAverageWeighted()))
                    && (getAverageSimple().equals(d.getAverageSimple()))
                    && (getFinalAverage().equals(d.getFinalAverage()));
        }
        return result;
    }

    /**
     * @return
     */
    public String getAverageSimple() {
        return averageSimple;
    }

    /**
     * @return
     */
    public String getAverageWeighted() {
        return averageWeighted;
    }

    /**
     * @return
     */
    public String getFinalAverage() {
        return finalAverage;
    }

    /**
     * @param string
     */
    public void setAverageSimple(String string) {
        averageSimple = string;
    }

    /**
     * @param string
     */
    public void setAverageWeighted(String string) {
        averageWeighted = string;
    }

    /**
     * @param string
     */
    public void setFinalAverage(String string) {
        finalAverage = string;
    }

}