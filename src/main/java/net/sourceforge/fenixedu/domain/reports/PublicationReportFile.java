package net.sourceforge.fenixedu.domain.reports;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class PublicationReportFile extends PublicationReportFile_Base {

    public PublicationReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return BundleUtil.getStringFromResourceBundle("resources.GEPResources", "link.publications.report");
    }

    @Override
    protected String getPrefix() {
        return BundleUtil.getStringFromResourceBundle("resources.GEPResources", "link.publications");
    }

    @Override
    public void renderReport(final Spreadsheet spreadsheet) {
        spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.GEPResources", "link.publication.type"));
        spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.GEPResources", "link.publication.author"));
        spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.GEPResources", "link.username"));
        spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.GEPResources", "link.publication.title"));
        spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.GEPResources", "link.publication.year"));
        spreadsheet
                .setHeader(BundleUtil.getStringFromResourceBundle("resources.GEPResources", "link.publication.journalOrEvent"));
        spreadsheet.setHeader(BundleUtil.getStringFromResourceBundle("resources.GEPResources", "link.publication.id"));

        for (final ResearchResult researchResult : Bennu.getInstance().getResultsSet()) {
            if (researchResult instanceof ResearchResultPublication) {
                final ResearchResultPublication researchResultPublication = (ResearchResultPublication) researchResult;
                for (final ResultParticipation resultParticipation : researchResultPublication.getResultParticipationsSet()) {
                    final Person person = resultParticipation.getPerson();
                    final Row row = spreadsheet.addRow();
                    row.setCell(researchResultPublication.getLocalizedType());
                    row.setCell(person.getName());
                    row.setCell(person.getUsername());
                    row.setCell(researchResultPublication.getTitle());
                    row.setCell(researchResultPublication.getYear());
                    row.setCell(researchResultPublication.getPublisher());
                    row.setCell(getPublicationId(researchResultPublication));
                }
            }
        }
    }

    private String getPublicationId(final ResearchResultPublication researchResultPublication) {
        if (researchResultPublication instanceof Article) {
            final Article article = (Article) researchResultPublication;
            return article.getIssn() == null ? "" : article.getIssn().toString();
        } else if (researchResultPublication instanceof Book) {
            final Book article = (Book) researchResultPublication;
            return article.getIsbn() == null ? "" : article.getIsbn().toString();
        } else if (researchResultPublication instanceof BookPart) {
            final BookPart article = (BookPart) researchResultPublication;
        } else if (researchResultPublication instanceof Inproceedings) {
            final Inproceedings article = (Inproceedings) researchResultPublication;
            return article.getEvent() == null ? "" : article.getEvent().getName();
        } else if (researchResultPublication instanceof Proceedings) {
            final Proceedings article = (Proceedings) researchResultPublication;
            return article.getEvent() == null ? "" : article.getEvent().getName();
        } else if (researchResultPublication instanceof Manual) {
            final Manual article = (Manual) researchResultPublication;

        } else if (researchResultPublication instanceof OtherPublication) {
            final OtherPublication article = (OtherPublication) researchResultPublication;

        } else if (researchResultPublication instanceof TechnicalReport) {
            final TechnicalReport article = (TechnicalReport) researchResultPublication;

        } else if (researchResultPublication instanceof Thesis) {
            final Thesis article = (Thesis) researchResultPublication;

        } else if (researchResultPublication instanceof Unstructured) {
            final Unstructured article = (Unstructured) researchResultPublication;

        }
        return "";
    }

}
