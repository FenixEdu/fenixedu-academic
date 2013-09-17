<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="definition.public.mainPage" beanName="" flush="true">
	<tiles:put name="executionCourseName" beanName="exeName" />
	<tiles:put name="degrees" value="/publico/associatedDegrees.jsp" />
	<tiles:put name="body" value="/publico/viewExecutionCourse_bd.jsp" />
	<tiles:put name="navbarGeral" value="/publico/commonNavLocalPub.jsp" />
	<tiles:put name="navbar" value="/publico/gesdisNavbarInitial.jsp"/>
	<tiles:put name="footer" value="/commons/blank.jsp" />
</tiles:insert>