<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<logic:present role="RESEARCHER">		
	<bean:define id="resultPublicationType" name="resultPublicationType" type="java.lang.String"/>
	<bean:define id="className" name="className" type="java.lang.String"/>

	<!-- Insert new publication -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.createPublication"/></h3>

	<!-- possible publication types -->		
	<html:form action="/publications/publicationsManagement"> 
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" />
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationType"/>
		<e:labelValues id="resultPublicationTypes" enumeration="net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication$ResultPublicationType" bundle="ENUMERATION_RESOURCES" />
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.resultPublicationType" value="<%=resultPublicationType%>" property="resultPublicationType">
			<html:options collection="resultPublicationTypes" property="value" labelProperty="label"  />
		</html:select>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='<%= "this.form.method.value='prepareCreatePublication' " %>'>
			<bean:message key="button.change"/>
		</html:submit>
	</html:form>
	<br />
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType." + resultPublicationType %>"/></h3>

<%-- TODO: type � obrigatorio, constru��o automatica  
--%>
	<!-- schema depends on selected option -->
	<fr:create id="createPublication" type="<%= className %>" schema="<%="result.publication.details." + resultPublicationType %>"
			action="/publications/publicationsManagement.do?method=listPublications">
		<fr:hidden slot="participation" name="participator"/>
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>

 		<fr:destination name="invalid" path="<%="/publications/publicationsManagement.do?method=prepareCreatePublication&resultPublicationType=" + resultPublicationType %>"/>
      	<fr:destination name="cancel" path="/publications/publicationsManagement.do?method=listPublications"/>
	</fr:create>

</logic:present>
