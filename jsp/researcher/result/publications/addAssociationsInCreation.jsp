<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
	<h2><bean:message key="label.associatingDocumentsAndUnits" bundle="RESEARCHER_RESOURCES"/></h2>

	<p class="mvert1 breadcumbs">
		<span>
		<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 1 : </strong>
		<bean:message key="researcher.result.publication.importBibtex.publicationData" bundle="RESEARCHER_RESOURCES"/></span>
		 > 
		<span class="actual">
		<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.step"/> 2 : </strong>
		<bean:message key="label.associatingDocumentsAndUnits" bundle="RESEARCHER_RESOURCES"/></span>
		</span>
 	</p>


	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>"/>
	
	<bean:define id="schema" name="result" property="schema" type="java.lang.String"/>
		
		
	<%-- Documents --%>
	
	<bean:define id="documents" name="result" property="resultDocumentFiles"/>
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="result" name="result"/>

	<!-- Action paths definitions -->
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>" toScope="request"/>

	<bean:define id="createFile" value="<%="/result/resultDocumentFilesManagement.do?forwardTo=fileAssociation&method=create&" + parameters%>" />
	<bean:define id="backLinkFile" value="<%="/result/resultDocumentFilesManagement.do?forwardTo=fileAssociation&method=backToResult&" + parameters %>" />
	<bean:define id="prepareEditFile" value="<%="/result/resultDocumentFilesManagement.do?forwardTo=fileAssociation&method=prepareEdit&" + parameters%>" />
	<bean:define id="prepareAlterFile" value="<%="/result/resultDocumentFilesManagement.do?forwardTo=fileAssociation&method=prepareAlter&" + parameters%>"/>	


<div class="infoop2">
<p><bean:message key="label.permissions.explaination" bundle="RESEARCHER_RESOURCES"/></p>
</div>


	<p class="mtop15 mbottom025"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication"/></b></p>
	<ul class="mtop025">
		<li class="mtop1">
			<fr:view name="result" layout="nonNullValues" schema="<%= schema %>">
				<fr:layout>
					<fr:property name="classes" value="mbottom025"/>
					<fr:property name="htmlSeparator" value=", "/>
					<fr:property name="indentation" value="false"/>
				</fr:layout>
			</fr:view>
		</li>
	</ul>


	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b></p>

	<logic:empty name="documents">
		<p class="mvert075"><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="documents">
		<logic:notPresent name="editExisting">
			<jsp:include page="../documents/viewDocuments.jsp"/>
<%-- 			<html:link page="<%=prepareAlterFile%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.edit"/></html:link>
	--%>
		</logic:notPresent>
		<logic:present name="editExisting">
			<jsp:include page="../documents/editDocuments.jsp"/>
		</logic:present>
	</logic:notEmpty>
	
	<div id="linkFile" class="switchInline">
		<a href="javascript:switchDisplay('addFile'); switchDisplay('linkFile');"><bean:message key="label.addFile" bundle="RESEARCHER_RESOURCES"/></a>
	</div>
	<div id="addFile" class="switchNone">
	<p class="mbottom0"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentAssociation.add"/></p>
	<fr:form action="<%= createFile %>" encoding="multipart/form-data">
	<fr:edit id="editBean" name="fileBean" schema="resultDocumentFile.submission.edit" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thtop mtop05 mbottom0" />
			<fr:property name="columnClasses" value="width7em,width46em,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="exception" path="<%= prepareEditFile %>"/>
		<fr:destination name="invalid" path="<%= prepareEditFile %>"/>

	</fr:edit>
	<table class="tstyle5 mtop0">
		<tr>
			<td class="width7em"></td>
			<td class="width46em">
				<html:submit><bean:message key="button.insert" bundle="RESEARCHER_RESOURCES"/></html:submit>
				<span class="switchInline">
				<input alt="input.input" type="button" onclick="javascript:switchDisplay('addFile');switchDisplay('linkFile');" value="<bean:message key="label.close" bundle="RESEARCHER_RESOURCES"/>"/>		
				</span>
			</td>
		</tr>
		
	</table>
	</fr:form>
	</div>

	<%-- Event Associations --%>

	<%-- 
	<p class="mtop25 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></b></p>
	<jsp:include page="../commons/viewEventAssociations.jsp"/>
	<ul class="mtop0 list5">
		<li>
		<html:link page="<%="/resultAssociations/prepareEditEventAssociations.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.eventAssociations.link" />
		</html:link>
		</li>
	</ul>
	--%>
	
	<%-- Unit Associations --%>
	
	<bean:define id="createUnit" value="<%="/resultAssociations/createUnitAssociation.do?forwardTo=unitAssociation&" + parameters%>" />
	<bean:define id="prepareEditUnit" value="<%="/resultAssociations/prepareEditUnitAssociations.do?forwardTo=unitAssociation&" + parameters%>" />
	<bean:define id="prepareAlterUnit" value="<%="/resultAssociations/prepareEditUnitRole.do?forwardTo=unitAssociation&" + parameters%>"/>	
	<bean:define id="removeUnit" value="<%="/resultAssociations/removeUnitAssociation.do?forwardTo=unitAssociation&" + parameters%>"/>
	<bean:define id="backLinkUnit" value="<%="/resultAssociations/backToResult.do?forwardTo=unitAssociation&" + parameters%>" />
	<bean:define id="associations" name="result" property="resultUnitAssociations"/>
	
	<p class="mtop25 mbottom05"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></strong></p>

	<logic:empty name="associations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.emptyList"/></em></p>
	</logic:empty>

	<logic:notEmpty name="associations">
		<logic:present name="editExisting">
			<fr:edit id="editRole" name="associations"schema="resultUnitAssociation.details" action="<%= prepareEditUnit %>">
				<fr:layout name="tabular-row">
					<fr:property name="classes" value="tstyle2 mtop05" />
					<fr:property name="columnClasses" value=",acenter,acenter"/>
				</fr:layout>
				<fr:destination name="exception" path="<%= prepareEditUnit + "&editExisting=true" %>" />
				<fr:destination name="invalid" path="<%= prepareEditUnit + "&editExisting=true"%>"/>	
			</fr:edit>
		</logic:present>
		<logic:notPresent name="editExisting">	
			<fr:view name="associations"schema="resultUnitAssociation.details">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 mtop05" />
					<fr:property name="columnClasses" value=",acenter,acenter"/>
					
					<fr:property name="link(remove)" value="<%= removeUnit %>"/>
					<fr:property name="param(remove)" value="idInternal/associationId"/>
					<fr:property name="key(remove)" value="link.remove"/>
					<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
				</fr:layout>
			</fr:view>
			<%-- 
			<html:link page="<%=prepareAlter%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.edit"/></html:link>
			--%>
		</logic:notPresent>
	</logic:notEmpty>
	
	<%-- Create new result unit association --%>

	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<logic:present name="unitBean">
		<div id="linkUnit" class="switchInline">
		<a href="javascript:switchDisplay('addUnit');switchDisplay('linkUnit');"><bean:message key="label.addUnit" bundle="RESEARCHER_RESOURCES"/></a>
	</div>
	<div id="addUnit" class="switchNone">

	<p class="mbottom0"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.add"/></p>

	
		<logic:equal name="unitBean" property="externalUnit" value="true">
				<bean:define id="unitSchema" value="resultUnitAssociation.create.external" toScope="request"/>
		</logic:equal>
		<logic:equal name="unitBean" property="externalUnit" value="false">
			<bean:define id="unitSchema" value="resultUnitAssociation.create.internal" toScope="request"/>
		</logic:equal>
		
		<bean:define id="unitSchema" name="unitSchema" type="java.lang.String"/>
		
  		<fr:form id="unitBean" action="<%= createUnit %>">
		<fr:edit id="unitBean" name="unitBean" schema="<%= unitSchema %>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mtop05 mbottom0"/>
				<fr:property name="columnClasses" value="width7em,width35em,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="exception" path="<%= prepareEditUnit %>"/>
			<fr:destination name="invalid" path="<%= prepareEditUnit %>"/>	
			<fr:destination name="cancel" path="<%= backLinkUnit %>"/>	
			<fr:destination name="postBack" path="<%= "/resultAssociations/changeTypeOfUnit.do?forwardTo=unitAssociation&" + parameters %>"/>
		</fr:edit> 
			<table class="tstyle5 mtop0">
	  			<tr>
	  				<td class="width7em"></td>
	  				<td class="width35em">
	  				<html:submit><bean:message key="button.associate" bundle="RESEARCHER_RESOURCES"/></html:submit>
	  				<span class="switchInline">
					<input alt="input.input" type="button" onclick="javascript:switchDisplay('addUnit');switchDisplay('linkUnit');" value="<bean:message key="label.close" bundle="RESEARCHER_RESOURCES"/>"/>		
					</span>
	  				</td>
	  			</tr>
	  		</table>
		</fr:form>
	</div>
	
	</logic:present>


<p class="mtop25 mbottom025"><bean:message key="label.backToPublications" bundle="RESEARCHER_RESOURCES"/>:</p>
	<p class="mtop025">
		<fr:form action="/resultPublications/listPublications.do">
			<html:submit><bean:message key="button.finish" bundle="RESEARCHER_RESOURCES"/></html:submit>
		</fr:form>
	</p>

<script type="text/javascript" language="javascript">
				switchGlobal();
				</script>
</logic:present>