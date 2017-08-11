#!/usr/bin/env bash

gradle clean build \
  && docker-compose up --build


# docker down to stop and remove containers
# check also docker down --volumes

# watch the size of /var/lib/mongodb
