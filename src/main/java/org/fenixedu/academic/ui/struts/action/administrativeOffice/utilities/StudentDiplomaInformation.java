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
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.joda.time.YearMonthDay;

public class StudentDiplomaInformation implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7632049994026398211L;

    static final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("ddMMyyyy");
        DATE_FORMAT.setLenient(false);
    }

    private String graduateTitle;

    private String name;

    private String nameOfFather;

    private String nameOfMother;

    private String birthLocale;

    private String degreeName;

    private String dissertationTitle;

    private String classificationResult;

    private YearMonthDay conclusionDate;

    private boolean isMasterDegree;

    private String filename;

    @SuppressWarnings("unchecked")
    public static StudentDiplomaInformation buildFromXmlFile(InputStream stream, String fileName) {

        final StudentDiplomaInformation result = createFrom(parseXml(stream));
        result.filename = fileName;

        return result;
    }

    private static StudentDiplomaInformation createFrom(final Map<String, String> parseResult) {
        final StudentDiplomaInformation result = new StudentDiplomaInformation();

        result.isMasterDegree = Boolean.parseBoolean(parseResult.get("MasterDegree"));
        result.graduateTitle = parseResult.get("GraduateTitle");
        result.name = parseResult.get("Name");
        result.nameOfFather = parseResult.get("NameOfFather");
        result.nameOfMother = parseResult.get("NameOfMother");
        result.birthLocale = parseResult.get("BirthLocale");
        result.degreeName = parseResult.get("DegreeFilteredName");
        result.dissertationTitle = parseResult.get("DissertationTitle");
        result.classificationResult = parseResult.get("ClassificationResult");
        result.conclusionDate = parseDate(parseResult.get("ConclusionDate"));

        return result;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> parseXml(InputStream stream) {

        try {
            final SAXBuilder parser = new SAXBuilder(false);
            final Document document = parser.build(stream);
            final Element rootElement = document.getRootElement();
            final Map<String, String> result = new HashMap<String, String>();

            if (rootElement.getName().equals("Aluno_Mestrado")) {
                result.put("MasterDegree", Boolean.TRUE.toString());
            } else if (rootElement.getName().equals("Aluno_Doutoramento")) {
                result.put("MasterDegree", Boolean.FALSE.toString());
            } else {
                throw new RuntimeException("Unexpected diploma type");
            }

            final Element diplomaElement = rootElement.getChild("Carta_Curso");
            for (final Element childElement : (List<Element>) diplomaElement.getChildren()) {
                result.put(childElement.getName(), childElement.getValue());
            }

            return result;

        } catch (JDOMException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static YearMonthDay parseDate(final String rawDate) {
        try {
            return YearMonthDay.fromDateFields(DATE_FORMAT.parse(rawDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getGraduateTitle() {
        return graduateTitle;
    }

    public void setGraduateTitle(String graduateTitle) {
        this.graduateTitle = graduateTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOfFather() {
        return nameOfFather;
    }

    public void setNameOfFather(String nameOfFather) {
        this.nameOfFather = nameOfFather;
    }

    public String getNameOfMother() {
        return nameOfMother;
    }

    public void setNameOfMother(String nameOfMother) {
        this.nameOfMother = nameOfMother;
    }

    public String getBirthLocale() {
        return birthLocale;
    }

    public void setBirthLocale(String birthLocale) {
        this.birthLocale = birthLocale;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getDissertationTitle() {
        return dissertationTitle;
    }

    public void setDissertationTitle(String dissertationTitle) {
        this.dissertationTitle = dissertationTitle;
    }

    public String getClassificationResult() {
        return classificationResult;
    }

    public void setClassificationResult(String classificationResult) {
        this.classificationResult = classificationResult;
    }

    public YearMonthDay getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(YearMonthDay conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public boolean isMasterDegree() {
        return isMasterDegree;
    }

    public void setMasterDegree(boolean isMasterDegree) {
        this.isMasterDegree = isMasterDegree;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}