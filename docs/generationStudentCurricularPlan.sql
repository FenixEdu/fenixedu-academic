select CONCAT("update STUDENT_CURRICULAR_PLAN set key_degree_curricular_plan =",
              dcp2.id_internal,
              " where key_degree_curricular_plan=", dcp.id_internal, ";") as ""
from DEGREE_CURRICULAR_PLAN dcp inner join DEGREE_CURRICULAR_PLAN dcp2
  on dcp2.name like CONCAT(dcp.name,"2%4")
where left(dcp.name,1) = "L"
order by dcp.name;