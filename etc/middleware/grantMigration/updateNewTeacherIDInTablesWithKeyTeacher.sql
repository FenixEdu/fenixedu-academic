#Grant Payment Entity
select concat('update GRANT_PAYMENT_ENTITY set KEY_TEACHER=',d.new_id_internal,' where id_internal=',p.id_internal,';') as "" from mwgrant_migracao_docente d inner join GRANT_PAYMENT_ENTITY p on d.codigoInterno=p.KEY_TEACHER;

#Grant Part
select concat('update GRANT_PART set KEY_TEACHER=',d.new_id_internal,' where id_internal=',p.id_internal,';') as "" from mwgrant_migracao_docente d inner join GRANT_PART p on d.codigoInterno=p.KEY_TEACHER;

#Grant Orientation
select concat('update GRANT_ORIENTATION_TEACHER set KEY_GRANT_TEACHER=',d.new_id_internal,' where id_internal=',p.id_internal,';') as "" from mwgrant_migracao_docente d inner join GRANT_ORIENTATION_TEACHER p on d.codigoInterno=p.KEY_GRANT_TEACHER;