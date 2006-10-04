package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;

public abstract class ResultPublicationBeanConversions {

    public static ResultPublicationBean articleTo(ArticleBean oldBean, ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Book:
	    newBean = new BookBean(oldBean);
	    ((BookBean) newBean).setVolume(oldBean.getVolume());
	    ((BookBean) newBean).setScope(oldBean.getScope());
	    ((BookBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Inbook:
	    newBean = new InbookBean(oldBean);
	    ((InbookBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((InbookBean) newBean).setLastPage(oldBean.getLastPage());
	    ((InbookBean) newBean).setVolume(oldBean.getVolume());
	    return newBean;
	case Incollection:
	    newBean = new IncollectionBean(oldBean);
	    ((IncollectionBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((IncollectionBean) newBean).setLastPage(oldBean.getLastPage());
	    return newBean;
	case Inproceedings:
	    newBean = new InproceedingsBean(oldBean);
	    ((InproceedingsBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((InproceedingsBean) newBean).setLastPage(oldBean.getLastPage());
	    ((InproceedingsBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Manual:
	    return new ManualBean(oldBean);
	case OtherPublication:
	    newBean = new OtherPublicationBean(oldBean);
	    ((OtherPublicationBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Proceedings:
	    return new ProceedingsBean(oldBean);
	case TechnicalReport:
	    newBean = new TechnicalReportBean(oldBean);
	    ((TechnicalReportBean) newBean).setNumber(oldBean.getNumber());
	    ((TechnicalReportBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean bookTo(BookBean oldBean, ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Article:
	    newBean = new ArticleBean(oldBean);
	    ((ArticleBean) newBean).setVolume(oldBean.getVolume());
	    ((ArticleBean) newBean).setScope(oldBean.getScope());
	    ((ArticleBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Inbook:
	    newBean = new InbookBean(oldBean);
	    ((InbookBean) newBean).setAddress(oldBean.getAddress());
	    ((InbookBean) newBean).setVolume(oldBean.getVolume());
	    ((InbookBean) newBean).setSeries(oldBean.getSeries());
	    ((InbookBean) newBean).setEdition(oldBean.getEdition());
	    return newBean;
	case Incollection:
	    newBean = new IncollectionBean(oldBean);
	    ((IncollectionBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Inproceedings:
	    newBean = new InproceedingsBean(oldBean);
	    ((InproceedingsBean) newBean).setAddress(oldBean.getAddress());
	    ((InproceedingsBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Manual:
	    newBean = new ManualBean(oldBean);
	    ((ManualBean) newBean).setAddress(oldBean.getAddress());
	    ((ManualBean) newBean).setEdition(oldBean.getEdition());
	    return newBean;
	case OtherPublication:
	    newBean = new OtherPublicationBean(oldBean);
	    ((OtherPublicationBean) newBean).setAddress(oldBean.getAddress());
	    ((OtherPublicationBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((OtherPublicationBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Proceedings:
	    newBean = new ProceedingsBean(oldBean);
	    ((ProceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case TechnicalReport:
	    newBean = new TechnicalReportBean(oldBean);
	    ((TechnicalReportBean) newBean).setAddress(oldBean.getAddress());
	    ((TechnicalReportBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((TechnicalReportBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setAddress(oldBean.getAddress());
	    ((ThesisBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((ThesisBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean inbookTo(InbookBean oldBean, ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Article:
	    newBean = new ArticleBean(oldBean);
	    ((ArticleBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((ArticleBean) newBean).setLastPage(oldBean.getLastPage());
	    ((ArticleBean) newBean).setVolume(oldBean.getVolume());
	    return newBean;
	case Book:
	    newBean = new BookBean(oldBean);
	    ((BookBean) newBean).setAddress(oldBean.getAddress());
	    ((BookBean) newBean).setVolume(oldBean.getVolume());
	    ((BookBean) newBean).setSeries(oldBean.getSeries());
	    ((BookBean) newBean).setEdition(oldBean.getEdition());
	    return newBean;
	case Incollection:
	    newBean = new IncollectionBean(oldBean);
	    ((IncollectionBean) newBean).setAddress(oldBean.getAddress());
	    ((IncollectionBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((IncollectionBean) newBean).setLastPage(oldBean.getLastPage());
	    return newBean;
	case Inproceedings:
	    newBean = new InproceedingsBean(oldBean);
	    ((InproceedingsBean) newBean).setAddress(oldBean.getAddress());
	    ((InproceedingsBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((InproceedingsBean) newBean).setLastPage(oldBean.getLastPage());
	    return newBean;
	case Manual:
	    newBean = new ManualBean(oldBean);
	    ((ManualBean) newBean).setAddress(oldBean.getAddress());
	    ((ManualBean) newBean).setEdition(oldBean.getEdition());
	    return newBean;
	case OtherPublication:
	    newBean = new OtherPublicationBean(oldBean);
	    ((OtherPublicationBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Proceedings:
	    newBean = new ProceedingsBean(oldBean);
	    ((ProceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case TechnicalReport:
	    newBean = new TechnicalReportBean(oldBean);
	    ((TechnicalReportBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean incollectionTo(IncollectionBean oldBean,
	    ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Article:
	    newBean = new ArticleBean(oldBean);
	    ((ArticleBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((ArticleBean) newBean).setLastPage(oldBean.getLastPage());
	    return newBean;
	case Book:
	    newBean = new BookBean(oldBean);
	    ((BookBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Inbook:
	    newBean = new InbookBean(oldBean);
	    ((InbookBean) newBean).setAddress(oldBean.getAddress());
	    ((InbookBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((InbookBean) newBean).setLastPage(oldBean.getLastPage());
	    return newBean;
	case Inproceedings:
	    newBean = new InproceedingsBean(oldBean);
	    ((InproceedingsBean) newBean).setAddress(oldBean.getAddress());
	    ((InproceedingsBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((InproceedingsBean) newBean).setLastPage(oldBean.getLastPage());
	    return newBean;
	case Manual:
	    newBean = new ManualBean(oldBean);
	    ((ManualBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case OtherPublication:
	    newBean = new OtherPublicationBean(oldBean);
	    ((OtherPublicationBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Proceedings:
	    newBean = new ProceedingsBean(oldBean);
	    ((ProceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case TechnicalReport:
	    newBean = new TechnicalReportBean(oldBean);
	    ((TechnicalReportBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean inproceedingsTo(InproceedingsBean oldBean,
	    ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Article:
	    newBean = new ArticleBean(oldBean);
	    ((ArticleBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((ArticleBean) newBean).setLastPage(oldBean.getLastPage());
	    ((ArticleBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Book:
	    newBean = new BookBean(oldBean);
	    ((BookBean) newBean).setAddress(oldBean.getAddress());
	    ((BookBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Inbook:
	    newBean = new InbookBean(oldBean);
	    ((InbookBean) newBean).setAddress(oldBean.getAddress());
	    ((InbookBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((InbookBean) newBean).setLastPage(oldBean.getLastPage());
	    return newBean;
	case Incollection:
	    newBean = new IncollectionBean(oldBean);
	    ((IncollectionBean) newBean).setAddress(oldBean.getAddress());
	    ((IncollectionBean) newBean).setFirstPage(oldBean.getFirstPage());
	    ((IncollectionBean) newBean).setLastPage(oldBean.getLastPage());
	    return newBean;
	case Manual:
	    newBean = new ManualBean(oldBean);
	    ((ManualBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case OtherPublication:
	    newBean = new OtherPublicationBean(oldBean);
	    ((OtherPublicationBean) newBean).setAddress(oldBean.getAddress());
	    ((OtherPublicationBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Proceedings:
	    newBean = new ProceedingsBean(oldBean);
	    ((ProceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case TechnicalReport:
	    newBean = new TechnicalReportBean(oldBean);
	    ((TechnicalReportBean) newBean).setAddress(oldBean.getAddress());
	    ((TechnicalReportBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setAddress(oldBean.getAddress());
	    ((ThesisBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean manualTo(ManualBean oldBean, ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Article:
	    return new ArticleBean(oldBean);
	case Book:
	    newBean = new BookBean(oldBean);
	    ((BookBean) newBean).setAddress(oldBean.getAddress());
	    ((BookBean) newBean).setEdition(oldBean.getEdition());
	    return newBean;
	case Inbook:
	    newBean = new InbookBean(oldBean);
	    ((InbookBean) newBean).setAddress(oldBean.getAddress());
	    ((InbookBean) newBean).setEdition(oldBean.getEdition());
	    return newBean;
	case Incollection:
	    newBean = new IncollectionBean(oldBean);
	    ((IncollectionBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Inproceedings:
	    newBean = new InproceedingsBean(oldBean);
	    ((InproceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case OtherPublication:
	    newBean = new OtherPublicationBean(oldBean);
	    ((OtherPublicationBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Proceedings:
	    newBean = new ProceedingsBean(oldBean);
	    ((ProceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case TechnicalReport:
	    newBean = new TechnicalReportBean(oldBean);
	    ((TechnicalReportBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean otherPublicationTo(OtherPublicationBean oldBean,
	    ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Article:
	    newBean = new ArticleBean(oldBean);
	    ((ArticleBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Book:
	    newBean = new BookBean(oldBean);
	    ((BookBean) newBean).setAddress(oldBean.getAddress());
	    ((BookBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((BookBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Inbook:
	    newBean = new InbookBean(oldBean);
	    ((InbookBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Incollection:
	    newBean = new IncollectionBean(oldBean);
	    ((IncollectionBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Inproceedings:
	    newBean = new InproceedingsBean(oldBean);
	    ((InproceedingsBean) newBean).setAddress(oldBean.getAddress());
	    ((InproceedingsBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Manual:
	    newBean = new ManualBean(oldBean);
	    ((ManualBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Proceedings:
	    newBean = new ProceedingsBean(oldBean);
	    ((ProceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case TechnicalReport:
	    newBean = new TechnicalReportBean(oldBean);
	    ((TechnicalReportBean) newBean).setAddress(oldBean.getAddress());
	    ((TechnicalReportBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((TechnicalReportBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setAddress(oldBean.getAddress());
	    ((ThesisBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((ThesisBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean proceedingsTo(ProceedingsBean oldBean, ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Article:
	    return new ArticleBean(oldBean);
	case Book:
	    newBean = new BookBean(oldBean);
	    ((BookBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Inbook:
	    newBean = new InbookBean(oldBean);
	    ((InbookBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Incollection:
	    newBean = new IncollectionBean(oldBean);
	    ((IncollectionBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Inproceedings:
	    newBean = new InproceedingsBean(oldBean);
	    ((InproceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Manual:
	    newBean = new ManualBean(oldBean);
	    ((ManualBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case OtherPublication:
	    newBean = new OtherPublicationBean(oldBean);
	    ((OtherPublicationBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case TechnicalReport:
	    newBean = new TechnicalReportBean(oldBean);
	    ((TechnicalReportBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean technicalReportTo(TechnicalReportBean oldBean,
	    ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Article:
	    newBean = new ArticleBean(oldBean);
	    ((ArticleBean) newBean).setNumber(oldBean.getNumber());
	    ((ArticleBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Book:
	    newBean = new BookBean(oldBean);
	    ((BookBean) newBean).setAddress(oldBean.getAddress());
	    ((BookBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((BookBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Inbook:
	    newBean = new InbookBean(oldBean);
	    ((InbookBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Incollection:
	    newBean = new IncollectionBean(oldBean);
	    ((IncollectionBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Inproceedings:
	    newBean = new InproceedingsBean(oldBean);
	    ((InproceedingsBean) newBean).setAddress(oldBean.getAddress());
	    ((InproceedingsBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Manual:
	    newBean = new ManualBean(oldBean);
	    ((ManualBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case OtherPublication:
	    newBean = new OtherPublicationBean(oldBean);
	    ((OtherPublicationBean) newBean).setAddress(oldBean.getAddress());
	    ((OtherPublicationBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((OtherPublicationBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Proceedings:
	    newBean = new ProceedingsBean(oldBean);
	    ((ProceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setAddress(oldBean.getAddress());
	    ((ThesisBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((ThesisBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean thesisTo(ThesisBean oldBean, ResultPublicationType type) {
	ResultPublicationBean newBean = null;
	switch (type) {
	case Article:
	    newBean = new ArticleBean(oldBean);
	    return newBean;
	case Book:
	    newBean = new BookBean(oldBean);
	    ((BookBean) newBean).setAddress(oldBean.getAddress());
	    ((BookBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((BookBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Inbook:
	    newBean = new InbookBean(oldBean);
	    ((InbookBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Incollection:
	    newBean = new IncollectionBean(oldBean);
	    ((IncollectionBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Inproceedings:
	    newBean = new InproceedingsBean(oldBean);
	    ((InproceedingsBean) newBean).setAddress(oldBean.getAddress());
	    ((InproceedingsBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Manual:
	    newBean = new ManualBean(oldBean);
	    ((ManualBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case OtherPublication:
	    newBean = new OtherPublicationBean(oldBean);
	    ((OtherPublicationBean) newBean).setAddress(oldBean.getAddress());
	    ((OtherPublicationBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((OtherPublicationBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case Proceedings:
	    newBean = new ProceedingsBean(oldBean);
	    ((ProceedingsBean) newBean).setAddress(oldBean.getAddress());
	    return newBean;
	case Thesis:
	    newBean = new ThesisBean(oldBean);
	    ((ThesisBean) newBean).setAddress(oldBean.getAddress());
	    ((ThesisBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((ThesisBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	case TechnicalReport:
	    newBean = new TechnicalReportBean(oldBean);
	    ((TechnicalReportBean) newBean).setAddress(oldBean.getAddress());
	    ((TechnicalReportBean) newBean).setNumberPages(oldBean.getNumberPages());
	    ((TechnicalReportBean) newBean).setLanguage(oldBean.getLanguage());
	    return newBean;
	default:
	    return oldBean;
	}
    }

    public static ResultPublicationBean unstructuredTo(UnstructuredBean oldBean,
	    ResultPublicationType type) {
	switch (type) {
	case Article:
	    return new ArticleBean(oldBean);
	case Book:
	    return new BookBean(oldBean);
	case Inbook:
	    return new InbookBean(oldBean);
	case Incollection:
	    return new IncollectionBean(oldBean);
	case Inproceedings:
	    return new InproceedingsBean(oldBean);
	case Manual:
	    return new ManualBean(oldBean);
	case OtherPublication:
	    return new OtherPublicationBean(oldBean);
	case Proceedings:
	    return new ProceedingsBean(oldBean);
	case Thesis:
	    return new ThesisBean(oldBean);
	case TechnicalReport:
	    return new TechnicalReportBean(oldBean);
	default:
	    return oldBean;
	}
    }

}
