package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookletBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InbookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.IncollectionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.MiscBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.UnpublishedBean;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Booklet;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.Misc;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.domain.research.result.publication.Unpublished;

public class CreateResultPublication extends ResultPublicationService {

    public Book run(BookBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createBookFromBean(bean);
    }

    public BookPart run(InbookBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createInbookFromBean(bean);
    }

    public BookPart run(IncollectionBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createIncollectionFromBean(bean);
    }

    public Article run(ArticleBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createArticleFromBean(bean);
    }

    public Inproceedings run(InproceedingsBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createInproceedingsFromBean(bean);
    }

    public Proceedings run(ProceedingsBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createProceedingsFromBean(bean);
    }

    public Thesis run(ThesisBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createThesisFromBean(bean);
    }

    public Manual run(ManualBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createManualFromBean(bean);
    }

    public TechnicalReport run(TechnicalReportBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createTechnicalReportFromBean(bean);
    }

    public Booklet run(BookletBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createBookletFromBean(bean);
    }

    public Misc run(MiscBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createMiscFromBean(bean);
    }

    public Unpublished run(UnpublishedBean bean) {
        if (bean == null)
            throw new NullPointerException();
        return createUnpublishedFromBean(bean);
    }
}
