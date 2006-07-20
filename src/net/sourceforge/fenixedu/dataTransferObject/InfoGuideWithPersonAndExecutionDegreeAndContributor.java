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
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoGuide#copyFromDomain(Dominio.Guide)
     */
    public void copyFromDomain(Guide guide) {
        super.copyFromDomain(guide);
        if (guide != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(guide.getPerson()));
            setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .newInfoFromDomain(guide.getExecutionDegree()));
            setInfoContributor(InfoContributor.newInfoFromDomain(guide.getContributorParty()));
            setInfoGuideSituation(InfoGuideSituation.newInfoFromDomain(guide.getActiveSituation()));

            if (guide.getGuideEntries() != null) {
                List infoGuideEntryList = (List) CollectionUtils.collect(guide.getGuideEntries(),
                        new Transformer() {

                            public Object transform(Object arg0) {
                                GuideEntry guideEntry = (GuideEntry) arg0;
                                return InfoGuideEntry.newInfoFromDomain(guideEntry);
                            }
                        });
                setInfoGuideEntries(infoGuideEntryList);
            }

            if (guide.getGuideSituations() != null) {
                List infoGuideSituationList = (List) CollectionUtils.collect(guide.getGuideSituations(),
                        new Transformer() {

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