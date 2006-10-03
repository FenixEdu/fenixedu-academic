package net.sourceforge.fenixedu.domain.tests.answers;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;

public class ConcreteAnswer implements Serializable {
	private static final long serialVersionUID = 1L;

	public Object answer;

	public ConcreteAnswer(Object answer) {
		super();

		this.answer = answer;
	}

	public ConcreteAnswer(List<? extends DomainObject> answer) {
		super();

		this.answer = CollectionUtils.toReferences(answer);
	}

	public Object getAnswer() {
		if(answer instanceof List) {
			return CollectionUtils.toObjects((List<DomainReference<DomainObject>>) answer);
		}
		
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
	 * choices = new ArrayList<NewChoice>(); List<DomainReference<NewChoice>>
	 * answer = (List<DomainReference<NewChoice>>) this.answer;
	 * 
	 * for(DomainReference<NewChoice> choice : answer) {
	 * choices.add(choice.getObject()); }
	 * 
	 * return choices; }
	 * 
	 * public void setMultipleChoiceAnswer(List<NewChoice> answer) { List<DomainReference<NewChoice>>
	 * choices = new ArrayList<DomainReference<NewChoice>>();
	 * 
	 * for(NewChoice choice : answer) { choices.add(new DomainReference<NewChoice>(choice)); }
	 * 
	 * this.answer = choices; }
	 */
}
