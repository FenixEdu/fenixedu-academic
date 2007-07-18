alter table TRANSACTION_STATISTICS 
      add column READ_ONLY_READS_MIN int(11),
      add column READ_ONLY_READS_MAX int(11),
      add column READ_ONLY_READS_SUM BIGINT,
      add column READ_WRITE_READS_MIN int(11),
      add column READ_WRITE_READS_MAX int(11),
      add column READ_WRITE_READS_SUM BIGINT,
      add column READ_WRITE_WRITES_MIN int(11),
      add column READ_WRITE_WRITES_MAX int(11),
      add column READ_WRITE_WRITES_SUM BIGINT;
