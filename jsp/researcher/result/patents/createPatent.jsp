<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<!-- Titles -->
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.management.title"/></em>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.create.link"/></h3>
	
	<!-- Author name -->
	<p><b>	<bean:message bundle="RESEARCHER_RESOURCES" key="label.author"/>:</b>
			<fr:view name="UserView" property="person.name"/></p>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></b></p>
	<fr:create 	id="createPatent" type="net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent" 
				schema="patent.create"
				action="/resultPatents/prepareEdit.do">
		<fr:hidden slot="participation" name="UserView" property="person"/>
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="style1"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	    <fr:destination name="exception" path="/resultPatents/prepareCreate.do"/>
	    <fr:destination name="invalid" path="/resultPatents/prepareCreate.do"/>
	    <fr:destination name="cancel" path="/resultPatents/management.do"/>
	</fr:create>
</logic:present>
<br/>