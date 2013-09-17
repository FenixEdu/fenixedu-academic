package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;


import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;
import pt.ist.fenixframework.Atomic;

public class UpdateMetaInformation {

    @Atomic
    public static void run(ResearchResult result) {
        ResearchResultMetaDataManager.updateMetaDataInStorageFor(result);
    }
}