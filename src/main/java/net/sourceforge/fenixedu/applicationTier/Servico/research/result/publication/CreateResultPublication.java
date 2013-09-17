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
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;
import pt.ist.fenixframework.Atomic;

public class CreateResultPublication extends ResultPublicationService {

    protected Book run(BookBean bean) {
        if (bean == null) {
            throw new NullPointerException();
        }
        return (Book) associateUnitIfNeeded(bean, ResearchResultMetaDataManager.addDefaultDocument(createBookFromBean(bean)));
    }

    protected BookPart run(BookPartBean bean) {
        if (bean == null) {
            throw new NullPointerException();
        }
        return (BookPart) associateUnitIfNeeded(bean,
                ResearchResultMetaDataManager.addDefaultDocument(createBookPartFromBean(bean)));
    }

    protected Article run(ArticleBean bean) {
        if (bean == null) {
            throw new NullPointerException();
        }
        return (Article) associateUnitIfNeeded(bean,
                ResearchResultMetaDataManager.addDefaultDocument(createArticleFromBean(bean)));
    }

    protected Inproceedings run(InproceedingsBean bean) {
        if (bean == null) {
            throw new NullPointerException();
        }
        return (Inproceedings) associateUnitIfNeeded(bean,
                ResearchResultMetaDataManager.addDefaultDocument(createInproceedingsFromBean(bean)));
    }

    protected Proceedings run(ProceedingsBean bean) {
        if (bean == null) {
            throw new NullPointerException();
        }
        return (Proceedings) associateUnitIfNeeded(bean,
                ResearchResultMetaDataManager.addDefaultDocument(createProceedingsFromBean(bean)));
    }

    protected Thesis run(ThesisBean bean) {
        if (bean == null) {
            throw new NullPointerException();
        }
        return (Thesis) associateUnitIfNeeded(bean, ResearchResultMetaDataManager.addDefaultDocument(createThesisFromBean(bean)));
    }

    protected Manual run(ManualBean bean) {
        if (bean == null) {
            throw new NullPointerException();
        }
        return (Manual) associateUnitIfNeeded(bean, ResearchResultMetaDataManager.addDefaultDocument(createManualFromBean(bean)));
    }

    protected TechnicalReport run(TechnicalReportBean bean) {
        if (bean == null) {
            throw new NullPointerException();
        }
        return (TechnicalReport) associateUnitIfNeeded(bean,
                ResearchResultMetaDataManager.addDefaultDocument(createTechnicalReportFromBean(bean)));
    }

    protected OtherPublication run(OtherPublicationBean bean) {
        if (bean == null) {
            throw new NullPointerException();
        }
        return (OtherPublication) associateUnitIfNeeded(bean,
                ResearchResultMetaDataManager.addDefaultDocument(createOtherPublicationFromBean(bean)));
    }

    private ResearchResult associateUnitIfNeeded(ResultPublicationBean bean, ResearchResult publication) {
        if (bean.getUnit() != null) {
            publication.addUnitAssociation(bean.getUnit(), ResultUnitAssociationRole.Participant);
        }
        return publication;
    }

    // Service Invokers migrated from Berserk

    private static final CreateResultPublication serviceInstance = new CreateResultPublication();

    @Atomic
    public static Book runCreateResultPublication(BookBean bean) {
        return serviceInstance.run(bean);
    }

    @Atomic
    public static ResearchResultPublication runCreateResultPublication(ResultPublicationBean currentPublicationBean) {
        if (currentPublicationBean instanceof BookBean) {
            return serviceInstance.run((BookBean) currentPublicationBean);
        } else if (currentPublicationBean instanceof BookPartBean) {
            return serviceInstance.run((BookPartBean) currentPublicationBean);
        } else if (currentPublicationBean instanceof ArticleBean) {
            return serviceInstance.run((ArticleBean) currentPublicationBean);
        } else if (currentPublicationBean instanceof InproceedingsBean) {
            return serviceInstance.run((InproceedingsBean) currentPublicationBean);
        } else if (currentPublicationBean instanceof ProceedingsBean) {
            return serviceInstance.run((ProceedingsBean) currentPublicationBean);
        } else if (currentPublicationBean instanceof ThesisBean) {
            return serviceInstance.run((ThesisBean) currentPublicationBean);
        } else if (currentPublicationBean instanceof ManualBean) {
            return serviceInstance.run((ManualBean) currentPublicationBean);
        } else if (currentPublicationBean instanceof TechnicalReportBean) {
            return serviceInstance.run((TechnicalReportBean) currentPublicationBean);
        } else if (currentPublicationBean instanceof OtherPublicationBean) {
            return serviceInstance.run((OtherPublicationBean) currentPublicationBean);
        } else {
            throw new UnsupportedOperationException("Sorry, I don't know how to handle "
                    + currentPublicationBean.getClass().getSimpleName());
        }
    }

}