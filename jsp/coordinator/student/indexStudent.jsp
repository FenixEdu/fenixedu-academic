<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Coordenador" />
  <tiles:put name="serviceName" value="Portal do Coordenador" />
  <tiles:put name="navLocal" value="/coordinator/student/studentMenu.jsp" />
  <tiles:put name="navGeral" value="/coordinator/globalNav.jsp" />
  <tiles:put name="body" value="/coordinator/welcomeScreen.jsp" />
  <tiles:put name="footer" value="/coordinator/copyrightDefault.jsp" />
</tiles:insert>
