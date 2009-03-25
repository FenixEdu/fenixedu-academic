<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/layout/publicGesDisLayout_2col.jsp" flush="true">
	<tiles:put name="serviceName" value="<bean:message key="institution.name" bundle="GLOBAL_RESOURCES"/>" />
	<tiles:put name="executionCourseName" beanName="exeName" />
	<tiles:put name="degrees" value="/publico/associatedDegrees.jsp" />
	<tiles:put name="body" value="/publico/viewExecutionCourse_bd.jsp" />
	<tiles:put name="navbarGeral" value="/publico/commonNavLocalPub.jsp" />
	<tiles:put name="navbar" value="/publico/gesdisNavbarInitial.jsp"/>
	<tiles:put name="footer" value="/commons/blank.jsp" />
</tiles:insert>