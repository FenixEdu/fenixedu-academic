<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="actionPath">
		/resultPatents/prepareEdit.do?resultId=<bean:write name="patent" property="idInternal"/>
	</bean:define>
	<bean:define id="exceptionPath">
		/resultPatents/prepareEditData.do?resultId=<bean:write name="patent" property="idInternal"/>
	</bean:define>
	<bean:define id="cancelPath">
		<%= actionPath %>
	</bean:define>

	<%-- Title messages --%>
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.superUseCase.title"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.edit.useCase.title"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></h3>	
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Form edit Patent Data --%>		
	<fr:edit 	id="editPatent" name="patent" type="net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent" 
				schema="patent.edit" action="<%= actionPath %>">
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="style1"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	    <fr:destination name="exception" path="<%= exceptionPath %>"/>
	    <fr:destination name="invalid" path="<%= exceptionPath %>"/>
   	    <fr:destination name="cancel" path="<%= cancelPath %>"/>
	</fr:edit>
</logic:present>
<br/>