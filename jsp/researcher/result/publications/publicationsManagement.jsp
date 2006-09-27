<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="publications" name="UserView" property="person.resultPublications"/>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></h2>

	<%-- Action messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<p><html:link page="/publications/bibtexManagement.do?method=prepareOpenBibtexFile">
		<bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.result.publication.importBibtex"/>
	</html:link></p>

	<p><html:link page="/resultPublications/prepareCreate.do">
		<bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.ResultPublication.insert.link"/>
	</html:link></p>

	<logic:empty name="publications">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.emptyList"/></em></p>
	</logic:empty>

	<logic:notEmpty name="books">
		<h3 id='books' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Books"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.Book" toScope="request"/>
		<bean:define id="results" name="books" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="inbooks">
		<h3 id='inbooks' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Inbooks"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.Inbook" toScope="request"/>
		<bean:define id="results" name="inbooks" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="incollections">
		<h3 id='incollections' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Incollections"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.Incollection" toScope="request"/>
		<bean:define id="results" name="incollections" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="articles">
		<h3 id='books' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Articles"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.Article" toScope="request"/>
		<bean:define id="results" name="articles" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="inproceedings">
		<h3 id='inproceedings' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Inproceedings"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
		<bean:define id="results" name="inproceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="proceedings">
		<h3 id='proceedings' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Proceedings"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.Proceedings" toScope="request"/>
		<bean:define id="results" name="proceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="theses">
		<h3 id='theses' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Theses"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.Thesis" toScope="request"/>
		<bean:define id="results" name="theses" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="manuals">
		<h3 id='manuals' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Manuals"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.Manual" toScope="request"/>
		<bean:define id="results" name="manuals" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="technicalReports">
		<h3 id='technicalReports' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.TechnicalReports"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.TechnicalReport" toScope="request"/>
		<bean:define id="results" name="technicalReports" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="otherPublications">
		<h3 id='otherPublications' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.OtherPublications"/> </span> </h3>
		<bean:define id="currentSchema" value="result.publication.presentation.OtherPublication" toScope="request"/>
		<bean:define id="results" name="otherPublications" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="unstructureds">
		<h3 id='unstructureds' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Unstructureds"/> </span> </h3>
		<bean:define id="results" name="unstructureds" toScope="request"/>
		<table style="text-align:left" width="100%">
			<logic:iterate id="result" name="results" scope="request">
	 			<bean:define id="resultId" name="result" property="idInternal"/>
				<tr>
			 		<td width="75%" class="listClasses" style="text-align:left">
			 			<fr:view name="result" layout="tabular" schema="result.publication.presentation.Unstructured" />
		 			</td>
					<td class="listClasses" style="text-align:center">
				 		<html:link page="<%="/resultPublications/prepareEdit.do?resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>
			 		</td>
			 		<td class="listClasses" style="text-align:center">
			 			<html:link page="<%="/resultPublications/prepareDelete.do?&resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			 		</td>
		 		</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>
