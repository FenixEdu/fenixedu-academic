/*
Mascon Dump
Source Host:           localhost
Source Server Version: 3.23.49-max-nt
Source Database:       AssiduidadeOracle
Date:                  2003-03-20 18:20:46
*/

------------------------------
-- Table structure for ass_marcas
------------------------------
drop table if exists ass_marcas;
create table ass_marcas (
   ASS_MARPESSOA int(10),
   ASS_MARCARTAO int(10),
   ASS_MARDHMARCA datetime,
   ASS_MARUNID varchar(8),
   ASS_MARTIPO char(1),
   ASS_MARAUTOJUST char(1),
   ASS_MARREGUL char(1),
   ASS_MARIES char(1),
   ASS_MARSTAT char(1),
   ASS_MARSEQ int(10) not null default '0',
   ASS_MARWHEN datetime,
   ASS_MARWHO int(10),
   primary key (ASS_MARSEQ))
   type=InnoDB comment="InnoDB free: 378880 kB";


------------------------------
-- Table structure for ass_marreg
------------------------------
drop table if exists ass_marreg;
create table ass_marreg (
   ASS_MARREGPESSOA int(10),
   ASS_MARREGMARCAS int(10) not null default '0',
   ASS_MARREGREGUL varchar(8),
   ASS_MARREGSISTEMA int(10),
   primary key (ASS_MARREGMARCAS))
   type=InnoDB comment="InnoDB free: 378880 kB";
