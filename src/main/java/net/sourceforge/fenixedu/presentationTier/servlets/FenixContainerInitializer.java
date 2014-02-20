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
