<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.owner.list"/></h2>

<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>

<logic:messagesNotPresent>

	<logic:empty name="listGrantOwners">
		<p>
			<em>
				Nenhum bolseiro encontrado.
			</em>
		</p>
	</logic:empty>

	<logic:notEmpty name="listGrantOwners">

		<%
			String sortCriteria = request.getParameter("orderBy");
		    if (sortCriteria == null) {
		    	sortCriteria = "grantOwnerNumber=asc";
		    }
		%>
	
		<bean:define id="showGrantOwnerUrl">
			/listGrantOwner.do?method=showGrantOwner
		</bean:define>
	
		<bean:define id="url" type="java.lang.String">/facultyAdmOffice/listGrantOwner.do?method=listGrantOwners&amp;orderBy=<%=sortCriteria%></bean:define>
		<cp:collectionPages url="<%= url %>" 
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
	
		<fr:view name="resultPage" schema="InfoListGrantOwnerByOrder.list">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle4 tdcenter mtop05" />
				<fr:property name="rowClasses" value="bgwhite," />
				<fr:property name="groupLinks" value="true" />
				<fr:property name="columnClasses" value="smalltxt,smalltxt,smalltxt" />
	
				<fr:property name="linkFormat(showGrantOwner)" value="<%= showGrantOwnerUrl + "&grantOwnerId=${grantOwnerId}" %>"/>
				<fr:property name="key(showGrantOwner)" value="link.grant.owner.show"/>

				<fr:property name="sortBy" value="<%= sortCriteria %>"/>
				<fr:property name="sortUrl" value="<%= "/listGrantOwner.do?method=listGrantOwners" %>"/>
				<fr:property name="sortParameter" value="orderBy"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:messagesNotPresent>
