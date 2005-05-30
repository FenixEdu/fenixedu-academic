/*
 * Created on 28/Jul/2003, 15:30:12
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.CaseStudy;
import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudy;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 15:30:12
 *  
 */
public class CaseStudyVO extends VersionedObjectsBase implements IPersistentSeminaryCaseStudy {

    public List readAll() {
        return (List) readAll(CaseStudy.class);
    }

    public List readByThemeID(Integer themeID) {
        final Collection<ICaseStudy> caseStudies = readAll(CaseStudy.class);
        final List result = new ArrayList();
        for (final ICaseStudy caseStudy : caseStudies) {
            if (caseStudy.getSeminaryTheme().getIdInternal().equals(themeID)) {
                result.add(caseStudy);
            }
        }
        return result;
    }

}