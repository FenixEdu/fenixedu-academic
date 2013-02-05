package net.sourceforge.fenixedu.domain.tests.answers;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;

public class ConcreteAnswer implements Serializable {
    private static final long serialVersionUID = 1L;

    public final Object answer;

    public ConcreteAnswer(Object answer) {
        this.answer = answer;
    }

    public ConcreteAnswer(List<? extends DomainObject> answer) {
        this.answer = answer;
    }

    public Object getAnswer() {
        return answer;
    }

    /*
     * public String getStringAnswer() { return (String) answer; }
     * 
     * public void setStringAnswer(String answer) { this.answer = answer; }
     * 
     * public Double getNumericAnswer() { return (Double) answer; }
     * 
     * public void setNumericAnswer(Double answer) { this.answer = answer; }
     * 
     * public DateTime getDateAnswer() { return (DateTime) answer; }
     * 
     * public void setDateAnswer(DateTime answer) { this.answer = answer; }
     * 
     * public List<NewChoice> getMultipleChoiceAnswer() { List<NewChoice>
     * choices = new ArrayList<NewChoice>(); List<NewChoice> answer =
     * (List<NewChoice>) this.answer;
     * 
     * for(NewChoice choice : answer) { choices.add(choice.getObject()); }
     * 
     * return choices; }
     * 
     * public void setMultipleChoiceAnswer(List<NewChoice> answer) {
     * List<NewChoice> choices = new ArrayList<NewChoice>();
     * 
     * for(NewChoice choice : answer) { choices.add(new NewChoice(choice)); }
     * 
     * this.answer = choices; }
     */
}
