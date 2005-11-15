select concat('update SUMMARY set SUMMARY.SUMMARY_TYPE = \'', SHIFT.TYPE,
  '\' where SUMMARY.ID_INTERNAL = ', SUMMARY.ID_INTERNAL, ';') as ""
  from SUMMARY inner join SHIFT 
  on SUMMARY.SUMMARY_TYPE is null 
  and SUMMARY.KEY_SHIFT is not null 
  and SHIFT.ID_INTERNAL = SUMMARY.KEY_SHIFT;