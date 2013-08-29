package net.sourceforge.fenixedu.domain.cardGeneration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import edu.emory.mathcs.backport.java.util.Collections;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.util.ConfigurationManager;

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
        setRootDomainObject(Bennu.getInstance());
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
        setSantanderSequenceNumberGenerator(null);
        for (SantanderEntry entry : getSantanderEntries()) {
            entry.delete();
        }
        for (SantanderProblem problem : getSantanderProblems()) {
            problem.delete();
        }
        setExecutionYear(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    public List<SantanderEntry> getSortedSantanderEntries() {
        List<SantanderEntry> sortedList = new ArrayList<SantanderEntry>(getSantanderEntries());
        Collections.sort(sortedList, SantanderEntry.COMPARATOR_BY_MOST_RECENTLY_CREATED);
        return sortedList;
    }

    public String generateTUI() throws Exception {
        if (getGenerated() == null) {
            throw new DomainException("santander.batch.cant.generate.TUI.");
        }
        StringBuilder fileBuilder = new StringBuilder(1500000);
        buildHeader(fileBuilder);
        for (SantanderEntry entryLine : getSortedSantanderEntries()) {
            String line = entryLine.getLine();
            String visibleLine = line.substring(0, 307);

            //Update line with the persons Santander PIN
            if (entryLine.getPerson().getSantanderPIN() == null) {
                SantanderSequenceNumberGenerator.generateSantanderPIN(entryLine.getPerson());
            }
            visibleLine += SantanderSequenceNumberGenerator.decodeSantanderPIN(entryLine.getPerson().getSantanderPIN());

            // Update line with the institutions own PIN
            visibleLine += ConfigurationManager.getProperty("app.institution.PIN");

            visibleLine += line.substring(315);

            fileBuilder.append(visibleLine);
        }
        buildTrailer(fileBuilder);
        return fileBuilder.toString();
    }

    private StringBuilder buildHeader(StringBuilder strBuilder) throws Exception {
        String recordType = "1";

        String institutionId = makeZeroPaddedNumber(42, 5);

        String institutionName = makeStringBlock("TECNICO LISBOA", 14, ' ');

        String fileName = makeStringBlock("SANTUNIV", 15, ' ');

        DateTime dateObj = new DateTime();
        String date = dateObj.toString("yyyyMMdd");

        String sequenceNumber = makeZeroPaddedNumber((getSequenceNumber() != null ? getSequenceNumber() : 0), 5);

        String filler = makeStringBlock("", 1451, ' ');

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

        String numberOfEntries = makeZeroPaddedNumber(getSantanderEntriesSet().size(), 5);

        String filler = makeStringBlock("", 1493, ' ');

        stringBuilder.append(recordType);
        stringBuilder.append(numberOfEntries);
        stringBuilder.append(filler);
        stringBuilder.append(recordEnd);
        stringBuilder.append(lineEnd);

        return stringBuilder;
    }

    private String makeStringBlock(String content, int size, char filler) throws Exception {
        int fillerLength = size - content.length();
        if (fillerLength < 0) {
            throw new Exception("Content is bigger than string block.");
        }
        StringBuilder blockBuilder = new StringBuilder(size);
        blockBuilder.append(content);

        for (int i = 0; i < fillerLength; i++) {
            blockBuilder.append(filler);
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.SantanderProblem> getSantanderProblems() {
        return getSantanderProblemsSet();
    }

    @Deprecated
    public boolean hasAnySantanderProblems() {
        return !getSantanderProblemsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.cardGeneration.SantanderEntry> getSantanderEntries() {
        return getSantanderEntriesSet();
    }

    @Deprecated
    public boolean hasAnySantanderEntries() {
        return !getSantanderEntriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasSent() {
        return getSent() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreated() {
        return getCreated() != null;
    }

    @Deprecated
    public boolean hasSantanderBatchSender() {
        return getSantanderBatchSender() != null;
    }

    @Deprecated
    public boolean hasSantanderSequenceNumberGenerator() {
        return getSantanderSequenceNumberGenerator() != null;
    }

    @Deprecated
    public boolean hasSantanderBatchRequester() {
        return getSantanderBatchRequester() != null;
    }

    @Deprecated
    public boolean hasSequenceNumber() {
        return getSequenceNumber() != null;
    }

    @Deprecated
    public boolean hasGenerated() {
        return getGenerated() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

    private String makeRightShiftedPaddedNumber(int number, int size) {
        if (String.valueOf(number).length() > size) {
            number = number % (int) Math.pow(10, size);
        }
        String format = "%0" + size + "d";
        return String.format(format, number);
    }

    private String getCardName(String name) {
        String purgedName = purgeString(name);
        String cleanedName = StringUtils.trimToEmpty(purgedName);
        String[] names = cleanedName.split(" ");
        return names[0] + " " + names[names.length - 1];
    }

    private String purgeString(final String name) {
        if (!StringUtils.isAlphaSpace(name)) {
            final char[] ca = new char[name.length()];
            int j = 0;
            for (int i = 0; i < name.length(); i++) {
                final char c = name.charAt(i);
                if (Character.isLetter(c) || c == ' ') {
                    ca[j++] = c;
                }
            }
            return new String(ca);
        }
        return name;
    }

    public byte[] getPhotosAndDDXR() throws Exception {
        final ByteArrayOutputStream file = new ByteArrayOutputStream();
        final ZipOutputStream zipFile = new ZipOutputStream(file);

        List<SantanderEntry> batch2k = new ArrayList<SantanderEntry>();
        int entryCnt = 0;
        int batchCnt = 0;
        DateTime today = new DateTime();

        for (SantanderEntry santanderEntry : getSortedSantanderEntries()) {
            if (santanderEntry.getSantanderPhotoEntry() != null) {
                batch2k.add(santanderEntry);
                if (++entryCnt % 2000 == 0) {
                    batchCnt++;
                    zipFile.putNextEntry(new ZipEntry(today.toString("yyyy-MM-dd") + "_E" + makeZeroPaddedNumber(batchCnt, 4)
                            + ".xml"));
                    zipFile.write(generateDDXR(batch2k, batchCnt, today));
                    zipFile.closeEntry();

                    zipFile.putNextEntry(new ZipEntry(today.toString("yyyy-MM-dd") + "_E" + makeZeroPaddedNumber(batchCnt, 4)
                            + ".zip"));
                    zipFile.write(generatePhotoZip(batch2k, batchCnt, today));
                    zipFile.closeEntry();

                    batch2k.clear();
                }
            }
        }
        if (batch2k.size() > 0) {
            batchCnt++;
            zipFile.putNextEntry(new ZipEntry(today.toString("yyyy-MM-dd") + "_E" + makeZeroPaddedNumber(batchCnt, 4) + ".xml"));
            zipFile.write(generateDDXR(batch2k, batchCnt, today));
            zipFile.closeEntry();

            zipFile.putNextEntry(new ZipEntry(today.toString("yyyy-MM-dd") + "_E" + makeZeroPaddedNumber(batchCnt, 4) + ".zip"));
            zipFile.write(generatePhotoZip(batch2k, batchCnt, today));
            zipFile.closeEntry();

            batch2k.clear();
        }
        zipFile.close();
        return file.toByteArray();
    }

    private byte[] generateDDXR(List<SantanderEntry> entries, int seqNumber, DateTime timestamp) throws Exception {
        StringBuilder ddxrBuilder = new StringBuilder();

        ddxrBuilder
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<ns0:Root xmlns:ns0=\"http://SIBS.Balc.Schemas.DDXR\">\n");

        ddxrBuilder.append("<HDR>\n");
        ddxrBuilder.append("\t<FICH>" + "DDXR" + "</FICH>\n");
        ddxrBuilder.append("\t<BANCO>" + "0018" + "</BANCO>\n");
        ddxrBuilder.append("\t<NSEQFICH>" + "E" + makeZeroPaddedNumber(seqNumber, 4) + "</NSEQFICH>\n");
        ddxrBuilder
                .append("\t<DTFICH>" + timestamp.toString("yyyy-MM-dd") + "T" + timestamp.toString("HH:mm:ss") + "</DTFICH>\n");
        ddxrBuilder.append("\t<DATAPROC>" + timestamp.toString("yyyy-MM-dd") + "</DATAPROC>\n");
        ddxrBuilder.append("\t<TIPOPRODUTO>" + "002" + "</TIPOPRODUTO>\n");
        ddxrBuilder.append("\t<OPERATIVA>" + "01" + "</OPERATIVA>\n");
        ddxrBuilder.append("\t<TOTLT>" + "1" + "</TOTLT>\n");
        ddxrBuilder.append("\t<TOTDOC>" + entries.size() + "</TOTDOC>\n");
        ddxrBuilder.append("\t<TOTDOCDET>" + entries.size() + "</TOTDOCDET>\n");
        ddxrBuilder.append("</HDR>\n");

        ddxrBuilder.append("<LT>\n");

        ddxrBuilder.append("\t<LTHDR>\n");
        ddxrBuilder.append("\t\t<NLOTE>" + "1" + "</NLOTE>\n");
        ddxrBuilder.append("\t\t<BANCO>" + "0018" + "</BANCO>\n");
        ddxrBuilder.append("\t\t<BALCAO>" + "0000" + "</BALCAO>\n");
        ddxrBuilder.append("\t\t<DATAREM>" + timestamp.toString("yyyy-MM-dd") + "</DATAREM>\n");
        ddxrBuilder.append("\t\t<SERIE>" + "0001" + "</SERIE>\n");
        ddxrBuilder.append("\t\t<SUBSERIE>" + "0001" + "</SUBSERIE>\n");
        ddxrBuilder.append("\t\t<TIPOREM>" + "001" + "</TIPOREM>\n");
        ddxrBuilder.append("\t\t<TIPOANOMA>" + "000" + "</TIPOANOMA>\n");
        ddxrBuilder.append("\t</LTHDR>\n");

        ddxrBuilder.append("\t<LTBODY>\n");
        for (SantanderEntry entry : entries) {
            ddxrBuilder.append("\t\t<DOC>\n");
            ddxrBuilder.append("\t\t\t<DOCHDR>\n");
            ddxrBuilder.append("\t\t\t\t<NUMDOC>" + timestamp.toString("yy") + "E0042"
                    + makeRightShiftedPaddedNumber(entry.getSantanderPhotoEntry().getSequenceNumber(), 7) + "</NUMDOC>\n");
            ddxrBuilder.append("\t\t\t\t<TIPOANOMA>" + "000" + "</TIPOANOMA>\n");
            ddxrBuilder.append("\t\t\t\t<TIPODOC>" + "000" + "</TIPODOC>\n");
            ddxrBuilder.append("\t\t\t\t<DTHCAPT>" + timestamp.toString("yyyy-MM-dd") + "T" + timestamp.toString("HH:mm:ss")
                    + "</DTHCAPT>\n");
            ddxrBuilder.append("\t\t\t\t<USRCAPT>" + "tecnico/fenix" + "</USRCAPT>\n");
            ddxrBuilder.append("\t\t\t</DOCHDR>\n");

            ddxrBuilder.append("\t\t\t<DOCDET>\n");
            ddxrBuilder.append("\t\t\t\t<NUMDOC>" + timestamp.toString("yy") + "E0042"
                    + makeRightShiftedPaddedNumber(entry.getSantanderPhotoEntry().getSequenceNumber(), 7) + "</NUMDOC>\n");
            ddxrBuilder.append("\t\t\t\t<NUMDOCFOTO>" + "018042" + makeStringBlock(entry.getPerson().getIstUsername(), 10, 'x')
                    + "</NUMDOCFOTO>\n");
            ddxrBuilder.append("\t\t\t\t<TIPODOC>" + "000" + "</TIPODOC>\n");
            ddxrBuilder.append("\t\t\t\t<NUMPAG>" + "2" + "</NUMPAG>\n");
            ddxrBuilder.append("\t\t\t\t<DADOSADIC>"
                    + "00042E"
                    + makeRightShiftedPaddedNumber(entry.getSantanderPhotoEntry().getSequenceNumber(), 6)
                    + "3"
                    + makeStringBlock(entry.getPerson().getIstUsername(), 10, 'x')
                    + "x"
                    + makeStringBlock(
                            getCardName(entry.getPerson().getName()).length() > 21 ? getCardName(entry.getPerson().getName())
                                    .substring(0, 21) : getCardName(entry.getPerson().getName()), 21, ' ').toUpperCase() + "0"
                    + makeZeroPaddedNumber(0, 21) + makeZeroPaddedNumber(0, 10) + "1" + "</DADOSADIC>\n");
            ddxrBuilder.append("\t\t\t</DOCDET>\n");
            ddxrBuilder.append("\t\t</DOC>\n");
        }
        ddxrBuilder.append("\t</LTBODY>\n");

        ddxrBuilder.append("</LT>\n");
        ddxrBuilder.append("</ns0:Root>\n");

        return ddxrBuilder.toString().getBytes("UTF-8");
    }

    private byte[] generatePhotoZip(List<SantanderEntry> entries, int seqNumber, DateTime timestamp) throws IOException {
        final ByteArrayOutputStream file = new ByteArrayOutputStream();
        final ZipOutputStream zipFile = new ZipOutputStream(file);
        for (SantanderEntry entry : entries) {
            zipFile.putNextEntry(new ZipEntry(timestamp.toString("yy") + "E0042"
                    + makeRightShiftedPaddedNumber(entry.getSantanderPhotoEntry().getSequenceNumber(), 7) + ".jpg"));
            zipFile.write(entry.getSantanderPhotoEntry().getPhotoAsByteArray());
            zipFile.closeEntry();
        }
        zipFile.close();
        return file.toByteArray();
    }
}
