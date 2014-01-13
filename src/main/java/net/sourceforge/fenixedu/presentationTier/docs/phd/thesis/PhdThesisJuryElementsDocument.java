package net.sourceforge.fenixedu.presentationTier.docs.phd.thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdThesisJuryElementsDocument extends FenixReport {

    static private final long serialVersionUID = 1L;

    private final PhdThesisProcess process;

    public PhdThesisJuryElementsDocument(PhdThesisProcess process) {
        this.process = process;

        fillReport();
    }

    @Override
    protected void fillReport() {

        addJuryElementsInformation();

        Unit adminOfficeUnit = process.getIndividualProgramProcess().getPhdProgram().getAdministrativeOffice().getUnit();

        addParameter("presidentTitle", this.process.getPresidentTitle().getContent(getLanguage()));
        addParameter("administrativeOfficeCoordinator", adminOfficeUnit.getActiveUnitCoordinator().getPartyName().getContent());
        addParameter("administrativeOfficeName", adminOfficeUnit.getPartyName().getContent());
        addParameter("ratificationEntityMessage",
                process.getPhdJuryElementsRatificationEntity().getRatificationEntityMessage(process, getLocale()));
    }

    private void addJuryElementsInformation() {
        final List<ThesisJuryElementInfo> elements = new ArrayList<ThesisJuryElementInfo>();

        for (final ThesisJuryElement element : process.getOrderedThesisJuryElements()) {
            elements.add(new ThesisJuryElementInfo(element));
        }

        addDataSourceElements(elements);
    }

    @Override
    public String getReportFileName() {
        return "JuryElements-" + new DateTime().toString(YYYYMMDDHHMMSS);
    }

    static public class ThesisJuryElementInfo {

        private final String description;

        public ThesisJuryElementInfo(ThesisJuryElement element) {
            this.description = buildDescription(element);
        }

        private String buildDescription(ThesisJuryElement element) {
            final StringBuilder builder = new StringBuilder();

            builder.append(!StringUtils.isEmpty(element.getTitle()) ? element.getTitle() : "").append(" ");
            builder.append(element.getName()).append(", ");
            builder.append(element.getCategory());

            if (!StringUtils.isEmpty(element.getWorkLocation())) {
                builder.append(" ").append(getMessage("label.phd.thesis.jury.elements.document.keyword.of")).append(" ")
                        .append(element.getWorkLocation());
            }

            if (!StringUtils.isEmpty(element.getInstitution())) {
                builder.append(" ").append(getMessage("label.phd.thesis.jury.elements.document.keyword.of")).append(" ")
                        .append(element.getInstitution());
            }

            if (element.getExpert()) {
                builder.append(" ").append(getMessage("label.phd.thesis.jury.elements.document.expert"));
            }

            builder.append(";");

            if (element.isAssistantGuiding()) {
                builder.append(" (").append(getMessage("label.phd.thesis.jury.elements.document.assistantGuiding")).append(")");
            } else if (element.isMainGuiding()) {
                builder.append(" (").append(getMessage("label.phd.thesis.jury.elements.document.guiding")).append(")");
            } else if (element.getReporter()) {
                builder.append(" - ").append(getMessage("label.phd.thesis.jury.elements.document.reporter"));
            }

            return builder.toString();
        }

        public String getDescription() {
            return description;
        }

        private String getMessage(String key) {
            return getBundle().getString(key);
        }

        protected ResourceBundle getBundle() {
            return ResourceBundle.getBundle("resources.PhdResources", Language.getLocale());
        }

    }

}
