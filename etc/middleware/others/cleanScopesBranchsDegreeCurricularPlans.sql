select CONCAT("delete from CURRICULAR_COURSE_SCOPE where id_internal = ", ccs.id_internal,";") as ""
	from CURRICULAR_COURSE_SCOPE ccs 
	left join CURRICULAR_COURSE c on c.id_internal = ccs.key_curricular_course where c.id_internal is null;
select CONCAT("delete from CURRICULAR_COURSE_SCOPE where id_internal = ", ccs.id_internal,";") as ""
	from CURRICULAR_COURSE_SCOPE ccs 
	left join BRANCH c on c.id_internal = ccs.key_branch where c.id_internal is null;	
select CONCAT("delete from BRANCH where id_internal = ", b.id_internal,";") as ""
	from BRANCH b 
	left join DEGREE_CURRICULAR_PLAN dcp on dcp.id_internal = b.key_degree_curricular_plan where dcp.id_internal is null;
		