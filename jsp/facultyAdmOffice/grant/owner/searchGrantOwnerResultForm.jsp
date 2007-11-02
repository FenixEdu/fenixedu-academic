<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="link.search.grant.owner"/></h2>


<%-- Present number of hits of search --%>
<logic:present name="numberOfTotalElementsInSearch">
	<bean:define id="resultSize" name="numberOfTotalElementsInSearch"/>
	<p><bean:message key="label.grant.owner.searchresult" arg0="<%= resultSize.toString() %>"/></p>
</logic:present>


<logic:equal name="justGrantOwner" value="on">
	<p><strong><bean:message key="label.grant.owner.filter"/></strong></p>
</logic:equal>

<table>
<tr>
	<logic:present name="beforeSpan">
	<td>
       	<html:link page='<%= "/searchGrantOwner.do?page=0&amp;method=doSearch&amp;startIndex=" + request.getAttribute("beforeSpan") + "&amp;name=" + request.getAttribute("name") + "&amp;justGrantOwner=" + request.getAttribute("justGrantOwner") %>' >
		   	<bean:message key="link.grant.owner.list.before.page"/>
		</html:link>
	</td>
	</logic:present>
	<td>
		&nbsp;
	</td>
	<td>
        <logic:present name="actualPage">            
    		<bean:write name="actualPage"/>/<bean:write name="numberOfPages"/>
        </logic:present>
	</td>
	<td>
		&nbsp;
	</td>
	<td>
	<logic:present name="nextSpan">
		<html:link page='<%= "/searchGrantOwner.do?page=0&amp;method=doSearch&amp;startIndex=" + request.getAttribute("nextSpan") + "&amp;name=" + request.getAttribute("name") + "&amp;justGrantOwner=" + request.getAttribute("justGrantOwner") %>' >
 		   	<bean:message key="link.grant.owner.list.after.page"/>
		</html:link>
	</td>
	</logic:present>
</tr>
</table>


<table class="tstyle4">
	<%-- Table description rows --%>
	<tr>
		<th colspan="3">
			<bean:message key="label.grant.owner.personalinformation"/>
		</th>
		<th colspan="2">
			<bean:message key="label.grant.owner.documentidentification"/>
		</th>
		<th>
			&nbsp;<%-- blank --%>
		</th>				
	</tr>
	<tr>
		<th>
			<bean:message key="label.grant.owner.infoperson.name"/>
		</th>
		<th>
			<bean:message key="label.grant.owner.number"/>
		</th>
		<th>
			<bean:message key="label.grant.owner.infoperson.socialSecurityNumber"/>
		</th>
		<th>
			<bean:message key="label.grant.owner.infoperson.documentId"/>
		</th>
		<th>
			<bean:message key="label.grant.owner.infoperson.documentIdType"/>
		</th>
		<th>
		</th>				
	</tr>
	<%-- Table with result of search --%>
	<logic:iterate id="infoGrantOwner" name="infoGrantOwnerList">
		<tr>
			<td>
				<bean:write name="infoGrantOwner" property="personInfo.nome"/>
			</td>
			<td class="acenter">&nbsp;
				<logic:present name="infoGrantOwner" property="grantOwnerNumber">
					<bean:write name="infoGrantOwner" property="grantOwnerNumber"/>
				</logic:present>
			</td>
			<td class="acenter">&nbsp;
				<bean:write name="infoGrantOwner" property="personInfo.numContribuinte"/>
			</td>
			<td class="acenter">&nbsp;
				<bean:write name="infoGrantOwner" property="personInfo.numeroDocumentoIdentificacao"/>
			</td>
			<td class="acenter">&nbsp;
				<bean:define id="idType" name="infoGrantOwner" property="personInfo.tipoDocumentoIdentificacao"/>
				<bean:message key='<%=idType.toString()%>'/>
			</td>
			<td>&nbsp;
				<%-- Person is a Grant Owner already --%>
				<logic:present name="infoGrantOwner" property="grantOwnerNumber">
					<html:link page="/manageGrantOwner.do?method=prepareManageGrantOwnerForm"
						       paramId="idInternal"
						       paramName="infoGrantOwner"
						       paramProperty="idInternal"> 
						<bean:message key="link.manage.grant.owner" />
					</html:link>
				</logic:present>
				<%-- Person is not a Grant Owner --%>
				<logic:notPresent name="infoGrantOwner" property="grantOwnerNumber">
					<html:link page='<%= "/editGrantOwner.do?method=prepareEditGrantOwnerForm&amp;loaddb=" + 1 %>'
							   paramId="personId"
							   paramName="infoGrantOwner"
							   paramProperty="personInfo.idInternal">
						<bean:message key="link.create.grant.owner" />
					</html:link>
				</logic:notPresent>
			</td>
		</tr>
	</logic:iterate>
</table>


<table>
<tr>
	<logic:present name="beforeSpan">
	<td>
       	<html:link page='<%= "/searchGrantOwner.do?page=0&amp;method=doSearch&amp;startIndex=" + request.getAttribute("beforeSpan") + "&amp;name=" + request.getAttribute("name") + "&amp;justGrantOwner=" + request.getAttribute("justGrantOwner") %>' >
		   	<bean:message key="link.grant.owner.list.before.page"/>
		</html:link>
	</td>
	</logic:present>
	<td>
		&nbsp;
	</td>
	<td>
        <logic:present name="actualPage">            
    		<bean:write name="actualPage"/>/<bean:write name="numberOfPages"/>
        </logic:present>
	</td>
	<td>
		&nbsp;
	</td>
	<td>
	<logic:present name="nextSpan">
		<html:link page='<%= "/searchGrantOwner.do?page=0&amp;method=doSearch&amp;startIndex=" + request.getAttribute("nextSpan") + "&amp;name=" + request.getAttribute("name") + "&amp;justGrantOwner=" + request.getAttribute("justGrantOwner") %>' >
 		   	<bean:message key="link.grant.owner.list.after.page"/>
		</html:link>
	</td>
	</logic:present>
</tr>
</table>


<%-- Create a new person Grant Owner --%>
<p>
	<bean:message key="message.grant.owner.creation"/>:&nbsp;
	<html:link page="/editGrantOwner.do?method=prepareEditGrantOwnerForm">
		<bean:message key="link.create.person.grant.owner"/>
	</html:link>
</p>