package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class UpdateThesis extends Service {

    public Thesis run(Integer degreeCurricularPlanId, ThesisBean bean) {
        Thesis thesis;
        
        if (bean.isNewThesis()) {
        //TODO: find thesis enrolment for student
        //Thesis thesis = new Thesis(bean.getStudent().getThesisEnrolment());
            thesis = new Thesis(bean.getDegree(), (Enrolment) null);
        }
        else {
            thesis = bean.getThesis();
        }
        
        thesis.setTitle(bean.getTitle());
        thesis.setComment(bean.getComment());
        
        thesis.setOrientator(bean.getOrientator());
        thesis.setCoorientator(bean.getCoorientator());
        thesis.setPresident(bean.getPresident());
        
        for (Person vowel : bean.getVowels()) {
            if (! thesis.hasVowels(vowel)) {
                thesis.addVowels(vowel);
            }
        }
        
        thesis.getVowels().retainAll(bean.getVowels());
        
        return thesis;
    }
    
}
