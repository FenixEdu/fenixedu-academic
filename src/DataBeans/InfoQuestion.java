/*
 * Created on 25/Jul/2003
 *
 */
package DataBeans;

import java.util.List;

/**
 * @author Susana Fernandes
 */
public class InfoQuestion extends InfoObject {
	private String xmlFile;
	private String xmlFileName;
	private InfoMetadata infoMetadata;
	private List question;
	private List options;
	private Integer questionValue;
	private String questionCardinality;

	public InfoQuestion() {
	}

	public InfoMetadata getInfoMetadata() {
		return infoMetadata;
	}

	public String getXmlFile() {
		return xmlFile;
	}

	public String getXmlFileName() {
		return xmlFileName;
	}

	public void setInfoMetadata(InfoMetadata metadata) {
		infoMetadata = metadata;
	}

	public void setXmlFile(String string) {
		xmlFile = string;
	}

	public void setXmlFileName(String string) {
		xmlFileName = string;
	}

	public List getQuestion() {
		return question;
	}

	public void setQuestion(List list) {
		question = list;
	}

	public List getOptions() {
		return options;
	}

	public void setOptions(List list) {
		options = list;
	}

	public Integer getQuestionValue() {
		return questionValue;
	}

	public void setQuestionValue(Integer integer) {
		questionValue = integer;
	}

	public String getQuestionCardinality() {
		return questionCardinality;
	}

	public void setQuestionCardinality(String string) {
		questionCardinality = string;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoQuestion) {
			InfoQuestion infoQuestion = (InfoQuestion) obj;
			result = getIdInternal().equals(infoQuestion.getIdInternal());
			result =
				result
					|| (getXmlFile().equals(infoQuestion.getXmlFile())
						&& (getXmlFileName()
							.equals(infoQuestion.getXmlFileName()))
						&& (getInfoMetadata()
							.equals(infoQuestion.getInfoMetadata()))
						&& (getQuestionValue()
							.equals(infoQuestion.getQuestionValue()))
						&& (getQuestionCardinality()
							.equals(infoQuestion.getQuestionValue()))
						&& (getQuestion().containsAll(infoQuestion.getQuestion()))
						&& (infoQuestion.getQuestion().containsAll(getQuestion()))
						&& (getOptions().containsAll(infoQuestion.getOptions()))
						&& (infoQuestion.getOptions().containsAll(getOptions())));
		}
		return result;
	}
}
