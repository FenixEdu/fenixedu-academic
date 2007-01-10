<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultPublicationType" name="resultPublicationType"/>
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="result" name="result" type="net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication"/>	
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>"/>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
	<h2><fr:view name="result" property="title"/></h2>
	
	
	<ul class="mvert2 list5">
		<li>
			<html:link page="/resultPublications/listPublications.do">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.backTo.link" />
			</html:link>
		</li>
		<li>
		<html:link page="<%="/resultPublications/prepareDelete.do?&resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.delete" /></html:link> 
		</li>
	</ul>
	
	<logic:equal name="confirm" value="yes">
	<p class="mbottom1 mtop2"><span class="warning0"><bean:message key="researcher.ResultPublication.delete.useCase.title"/></span></p>
	<fr:form action="<%= "/resultPublications/delete.do?resultId=" + resultId %>">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
			<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
		</html:submit>
	</fr:form>
	</logic:equal>
		
	<%-- Last Modification Date --%>
	<p class="mtop15 mbottom2">
		<span class="greytxt1">
			<bean:message key="label.lastModificationDate"/>
				<fr:view name="result" property="lastModificationDate"/> (<fr:view name="result" property="modifiedBy"/>)
		</span>
	</p>


	<%-- Participations --%>
	<%-- <p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></strong>
		(<html:link page="<%="/resultParticipations/prepareEdit.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.edit" />
		</html:link>)
	</p>
	<jsp:include page="../commons/viewParticipations.jsp"/>
	--%>
	
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
	
	<%-- Unit Associations --%>
	<%-- <p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></b>
		(<html:link page="<%="/resultAssociations/prepareEditUnitAssociations.do?forwardTo=editUnitAssociations&" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.edit" />
		</html:link>)
	</p>
	<jsp:include page="../commons/viewUnitAssociations.jsp"/>
	--%>

	<%-- Data --%>
	<%-- <p class="mtop2 mbottom0"><strong><bean:message key="label.publicationData" bundle="RESEARCHER_RESOURCES"/></strong>
		(<html:link page="<%="/resultPublications/prepareEditData.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.edit" />
		</html:link>)
	</p>--%>

	<bean:message key="link.edit" bundle="RESEARCHER_RESOURCES"/>: 
	<html:link page="<%="/resultPublications/prepareEditData.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="label.publicationData" />
	</html:link>, 
	<html:link page="<%="/resultParticipations/prepareEdit.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.importBibtex.authors" />
	</html:link>, 
	<html:link page="<%="/resultAssociations/prepareEditUnitAssociations.do?forwardTo=editUnitAssociations&" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label" />
	</html:link>
			
	<fr:view name="result" schema="<%="result.publication.presentation."+resultPublicationType + ".mainInfo" %>">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
	</fr:layout>
	</fr:view>
	
		<%-- Documents --%>
	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b>
		(<html:link page="<%="/resultDocumentFiles/prepareEdit.do?forwardTo=editDocumentFiles&" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.edit" />
		</html:link>)
	</p>
	<jsp:include page="../commons/viewDocumentFiles.jsp"/>
	
	
	<%-- 
	<fr:view name="result" layout="tabular"  schema="<%="result.publication.details."+resultPublicationType %>" >
 		<fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 thlight thright thtop width600px"/>
        	<fr:property name="columnClasses" value="width12em,,"/>
		</fr:layout>
	</fr:view> 
	--%>

</logic:present>