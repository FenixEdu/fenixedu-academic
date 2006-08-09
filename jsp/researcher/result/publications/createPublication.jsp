<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

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

	<fr:form action="/publications/publicationsManagement.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="publicationsForm" property="method" />

		<!-- Present publication fields -->
		<fr:edit nested="true" id="createPublication" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean"
				schema="<%=publicationBean.getActiveSchema() %>">
	 	    <fr:layout name="tabular">
	    	    <fr:property name="classes" value="style1"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
		    </fr:layout>
		    <fr:destination name="bookPartPostBack" path="/publications/publicationsManagement.do?method=changeBookPartTypePostBack"/>
	   		<fr:destination name="invalid" path="/publications/publicationsManagement.do?method=prepareCreatePublication"/>
		</fr:edit>

		<!-- Create event in case of inproceedings or proceedings -->
		<logic:equal name="publicationBean" property="createEvent" value="true">
		<br/><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.createEvent"/>
			<fr:edit nested="true" id="createEvent" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationCreationBean"
					schema="result.publication.create.Event">
		 	    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="style1"/>
		        	<fr:property name="columnClasses" value="listClasses,,"/>
			    </fr:layout>
		   		<fr:destination name="invalid" path="/publications/publicationsManagement.do?method=prepareCreatePublication"/>
			</fr:edit>
		</logic:equal>	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createResultPublication';"><bean:message key="button.submit" /></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='listPublications';"><bean:message key="button.cancel" /></html:cancel>
	</fr:form>
</logic:present>
