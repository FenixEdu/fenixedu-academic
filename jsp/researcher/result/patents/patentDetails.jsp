<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><html:errors/></span>
<html:messages id="message" message="true" bundle="RESEARCHER_RESOURCES">
	<span class="error">
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present role="RESEARCHER">
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.patentsManagement.title"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.patentDetailsUseCase.title"/></h3>

	<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.authors"/></b></p>	
	<fr:view name="authorsList" layout="tabular-list">
		<fr:layout>
			<fr:property name="subLayout" value="values"/>
			<fr:property name="subSchema" value="result.authors"/>
		</fr:layout>
	</fr:view>
	<br/>
	
	<fr:view name="patent" schema="patent.details">
		<fr:layout name="tabular">
	        <fr:property name="classes" value="style1"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<br/>
		
	<html:form action="/result/patents/patentsManagement">
		<html:hidden property="method" value="listPatents"/>
		<html:submit styleClass="inputButton">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.back"/>
		</html:submit>
	</html:form>
</logic:present>
