/*
 * Created on 17/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 17/Nov/2003
 */
public class ReimbursementGuideVO extends VersionedObjectsBase implements IPersistentReimbursementGuide {

    public Integer generateReimbursementGuideNumber() throws ExcepcaoPersistencia {

        int maxNumber = 0;
        final List<IReimbursementGuide> reimbursementGuides = (List<IReimbursementGuide>) readAll(ReimbursementGuide.class);

        for (IReimbursementGuide reimbursementGuide : reimbursementGuides) {
            maxNumber = Math.max(maxNumber, reimbursementGuide.getNumber());
        }

        return maxNumber + 1;
    }

}