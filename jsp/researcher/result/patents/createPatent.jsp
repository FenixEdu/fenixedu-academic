<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<!-- Titles -->
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.superUseCase.title"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPatent.create.link"/></h2>
	
	<!-- Author name -->
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h3>
	<fr:view name="UserView" property="person.name"/>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></h3>	
	<fr:create 	id="createPatent" type="net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent" 
				schema="patent.create"
				action="/patents/management.do?method=prepareEdit">
		<fr:hidden slot="participation" name="UserView" property="person"/>
	    <fr:layout name="tabular">
	        <fr:property name="classes" value="style1"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	    <fr:destination name="exception" path="/patents/management.do?method=prepareCreate"/>
	    <fr:destination name="invalid" path="/patents/management.do?method=prepareCreate"/>
	    <fr:destination name="cancel" path="/patents/management.do?method=management"/>
	</fr:create>
</logic:present>
<br/>