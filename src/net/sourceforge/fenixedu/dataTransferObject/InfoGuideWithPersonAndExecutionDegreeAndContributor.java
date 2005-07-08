/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IGuideSituation;

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
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoGuide#copyFromDomain(Dominio.IGuide)
     */
    public void copyFromDomain(IGuide guide) {
        super.copyFromDomain(guide);
        if (guide != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(guide.getPerson()));
            setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan
                    .newInfoFromDomain(guide.getExecutionDegree()));
            setInfoContributor(InfoContributor.newInfoFromDomain(guide.getContributor()));
            setInfoGuideSituation(InfoGuideSituation.newInfoFromDomain(guide.getActiveSituation()));

            if (guide.getGuideEntries() != null) {
                List infoGuideEntryList = (List) CollectionUtils.collect(guide.getGuideEntries(),
                        new Transformer() {

                            public Object transform(Object arg0) {
                                IGuideEntry guideEntry = (IGuideEntry) arg0;
                                return InfoGuideEntry.newInfoFromDomain(guideEntry);
                            }
                        });
                setInfoGuideEntries(infoGuideEntryList);
            }

            if (guide.getGuideSituations() != null) {
                List infoGuideSituationList = (List) CollectionUtils.collect(guide.getGuideSituations(),
                        new Transformer() {

                            public Object transform(Object arg0) {
                                IGuideSituation guideSituation = (IGuideSituation) arg0;
                                return InfoGuideSituation.newInfoFromDomain(guideSituation);
                            }
                        });
                setInfoGuideSituations(infoGuideSituationList);
            }
        }
    }

    public static InfoGuide newInfoFromDomain(IGuide guide) {
        InfoGuideWithPersonAndExecutionDegreeAndContributor infoGuide = new InfoGuideWithPersonAndExecutionDegreeAndContributor();
        if (guide != null) {
            infoGuide.copyFromDomain(guide);
        }

        return infoGuide;
    }
}