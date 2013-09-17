<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="resultPublicationType" name="resultPublicationType"/>
<bean:define id="resultId" name="result" property="externalId"/>
<bean:define id="resultType" name="result" property="class.simpleName"/>
<bean:define id="result" name="result" type="net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication"/>	
<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>"/>
<logic:present name="unit">
	<bean:define id="unitID" name="unit" property="externalId"/>
	<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
</logic:present>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></h2>

<ul class="mvert15">
	<li>
		<html:link page="<%= "/resultPublications/goBackFromDetails.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.backTo.link" />
		</html:link>
	</li>
	<logic:present name="result" property="creator">
	<bean:write name="result" property="creator.name"/>
	</logic:present>
	<logic:equal name="result" property="deletableByCurrentUser" value="true">
		<li>
		<html:link page="<%="/resultPublications/prepareDelete.do?"+ parameters%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.delete" /></html:link> 
		</li>
	</logic:equal>
</ul>
	
<logic:equal name="confirm" value="yes">
	<div  class="mtop1 mbottom15">
	<p><span class="warning0"><bean:message key="researcher.ResultPublication.delete.useCase.title"/></span></p>
	<fr:form action="<%= "/resultPublications/delete.do?" + parameters %>">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:submit>
	</fr:form>
	</div>
</logic:equal>
	
<%-- Last Modification Date --%>
<p>
	<span class="greytxt1">
		<bean:message key="label.lastModificationDate"/>
			<fr:view name="result" property="lastModificationDate"/> 	
				<logic:present name="result" property="modifiedBy">
					(<fr:view name="result" property="modifiedBy" type="java.lang.String"/>)
				</logic:present>
	</span>
</p>

<%-- Event Associations --%>
<%-- 
<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></b></p>
<jsp:include page="../commons/viewEventAssociations.jsp"/>
<ul class="mtop0 list5">
	<li>
	<html:link page="<%="/resultAssociations/prepareEditEventAssociations.do?" + parameters %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.eventAssociations.link" />
	</html:link>
	</li>
</ul>
--%>

<logic:equal name="result" property="editableByCurrentUser" value="true">
<bean:message key="link.edit" bundle="RESEARCHER_RESOURCES"/>: 
<html:link page="<%="/resultPublications/prepareEditData.do?" + parameters %>">
	<bean:message bundle="RESEARCHER_RESOURCES" key="label.publicationData" />
</html:link>,
<logic:equal name="result" property="class.simpleName" value="Article">
	<html:link page="<%= "/resultPublications/prepareEditJournal.do?" + parameters %>">
		<bean:message key="label.journal" bundle="RESEARCHER_RESOURCES"/>
	</html:link>, 
</logic:equal>
<logic:equal name="result" property="class.simpleName" value="Proceedings">
	<html:link page="<%= "/resultPublications/prepareEditEvent.do?" + parameters %>">
		<bean:message key="label.event" bundle="RESEARCHER_RESOURCES"/>
	</html:link>, 
</logic:equal>
<logic:equal name="result" property="class.simpleName" value="Inproceedings">
	<html:link page="<%= "/resultPublications/prepareEditEvent.do?" + parameters %>">
		<bean:message key="label.event" bundle="RESEARCHER_RESOURCES"/>
	</html:link>, 
</logic:equal> 
<html:link page="<%="/resultParticipations/prepareEdit.do?" + parameters %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.authors" />
</html:link>, 
<html:link page="<%="/resultAssociations/prepareEditUnitAssociations.do?" + parameters %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label" />
</html:link>, 
<html:link page="<%="/resultPublications/associatePrize.do?" + parameters %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.PrizeAssociation.title.label"/>
</html:link>
</logic:equal>

<fr:view name="result" schema="<%="result.publication.presentation."+resultPublicationType + ".mainInfo" %>">
<fr:layout name="tabular-nonNullValues">
	<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
	<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,"/>
	<fr:property name="columnClasses" value="width10em, width50em"/>
	<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
</fr:layout>
		<fr:destination name="view.prize" path="/prizes/prizeManagement.do?method=showPrize&oid=${externalId}"/>
</fr:view>


	<%-- Documents --%>
<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b>
	<logic:equal name="result" property="editableByCurrentUser" value="true"> | 
		<html:link page="<%="/resultDocumentFiles/prepareEdit.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.managed.associated.documents" />
		</html:link>
	</logic:equal>
</p>
<jsp:include page="../commons/viewDocumentFiles.jsp"/>
