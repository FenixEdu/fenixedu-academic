#!/bin/sh
#
# Save the current state of the functionalities tables to detect changes
#

COMMON_FILE="`dirname $0`/common.sh"
if [ -r $COMMON_FILE ] ; then
  . $COMMON_FILE
else
  echo "ERROR: Please provide the file 'common.sh'. It's needed."
  exit 1
fi

#
# Functions
#

function export_functionalities_table() {
  local table=ACCESSIBLE_ITEM
  local dump_args="--default-character-set=latin1 --skip-opt -t -c"

  if [ -z "$PASSWORD" ] ; then
    mysqldump -u$USER $dump_args $DB $table | grep "^INSERT INTO" | grep "net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality\|net.sourceforge.fenixedu.domain.functionalities.Module" > STATE.sql
  else
    mysqldump -u$USER -p$PASSWORD $dump_args $DB $table | grep "^INSERT INTO" | grep "net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality\|net.sourceforge.fenixedu.domain.functionalities.Module" > STATE.sql
  fi
}

#
# Body
#

require_exec grep
require_exec mysqldump

info "Saving functionalities state..."
export_functionalities_table

info "done"