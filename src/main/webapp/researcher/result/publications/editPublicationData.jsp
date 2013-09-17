<%@ page language="java" %> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>

<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"/>
<bean:define id="parameters" value="<%= "resultId=" + publicationBean.getExternalId().toString() %>"/>
<logic:present name="unit">
	<bean:define id="unitID" name="unit" property="externalId"/>
	<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
</logic:present>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.edit.publicationData"/></h2>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<bean:define id="action" value="/resultPublications/editData.do" type="java.lang.String"/>
	<logic:equal name="publicationBean" property="class.simpleName" value="ArticleBean">
		<bean:define id="action" value="/resultPublications/prepareEditJournal.do" type="java.lang.String"/>
	</logic:equal>
	<logic:equal name="publicationBean" property="class.simpleName" value="InproceedingsBean">
		<bean:define id="action" value="/resultPublications/prepareEditEvent.do" type="java.lang.String"/>
	</logic:equal>
	<logic:equal name="publicationBean" property="class.simpleName" value="ProceedingsBean">
		<bean:define id="action" value="/resultPublications/prepareEditEvent.do" type="java.lang.String"/>
	</logic:equal>

<fr:form action="<%= action + "?" + parameters %>">


	<logic:equal name="publicationBean" property="createEvent" value="false">
		<!-- Present publication fields -->
		<p class="mtop2 mbottom05"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/> (<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.ResultPublication.type."+publicationBean.getPublicationTypeString()%>"/>)</b></p>
		<fr:edit id="publicationBean" name="publicationBean" schema="<%= publicationBean.getActiveSchema() %>" nested="true">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true"/>
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
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
	<bean:define id="submitLabel" value="button.submit"/>
		<logic:equal name="publicationBean" property="class.simpleName" value="ArticleBean">
				<bean:define id="submitLabel" value="button.next"/>
		</logic:equal>
		<logic:equal name="publicationBean" property="class.simpleName" value="InproceedingsBean">
				<bean:define id="submitLabel" value="button.next"/>
		</logic:equal>
		<logic:equal name="publicationBean" property="class.simpleName" value="ProceedingsBean">
				<bean:define id="submitLabel" value="button.next"/>
		</logic:equal>			

		<bean:message bundle="RESEARCHER_RESOURCES" key="<%= submitLabel %>"/>
	</html:submit>
	<html:cancel>
		<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
	</html:cancel>	
</fr:form>
