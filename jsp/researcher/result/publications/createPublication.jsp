<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<logic:present role="RESEARCHER">		
	<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean"/>

	<!-- Insert new publication -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.createPublication"/></h3>

	<!-- possible publication types -->		
	<html:form action="/publications/publicationsManagement"> 
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" />
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationType"/>
		<e:labelValues id="resultPublicationTypes" enumeration="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean$ResultPublicationType" bundle="ENUMERATION_RESOURCES" />
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.resultPublicationType" value="<%=publicationBean.getPublicationType().toString()%>" property="resultPublicationType">
			<html:options collection="resultPublicationTypes" property="value" labelProperty="label"  />
		</html:select>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='<%= "this.form.method.value='prepareCreatePublication' " %>'>
			<bean:message key="button.change"/>
		</html:submit>
	</html:form>
	<br />
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType." + publicationBean.getPublicationType() %>"/></h3>

	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
		</html:messages>
		<br/><br/>
	</logic:messagesPresent>

	<fr:edit id="createPublication" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean"
			action="/publications/publicationsManagement.do?method=createResultPublication" schema="<%=publicationBean.getActiveSchema() %>">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	    
	    <fr:destination name="bookPartPostBack" path="/publications/publicationsManagement.do?method=changeBookPartTypePostBack"/>
   		<fr:destination name="invalid" path="/publications/publicationsManagement.do?method=prepareCreatePublication"/>
      	<fr:destination name="cancel" path="/publications/publicationsManagement.do?method=listPublications"/>
	</fr:edit>

</logic:present>
