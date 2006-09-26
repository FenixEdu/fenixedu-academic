<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

<logic:present role="RESEARCHER">		
	<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"/>

	<!-- Insert new publication -->
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></em>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.create"/></h3>

	<!-- possible publication types -->
	<fr:form action="/resultPublications/prepareCreate.do">
		<b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.type"/></b>
		<e:labelValues id="resultPublicationTypes" enumeration="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean$ResultPublicationType" bundle="ENUMERATION_RESOURCES" />		
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.resultPublicationType" value="<%=publicationBean.getPublicationType().toString()%>" property="resultPublicationType" onchange="this.form.submit();">
			<html:options collection="resultPublicationTypes" property="value" labelProperty="label"/>
		</html:select>
	</fr:form>
	<br/>
	
	<logic:messagesPresent message="true">
		<br/>
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
		</html:messages>
		<br/><br/>
	</logic:messagesPresent>

	<fr:form action="/resultPublications/create.do">
		<!-- Present Author -->
		<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.author"/>:</b></p>
		<fr:edit id="author" name="publicationBean" schema="<%= publicationBean.getParticipationSchema() %>" nested="true">
			<fr:layout name="tabular">
				<fr:property name="classes" value="style1"/>
				<fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
	   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
		</fr:edit>	

		
		<!-- Present publication fields -->
		<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.ResultPublication.type." + publicationBean.getPublicationType() %>"/></b></p>
		<fr:edit id="createPublication" name="publicationBean" schema="<%=publicationBean.getActiveSchema() %>" nested="true">
	 	    <fr:layout name="tabular">
	    	    <fr:property name="classes" value="style1"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
		    </fr:layout>
	   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
		</fr:edit>

		<!-- Create event in case of inproceedings or proceedings -->
		<logic:equal name="publicationBean" property="createEvent" value="true">
			<br/>
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.createConference"/>
			<fr:edit id="createEvent" name="publicationBean" schema="result.publication.create.Event" nested="true">
		 	    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="style1"/>
		        	<fr:property name="columnClasses" value="listClasses,,"/>
			    </fr:layout>
		   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
		   		<fr:destination name="input" path="/resultPublications/prepareCreate.do"/>
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
