package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.tests.NewPresentationMaterialType;
import net.sourceforge.fenixedu.domain.tests.NewTestElement;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class PresentationMaterialBean implements Serializable {

	private NewPresentationMaterialType presentationMaterialType;

	private DomainReference<NewTestElement> testElement;

	private String returnPath;

	private Integer returnId;

	private String contextKey;

	private transient InputStream inputStream;
	
	private String originalFileName;
	
	private String fileContentType;
	
	private String fileName;
	
	private boolean inline;

	public PresentationMaterialBean(NewTestElement testElement) {
		presentationMaterialType = null;
		this.setTestElement(testElement);
	}

	public PresentationMaterialBean(NewTestElement testElement, String returnPath, Integer returnId,
			String contextKey) {
		this(testElement);

		this.setReturnPath(returnPath);
		this.setReturnId(returnId);
		this.setContextKey(contextKey);
	}

	public PresentationMaterialBean(PresentationMaterialBean bean) {
		this(bean.getTestElement(), bean.getReturnPath(), bean.getReturnId(), bean.getContextKey());
	}

	public NewPresentationMaterialType getPresentationMaterialType() {
		return presentationMaterialType;
	}

	public void setPresentationMaterialType(NewPresentationMaterialType presentationMaterialType) {
		this.presentationMaterialType = presentationMaterialType;
	}

	public NewTestElement getTestElement() {
		return testElement.getObject();
	}

	public void setTestElement(NewTestElement testElement) {
		this.testElement = new DomainReference<NewTestElement>(testElement);
	}

	public String getContextKey() {
		return contextKey;
	}

	public void setContextKey(String contextKey) {
		this.contextKey = contextKey;
	}

	public Integer getReturnId() {
		return returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}

	public String getReturnPath() {
		return returnPath;
	}

	public void setReturnPath(String returnPath) {
		this.returnPath = returnPath;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream fileInputStream) {
		this.inputStream = fileInputStream;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isInline() {
		return inline;
	}

	public void setInline(boolean inline) {
		this.inline = inline;
	}

	public LabelFormatter getContextLabel() {
		LabelFormatter formatter = new LabelFormatter();

		formatter.appendLabel(this.getContextKey(), "TESTS_RESOURCES");

		return formatter;
	}

}
