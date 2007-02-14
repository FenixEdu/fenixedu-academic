#!/bin/sh

COMMON_FILE="`dirname $0`/common.sh"
if [ -r $COMMON_FILE ] ; then
  . $COMMON_FILE
else
  echo "ERROR: Please provide the file 'common.sh'. It's needed."
fi

#
# Functions
#

function set_fields() {
  local line=$1
  local name=FIELDS$2

  eval unset $name[*]

  local fields_section=`echo $line | sed -e 's/[^)]\+ (\([^)]\+\)) .*/\1/'`
  local fields_list=`echo $fields_section | sed -e "s/\('[^']*'\|\"[^\"]*\"\|[^'\",]\+\),\?/\1\n/g"`

  NUM_FIELDS=`echo "$fields_list" | wc -l`
  for (( index=1; index <= $NUM_FIELDS; index++)) ; do
    local field_name=`echo "$fields_list" | cut -d '
' -f $index`

    eval $name[$index]=`echo $field_name | sed -e 's/[\t ]*\`\([^\`]\+\)\`/\1/'`
  done
}

function set_values() {
  local line=$1
  local name=VALUES$2

  eval unset $name[*]

  local values_section=`echo $line | sed -e 's/[^)]\+ ([^)]\+) VALUES (\(.*\));[ \t]*/\1/'`
  local values_list=`echo $values_section | sed -e "s/\(\('\([^\\\\']\|\\\\[^']\|\\\\'\)*'\)\|\(\"\([^\\\\\"]\|\\\\[^\"]\|\\\\\"\)*\"\)\|[^'\",]\+\),\?/\1\n/g"`
  #local values_list=`echo $values_section | sed -e "s/\('([^'\]|\')*'\|\"([^\"]|\\\")*\"\|[^'\",]\+\),\?/\1\n/g"`

  for (( index=1; index <= $NUM_FIELDS; index++)) ; do
    local value=`echo "$values_list" | cut -d '
' -f $index`

    eval $name[$index]=\"$value\"
  done
}

function get_field_value() {
  local name=$1

  for (( index=1; index <= $NUM_FIELDS; index++)) ; do
    if [ "x${FIELDS[$index]}" == "x$name" ] ; then
      echo ${VALUES[$index]}
      break
    fi
  done
}

function setup_line() {
  set_fields "$1" $2
  set_values "$1" $2
}

function save_uuids() {
  for line in `cat STATE.sql | \
      sed -e "s/[^(]\+([^(]\+(\([0-9]\+\),\('[^']\+'\),.*/UUID[\1]=\"\2\"/"` ; do
    eval $line
  done
}

function save_aps() {
  OLD_IFS=$IFS
  IFS='
'

  local id=
  local value=

  for line in `cat $1 | sed -e 's/"/\\"/g' | sed -e 's/.* VALUES (\([0-9]\+\),.*/APS[\1]=&/'` ; do
    id=`echo $line | sed -e 's/APS\[\([0-9]\+\)\]=.*/\1/'`
    value=`echo $line | sed -e 's/APS\[\([0-9]\+\)\]=\(.*\)/\2/'`
    APS[$id]=$value
  done
  IFS=$OLD_IFS
}

#
# Insert
#

function create_ap_insert() {
  local head="INSERT INTO \`AVAILABILITY_POLICY\` ("
  local tail=") SELECT "

  local generate=0
  local parent=

  for (( index=1; index <= $NUM_FIELDS; index++)) ; do
    local FIELD=${FIELDS[$index]}
    local VALUE=${VALUES[$index]}

    if [ $FIELD == 'ID_INTERNAL' ] ; then
      continue;
    fi

    if [ $FIELD == 'KEY_ACCESSIBLE_ITEM' ] ; then
      if [ $VALUE != "NULL" ]; then
        parent=$VALUE
        VALUE="\`ID_INTERNAL\`"
      else
        VALUE=NULL
      fi
    fi

    if [ $generate -eq 1 ] ; then
      head="$head, "
      tail="$tail, "
    fi

    head="$head\`$FIELD\`" 
    tail="$tail$VALUE"

    generate=1
  done

  local script="$head$tail"

  if [ -z "$parent" ] ; then
    script="$script;"
  else
    script="$script FROM \`ACCESSIBLE_ITEM\` WHERE \`UUID\` = $UUID;"
  fi

  script="$script
UPDATE \`ACCESSIBLE_ITEM\` AS f, \`AVAILABILITY_POLICY\` AS ap SET f.\`KEY_AVAILABILITY_POLICY\` = ap.\`ID_INTERNAL\` WHERE f.\`UUID\` = $UUID AND ap.\`KEY_ACCESSIBLE_ITEM\` = f.\`ID_INTERNAL\`;"

  AP_INSERTED="$script"
}

function create_insert() {
  local head="INSERT INTO \`ACCESSIBLE_ITEM\` ("
  local tail=") SELECT "

  local generate=0
  local ap=
  local parent=

  for (( index=1; index <= $NUM_FIELDS; index++)) ; do
    local FIELD=${FIELDS[$index]}
    local VALUE=${VALUES[$index]}

    if [ $FIELD == 'ID_INTERNAL' ] ; then
      continue;
    fi

    if [ $FIELD == 'KEY_AVAILABILITY_POLICY' ] ; then
      if [ $VALUE != "NULL" ]; then
        ap=$VALUE
        VALUE=NULL
      fi
    fi

    if [ $FIELD == 'KEY_MODULE' ] ; then
      if [ $VALUE != "NULL" ]; then
        parent=$VALUE
        VALUE="\`ID_INTERNAL\`"
      else
        VALUE=NULL
      fi
    fi

    if [ $FIELD == 'KEY_PARENT' ] ; then
      if [ $VALUE != "NULL" ]; then
        parent=$VALUE
        VALUE="\`ID_INTERNAL\`"
      else
        VALUE=NULL
      fi
    fi

    if [ $generate -eq 1 ] ; then
      head="$head, "
      tail="$tail, "
    fi

    head="$head\`$FIELD\`" 
    tail="$tail$VALUE"

    generate=1
  done

  local script="$head$tail"

  if [ -z "$parent" ] ; then
    script="$script;"
  else
    script="$script FROM \`ACCESSIBLE_ITEM\` WHERE \`UUID\` = ${UUID[$parent]};"
  fi

  if [ ! -z $ap ] ; then
    local ap_line=${APS[$ap]}

    setup_line "$ap_line"
    create_ap_insert

    script="$script
$AP_INSERTED"
  fi

  INSERTED[$ID]="$script"
}

#
# delete
#

function create_ap_delete() {
  local script="DELETE FROM ap USING \`AVAILABILITY_POLICY\` AS ap, \`ACCESSIBLE_ITEM\` AS f WHERE ap.\`KEY_ACCESSIBLE_ITEM\` = f.\`ID_INTERNAL\` AND f.\`UUID\` = $UUID;"
  
  AP_DELETED=$script
}

function create_delete() {
  local uuid="`get_field_value "UUID"`"

  create_ap_delete

  local script="$AP_DELETED
DELETE FROM \`ACCESSIBLE_ITEM\` WHERE \`UUID\` = $uuid;"

  DELETED[$ID]="$script"
}

#
# update
#

function create_update() {
  local generate=0
  local parent=

  local ap=
  local old_ap=

  local script='UPDATE `ACCESSIBLE_ITEM` AS own'

  for (( index=1; index <= $NUM_FIELDS; index++)) ; do
    local FIELD=${FIELDS[$index]}
    local VALUE=${VALUES[$index]}

    local OLD_FIELD={FIELDS_$ID[$index]}
    local OLD_VALUE={VALUES_$ID[$index]}

    eval OLD_FIELD=\$$OLD_FIELD
    eval OLD_VALUE=\$$OLD_VALUE

    if [ $FIELD == 'ID_INTERNAL' ] ; then
      continue;
    fi

    if [ $FIELD == 'UUID' ] ; then
      continue;
    fi

    if [ "x$VALUE" == "x$OLD_VALUE" ] ; then
      continue
    fi 

    if [ $FIELD == 'KEY_MODULE' ] ; then
      if [ $VALUE != "NULL" ]; then
        parent=$VALUE
        VALUE="parent.\`ID_INTERNAL\`"
      else
        VALUE=NULL
      fi
    fi

    if [ $FIELD == 'KEY_PARENT' ] ; then
      if [ $VALUE != "NULL" ]; then
        parent=$VALUE
        VALUE="parent.\`ID_INTERNAL\`"
      else
        VALUE=NULL
      fi
    fi

    if [ $FIELD == 'KEY_AVAILABILITY_POLICY' ] ; then
      if [ $VALUE != "NULL" ] ; then
        ap=$VALUE
        VALUE=NULL
      fi 

      if [ $OLD_VALUE != "NULL" ] ; then
        old_ap=$OLD_VALUE
      fi
    fi

    if [ $generate -eq 1 ] ; then
      script="$script,"
    else 
      if [ ! -z "$parent" ] ; then
        script="$script, \`ACCESSIBLE_ITEM\` AS parent SET"
      else
        script="$script SET"
      fi
    fi

    script="$script own.\`$FIELD\` = $VALUE"
    generate=1
  done

  script="$script WHERE own.\`UUID\` = $UUID"

  if [ -z "$parent" ] ; then
    script="$script;"
  else
    script="$script AND parent.\`UUID\` = ${UUID[$parent]};"
  fi

  # detect new availability policy
  if [ ! -z $ap ] ; then
    local ap_line=${APS[$ap]}

    setup_line "$ap_line"
    create_ap_insert

    script="$script
$AP_INSERTED"
  fi

  # delete old availability policy if changed
  if [ ! -z $old_ap ] ; then
    local ap_line=${APS[$old_ap]}

    setup_line "$ap_line"
    create_ap_delete

    script="$AP_DELETED
$script"
  fi

  UPDATED[$ID]="$script"
}

#
# parse diff
#

function match() {
  REGEXP=$1
  LINE=$2

  echo $LINE | egrep -q "$1"
}

function parse_diff() {
  LINE_NUMBER=0

  while [ 1 ] ; do
    read line
    RESULT=$?

    if [ $RESULT -ne 0 ] ; then
      break
    fi

    if match "^[^<>].*" "$line" ; then
      continue
    fi

    setup_line "$line"

    ID="`get_field_value "ID_INTERNAL"`"
    UUID="`get_field_value "UUID"`"

    if match "^<.*" "$line" ; then
      setup_line "$line" "_$ID"

      create_delete
    fi

    if match "^>.*" "$line" ; then
      if [ "x${DELETED[$ID]:-none}" == "xnone" ] ; then
        UUID[$ID]=$UUID
        create_insert
      else
        unset DELETED[$ID]
        create_update
      fi
    fi

    # print progress
    LINE_NUMBER=`expr $LINE_NUMBER + 1`
    printf "          %3d.%02d%%\n" \
      `expr \( $LINE_NUMBER \* 100 \) / $TOTAL` \
      `expr \( \( $LINE_NUMBER \* 10000 \) / $TOTAL \) % 100`

  done

  printf "          %3d.%02d%%\n" 100 0

  write_changes
}

#
# write
#

function sqlmsg() {
  echo "-- " $@ >> $FILE
}

function sqlecho() {
  echo "$1" >> $FILE
}

function write_header() {
  sqlmsg "SQL file representing changes to the functionalities model"
  sqlmsg "Generated at `date`"
  sqlmsg "DO NOT EDIT THIS FILE, run the generating script instead"
  sqlecho
  sqlmsg "Preamble"
  sqlecho "SET AUTOCOMMIT = 0;"
  sqlecho 
}

function write_changes() {
  write_header

  sqlecho "START TRANSACTION;"
  sqlecho

  if [ ${#DELETED[*]} -gt 0 ] ; then
    sqlmsg
    sqlmsg "Deleting functionalities"
    sqlmsg
    sqlecho

    for key in ${!DELETED[*]} ; do
      sqlmsg "ID: $key UUID: ${UUID[$key]}"
      sqlecho "${DELETED[$key]}
"
    done
  fi

  if [ ${#UPDATED[*]} -gt 0 ] ; then
    sqlmsg
    sqlmsg "Updating existing functionalities"
    sqlmsg
    sqlecho

    for key in ${!UPDATED[*]} ; do
      sqlmsg "ID: $key UUID: ${UUID[$key]}"
      sqlecho "${UPDATED[$key]}
"
    done
  fi

  if [ ${#INSERTED[*]} -gt 0 ] ; then
    sqlmsg
    sqlmsg "Inserting new functionalities"
    sqlmsg
    sqlecho

    for key in ${!INSERTED[*]} ; do
      sqlmsg "ID: $key UUID: ${UUID[$key]}"
      sqlecho "${INSERTED[$key]}
"
    done
  fi

  sqlecho "COMMIT;"
}

function export_functionalities_table_stdout() {
  local table=ACCESSIBLE_ITEM
  local dump_args="--default-character-set=latin1 --skip-opt -t -c"

  if [ -z "$PASSWORD" ] ; then
    mysqldump -u$USER $dump_args $DB $table | grep "^INSERT INTO" | grep "net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality\|net.sourceforge.fenixedu.domain.functionalities.Module"
  else
    mysqldump -u$USER -p$PASSWORD $dump_args $DB $table | grep "^INSERT INTO" | grep "net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality\|net.sourceforge.fenixedu.domain.functionalities.Module"
  fi
}

function export_availability_table_stdout() {
  local table=AVAILABILITY_POLICY
  local dump_args="--default-character-set=latin1 --skip-opt -t -c"

  if [ -z "$PASSWORD" ] ; then
    mysqldump -u$USER $dump_args $DB $table | grep "^INSERT INTO" 
  else
    mysqldump -u$USER -p$PASSWORD $dump_args $DB $table | grep "^INSERT INTO"
  fi
}

#
# Body
#

FILE=$1
if [ -z $FILE ] ; then
  FILE="changes-`date '+%Y%m%d%H%M%S'`.sql"
fi

info "output=$FILE"

require_file STATE.sql "There is no saved state! Please run 'save.sh' first."
require_exec mysqldump
require_exec egrep
require_exec sed
require_exec wc
require_exec cut
require_exec mktemp

info "Saving UUIDS of existing functionalities..."
save_uuids

info "Saving current availability policy entries..."
TMP_FILE=`mktemp`
export_availability_table_stdout > $TMP_FILE
save_aps $TMP_FILE
rm -f $TMP_FILE

info "Comparing current functionalities with saved state..."
ACCESSIBLE_ITEM_TEMP_FILE=`mktemp`
export_functionalities_table_stdout | diff STATE.sql - > $ACCESSIBLE_ITEM_TEMP_FILE

info "Generating script from detected differences..."
TOTAL=`cat $ACCESSIBLE_ITEM_TEMP_FILE | wc -l`
cat $ACCESSIBLE_ITEM_TEMP_FILE | parse_diff

info "done"
