<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultPublicationType" name="resultPublicationType" type="java.lang.String"/>
	<bean:define id="publicationId" name="publication" property="idInternal" />
	<bean:define id="documents" name="publication" property="resultDocumentFiles"/>
	<bean:define id="resultType" name="publication" property="class.simpleName"/>
	<bean:define id="newParticipationsSchema" value="result.participations" type="java.lang.String"/>
	<logic:present name="participationsSchema">
		<bean:define id="newParticipationsSchema" name="participationsSchema" type="java.lang.String"/>
	</logic:present>
	
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link"/></h3>

	<%-- Participations --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h3>
	<fr:view name="publication" property="resultParticipations" schema="<%=newParticipationsSchema%>" layout="tabular">
		<fr:layout>
			<fr:property name="sortBy" value="personOrder"/>
		</fr:layout>
	</fr:view>
	<html:link page="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + publicationId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.participations.link" />
	</html:link>
	<br/><br/>
	
	<%-- Data --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication"/>
	&nbsp;(<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.ResultPublication.type."+resultPublicationType%>"/>)</h3>
	<fr:view name="publication" layout="tabular" schema="<%="result.publication.details."+resultPublicationType %>">
 		<fr:layout name="tabular">
    	   	<fr:property name="classes" value="style1"/>
	       	<fr:property name="columnClasses" value="listClasses,,"/>
		   </fr:layout>
	</fr:view>
	<html:link page="<%="/publications/publicationsManagement.do?method=prepareEditPublicationData&publicationId="+ publicationId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.edit.data" />
	</html:link>
	<br/>
	
	<%-- Documents --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></h3>
	<logic:empty name="documents">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="documents">
		<fr:view name="documents" schema="resultDocumentFile.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="columnClasses" value=",,,acenter"/>
				<fr:property name="sortBy" value="uploadTime=desc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<html:link page="<%="/resultDocumentFiles/prepareEdit.do?resultId=" + publicationId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.manage.documents.link" />
	</html:link>
	<br/>
	<br/>
	
	<html:link page="/publications/publicationsManagement.do?method=listPublications">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.backTo.link" />
	</html:link>
</logic:present>
<br/>