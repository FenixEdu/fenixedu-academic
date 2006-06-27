<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<logic:present role="RESEARCHER">		
	<bean:define id="resultPublicationType" name="resultPublicationType" type="net.sourceforge.fenixedu.domain.research.result.publication.ResultPublicationType"/>

	<!-- Insert new publication -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.createPublication"/></h3>

	<!-- possible publication types -->		
	<html:form action="/publications/publicationsManagement"> 
		<html:hidden property="method" />
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationType"/>
		<html:select value="<%= resultPublicationType.getIdInternal().toString() %>" property="resultPublicationTypeId">
			<html:options collection="publicationTypes" property="idInternal" labelProperty="publicationType"/>
		</html:select>
				
		<html:submit onclick='<%= "this.form.method.value='prepareCreatePublication' " %>'>
			<bean:message key="button.change"/>
		</html:submit>
	</html:form>
	<br />
	
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType." + resultPublicationType.getPublicationType().toString().toLowerCase() %>"/></h3>

	<!-- schema depends on selected option -->
	<fr:create id="createPublication" type="net.sourceforge.fenixedu.domain.research.result.Publication" schema="<%="result.publication.details." + resultPublicationType.getPublicationType().toString().toLowerCase() %>"
			action="/publications/publicationsManagement.do?method=listPublications">
		<fr:hidden slot="authorship" name="author"/>
		<fr:hidden slot="resultPublicationType" name="resultPublicationType" />
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>

 		<fr:destination name="invalid" path="<%="/publications/publicationsManagement.do?method=prepareCreatePublication&resultPublicationTypeId=" + resultPublicationType.getIdInternal() %>"/>
      	<fr:destination name="cancel" path="/publications/publicationsManagement.do?method=listPublications"/>
	</fr:create>
	
</logic:present>
