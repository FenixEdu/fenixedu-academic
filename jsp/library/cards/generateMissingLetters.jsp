<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.generateMissingLetters"/>
</h2>	



<logic:present name="nothingToBeDone">
	<br/><p><em><bean:message key="message.card.noMissingLetters" bundle="LIBRARY_RESOURCES"/></em></p>
</logic:present>