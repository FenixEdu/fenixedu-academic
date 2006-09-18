<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<logic:present role="RESEARCHER">		
	<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"/>
	<bean:define id="parameters" value="<%= "resultId=" + publicationBean.getIdInternal().toString() %>"/>
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></em>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.edit.data"/></h3>
	<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/>
	&nbsp;(<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.ResultPublication.type."+publicationBean.getPublicationType().toString()%>"/>)</b></p>

	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
		</html:messages>
		<br/><br/>
	</logic:messagesPresent>
	
	<fr:form action="<%= "/resultPublications/editData.do?" + parameters %>">
		<!-- Present publication fields -->
		<fr:edit id="editPublication" name="publicationBean" schema="<%= publicationBean.getActiveSchema() %>" nested="true">
	 	    <fr:layout name="tabular">
	    	    <fr:property name="classes" value="style1"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
		    </fr:layout>
		    <fr:destination name="bookPartPostBack" path="<%= "/resultPublications/changeBookPartTypePostBack.do?" + parameters %>"/>
	   		<fr:destination name="invalid" path="<%= "/resultPublications/prepareEditData.do?" + parameters %>"/>
		</fr:edit>

		<!-- Edit event in case of inproceedings or proceedings -->
		<logic:equal name="publicationBean" property="createEvent" value="true">
			<br/>
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.createEvent"/>
			<fr:edit id="createEvent" name="publicationBean" schema="result.publication.create.Event" nested="true">
		 	    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="style1"/>
		        	<fr:property name="columnClasses" value="listClasses,,"/>
			    </fr:layout>
		   		<fr:destination name="invalid" path="<%= "/resultPublications/prepareEditData.do?" + parameters %>"/>
			</fr:edit>
		</logic:equal>
		
		<html:submit property="confirm">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.submit"/>
		</html:submit>
		<html:cancel>
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:cancel>	
	</fr:form>
</logic:present>
