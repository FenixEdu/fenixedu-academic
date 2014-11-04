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
/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.GratuityValues;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoGratuityValuesWithInfoExecutionDegree extends InfoGratuityValues {

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues#copyFromDomain
     * (Dominio.GratuityValues)
     */
    @Override
    public void copyFromDomain(GratuityValues gratuityValues) {
        super.copyFromDomain(gratuityValues);
        if (gratuityValues != null) {
            setInfoExecutionDegree(InfoExecutionDegree.newInfoFromDomain(gratuityValues.getExecutionDegree()));
        }
    }

    public static InfoGratuityValues newInfoFromDomain(GratuityValues gratuityValues) {
        InfoGratuityValuesWithInfoExecutionDegree infoGratuityValues = null;
        if (gratuityValues != null) {
            infoGratuityValues = new InfoGratuityValuesWithInfoExecutionDegree();
            infoGratuityValues.copyFromDomain(gratuityValues);
        }
        return infoGratuityValues;
    }
}