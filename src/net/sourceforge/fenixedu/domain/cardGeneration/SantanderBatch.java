package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class SantanderBatch extends SantanderBatch_Base {

    private static String recordEnd = "*";
    private static String lineEnd = "\r\n";

    static final public Comparator<SantanderBatch> COMPARATOR_BY_MOST_RECENTLY_CREATED = new Comparator<SantanderBatch>() {

        @Override
        public int compare(SantanderBatch o1, SantanderBatch o2) {
            return o1.getCreated().isAfter(o2.getCreated()) ? 1 : 0;
        }

    };

    private SantanderBatch() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public SantanderBatch(Person requester, ExecutionYear executionYear) {
        this();
        setSantanderBatchRequester(new SantanderBatchRequester(requester));
        setExecutionYear(executionYear);
        setCreated(new DateTime());
    }

    public void delete() {
        if (getSent() != null) {
            throw new DomainException("santander.cards.cant.delete.batch.was.already.sent");
        }
        getSantanderBatchRequester().delete();
        if (getSantanderBatchSender() != null) {
            getSantanderBatchSender().delete();
        }
        removeSantanderSequenceNumberGenerator();
        for (SantanderEntry entry : getSantanderEntries()) {
            entry.delete();
        }
        for (SantanderProblem problem : getSantanderProblems()) {
            problem.delete();
        }
        removeExecutionYear();
        removeRootDomainObject();

        super.deleteDomainObject();
    }

    public String generateTUI() throws Exception {
        if (getGenerated() == null) {
            throw new DomainException("santander.batch.cant.generate.TUI.");
        }
        StringBuilder fileBuilder = new StringBuilder(1500000);
        buildHeader(fileBuilder);
        for (SantanderEntry line : getSantanderEntries()) {
            fileBuilder.append(line.getLine());
        }
        buildTrailer(fileBuilder);
        return fileBuilder.toString();
    }

    private StringBuilder buildHeader(StringBuilder strBuilder) throws Exception {
        String recordType = "1";

        String institutionId = makeZeroPaddedNumber(42, 5);

        String institutionName = makeStringBlock("TECNICO LISBOA", 14);

        String fileName = makeStringBlock("SANTUNIV", 15);

        DateTime dateObj = new DateTime();
        String date = dateObj.toString("yyyyMMdd");

        String sequenceNumber = makeZeroPaddedNumber((getSequenceNumber() != null ? getSequenceNumber() : 0), 5);

        String filler = makeStringBlock("", 1451);

        strBuilder.append(recordType);
        strBuilder.append(institutionId);
        strBuilder.append(institutionName);
        strBuilder.append(fileName);
        strBuilder.append(date);
        strBuilder.append(sequenceNumber);
        strBuilder.append(filler);
        strBuilder.append(recordEnd);
        strBuilder.append(lineEnd);

        return strBuilder;
    }

    private StringBuilder buildTrailer(StringBuilder stringBuilder) throws Exception {
        String recordType = "9";

        String numberOfEntries = makeZeroPaddedNumber(getSantanderEntriesCount(), 5);

        String filler = makeStringBlock("", 1493);

        stringBuilder.append(recordType);
        stringBuilder.append(numberOfEntries);
        stringBuilder.append(filler);
        stringBuilder.append(recordEnd);
        stringBuilder.append(lineEnd);

        return stringBuilder;
    }

    private String makeStringBlock(String content, int size) throws Exception {
        int fillerLength = size - content.length();
        if (fillerLength < 0) {
            throw new Exception("Content is bigger than string block.");
        }
        StringBuilder blockBuilder = new StringBuilder(size);
        blockBuilder.append(content);

        for (int i = 0; i < fillerLength; i++) {
            blockBuilder.append(" ");
        }

        return blockBuilder.toString();
    }

    private String makeZeroPaddedNumber(int number, int size) throws Exception {
        if (String.valueOf(number).length() > size) {
            throw new Exception("Number has more digits than allocated room.");
        }
        String format = "%0" + size + "d";
        return String.format(format, number);
    }

}
