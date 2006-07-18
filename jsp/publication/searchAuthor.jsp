<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.publications.Management"/></h2>
<logic:messagesPresent>
		<span class="error">
			<html:errors property="nonValidating"/>
			<html:errors property="nonValidatingName"/>
			<html:errors property="nonValidatingOrganization"/>
		</span>
</logic:messagesPresent>
<html:form action="/searchAuthor">
<br/>
<h3>
		<bean:message key="message.publications.insertPublication" />/
		<bean:message key="message.publications.insertAuthors" />
</h3>

	<bean:message key="message.publication.InsertSearchAuthors" /><br/>
	<bean:message key="message.publication.manageSearchAuthors" /><br/>
	<bean:message key="message.publication.manageSearchAuthors4" /><br/>
	<bean:message key="message.publication.manageSearchAuthors5" /><br/>
	<bean:message key="message.publication.manageSearchAuthors6" /><br/>
	<bean:message key="message.publication.manageSearchAuthors7" /><br/>

</P>
	<br />
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsId" property="authorsId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsName" property="authorsName"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.index" property="index"/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.title" property="title" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.description" property="description" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoPublicationTypeId" property="infoPublicationTypeId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.subtype" property="subtype" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.journalName" property="journalName" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.volume" property="volume" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.firstPage" property="firstPage" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.lastPage" property="lastPage" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.language" property="language" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.format" property="format" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.observation" property="observation" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.number" property="number" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.month" property="month" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.year" property="year" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.month_end" property="month_end" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.year_end" property="year_end" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.editor" property="editor" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.country" property="country" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.issn" property="issn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.scope" property="scope" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.url" property="url" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.editorCity" property="editorCity" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.numberPages" property="numberPages" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.edition" property="edition" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.fascicle" property="fascicle" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.serie" property="serie" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.isbn" property="isbn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.local" property="local" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.conference" property="conference" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.instituition" property="instituition" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.originalLanguage" property="originalLanguage" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.translatedAuthor" property="translatedAuthor" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.criticizedAuthor" property="criticizedAuthor" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.publicationType" property="publicationType" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.university" property="university" />
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoPublicationTypeId" property="infoPublicationTypeId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.typePublication" property="typePublication"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="searchAuthor"/>
	<logic:present name="infoAuthorsList">
		<logic:iterate id="infoAuthor" name="infoAuthorsList" type="net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor">
			<bean:define id="authorIdInserted" name="infoAuthor" property="idInternal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsIds" property="authorsIds" value="<%= pageContext.findAttribute("authorIdInserted").toString() %>"/>
		</logic:iterate>
	</logic:present>
		<table>
			<tr>
				<td>
					<bean:message key="message.publication.stringNomeSearch" />
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.searchAuthorString" size="20"  property="searchAuthorString"/>
				</td>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.searchAuthorString" styleClass="inputbutton" property="searchAuthorString">
						<bean:message key="button.publication.search"/>
					</html:submit> 
				</td>
				<td>
					<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="button.publication.clear"/>
					</html:reset>
				</td>
			</tr>
		</table>
		<br/>
</html:form>	

<html:form action="/insertAuthorsInPublication">
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoPublicationTypeId" property="infoPublicationTypeId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.typePublication" property="typePublication"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertAuthorsInPublication"/>	
	<logic:present name="infoAuthorsList">
		<logic:iterate id="infoAuthor" name="infoAuthorsList" type="net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor">
			<bean:define id="authorIdInserted" name="infoAuthor" property="idInternal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsIds" property="authorsIds" value="<%= pageContext.findAttribute("authorIdInserted").toString() %>"/>
		</logic:iterate>
	</logic:present>
	
	<logic:present name="infoAuthorsPersons">
		<logic:notEmpty name="infoAuthorsPersons">
			<bean:message key="message.publication.insertAuthorExplanation"	/>
			<table>
				<br/>
				<tr>
					<bean:message key="message.publications.table.result" />
					<br/>
				</tr>
				<tr>
					<th class="listClasses-header">&nbsp;
					</th>
					<th class="listClasses-header"><bean:message key="message.publications.table.name" />
					</th>
					<th class="listClasses-header"><bean:message key="message.publications.table.organization" />
					</th>
				</tr>
				<logic:iterate id="authorPersons" name="infoAuthorsPersons" type="net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthorPerson">
					<tr>	 			
						<td class="listClasses">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.authorsPersonsCodes" property="authorsPersonsCodes">
								<bean:write name="authorPersons" property="keyFinal"/>
							</html:multibox>
						</td>
						<td class="listClasses" style="text-align:left"><bean:write name="authorPersons" property="name"/>
						</td>
						<td class="listClasses" style="text-align:left"><bean:write name="authorPersons" property="organization"/>
						</td>
					</tr>
				</logic:iterate>
				<br/>
				<tr>
					<td>
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
							<bean:message key="button.publication.InsertAuthors"/>
						</html:submit> 
					</td>
				</tr>						
			</table>
		</logic:notEmpty>
		<br/>
	</logic:present>			
</html:form>

<html:form action="/insertAuthorInPublication">
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoPublicationTypeId" property="infoPublicationTypeId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.typePublication" property="typePublication"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertAuthorInPublication"/>	
	<logic:present name="infoAuthorsList">
		<logic:iterate id="infoAuthor" name="infoAuthorsList" type="net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor">
			<bean:define id="authorIdInserted" name="infoAuthor" property="idInternal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsIds" property="authorsIds" value="<%= pageContext.findAttribute("authorIdInserted").toString() %>"/>
		</logic:iterate>
	</logic:present>				
	<logic:present name="infoAuthorsPersons">	
		<logic:empty name="infoAuthorsPersons">
			<bean:message key="message.publications.notFound" />
			<br/>
			<table>
				<tr>
					<td>
						<bean:message key="message.publications.name" />
					</td>
					<td>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.authorName" size="50"  property="authorName"/>
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="message.publications.organization" />
					</td>
					<td>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.authorOrganization" size="50"  property="authorOrganization"/>
					</td>
				</tr>
			</table>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
				<bean:message key="button.publication.InsertAuthor"/>
			</html:submit> 
		</logic:empty>
	</logic:present>
</html:form>

<html:form action="/deleteAuthorInPublication">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoPublicationTypeId" property="infoPublicationTypeId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.typePublication" property="typePublication"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteAuthorInPublication"/>
<h4><bean:message key="message.publications.authorsInserteds" />
</h4>
	<logic:present name="infoAuthorsList">
		<logic:notEmpty name="infoAuthorsList">
			<bean:message key="message.publication.deleteAuthorExplanation" />
			<br/>			
			<bean:message key="message.publications.teacherPresentAndListPresent" />
			<br/>
			<table>
				<tr>
					<th class="listClasses-header"><bean:message key="message.publications.table.name" />
					</th>
					<th class="listClasses-header"><bean:message key="message.publications.table.organization" />
					</th>
					<th class="listClasses-header"><bean:message key="message.publications.table.select" />
					</th>
				</tr>
				<logic:iterate id="infoAuthor" name="infoAuthorsList" type="net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor">
					<bean:define id="authorIdDeleted" name="infoAuthor" property="idInternal"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsIds" property="authorsIds" value="<%= pageContext.findAttribute("authorIdDeleted").toString() %>"/>
					<tr>
						<td class="listClasses" style="text-align:center">
							<logic:notEmpty name="infoAuthor" property="infoPessoa">
								<bean:define id="infoPessoa" name="infoAuthor" property="infoPessoa" type="net.sourceforge.fenixedu.dataTransferObject.InfoPerson"/>
								<bean:write name="infoPessoa" property="nome"/>
							</logic:notEmpty>
							<logic:empty name="infoAuthor" property="infoPessoa">
								<bean:write name="infoAuthor" property="author"/>
							</logic:empty>
						</td>
						<td class="listClasses" style="text-align:center">
							<bean:write name="infoAuthor" property="organization"/>
						</td>
						<td class="listClasses" style="text-align:center">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.authorsIdstoDelete" property="authorsIdstoDelete">
								<bean:write name="infoAuthor" property="idInternal"/>
							</html:multibox>
						</td>
					</tr>
				</logic:iterate>
			</table>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
				<bean:message key="button.publication.DeleteAuthors"/>
			</html:submit> 
		</logic:notEmpty>
		<logic:empty name="infoAuthorsList">
			<bean:message key="message.publications.teacherPresent" />
		</logic:empty>
	</logic:present>
</html:form>	


<html:form action="/readPublicationAttributes">
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoPublicationTypeId" property="infoPublicationTypeId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.typePublication" property="typePublication"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	
	<logic:present name="infoAuthorsList">
		<logic:iterate id="infoAuthor" name="infoAuthorsList" type="net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor">
			<bean:define id="authorIdInserted" name="infoAuthor" property="idInternal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorsIds" property="authorsIds" value="<%= pageContext.findAttribute("authorIdInserted").toString() %>"/>
		</logic:iterate>
	</logic:present>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm"><bean:message key="button.publication.continue"/>
</html:submit> 
<br/> 
</html:form>
