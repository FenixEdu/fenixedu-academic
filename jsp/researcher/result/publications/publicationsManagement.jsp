<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="publications" name="UserView" property="person.researchResultPublications"/>
	<bean:define id="personId" name="person" property="idInternal"/>
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></h2>

	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<ul class="list5 mbottom15">
		<li>
			<html:link page="/resultPublications/prepareCreate.do">
				<bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.ResultPublication.insert.link"/>
			</html:link>
		</li>
		<li>
			<html:link page="/publications/bibtexManagement.do?method=prepareOpenBibtexFile">
				<bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.result.publication.importBibtex"/>
			</html:link>
		</li>
		<li>
			<html:link target="_blank" page="<%="/publications/bibtexManagement.do?method=exportAllPublicationsToBibtex&amp;personOID=" + personId %>">
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportAllToBibTeX" />
			</html:link>
		</li>
	</ul>

	<logic:empty name="publications">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.emptyList"/></em></p>
	</logic:empty>

	<logic:notEmpty name="books">
		<p id='books' class="mtop3 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Books"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Book" toScope="request"/>
		<bean:define id="results" name="books" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="inbooks">
		<p id='inbooks' class="mtop3 mbottom0"><strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.BookParts"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.BookPart" toScope="request"/>
		<bean:define id="results" name="inbooks" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
<bean:define id="hasArticles" value="false" toScope="request"/>
	<logic:notEmpty name="national-articles"> 
		<bean:define id="hasArticles" value="true" toScope="request"/>
	</logic:notEmpty>
	<logic:notEmpty name="international-articles"> 
		<bean:define id="hasArticles" value="true" toScope="request"/>
	</logic:notEmpty>	

	<logic:equal name="hasArticles" value="true">
		<p id='books' class="mtop3 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Articles"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Article" toScope="request"/>

		<logic:notEmpty name="international-articles">
		<p class="indent1p5 mtop15"><em><bean:message key="label.internationalScope" bundle="RESEARCHER_RESOURCES"/>:</em></p>
		<bean:define id="results" name="international-articles" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
		</logic:notEmpty>
				
		<logic:notEmpty name="national-articles">
		<p class="indent1p5 mtop15"><em><bean:message key="label.nationalScope" bundle="RESEARCHER_RESOURCES"/>:</em></p>
		<bean:define id="results" name="national-articles" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
		</logic:notEmpty>
				
	</logic:equal>
	
	<logic:notEmpty name="inproceedings">
		<p id='inproceedings' class="mtop3 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Inproceedings"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
		<bean:define id="results" name="inproceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="proceedings">
		<p id='proceedings' class="mtop3 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Proceedings"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Proceedings" toScope="request"/>
		<bean:define id="results" name="proceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="theses">
		<p id='theses' class="mtop3 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Theses"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Thesis" toScope="request"/>
		<bean:define id="results" name="theses" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="manuals">
		<p id='manuals' class="mtop3 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Manuals"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Manual" toScope="request"/>
		<bean:define id="results" name="manuals" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="technicalReports">
		<p id='technicalReports' class="mtop3 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.TechnicalReports"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.TechnicalReport" toScope="request"/>
		<bean:define id="results" name="technicalReports" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="otherPublications">
		<p id='otherPublications' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.OtherPublications"/></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.OtherPublication" toScope="request"/>
		<bean:define id="results" name="otherPublications" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="unstructureds">
		<p id='unstructureds' class="mtop3 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Unstructureds"/></span></strong></p>
		<bean:define id="results" name="unstructureds" toScope="request"/>
		<ul>
			<logic:iterate id="result" name="results" scope="request">
	 			<bean:define id="resultId" name="result" property="idInternal"/>
				<li class="mtop1">
		 			<fr:view name="result" layout="values" schema="result.publication.presentation.Unstructured">
		 				<fr:layout>
		 					<fr:property name="htmlSeparator" value=", "/>
		 					<fr:property name="indentation" value="false"/>
		 				</fr:layout>
	 				</fr:view>
		 			<p class="mtop025">
			 		<html:link page="<%="/resultPublications/prepareEditData.do?resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.convertUnstructured" /></html:link>, 
		 			<html:link page="<%="/resultPublications/prepareDelete.do?&resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.delete" /></html:link>
			 		</p>
	 			</li>
			</logic:iterate>
		</ul>
	</logic:notEmpty>
</logic:present>