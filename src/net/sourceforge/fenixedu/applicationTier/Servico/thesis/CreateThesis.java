package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisBean;

public class CreateThesis extends Service {

    public Thesis run(Integer degreeCurricularPlanId, ThesisBean bean) {
        Thesis thesis = new Thesis();
        
        thesis.setTitle(bean.getTitle());
        thesis.setComment(bean.getComment());
        
        //TODO: thesis.setEnrolment(student.getThesisEnrolment());
        
        thesis.setOrientator(bean.getOrientator());
        thesis.setCoorientator(bean.getCoorientator());
        thesis.setPresident(bean.getPresident());
        
        for (Person vowel : bean.getVowels()) {
            thesis.addVowels(vowel);
        }
        
        return thesis;
    }
    
}
