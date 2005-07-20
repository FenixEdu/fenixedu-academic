package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.credits.util.InfoCreditsBuilder;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

/**
 * @author EP15
 * @author Ivo Brandão
 */
public class Teacher extends Teacher_Base {
    private Map creditsMap = new HashMap();

    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/
    
    public void addToTeacherSheet(IPublication publication, PublicationArea publicationArea) throws DomainException {

        checkForMaximumPublicationsInTeacherSheet(publicationArea);

        new PublicationTeacher(publication, this, publicationArea);
        
    }
    
    public void removeFromTeacherSheet(IPublication publication) throws DomainException {
        Iterator<IPublicationTeacher> iterator = getTeacherPublications().iterator();

        while(iterator.hasNext())
        {
            IPublicationTeacher publicationTeacher = iterator.next();
            if(publicationTeacher.getPublication().equals(publication))
            {
                iterator.remove();
                publicationTeacher.delete();
                return;
            }
        }
        //In case the publication isn't associated with the teacher
        throw new DomainException(publication.getTitle());
    }

    public List responsibleFors() {
        List<IProfessorship> professorships = this.getProfessorships();
        List res = new ArrayList();

        for (IProfessorship professorship : professorships) {
            if (professorship.getResponsibleFor())
                res.add(professorship);
        }
        return res;
    }

    public IProfessorship responsibleFor(Integer executionCourseId) {
        List<IProfessorship> professorships = this.getProfessorships();

        for (IProfessorship professorship : professorships) {
            if (professorship.getResponsibleFor()
                    && professorship.getExecutionCourse().getIdInternal().equals(executionCourseId))
                return professorship;
        }
        return null;
    }

    public void updateResponsabilitiesFor(Integer executionYearId, List<Integer> executionCourses) throws MaxResponsibleForExceed, InvalidCategory {

        if (executionYearId == null || executionCourses == null)
            throw new NullPointerException();

        List<IProfessorship> professorships = this.getProfessorships();

        for (IProfessorship professorship : professorships) {
            IExecutionCourse executionCourse = professorship.getExecutionCourse();
            if (executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal().equals(
                    executionYearId)) {                
                if (executionCourses.contains(executionCourse.getIdInternal())){
                    ResponsibleForValidator.getInstance().validateResponsibleForList(this, executionCourse, professorship);
                    professorship.setResponsibleFor(true);
                }
                else{
                    professorship.setResponsibleFor(false);
                }
            }
        }
    }
    
    /********************************************************************
     *                          OTHER METHODS                           *
     ********************************************************************/    
    
    public String toString() {
        String result = "[Dominio.Teacher ";
        result += ", teacherNumber=" + getTeacherNumber();
        result += ", person=" + getPerson();
        result += ", category= " + getCategory();
        result += "]";
        return result;
    }

    public InfoCredits getExecutionPeriodCredits(IExecutionPeriod executionPeriod) {
        InfoCredits credits = (InfoCredits) creditsMap.get(executionPeriod);
        if (credits == null) {
            credits = InfoCreditsBuilder.build(this, executionPeriod);
            creditsMap.put(executionPeriod, credits);
        }
        return credits;
    }

    public void notifyCreditsChange(CreditsEvent creditsEvent, ICreditsEventOriginator originator) {
        Iterator iterator = this.creditsMap.keySet().iterator();
        while (iterator.hasNext()) {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) iterator.next();
            if (originator.belongsToExecutionPeriod(executionPeriod)) {
                iterator.remove();
            }
        }
    }
    
    /********************************************************************
     *                         PRIVATE METHODS                          *
     ********************************************************************/

    private void checkForMaximumPublicationsInTeacherSheet(PublicationArea publicationArea) throws DomainException {
        int publicationCount = 0;
        for (IPublicationTeacher publicationTeacher : getTeacherPublications()) {
            if (publicationTeacher.getPublicationArea().equals(publicationArea)) {
                publicationCount++;
            }
        }
        if (publicationCount >= 5) {
            throw new DomainException("error.teacherSheetFull", publicationArea.getName());
        }
    }
}
