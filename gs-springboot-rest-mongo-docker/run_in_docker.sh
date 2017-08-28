#!/usr/bin/env bash


docker-compose down \
  && gradle clean build -x test \
  && docker-compose up --build


# docker down to stop and remove containers
# check also docker down --volumes

# watch the size of /var/lib/mongodb
