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
package net.sourceforge.fenixedu.presentationTier.Action.commons.ects;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.AcademicIntervalConverter;

import org.fenixedu.commons.StringNormalizer;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class EctsTableFilter implements Serializable {
    private static final long serialVersionUID = 4127180744673004205L;

    public static class ExecutionIntervalProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new AcademicIntervalConverter();
        }

        @Override
        public Object provide(Object source, Object current) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            ExecutionYear year = ExecutionYear.readFirstBolonhaExecutionYear();
            while (year != null) {
                result.add(year.getAcademicInterval());
                year = year.getNextExecutionYear();
            }
            return result;
        }
    }

    public static class EctsTableLevelProvider implements DataProvider {
        @Override
        public Converter getConverter() {
            return new EnumConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            EctsTableFilter filter = (EctsTableFilter) source;
            return filter.type != null ? filter.type.getAllowedLevels() : Collections.emptyList();
        }
    }

    AcademicInterval executionInterval;

    EctsTableType type;

    EctsTableLevel level;

    private transient InputStream inputStream;

    private String filename;

    private String content;

    public AcademicInterval getExecutionInterval() {
        return executionInterval;
    }

    public void setExecutionInterval(AcademicInterval executionInterval) {
        this.executionInterval = executionInterval;
    }

    public EctsTableType getType() {
        return type;
    }

    public void setType(EctsTableType type) {
        this.type = type;
    }

    public EctsTableLevel getLevel() {
        return level;
    }

    public void setLevel(EctsTableLevel level) {
        this.level = level;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContent() throws IOException {
        if (content == null) {
            if (getInputStream() == null) {
                throw new IOException();
            }
            content = FileUtils.readFile(getInputStream());
        }
        return content;
    }

    public void clearFileContent() {
        this.inputStream = null;
        this.content = null;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = StringNormalizer.normalize(filename);
    }
}