<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.generateMissingCards"/>
</h2>	

<logic:notPresent name="nothingMissing">
	<p>
		<html:link page="/cardManagement.do?method=generateMissingCards">
			<bean:message key="link.card.generateMissingCards" bundle="LIBRARY_RESOURCES"/>
		</html:link>
	</p>
</logic:notPresent>

<logic:present name="nothingMissing">
	<p><em><bean:message key="message.card.noMissingCards" bundle="LIBRARY_RESOURCES"/></em></p>
</logic:present>