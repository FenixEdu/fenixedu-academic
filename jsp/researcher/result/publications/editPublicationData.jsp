<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<logic:present role="RESEARCHER">		
	<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"/>
	
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.editPublication"/>
	&nbsp;(<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType."+publicationBean.getPublicationType().toString()%>"/>)</h3>

	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
		</html:messages>
		<br/><br/>
	</logic:messagesPresent>
	
	<fr:form action="/publications/publicationsManagement.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="publicationsForm" property="method" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.publicationId" name="publicationsForm" property="publicationId" value="<%=publicationBean.getIdInternal().toString()%>"/>

		<!-- Present publication fields -->
		<fr:edit nested="true" id="editPublication" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"
				schema="<%=publicationBean.getActiveSchema() %>">
	 	    <fr:layout name="tabular">
	    	    <fr:property name="classes" value="style1"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
		    </fr:layout>
		    <fr:destination name="bookPartPostBack" path="/publications/publicationsManagement.do?method=changeBookPartTypePostBack"/>
	   		<fr:destination name="invalid" path="/publications/publicationsManagement.do?method=prepareEditPublicationData"/>
		</fr:edit>

		<!-- Create event in case of inproceedings or proceedings -->
		<logic:equal name="publicationBean" property="createEvent" value="true">
		<br/><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.createEvent"/>
			<fr:edit nested="true" id="createEvent" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"
					schema="result.publication.create.Event">
		 	    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="style1"/>
		        	<fr:property name="columnClasses" value="listClasses,,"/>
			    </fr:layout>
		   		<fr:destination name="invalid" path="/publications/publicationsManagement.do?method=prepareEditPublicationData"/>
			</fr:edit>
		</logic:equal>	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='editPublicationData';"><bean:message key="button.submit" /></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='prepareViewEditPublication';"><bean:message key="button.cancel" /></html:cancel>
	</fr:form>
</logic:present>
