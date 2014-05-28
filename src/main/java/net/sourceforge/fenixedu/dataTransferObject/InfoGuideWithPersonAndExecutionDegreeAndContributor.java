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

import java.util.List;

import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.GuideSituation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoGuideWithPersonAndExecutionDegreeAndContributor extends InfoGuide {

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoGuide#copyFromDomain(
     * Dominio.Guide)
     */
    @Override
    public void copyFromDomain(Guide guide) {
        super.copyFromDomain(guide);
        if (guide != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(guide.getPerson()));
            setInfoExecutionDegree(InfoExecutionDegree.newInfoFromDomain(guide.getExecutionDegree()));
            setInfoContributor(InfoContributor.newInfoFromDomain(guide.getContributorParty()));
            setInfoGuideSituation(InfoGuideSituation.newInfoFromDomain(guide.getActiveSituation()));

            if (guide.getGuideEntries() != null) {
                List infoGuideEntryList = (List) CollectionUtils.collect(guide.getGuideEntries(), new Transformer() {

                    @Override
                    public Object transform(Object arg0) {
                        GuideEntry guideEntry = (GuideEntry) arg0;
                        return InfoGuideEntry.newInfoFromDomain(guideEntry);
                    }
                });
                setInfoGuideEntries(infoGuideEntryList);
            }

            if (guide.getGuideSituations() != null) {
                List infoGuideSituationList = (List) CollectionUtils.collect(guide.getGuideSituations(), new Transformer() {

                    @Override
                    public Object transform(Object arg0) {
                        GuideSituation guideSituation = (GuideSituation) arg0;
                        return InfoGuideSituation.newInfoFromDomain(guideSituation);
                    }
                });
                setInfoGuideSituations(infoGuideSituationList);
            }
        }
    }

    public static InfoGuide newInfoFromDomain(Guide guide) {
        InfoGuideWithPersonAndExecutionDegreeAndContributor infoGuide = new InfoGuideWithPersonAndExecutionDegreeAndContributor();
        if (guide != null) {
            infoGuide.copyFromDomain(guide);
        }

        return infoGuide;
    }
}