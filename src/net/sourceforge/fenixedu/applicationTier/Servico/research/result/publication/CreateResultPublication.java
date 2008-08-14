package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ArticleBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.BookPartBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.InproceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ManualBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.OtherPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ProceedingsBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.TechnicalReportBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ThesisBean;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation.ResultUnitAssociationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;

public class CreateResultPublication extends ResultPublicationService {

    public Book run(BookBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	return (Book) associateUnitIfNeeded(bean, ResearchResultMetaDataManager.addDefaultDocument(createBookFromBean(bean)));
    }

    public BookPart run(BookPartBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	return (BookPart) associateUnitIfNeeded(bean, ResearchResultMetaDataManager
		.addDefaultDocument(createBookPartFromBean(bean)));
    }

    public Article run(ArticleBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	return (Article) associateUnitIfNeeded(bean, ResearchResultMetaDataManager
		.addDefaultDocument(createArticleFromBean(bean)));
    }

    public Inproceedings run(InproceedingsBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	return (Inproceedings) associateUnitIfNeeded(bean, ResearchResultMetaDataManager
		.addDefaultDocument(createInproceedingsFromBean(bean)));
    }

    public Proceedings run(ProceedingsBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	return (Proceedings) associateUnitIfNeeded(bean, ResearchResultMetaDataManager
		.addDefaultDocument(createProceedingsFromBean(bean)));
    }

    public Thesis run(ThesisBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	return (Thesis) associateUnitIfNeeded(bean, ResearchResultMetaDataManager.addDefaultDocument(createThesisFromBean(bean)));
    }

    public Manual run(ManualBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	return (Manual) associateUnitIfNeeded(bean, ResearchResultMetaDataManager.addDefaultDocument(createManualFromBean(bean)));
    }

    public TechnicalReport run(TechnicalReportBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	return (TechnicalReport) associateUnitIfNeeded(bean, ResearchResultMetaDataManager
		.addDefaultDocument(createTechnicalReportFromBean(bean)));
    }

    public OtherPublication run(OtherPublicationBean bean) {
	if (bean == null)
	    throw new NullPointerException();
	return (OtherPublication) associateUnitIfNeeded(bean, ResearchResultMetaDataManager
		.addDefaultDocument(createOtherPublicationFromBean(bean)));
    }

    private ResearchResult associateUnitIfNeeded(ResultPublicationBean bean, ResearchResult publication) {
	if (bean.getUnit() != null) {
	    publication.addUnitAssociation(bean.getUnit(), ResultUnitAssociationRole.Participant);
	}
	return publication;
    }

}
