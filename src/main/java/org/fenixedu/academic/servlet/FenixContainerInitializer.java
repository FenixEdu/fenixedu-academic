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
package net.sourceforge.fenixedu.presentationTier.servlets;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import net.sourceforge.fenixedu.presentationTier.candidacydocfiller.PdfFiller;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ProcessCandidacyPrintAllDocumentsFilter;

@HandlesTypes({ PdfFiller.class })
public class FenixContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        if (classes != null) {
            for (Class<?> type : classes) {
                if (PdfFiller.class.isAssignableFrom(type)) {
                    registerFiller(type);
                }
            }
        }
    }

    private void registerFiller(Class<?> type) {
        try {
            PdfFiller filler = (PdfFiller) type.newInstance();
            ProcessCandidacyPrintAllDocumentsFilter.registerFiller(filler);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new Error("Could not instantiate filler", e);
        }
    }

}
