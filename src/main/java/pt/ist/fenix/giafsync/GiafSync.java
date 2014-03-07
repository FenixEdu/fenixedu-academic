package pt.ist.fenix.giafsync;

import java.io.PrintWriter;
import java.util.List;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@Task(englishTitle = "Giaf Sync", readOnly = true)
public class GiafSync extends CronTask {
    private static final Logger logger = LoggerFactory.getLogger(ImportContractSituationsFromGiaf.class);

    public static interface Modification {
        public void execute();
    }

    public static abstract class ImportProcessor {
        public void process(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception {
            internalProcess(processChanges(metadata, log, logger));
        }

        @Atomic(mode = TxMode.WRITE)
        private void internalProcess(List<Modification> modifications) {
            for (Modification modification : modifications) {
                modification.execute();
            }
        }

        public abstract List<Modification> processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception;
    }

    public static interface MetadataProcessor {
        public void processChanges(GiafMetadata metadata, PrintWriter log, Logger logger) throws Exception;
    }

    @Override
    public void runTask() throws Exception {
        GiafMetadata metadata = new GiafMetadata();
        updateMetadata(metadata);

        PrintWriter logWriter = getTaskLogWriter();
        new UpdatePersonsFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonProfessionalData().process(metadata, logWriter, logger);
        new ImportPersonContractSituationsFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonProfessionalCategoriesFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonProfessionalContractsFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonProfessionalRegimesFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonProfessionalRelationsFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonFunctionsAccumulationsFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonSabbaticalsFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonServiceExemptionsFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonGrantOwnerEquivalentFromGiaf().process(metadata, logWriter, logger);
        new ImportPersonAbsencesFromGiaf().process(metadata, logWriter, logger);
        new ImportEmployeeUnitsFromGiaf().process(metadata, logWriter, logger);
    }

    @Atomic(mode = TxMode.WRITE)
    private void updateMetadata(GiafMetadata metadata) throws Exception {
        PrintWriter logWriter = getTaskLogWriter();
        new ImportTypesFromGiaf().processChanges(metadata, logWriter, logger);
        new ImportContractSituationsFromGiaf().processChanges(metadata, logWriter, logger);
        new ImportProfessionalCategoryFromGiaf().processChanges(metadata, logWriter, logger);
        new ImportProfessionalRegimesFromGiaf().processChanges(metadata, logWriter, logger);
        new ImportProfessionalRelationsFromGiaf().processChanges(metadata, logWriter, logger);
    }
}
