#!/usr/bin/env bash

gradle build buildDocker

docker run -p 8080:8080 -t sb/arkham-api:0.1.0
