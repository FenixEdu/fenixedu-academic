<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoRequiredAttributeList" name="component" property="infoRequiredAttributes"/>
<bean:define id="infoNonRequiredAttributeList" name="component" property="infoNonRequiredAttributes"/>

<html:form action="/publicationCientific">	
	
<h2><bean:message key="title.teacherInformation"/></h2>

	<logic:messagesPresent>
		<span class="error">
			<html:errors /> 
		</span>
	</logic:messagesPresent>	
<br/>
<h3>	
		<logic:present name="infoPublication">
			<bean:message key="message.publications.editPublication" />		
		</logic:present>
		<logic:notPresent name="infoPublication">
			<bean:message key="message.publications.insertPublication" />	
		</logic:notPresent>
		
</h3>
	
<p class="infoop"><span class="emphasis-box">1</span>
	<logic:present name="infoPublication">
			<bean:message key="message.publications.managementEdit" />		
		</logic:present>
		<logic:notPresent name="infoPublication">
			<bean:message key="message.publications.managementInsert" />	
		</logic:notPresent>
</P>

<p>
	<bean:message key="message.publications.authorsInserteds"/>

	<br />			
	
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>
	<!--<html:hidden property="infoPublicationTypeId"/>-->
	<html:hidden property="typePublication"/>
	<html:hidden property="method" value="edit"/>
	
	<logic:present name="infoAuthorsList">
		<logic:iterate id="infoAuthor" name="infoAuthorsList" type="DataBeans.publication.InfoAuthor">
			<bean:define id="authorIdInserted" name="infoAuthor" property="idInternal"/>
			<html:hidden property="authorsIds" value="<%= pageContext.findAttribute("authorIdInserted").toString() %>"/>
		</logic:iterate>
		<logic:notEmpty name="infoAuthorsList">
			<table>
				<tr>
					<td class="listClasses-header"><bean:message key="message.publications.table.name" />
					</td>
					<td class="listClasses-header"><bean:message key="message.publications.table.organization" />
					</td>
				</tr>
				<logic:iterate id="infoAuthor" name="infoAuthorsList" type="DataBeans.publication.InfoAuthor">
					<tr>
						<td class="listClasses" style="text-align:center">
							<logic:notEmpty name="infoAuthor" property="infoPessoa">
								<bean:define id="infoPessoa" name="infoAuthor" property="infoPessoa" type="DataBeans.InfoPerson"/>
								<bean:write name="infoPessoa" property="nome"/>
							</logic:notEmpty>
							<logic:empty name="infoAuthor" property="infoPessoa">
								<bean:write name="infoAuthor" property="author"/>
							</logic:empty>
						</td>
						<td class="listClasses" style="text-align:center">
							<bean:write name="infoAuthor" property="organization"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoAuthorsList">
			<bean:message key="message.publications.noAuthors"/>
		</logic:empty>
	</logic:present>
	<br />
</p>
	<bean:message key="message.publications.fieldsToFill"/>
	
	<html:select property="infoPublicationTypeId" onchange="this.form.method.value='prepareEdit';
		this.form.page.value='0';this.form.typePublication.value='Cientific';this.form.submit();">
		<html:options collection="publicationTypesList" property="idInternal" labelProperty="publicationType"/>
	</html:select>
	<br/><br/>	
	
<table>
		<logic:iterate id="att" name="infoRequiredAttributeList">
						
			<logic:equal name="att" property="attributeType" value="subtype">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.subType" />
					</td>
					<td>
						<html:select property="subtype">
							<html:options collection="subTypeList" property="subtype" />
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="journalName">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.journalName" />
					</td>
					<td>
						<html:text size="20" property="journalName"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="title">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.title" />
					</td>
					<td>
						<html:text size="20" property="title"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="volume">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.volume" />
					</td>
					<td>
						<html:text size="20" property="volume"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="editor">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.editor" />
					</td>
					<td>
						<html:text size="30" property="editor"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="month">
				<tr>
					<td>
						<bean:define id="valueMonth" name="publicationManagementForm" property="infoPublicationTypeId"/>
						<logic:equal name="valueMonth" value="3">
							<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.monthInith" />
						</logic:equal>
						<logic:notEqual name="valueMonth" value="3">
							<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.month" />
						</logic:notEqual>	
					</td>
					<td>
						<html:select property="month">
							<logic:iterate id="month" name="monthList" >
								<html:option value='<%=month.toString()%>'>
									<bean:write name="month" />		
								</html:option>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
				
			<logic:equal name="att" property="attributeType" value="year">
				<tr>
					<td>
						<bean:define id="valueYear" name="publicationManagementForm" property="infoPublicationTypeId"/>
						<logic:equal name="valueYear" value="3">
							<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.yearInith" />
						</logic:equal>
						<logic:notEqual name="valueYear" value="3">
							<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.year" />
						</logic:notEqual>
					</td>
					<td>
						<html:text size="9" property="year"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="month_end">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.month_end" />
					</td>
					<td>
						<html:select property="month_end">
							<logic:iterate id="month" name="monthList" >
								<html:option value='<%=month.toString()%>'>
									<bean:write name="month" />		
								</html:option>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
				
			<logic:equal name="att" property="attributeType" value="year_end">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.year_end" />
					</td>
					<td>
						<html:text size="9" property="year_end"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="firstPage">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.inicialPage" />
					</td>
					<td>
						<html:text size="3" property="firstPage"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="lastPage">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.finalPage" />
					</td>
					<td>
						<html:text size="3" property="lastPage"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="language">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.language" />
					</td>
					<td>
						<html:text size="20" property="language"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="country">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.country" />
					</td>
					<td>
						<html:text size="20" property="country"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="issn">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.issn" />
					</td>
					<td>
						<html:text size="20" property="issn"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="isbn">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.isbn" />
					</td>
					<td>
						<html:text size="20" property="isbn"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="format">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.format" />
					</td>
					<td>
						<html:select property="format">
							<html:options collection="formatList" property="format" />
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="scope">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.scope" />
					</td>
					<td>
						<html:select property="scope">
							<logic:iterate id="scopes" name="scopeList" >
								<html:option value='<%=scopes.toString()%>'>
									<bean:write name="scopes" />
								</html:option>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="observation">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.observations" />
					</td>
					<td>
						<html:text size="20" property="observation"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="edition">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.edition" />
					</td>
					<td>
						<html:text size="20" property="edition"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="editorCity">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.editorCity" />
					</td>
					<td>
						<html:text size="20" property="editorCity"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="numberPages">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.numberPages" />
					</td>
					<td>
						<html:text size="20" property="numberPages"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="fascicle">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.fascicle" />
					</td>
					<td>
						<html:text size="20" property="fascicle"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="serie">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.serie" />
					</td>
					<td>
						<html:text size="20" property="serie"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="url">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.url" />
					</td>
					<td>
						<html:text size="70" property="url"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="university">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.university" />
					</td>
					<td>
						<html:text size="40" property="university"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="local">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.local" />
					</td>
					<td>
						<html:text size="40" property="local"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="translatedAuthor">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.translatedAuthor" />
					</td>
					<td>
						<html:text size="60" property="translatedAuthor"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="originalLanguage">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.originalLanguage" />
					</td>
					<td>
						<html:text size="20" property="originalLanguage"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="criticizedAuthor">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.criticizedAuthor" />
					</td>
					<td>
						<html:text size="60" property="criticizedAuthor"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="publicationType">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.publicationType" />
					</td>
					<td>
						<html:text size="60" property="publicationType"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="conference">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.conference" />
					</td>
					<td>
						<html:text size="60" property="conference"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="instituition">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.instituition" />
					</td>
					<td>
						<html:text size="60" property="instituition"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="number">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.required" /><bean:message key="message.publicationAttribute.number" />
					</td>
					<td>
						<html:text size="10" property="number"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
		</logic:iterate>

		<logic:iterate id="att" name="infoNonRequiredAttributeList">
						
			<logic:equal name="att" property="attributeType" value="subtype">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.subType" />
					</td>
					<td>
						<html:select property="subtype">
							<html:options collection="subTypeList" property="subtype" />
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="journalName">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.journalName" />
					</td>
					<td>
						<html:text size="20" property="journalName"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="title">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.title" />
					</td>
					<td>
						<html:text size="20" property="title"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="volume">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.volume" />
					</td>
					<td>
						<html:text size="20" property="volume"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="editor">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.editor" />
					</td>
					<td>
						<html:text size="30" property="editor"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="month">
				<tr>
					<td>
					<bean:define id="valueMonth" name="publicationManagementForm" property="infoPublicationTypeId"/>
					<logic:equal name="valueMonth" value="3">
						<bean:message key="message.publicationAttribute.monthInith" />
					</logic:equal>
					<logic:notEqual name="valueMonth" value="3">
						<bean:message key="message.publicationAttribute.month" />
					</logic:notEqual>	
					</td>
					<td>
						<html:select property="month">
							<logic:iterate id="month" name="monthList" >
								<html:option value='<%=month.toString()%>'>
									<bean:write name="month" />		
								</html:option>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
				
			<logic:equal name="att" property="attributeType" value="year">
				<tr>
					<td>
						<bean:define id="valueYear" name="publicationManagementForm" property="infoPublicationTypeId"/>
						<logic:equal name="valueYear" value="3">
							<bean:message key="message.publicationAttribute.yearInith" />
						</logic:equal>
						<logic:notEqual name="valueYear" value="3">
							<bean:message key="message.publicationAttribute.year" />
						</logic:notEqual>
					</td>
					<td>
						<html:text size="9" property="year"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="month_end">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.month_end" />
					</td>
					<td>
						<html:select property="month_end">
							<logic:iterate id="month" name="monthList" >
								<html:option value='<%=month.toString()%>'>
									<bean:write name="month" />
								</html:option>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
				
			<logic:equal name="att" property="attributeType" value="year_end">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.year_end" />
					</td>
					<td>
						<html:text size="9" property="year_end"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="firstPage">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.inicialPage" />
					</td>
					<td>
						<html:text size="3" property="firstPage"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="lastPage">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.finalPage" />
					</td>
					<td>
						<html:text size="3" property="lastPage"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="language">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.language" />
					</td>
					<td>
						<html:text size="20" property="language"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="country">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.country" />
					</td>
					<td>
						<html:text size="20" property="country"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="issn">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.issn" />
					</td>
					<td>
						<html:text size="20" property="issn"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="isbn">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.isbn" />
					</td>
					<td>
						<html:text size="20" property="isbn"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="format">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.format" />
					</td>
					<td>
						<html:select property="format">
							<html:options collection="formatList" property="format" />
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="scope">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.scope" />
					</td>
					<td>
						<html:select property="scope">
							<logic:iterate id="scopes" name="scopeList" >
								<html:option value='<%=scopes.toString()%>'>
									<bean:write name="scopes" />
								</html:option>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="observation">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.observations" />
					</td>
					<td>
						<html:text size="20" property="observation"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="edition">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.edition" />
					</td>
					<td>
						<html:text size="20" property="edition"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="editorCity">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.editorCity" />
					</td>
					<td>
						<html:text size="20" property="editorCity"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="numberPages">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.numberPages" />
					</td>
					<td>
						<html:text size="20" property="numberPages"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="fascicle">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.fascicle" />
					</td>
					<td>
						<html:text size="20" property="fascicle"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="serie">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.serie" />
					</td>
					<td>
						<html:text size="20" property="serie"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="url">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.url" />
					</td>
					<td>
						<html:text size="70" property="url"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="university">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.university" />
					</td>
					<td>
						<html:text size="40" property="university"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="local">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.local" />
					</td>
					<td>
						<html:text size="40" property="local"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="translatedAuthor">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.translatedAuthor" />
					</td>
					<td>
						<html:text size="60" property="translatedAuthor"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="originalLanguage">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.originalLanguage" />
					</td>
					<td>
						<html:text size="20" property="originalLanguage"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="criticizedAuthor">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.criticizedAuthor" />
					</td>
					<td>
						<html:text size="60" property="criticizedAuthor"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="publicationType">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.publicationType" />
					</td>
					<td>
						<html:text size="60" property="publicationType"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="conference">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.conference" />
					</td>
					<td>
						<html:text size="60" property="conference"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="instituition">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.instituition" />
					</td>
					<td>
						<html:text size="60" property="instituition"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
			
			<logic:equal name="att" property="attributeType" value="number">
				<tr>
					<td>
						<bean:message key="message.publicationAttribute.number" />
					</td>
					<td>
						<html:text size="10" property="number"/>
					</td>
				</tr>
				<tr></tr>
				<tr></tr>
			</logic:equal>
	
		</logic:iterate>
	</tr>
</table>
<bean:message key="message.publications.savePublication"/>
<br/>
<bean:message key="message.publications.cleanPublication"/>
<br/>
<html:submit styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
<br/> 
</html:form>
