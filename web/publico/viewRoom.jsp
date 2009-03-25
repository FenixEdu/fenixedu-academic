<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/publicGesDisLayout_2col.jsp" flush="true">
	<tiles:put name="title" value=".Instituto Superior Técnico" />
	<tiles:put name="serviceName" value="<bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/>" /> 
	<tiles:put name="symbols_row" value="/publico/degreeSite/symbolsRow.jsp" />
	<tiles:put name="profile_navigation" value="/publico/degreeSite/profileNavigation.jsp" />
    <tiles:put name="lateral_nav" value="/publico/commonNavLocalPub.jsp" />
	<tiles:put name="body" value="/publico/viewRoom_bd.jsp" />
	<tiles:put name="footer" value="/publico/degreeSite/footer.jsp" />
</tiles:insert>
