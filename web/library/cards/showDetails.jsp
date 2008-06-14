<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.details"/>
</h2>
	
<p>
	<span class="warning0">
		<html:messages id="message" property="message" message="true">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>

<fr:form action="/cardManagement.do?method=generatePdfCard">
	<fr:edit id="libraryCardSearch" name="libraryCardSearch" visible="false"/>
	<fr:edit id="libraryCardDTO" name="libraryCardDTO" visible="false"/>
	<fr:view name="libraryCardDTO" schema="library.card.generated">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright"/>
		</fr:layout>			
	</fr:view>

	<p>
		<html:submit property="modify"><bean:message key="button.modify" bundle="LIBRARY_RESOURCES"/></html:submit>
		<html:submit><bean:message key="button.generateCard" bundle="LIBRARY_RESOURCES"/></html:submit>
		<html:submit property="back" bundle="HTMLALT_RESOURCES" altKey="submit.back">
			<bean:message key="button.back" bundle="LIBRARY_RESOURCES"/>
		</html:submit>
	</p>
</fr:form>