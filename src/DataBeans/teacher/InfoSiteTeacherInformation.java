/*
 * Created on 16/Nov/2003
 *  
 */
package DataBeans.teacher;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.ISiteComponent;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoSiteTeacherInformation implements ISiteComponent
{
    private InfoTeacher infoTeacher;
    private List infoQualifications;
    private List infoProfessionalCareers;
    private List infoTeachingCareers;
    private InfoServiceProviderRegime infoServiceProviderRegime;
    private List infoExternalActivities;
    private List infoLecturingExecutionCourses;
    private List infoResponsibleExecutionCourses;
    private InfoWeeklyOcupation infoWeeklyOcupation;
    private InfoOrientation infoDegreeOrientation;
    private InfoOrientation infoMasterOrientation;
    private InfoOrientation infoPhdOrientation;
    private InfoPublicationsNumber infoComunicationPublicationsNumber;
    private InfoPublicationsNumber infoMagArticlePublicationsNumber;
    private InfoPublicationsNumber infoAuthorBookPublicationsNumber;
    private InfoPublicationsNumber infoEditBookPublicationsNumber;
    private InfoPublicationsNumber infoArticleChapterPublicationsNumber;
    private List infoOldCientificPublications;
    private List infoOldDidacticPublications;
    private InfoExecutionPeriod infoExecutionPeriod;

    /**
	 *  
	 */
    public InfoSiteTeacherInformation()
    {
    }

    public Date getLastModificationDate()
    {
        List dates = new ArrayList();
        dates.add(infoServiceProviderRegime.getLastModificationDate());
        dates.add(infoWeeklyOcupation.getLastModificationDate());
        dates.add(infoDegreeOrientation.getLastModificationDate());
        dates.add(infoMasterOrientation.getLastModificationDate());
        dates.add(infoPhdOrientation.getLastModificationDate());
        dates.add(infoComunicationPublicationsNumber.getLastModificationDate());
        dates.add(infoMagArticlePublicationsNumber.getLastModificationDate());
        dates.add(infoAuthorBookPublicationsNumber.getLastModificationDate());
        dates.add(infoEditBookPublicationsNumber.getLastModificationDate());
        dates.add(infoArticleChapterPublicationsNumber.getLastModificationDate());
        dates.addAll(CollectionUtils.collect(infoProfessionalCareers, new Transformer()
        {
            public Object transform(Object arg0)
            {
                InfoProfessionalCareer infoProfessionalCareer = (InfoProfessionalCareer) arg0;
                return infoProfessionalCareer.getLastModificationDate();
            }
        }));
        dates.addAll(CollectionUtils.collect(infoTeachingCareers, new Transformer()
        {
            public Object transform(Object arg0)
            {
                InfoTeachingCareer infoTeachingCareer = (InfoTeachingCareer) arg0;
                return infoTeachingCareer.getLastModificationDate();
            }
        }));
        dates.addAll(CollectionUtils.collect(infoExternalActivities, new Transformer()
        {
            public Object transform(Object arg0)
            {
                InfoExternalActivity infoExternalActivity = (InfoExternalActivity) arg0;
                return infoExternalActivity.getLastModificationDate();
            }
        }));
        dates.addAll(CollectionUtils.collect(infoOldCientificPublications, new Transformer()
        {
            public Object transform(Object arg0)
            {
                InfoOldPublication infoOldPublication = (InfoOldPublication) arg0;
                return infoOldPublication.getLastModificationDate();
            }
        }));
        dates.addAll(CollectionUtils.collect(infoOldDidacticPublications, new Transformer()
        {
            public Object transform(Object arg0)
            {
                InfoOldPublication infoOldPublication = (InfoOldPublication) arg0;
                return infoOldPublication.getLastModificationDate();
            }
        }));
        return getMostRecentDate(dates);
    }

    /**
	 * @param dates
	 * @return
	 */
    private Date getMostRecentDate(List dates)
    {
        Date maxDate = new Date(Long.MAX_VALUE);
        Date minDate = maxDate;
        Iterator iter = dates.iterator();
        while (iter.hasNext())
        {
            Date date = (Date) iter.next();

            if (date == null)
                continue;

            if (date.getTime() < minDate.getTime())
                minDate = date;
        }

        // if the minDate is equal to maxDate then the information wasn't filled
        if (minDate == maxDate)
            minDate = null;

        return minDate;
    }

    /**
	 * @return Returns the infoExternalActivities.
	 */
    public List getInfoExternalActivities()
    {
        return infoExternalActivities;
    }

    /**
	 * @return Returns the infoLecturingExecutionCourses.
	 */
    public List getInfoLecturingExecutionCourses()
    {
        return infoLecturingExecutionCourses;
    }

    /**
	 * @param infoLecturingExecutionCourses
	 *            The infoLecturingExecutionCourses to set.
	 */
    public void setInfoLecturingExecutionCourses(List infoLecturingExecutionCourses)
    {
        this.infoLecturingExecutionCourses = infoLecturingExecutionCourses;
    }

    /**
	 * @return Returns the infoResponsibleExecutionCourses.
	 */
    public List getInfoResponsibleExecutionCourses()
    {
        return infoResponsibleExecutionCourses;
    }

    /**
	 * @param infoResponsibleExecutionCourses
	 *            The infoResponsibleExecutionCourses to set.
	 */
    public void setInfoResponsibleExecutionCourses(List infoResponsibleExecutionCourses)
    {
        this.infoResponsibleExecutionCourses = infoResponsibleExecutionCourses;
    }

    /**
	 * @param infoExternalActivities
	 *            The infoExternalActivities to set.
	 */
    public void setInfoExternalActivities(List infoExternalActivities)
    {
        this.infoExternalActivities = infoExternalActivities;
    }

    /**
	 * @return Returns the infoProfessionalCareers.
	 */
    public List getInfoProfessionalCareers()
    {
        return infoProfessionalCareers;
    }

    /**
	 * @param infoProfessionalCareers
	 *            The infoProfessionalCareers to set.
	 */
    public void setInfoProfessionalCareers(List infoProfessionalCareers)
    {
        this.infoProfessionalCareers = infoProfessionalCareers;
    }

    /**
	 * @return Returns the infoQualifications.
	 */
    public List getInfoQualifications()
    {
        return infoQualifications;
    }

    /**
	 * @param infoQualifications
	 *            The infoQualifications to set.
	 */
    public void setInfoQualifications(List infoQualifications)
    {
        this.infoQualifications = infoQualifications;
    }

    /**
	 * @return Returns the infoServiceProviderRegime.
	 */
    public InfoServiceProviderRegime getInfoServiceProviderRegime()
    {
        return infoServiceProviderRegime;
    }

    /**
	 * @param infoServiceProviderRegime
	 *            The infoServiceProviderRegime to set.
	 */
    public void setInfoServiceProviderRegime(InfoServiceProviderRegime infoServiceProviderRegime)
    {
        this.infoServiceProviderRegime = infoServiceProviderRegime;
    }

    /**
	 * @return Returns the infoTeacher.
	 */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
	 * @param infoTeacher
	 *            The infoTeacher to set.
	 */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }

    /**
	 * @return Returns the infoTeachingCareers.
	 */
    public List getInfoTeachingCareers()
    {
        return infoTeachingCareers;
    }

    /**
	 * @param infoTeachingCareers
	 *            The infoTeachingCareers to set.
	 */
    public void setInfoTeachingCareers(List infoTeachingCareers)
    {
        this.infoTeachingCareers = infoTeachingCareers;
    }

    /**
	 * @return Returns the infoWeeklyOcupation.
	 */
    public InfoWeeklyOcupation getInfoWeeklyOcupation()
    {
        return infoWeeklyOcupation;
    }

    /**
	 * @param infoWeeklyOcupation
	 *            The infoWeeklyOcupation to set.
	 */
    public void setInfoWeeklyOcupation(InfoWeeklyOcupation infoWeeklyOcupation)
    {
        this.infoWeeklyOcupation = infoWeeklyOcupation;
    }

    /**
	 * @return Returns the infoDegreeOrientation.
	 */
    public InfoOrientation getInfoDegreeOrientation()
    {
        return infoDegreeOrientation;
    }

    /**
	 * @param infoDegreeOrientation
	 *            The infoDegreeOrientation to set.
	 */
    public void setInfoDegreeOrientation(InfoOrientation infoDegreeOrientation)
    {
        this.infoDegreeOrientation = infoDegreeOrientation;
    }

    /**
	 * @return Returns the infoMasterOrientation.
	 */
    public InfoOrientation getInfoMasterOrientation()
    {
        return infoMasterOrientation;
    }

    /**
	 * @param infoMasterOrientation
	 *            The infoMasterOrientation to set.
	 */
    public void setInfoMasterOrientation(InfoOrientation infoMasterOrientation)
    {
        this.infoMasterOrientation = infoMasterOrientation;
    }

    /**
	 * @return Returns the infoPhdOrientation.
	 */
    public InfoOrientation getInfoPhdOrientation()
    {
        return infoPhdOrientation;
    }

    /**
	 * @param infoPhdOrientation
	 *            The infoPhdOrientation to set.
	 */
    public void setInfoPhdOrientation(InfoOrientation infoPhdOrientation)
    {
        this.infoPhdOrientation = infoPhdOrientation;
    }

    /**
	 * @return Returns the infoArticleChapterPublicationsNumber.
	 */
    public InfoPublicationsNumber getInfoArticleChapterPublicationsNumber()
    {
        return infoArticleChapterPublicationsNumber;
    }

    /**
	 * @param infoArticleChapterPublicationsNumber
	 *            The infoArticleChapterPublicationsNumber to set.
	 */
    public void setInfoArticleChapterPublicationsNumber(InfoPublicationsNumber infoArticleChapterPublicationsNumber)
    {
        this.infoArticleChapterPublicationsNumber = infoArticleChapterPublicationsNumber;
    }

    /**
	 * @return Returns the infoAuthorBookPublicationsNumber.
	 */
    public InfoPublicationsNumber getInfoAuthorBookPublicationsNumber()
    {
        return infoAuthorBookPublicationsNumber;
    }

    /**
	 * @param infoAuthorBookPublicationsNumber
	 *            The infoAuthorBookPublicationsNumber to set.
	 */
    public void setInfoAuthorBookPublicationsNumber(InfoPublicationsNumber infoAuthorBookPublicationsNumber)
    {
        this.infoAuthorBookPublicationsNumber = infoAuthorBookPublicationsNumber;
    }

    /**
	 * @return Returns the infoComunicationPublicationsNumber.
	 */
    public InfoPublicationsNumber getInfoComunicationPublicationsNumber()
    {
        return infoComunicationPublicationsNumber;
    }

    /**
	 * @param infoComunicationPublicationsNumber
	 *            The infoComunicationPublicationsNumber to set.
	 */
    public void setInfoComunicationPublicationsNumber(InfoPublicationsNumber infoComunicationPublicationsNumber)
    {
        this.infoComunicationPublicationsNumber = infoComunicationPublicationsNumber;
    }

    /**
	 * @return Returns the infoEditBookPublicationsNumber.
	 */
    public InfoPublicationsNumber getInfoEditBookPublicationsNumber()
    {
        return infoEditBookPublicationsNumber;
    }

    /**
	 * @param infoEditBookPublicationsNumber
	 *            The infoEditBookPublicationsNumber to set.
	 */
    public void setInfoEditBookPublicationsNumber(InfoPublicationsNumber infoEditBookPublicationsNumber)
    {
        this.infoEditBookPublicationsNumber = infoEditBookPublicationsNumber;
    }

    /**
	 * @return Returns the infoMagArticlePublicationsNumber.
	 */
    public InfoPublicationsNumber getInfoMagArticlePublicationsNumber()
    {
        return infoMagArticlePublicationsNumber;
    }

    /**
	 * @param infoMagArticlePublicationsNumber
	 *            The infoMagArticlePublicationsNumber to set.
	 */
    public void setInfoMagArticlePublicationsNumber(InfoPublicationsNumber infoMagArticlePublicationsNumber)
    {
        this.infoMagArticlePublicationsNumber = infoMagArticlePublicationsNumber;
    }

    /**
	 * @return Returns the infoOldCientificPublications.
	 */
    public List getInfoOldCientificPublications()
    {
        return infoOldCientificPublications;
    }

    /**
	 * @param infoOldCientificPublications
	 *            The infoOldCientificPublications to set.
	 */
    public void setInfoOldCientificPublications(List infoOldCientificPublications)
    {
        this.infoOldCientificPublications = infoOldCientificPublications;
    }

    /**
	 * @return Returns the infoOldDidacticPublications.
	 */
    public List getInfoOldDidacticPublications()
    {
        return infoOldDidacticPublications;
    }

    /**
	 * @param infoOldDidacticPublications
	 *            The infoOldDidacticPublications to set.
	 */
    public void setInfoOldDidacticPublications(List infoOldDidacticPublications)
    {
        this.infoOldDidacticPublications = infoOldDidacticPublications;
    }

    /**
	 * @return Returns the infoExecutionPeriod.
	 */
    public InfoExecutionPeriod getInfoExecutionPeriod()
    {
        return infoExecutionPeriod;
    }

    /**
	 * @param infoExecutionPeriod
	 *            The infoExecutionPeriod to set.
	 */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
    {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

}
