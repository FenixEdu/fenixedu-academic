/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;

public class InfoExecutionDegree extends InfoObject {

    public static final Comparator<InfoExecutionDegree> COMPARATOR_BY_DEGREE_TYPE_AND_NAME =
            new Comparator<InfoExecutionDegree>() {

                @Override
                public int compare(InfoExecutionDegree o1, InfoExecutionDegree o2) {
                    final Degree degree1 = o1.getExecutionDegree().getDegree();
                    final Degree degree2 = o2.getExecutionDegree().getDegree();
                    final int c = degree1.getDegreeType().compareTo(degree2.getDegreeType());
                    return c == 0 ? degree1.getNome().compareTo(degree2.getName()) : c;
                }

            };

    private final ExecutionDegree executionDegreeDomainReference;

    private String qualifiedName;

    private boolean getNextExecutionYear = false;

    public InfoExecutionDegree(final ExecutionDegree executionDegree) {
        executionDegreeDomainReference = executionDegree;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegreeDomainReference;
    }

    public InfoExecutionYear getInfoExecutionYear() {
        return InfoExecutionYear.newInfoFromDomain(getNextExecutionYear ? getExecutionDegree().getExecutionYear()
                .getNextExecutionYear() : getExecutionDegree().getExecutionYear());
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return InfoDegreeCurricularPlan.newInfoFromDomain(getExecutionDegree().getDegreeCurricularPlan());
    }

    public boolean isPublishedExam(ExecutionInterval executionInterval) {
        return true;
    }

    public InfoCampus getInfoCampus() {
        return InfoCampus.newInfoFromDomain(getExecutionDegree().getCampus());
    }

    public List<InfoCoordinator> getCoordinatorsList() {
        final List<InfoCoordinator> infoCoordinators = new ArrayList<InfoCoordinator>();
        for (final Coordinator coordinator : getExecutionDegree().getCoordinatorsListSet()) {
            infoCoordinators.add(InfoCoordinator.newInfoFromDomain(coordinator));
        }
        return infoCoordinators;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getExecutionDegree() == ((InfoExecutionDegree) obj).getExecutionDegree();
    }

    @Override
    public String toString() {
        return getExecutionDegree().toString();
    }

    public static List buildLabelValueBeansForList(List executionDegrees, MessageResources messageResources) {
        List copyExecutionDegrees = new ArrayList();
        copyExecutionDegrees.addAll(executionDegrees);
        List result = new ArrayList();
        Iterator iter = executionDegrees.iterator();
        while (iter.hasNext()) {
            final InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iter.next();
            List equalDegrees = (List) CollectionUtils.select(copyExecutionDegrees, new Predicate() {
                @Override
                public boolean evaluate(Object arg0) {
                    InfoExecutionDegree infoExecutionDegreeElem = (InfoExecutionDegree) arg0;
                    if (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla()
                            .equals(infoExecutionDegreeElem.getInfoDegreeCurricularPlan().getInfoDegree().getSigla())) {
                        return true;
                    }
                    return false;
                }
            });
            if (equalDegrees.size() == 1) {
                copyExecutionDegrees.remove(infoExecutionDegree);

                String degreeType = null;
                if (messageResources != null) {
                    degreeType = messageResources.getMessage(
                            infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString());
                }
                if (degreeType == null) {
                    degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString();
                }

                result.add(new LabelValueBean(
                        degreeType + "  " + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome(),
                        infoExecutionDegree.getExternalId().toString()));
            } else {
                String degreeType = null;
                if (messageResources != null) {
                    degreeType = messageResources.getMessage(
                            infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString());
                }
                if (degreeType == null) {
                    degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString();
                }

                result.add(new LabelValueBean(
                        degreeType + "  " + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome() + " - "
                                + infoExecutionDegree.getInfoDegreeCurricularPlan().getName(),
                        infoExecutionDegree.getExternalId().toString()));
            }
        }
        return result;

    }

    public static InfoExecutionDegree newInfoFromDomain(ExecutionDegree executionDegree) {
        return executionDegree == null ? null : new InfoExecutionDegree(executionDegree);
    }

    @Override
    public String getExternalId() {
        return getExecutionDegree().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public void setGetNextExecutionYear(boolean getNextExecutionYear) {
        this.getNextExecutionYear = getNextExecutionYear;
    }

}
