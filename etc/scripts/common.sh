#
# common functions and utilities
#

#
# Customization
#

USER=root
PASSWORD=
DB=ciapl

#
# Functions
#

function get_env_prop() {
  local name=$1
  local default=$2

  echo ${!name:-$default}
}

function info() {
  echo "INFO: " $@
}

function warn() {
  echo "WARN:" $@ > /dev/stderr
}

function error() {
  echo "ERROR:" $@ > /dev/stderr
  exit 1
}

function require_file() {
  local file=$1
  local message=$2
  
  if [ ! -r $file ] ; then
    error $2
    exit 1
  fi
}

function require_exec() {
  local file=$1

  [ -x /bin/$file ] || \
  [ -x /usr/bin/$file ] || \
  [ -x /usr/sbin/$file ] || \
  [ -x /usr/local/bin/$file ] || \
  error "'$file' could not be found. Please make sure it is installed and in the PATH."
}

#
# Environment
#

USER=`get_env_prop FENIX_USER $USER`
PASSWORD=`get_env_prop FENIX_PASSWORD $PASSWORD`
DB=`get_env_prop FENIX_DATABASE $DB`

info "db='$DB', user='$USER', pass='$PASSWORD'"

