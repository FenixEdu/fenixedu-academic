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
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.edit.data"/></h2>

	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
			</p>
		</html:messages>
	</logic:messagesPresent>

	<fr:form action="/resultPublications/editData.do">
	
		<logic:equal name="publicationBean" property="createEvent" value="false">
			<!-- Present publication fields -->
			<p class="mtop2 mbottom05"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/> (<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.ResultPublication.type."+publicationBean.getPublicationTypeString()%>"/>)</b></p>
			<fr:edit id="publicationData" name="publicationBean" schema="<%= publicationBean.getActiveSchema() %>" nested="true">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
		   		<fr:destination name="invalid" path="<%= "/resultPublications/prepareEditData.do?" + parameters %>"/>
		   		<fr:destination name="typePostBack" path="<%= "/resultPublications/changeType.do?" + parameters %>"/>
			</fr:edit>
		</logic:equal>
	
		<!-- Edit event in case of inproceedings or proceedings -->
		<logic:equal name="publicationBean" property="createEvent" value="true">
			<!-- Present Event -->
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.createConference"/>
			<fr:edit id="createEvent" name="publicationBean" schema="result.publication.create.Event" nested="true">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
		   		<fr:destination name="invalid" path="<%= "/resultPublications/prepareEditData.do?" + parameters %>"/>
				<fr:destination name="input" path="<%= "/resultPublications/prepareEditData.do?" + parameters %>"/>
			</fr:edit>
			
			<!-- Present publication fields -->
			<p class="mtop2 mbottom05"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/> (<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.ResultPublication.type."+publicationBean.getPublicationTypeString()%>"/>)</b></p>
			<fr:view name="publicationBean" schema="<%= publicationBean.getActiveSchema() %>">
				<fr:layout name="tabular">
    		    	<fr:property name="classes" value="tstyle1 thlight thright thtop width600px"/>
	    	    	<fr:property name="columnClasses" value="width12em,,"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
		
		<html:submit property="confirm">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.submit"/>
		</html:submit>
		<html:cancel>
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:cancel>	
	</fr:form>
</logic:present>
