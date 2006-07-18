<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<style>
		table.search {
		background-color: #f5f5f5;
		border-collapse: collapse;
		}
		table.search tr td {
		border: 1px solid #fff;
		padding: 0.3em;
		}
		.leftcolumn {
		text-align: right;
		}
		
		h3.cd_heading {
		font-weight: normal;
		margin-top: 3em;
		border-top: 1px solid #e5e5e5;
		background-color: #fafafa;
		padding: 0.25em 0 0em 0.25em;
		padding: 0.5em 0.25em;
		}
		h3.cd_heading span {
		margin-top: 2em;
		border-bottom: 2px solid #fda;
		}
		
		div.cd_block {
		background-color: #fed;
		padding: 0.5em 0.5em 0.5em 0.5em;
		}
		
		table.cd {
		border-collapse: collapse;
		}
		table.cd th {
		border: 1px solid #ccc;
		background-color: #eee;
		padding: 0.5em;
		text-align: center;
		}
		table.cd td {
		border: 1px solid #ccc;
		background-color: #fff;
		padding: 0.5em;
		text-align: center;
		}
		
		p.insert {
		padding-left: 2em;
		}
		div.cd_float {
		width: 100%;
		float: left;
		padding: 0 2.5em;
		padding-bottom: 1em;
		}
		ul.cd_block {
		width: 43%;
		list-style: none;
		float: left; 
		margin: 0;
		padding: 0;
		padding: 1em;
		}
		ul.cd_block li {
		}
		ul.cd_nostyle {
		list-style: none;
		}
</style>

<logic:present role="RESEARCHER">
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement"/></h2>

	<html:link page="/publications/publicationsManagement.do?method=prepareCreatePublication"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement.insertPublication" /></html:link>
	
	<logic:notEmpty name="books">
		<h3 id='books' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.books"/> </span> </h3>
		<logic:iterate id="book" name="books">
			<fr:view name="book" layout="tabular" schema="result.publication.resume.Book"/>
	 		<bean:define id="bookId" name="book" property="idInternal"/>
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ bookId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
	 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ bookId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
	  		<br /><br />
		</logic:iterate>
	</logic:notEmpty>
<%--
	<h3 id='booksParts' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.bookParts"/> </span> </h3>
	<logic:iterate id="bookPart" name="bookParts">
		<fr:view name="bookPart" layout="tabular" schema="result.publication.resume.book_part"/>
 		<bean:define id="bookPartId" name="bookPart" property="idInternal"/>
 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ bookPartId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ bookPartId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
  		<br /><br />
	</logic:iterate>
	
	<h3 id='articles' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.articles"/> </span> </h3>
	<logic:iterate id="article" name="articles">
		<fr:view name="article" layout="tabular" schema="result.publication.resume.article"/>
		<bean:define id="articleId" name="article" property="idInternal"/>
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ articleId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ articleId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
		<br /><br />
	</logic:iterate>
		
	<h3 id='thesis' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.thesis"/> </span> </h3>
	<logic:iterate id="thesi" name="thesis">
		<fr:view name="thesi" layout="tabular" schema="result.publication.resume.thesis"/>
		<bean:define id="thesiId" name="thesi" property="idInternal"/>
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ thesiId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ thesiId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
		<br /><br />
	</logic:iterate>
		
	<h3 id='conferences' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.conferences"/> </span> </h3>
	<logic:iterate id="conference" name="conferences">
		<fr:view name="conference" layout="tabular" schema="result.publication.resume.conference"/>
		<bean:define id="conferenceId" name="conference" property="idInternal"/>
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ conferenceId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ conferenceId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
		<br /><br />
	</logic:iterate>
		
	<h3 id='technicalReports' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.technicalReports"/> </span> </h3>
	<logic:iterate id="technicalReport" name="technicalReports">
		<fr:view name="technicalReport" layout="tabular" schema="result.publication.resume.technical_report"/>
 		<bean:define id="technicalReportId" name="technicalReport" property="idInternal"/>
 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ technicalReportId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
 		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ technicalReportId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
  		<br /><br />
	</logic:iterate>
		
	<h3 id='otherPublicationss' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.otherPublications"/> </span> </h3>
	<logic:iterate id="otherPublication" name="otherPublications">
		<fr:view name="otherPublication" layout="tabular" schema="result.publication.resume.other_publication"/>
		<bean:define id="otherPublicationId" name="otherPublication" property="idInternal"/>
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ otherPublicationId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ otherPublicationId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
		<br /><br />
	</logic:iterate>
		
	<h3 id='unstructureds' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.unstructureds"/> </span> </h3>
	<logic:iterate id="unstructured" name="unstructureds">
		<fr:view name="unstructured" layout="tabular" schema="result.publication.resume.unstructured"/>
		<bean:define id="unstructuredId" name="unstructured" property="idInternal"/>
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareViewEditPublication&publicationId="+ unstructuredId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsDetails" /></html:link>&nbsp;&nbsp;&nbsp;
		<html:link page="<%="/publications/publicationsManagement.do?method=prepareDeletePublication&publicationId="+ unstructuredId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
		<br /><br />
	</logic:iterate>
		
--%>
    <%--   
		<logic:iterate id="publication" name="publications">
			<bean:define id="resultType" name="publication" property="publicationType" type="net.sourceforge.fenixedu.domain.research.result.publication.PublicationType"/>
		  	<u><bean:message bundle="RESEARCHER_RESOURCES" key="<%="researcher.result.publication.publicationType."+resultType.toString().toLowerCase() %>"/></u>
 			<fr:view name="publication" layout="tabular" schema="<%="result.publication.resume."+resultType.toString().toLowerCase() %>"/>
  			<br /><hr />
		</logic:iterate>
	--%>



	<br />	<br />
	<html:link page="/publications/publicationsManagement.do?method=prepareCreatePublication"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.publicationsManagement.insertPublication" /></html:link>
</logic:present>
