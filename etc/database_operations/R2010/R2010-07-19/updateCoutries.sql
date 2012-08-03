update COUNTRY set THREE_LETTER_CODE='SCG' where OID=712964571191;
update COUNTRY set code='RS',THREE_LETTER_CODE='SRB' where OID=712964571291;
insert into COUNTRY values (null, "MONTENEGRO", "ME", 1, 0, "MNE", "pt10:MONTENEGROen10:MONTENEGRO", "pt10:MONTENEGROen10:MONTENEGRO", null, 1035087118337);
insert into COUNTRY values (null, "ZAIRE", "ZR", 1, 0, "ZAR", "pt8:ZAIRENSEen5:ZAIRE", "pt5:ZAIREen5:ZAIRE", null, 1035087118337);

update COUNTRY, FF$DOMAIN_CLASS_INFO set OID = (DOMAIN_CLASS_ID << 32) + ID_INTERNAL where DOMAIN_CLASS_NAME='net.sourceforge.fenixedu.domain.Country' and OID is null;
