#!/bin/bash

hostname=localhost:8080

name=`curl $hostname/file-details/$1 | jq -r .name`
curl $hostname/download/$1 -o $name
