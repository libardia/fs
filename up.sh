#!/bin/bash

hostname=localhost:8080

id=`curl -F "file=@$1" $hostname/upload | jq -r .id`
echo -e "\nID of uploaded file is: $id"
