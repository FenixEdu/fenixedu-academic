/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject;

public class InfoFinalResult extends InfoObject {

    // Média ponderada
    private String averageWeighted;

    // Média simples
    private String averageSimple;

    // Média final
    private String finalAverage;

    //        
    public InfoFinalResult() {
    }

    public InfoFinalResult(String averageWeighted, String averageSimple, String finalAverage) {
        setAverageWeighted(averageWeighted);
        setAverageSimple(averageSimple);
        setFinalAverage(finalAverage);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoFinalResult) {
            InfoFinalResult d = (InfoFinalResult) obj;
            result =
                    (getAverageWeighted().equals(d.getAverageWeighted())) && (getAverageSimple().equals(d.getAverageSimple()))
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