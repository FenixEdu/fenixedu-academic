<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultPublicationType" name="resultPublicationType" type="java.lang.String"/>
	<bean:define id="publicationId" name="publication" property="idInternal" />
	<bean:define id="newParticipationsSchema" value="result.participations" type="java.lang.String"/>
	<logic:present name="participationsSchema">
		<bean:define id="newParticipationsSchema" name="participationsSchema" type="java.lang.String"/>
	</logic:present>
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails"/></h2>

  	<br/>
	<%-- Participations --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h3>
	<fr:view name="publication" property="resultParticipations" schema="<%=newParticipationsSchema%>" layout="tabular">
		<fr:layout>
			<fr:property name="sortBy" value="personOrder"/>
		</fr:layout>
	</fr:view>
	<html:link page="<%="/result/resultParticipationManagement.do?method=prepareEditParticipation&resultId=" + publicationId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.editParticipations" />
	</html:link>
	<br/><br/>
	
	<%-- Data --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication"/>
	&nbsp;(<bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType."+resultPublicationType%>"/>)</h3>
	<fr:view name="publication" layout="tabular" schema="<%="result.publication.details."+resultPublicationType %>">
 		<fr:layout name="tabular">
    	   	<fr:property name="classes" value="style1"/>
	       	<fr:property name="columnClasses" value="listClasses,,"/>
		   </fr:layout>
	</fr:view>
	<html:link page="<%="/publications/publicationsManagement.do?method=prepareEditPublicationData&publicationId="+ publicationId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.editResultData" />
	</html:link>
	
	<br/><br/>
	<html:link page="/publications/publicationsManagement.do?method=listPublications">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.backToPublications" />
	</html:link>
</logic:present>
<br/>