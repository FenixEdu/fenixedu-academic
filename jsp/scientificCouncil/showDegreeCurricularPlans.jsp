<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/teacherLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="Conselho Científico" />
  <tiles:put name="serviceName" value="Portal do Conselho Científico" />
  <tiles:put name="navLocal" value="/scientificCouncil/navigation.jsp" />
  <tiles:put name="navGeral" value="/scientificCouncil/commonNavGeral.jsp" />
  <tiles:put name="body" value="/scientificCouncil/showDegreeCurricularPlans_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>
