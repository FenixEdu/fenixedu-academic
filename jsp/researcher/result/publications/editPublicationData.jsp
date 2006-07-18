<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<logic:present role="RESEARCHER">		
	<bean:define id="resultPublicationType" name="resultPublicationType" type="java.lang.String"/>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.editPublication"/>
	&nbsp;(<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType."+resultPublicationType%>"/>)</h3>

	<bean:define id="publicationId" name="publication" property="idInternal"/>
	
	<!-- schema depends on selected option -->
	<fr:edit schema="<%="result.publication.details." + resultPublicationType %>"
			action="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId=" + publicationId %>"
			name="publication">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
   		<fr:destination name="cancel" path="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId=" + publicationId %>"/>
	</fr:edit>
</logic:present>
