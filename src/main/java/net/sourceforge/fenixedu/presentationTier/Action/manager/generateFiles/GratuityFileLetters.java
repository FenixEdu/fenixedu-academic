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
 * Created on 28/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.generateFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tânia Pousão
 * 
 */
public class GratuityFileLetters extends GratuityFile {

    private static final Logger logger = LoggerFactory.getLogger(GratuityFileLetters.class);

    public static final String SEPARATOR = ";";

    public static final String NOTHING = "-";

    public static File buildFile(List infoGratuitySituations) throws Exception {
        if (infoGratuitySituations == null) {
            return null;
        }

        String fileName = null;
        File file = null;
        BufferedWriter writer = null;

        String fileNameErrors = null;
        File fileErrors = null;
        BufferedWriter writerErrors = null;
        try {
            // build the file's name with the first element
            fileName = nameFile((InfoGratuitySituation) infoGratuitySituations.iterator().next());
            file = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
            writer = new BufferedWriter(new FileWriter(file));

            // errors if student hasn´t address or nothing to pay
            fileNameErrors = "erros_" + nameFile((InfoGratuitySituation) infoGratuitySituations.iterator().next());
            fileErrors = new File(System.getProperty("java.io.tmpdir") + File.separator + fileNameErrors);
            writerErrors = new BufferedWriter(new FileWriter(fileErrors));

            writeHeader(writer);

            Iterator iterator = infoGratuitySituations.listIterator();
            while (iterator.hasNext()) {
                InfoGratuitySituation infoGratuitySituation = (InfoGratuitySituation) iterator.next();

                if (valid(infoGratuitySituation, writerErrors)) {
                    writeLine(writer, infoGratuitySituation);
                }
            }

            writer.close();
            writerErrors.close();

            return file;
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new Exception();
        }
    }

    private static String nameFile(InfoGratuitySituation infoGratuitySituation) {
        StringBuilder fileName = new StringBuilder();

        String year =
                infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree().getInfoExecutionYear().getYear()
                        .replace('/', '-');
        fileName.append("cartasPropinas");
        fileName.append(year);
        fileName.append(".txt");

        return fileName.toString();
    }

    /**
     * Creates a header that describes all column in file like <name
     * column>;...; <name column>
     * 
     * @param writer
     */
    private static void writeHeader(BufferedWriter writer) throws IOException {
        StringBuilder header = new StringBuilder();
        header.append("NOME");
        header.append(SEPARATOR);
        header.append("MORADA");
        header.append(SEPARATOR);
        header.append("LOCALIDADE");
        header.append(SEPARATOR);
        header.append("CODIGO_POSTAL");
        header.append(SEPARATOR);
        header.append("LOCALIDADE_CODIGO_POSTAL");
        header.append(SEPARATOR);
        header.append("REFERÊNCIA_PAGAMENTO");
        header.append(SEPARATOR);
        header.append("MESTRADO");
        header.append(SEPARATOR);
        header.append("PROPINA");
        header.append(SEPARATOR);
        header.append("SEGURO");
        header.append(SEPARATOR);
        header.append("TOTAL");

        writer.write(header.toString());
        writer.newLine();
    }

    /**
     * Creates a line with student's data separate by tab
     * 
     * @param writer
     * @param infoGratuitySituation
     */
    private static void writeLine(BufferedWriter writer, InfoGratuitySituation infoGratuitySituation) throws IOException {
        StringBuilder line = new StringBuilder();
        // student´s name
        line.append(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getInfoPerson().getNome());
        line.append("\t");
        // student´s address
        line.append(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getInfoPerson().getMorada());
        line.append("\t");
        // address's area
        line.append(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getInfoPerson().getLocalidade());
        line.append("\t");
        // address's area code
        line.append(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getInfoPerson().getCodigoPostal());
        line.append("\t");
        // address's area code's area
        line.append(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent().getInfoPerson()
                .getLocalidadeCodigoPostal());
        line.append("\t");
        // payment's reference
        line.append(buildPaymentReference(infoGratuitySituation));
        line.append("\t");
        // degree's name
        line.append(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoDegreeCurricularPlan().getInfoDegree().getNome());
        line.append("\t");
        // gratuity value
        double totalValue = infoGratuitySituation.getRemainingValue().doubleValue();
        if (totalValue > 0) {
            line.append(infoGratuitySituation.getRemainingValue());
        } else {
            // nothig to payed
            line.append(NOTHING);
            totalValue = 0;
        }
        line.append("\t");
        // first verify if the student already payed the insurance
        // and if the student not payed add to the total value
        // after the insurance value is appended to the line

        if (infoGratuitySituation.getInsurancePayed().equals("label.notPayed")) {
            // insurance not payed
            line.append(INSURANCE);

            totalValue = totalValue + INSURANCE;
        } else {
            // insurance payed
            line.append(NOTHING);
        }
        line.append("\t");
        // total value
        if (totalValue > 0) {
            line.append(totalValue);
        } else {
            // nothig to payed
            line.append(NOTHING);
        }

        // write the line
        writer.write(line.toString());
        writer.newLine();
    }
}