package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.credits.util.InfoCreditsBuilder;

/**
 * @author EP15
 * @author Ivo Brandão
 */
public class Teacher extends Teacher_Base {
    private Map creditsMap = new HashMap();
    
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
    
    public List responsibleFors(){
        List<IProfessorship> professorships = this.getProfessorships();
        List res = new ArrayList();
        
        for(IProfessorship professorship : professorships){
            if(professorship.getResponsibleFor())
                res.add(professorship);
        }
        
        return res;
    }
    
    public IProfessorship responsibleFor(Integer executionCourseId){
        List<IProfessorship> professorships = this.getProfessorships();
        
        for(IProfessorship professorship : professorships){
            if(professorship.getResponsibleFor() && professorship.getExecutionCourse().getIdInternal().equals(executionCourseId))
                return professorship;
        }
        
        return null;
    }
    
    public void updateResponsabilitiesFor(Integer executionYearId, List<Integer> newResponsabilities){
        List<IProfessorship> responsibleFors = this.responsibleFors();
        
        for(IProfessorship professorship : responsibleFors){
            IExecutionCourse executionCourse = professorship.getExecutionCourse();
            if(executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal().equals(executionYearId)){
                if(newResponsabilities.contains(executionCourse.getIdInternal()))
                    professorship.setResponsibleFor(true);
                else
                    professorship.setResponsibleFor(false);
            }
        }
    }
}
