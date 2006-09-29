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
	<bean:define id="result" name="result"/>	
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>"/>

	<em>Publicações</em> <!-- tobundle -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.title"/></h2>
	
	
	<ul class="mvert2 list5">
		<li>
			<html:link page="/resultPublications/listPublications.do">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.backTo.link" />
			</html:link>
		</li>
	</ul>
	
	<%-- Last Modification Date --%>
	<p class="mtop15 mbottom2">
		<span class="greytxt1">
			<bean:message key="label.lastModificationDate"/>
				<fr:view name="result" property="lastModificationDate"/> (<fr:view name="result" property="modifyedBy"/>)
		</span>
	</p>

	<%-- Participations --%>
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></strong></p>
	<jsp:include page="../commons/viewParticipations.jsp"/>
	<ul class="mtop0 list5">
		<li>
		<html:link page="<%="/resultParticipations/prepareEdit.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.participations.link" />
		</html:link>
		</li>
	</ul>
	
	<%-- Data --%>
	<p class="mtop2 mbottom0"><b>Detalhes da publicação</b></p> <!-- tobundle -->
	<fr:view name="result" layout="tabular" schema="<%="result.publication.details."+resultPublicationType %>">
 		<fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 thlight thright thtop width600px"/>
        	<fr:property name="columnClasses" value="width12em,,"/>
		</fr:layout>
	</fr:view>
	<ul class="mtop0 list5">
		<li>
		<html:link page="<%="/resultPublications/prepareEditData.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.edit.data" />
		</html:link>
		</li>
	</ul>

	
	<%-- Documents --%>
	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b></p>
	<jsp:include page="../commons/viewDocumentFiles.jsp"/>
	<ul class="mtop0 list5">
		<li>
		<html:link page="<%="/resultDocumentFiles/prepareEdit.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.documents.link" />
		</html:link>
		</li>
	</ul>

	<%-- Event Associations --%>
	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></b></p>
	<jsp:include page="../commons/viewEventAssociations.jsp"/>
	<ul class="mtop0 list5">
		<li>
		<html:link page="<%="/resultAssociations/prepareEditEventAssociations.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.eventAssociations.link" />
		</html:link>
		</li>
	</ul>
	
	<%-- Unit Associations --%>
	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultUnitAssociation.title.label"/></b></p>
	<jsp:include page="../commons/viewUnitAssociations.jsp"/>
	<ul class="mtop0 list5">
		<li>
		<html:link page="<%="/resultAssociations/prepareEditUnitAssociations.do?" + parameters %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.unitAssociations.link" />
		</html:link>	
		</li>
	</ul>

</logic:present>