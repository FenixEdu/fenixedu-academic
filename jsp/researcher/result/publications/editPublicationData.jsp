<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<logic:present role="RESEARCHER">		
	<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean"/>
	
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.editPublication"/>
	&nbsp;(<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType."+publicationBean.getPublicationType().toString()%>"/>)</h3>

	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<span class="error"><bean:write name="messages" /></span>
		</html:messages>
		<br/><br/>
	</logic:messagesPresent>
	
	<!-- schema depends on selected option -->
	<fr:edit id="editPublication" name="publicationBean"
			action="<%="/publications/publicationsManagement.do?method=editPublicationData&publicationId=" + publicationBean.getIdInternal() %>"
			schema="<%= publicationBean.getActiveSchema() %>">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	    
	    <fr:destination name="bookPartPostBack" path="/publications/publicationsManagement.do?method=changeBookPartTypePostBack"/>
   		<fr:destination name="invalid" path="/publications/publicationsManagement.do?method=prepareEditPublicationData"/>
      	<fr:destination name="cancel" path="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId=" + publicationBean.getIdInternal() %>"/>
	</fr:edit>
</logic:present>
