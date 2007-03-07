<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<!-- Titles -->
	<em><bean:message key="link.patentsManagement" bundle="RESEARCHER_RESOURCES"/></em> <!-- tobundle -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.create.title"/></h2>
	
	<!-- Author name -->
	<p>
		<bean:message bundle="RESEARCHER_RESOURCES" key="label.author"/>: <fr:view name="UserView" property="person.name"/>
	</p>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<p class="mbottom05"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.patentData"/></strong></p>
	<fr:create id="createPatent" type="net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent" 
				schema="patent.create"
				action="/resultPatents/createPatent.do">
		<fr:hidden slot="participation" name="UserView" property="person"/>
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
	        <fr:property name="columnClasses" value="listClasses,,tdclear tderror1"/>
	    </fr:layout>
	    <fr:destination name="exception" path="/resultPatents/prepareCreate.do"/>
	    <fr:destination name="invalid" path="/resultPatents/prepareCreate.do"/>
	    <fr:destination name="cancel" path="/resultPatents/management.do"/>
	</fr:create>
</logic:present>
<br/>