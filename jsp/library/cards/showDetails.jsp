<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.generated"/>
</h2>
	
<fr:form action="/cardManagement.do?method=generatePdfCard">
	<fr:edit id="libraryCard" name="libraryCard" visible="false"/>
	
	<fr:view name="libraryCard" schema="library.card.generated">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5"/>
		</fr:layout>			
	</fr:view>
	
	<html:submit ><bean:message key="button.emitCard" bundle="LIBRARY_RESOURCES"/></html:submit>
	<html:cancel property="cancel" bundle="HTMLALT_RESOURCES" altKey="submit.back">
		<bean:message key="button.back" bundle="LIBRARY_RESOURCES"/>
	</html:cancel>
</fr:form>