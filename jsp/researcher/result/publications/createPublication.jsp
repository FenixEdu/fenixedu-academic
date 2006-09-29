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
	<em>Publicações</em> <!-- tobundle -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.create"/></h2>
	
	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
		</html:messages>
	</logic:messagesPresent>

	<fr:form action="/resultPublications/create.do">
		<!-- Present Author -->
		<p class="mtop15 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.author"/></b></p>
		<fr:edit id="author" name="publicationBean" schema="<%= publicationBean.getParticipationSchema() %>" nested="true">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
	   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
		</fr:edit>	

		
		<!-- Present publication fields -->
		<p class="mtop1 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES"key="<%="researcher.ResultPublication.type." + publicationBean.getPublicationTypeString() %>"/></b></p>
		<fr:edit id="publicationData" name="publicationBean" schema="<%= publicationBean.getActiveSchema() %>" nested="true">
	 	    <fr:layout name="tabular">
	    	    <fr:property name="classes" value="tstyle1 thright thlight thtop"/>
	        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		    </fr:layout>
	   		<fr:destination name="invalid" path="/resultPublications/prepareCreate.do"/>
	   		<fr:destination name="typePostBack" path="/resultPublications/changeType.do"/>
		</fr:edit>

		<!-- Create event in case of inproceedings or proceedings -->
		<logic:equal name="publicationBean" property="createEvent" value="true">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.createConference"/>
			<fr:edit id="createEvent" name="publicationBean" schema="result.publication.create.Event" nested="true">
		 	    <fr:layout name="tabular">
		    	    <fr:property name="classes" value="tstyle1 thright thlight thtop"/>
		        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
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
