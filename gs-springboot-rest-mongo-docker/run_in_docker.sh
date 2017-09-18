#!/usr/bin/env bash

# building the jar could be in staged docker,
# but it would not reuse the local gradle repository.

USAGE="$(basename "$0") [-h] [-m <message>] [SERVICE...] -- program to launch the asylum environment.
where:
    SERVICE... Are the docker services to restart.
    -h           Shows this help."

while getopts ":hm:" opt; do
  case $opt in
    h)
      echo "$USAGE"
      exit 0
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
  esac
done

shift $((OPTIND-1)) # shift all processed options away from parameters (keep only operands)
SERVICES=$*

if [[ " $@ " =~ " api " ]] || [ $# = 0 ]; then
  # devrait etre lancé aussi quand $# == 0
  echo ""
  echo "===[ building app ]==="
  (set -x; gradle clean build -x test) # use a subshell and print command (https://stackoverflow.com/a/31392037)
fi

echo ""
echo "===[ run docker ]==="
(set -x; docker-compose up --build $SERVICES)
