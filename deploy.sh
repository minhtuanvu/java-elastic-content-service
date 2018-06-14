#!/bin/bash
eval `aws ecr get-login --no-include-email`
cp ~/.aws/credentials .
number=`git rev-parse --short HEAD`
echo ${number}
docker build -t fuzo-content-service:${number}  .
docker tag fuzo-content-service:${number} 656201843059.dkr.ecr.eu-central-1.amazonaws.com/fuzo:latest
aws ecr create-repository --repository-name fuzo
docker push 656201843059.dkr.ecr.eu-central-1.amazonaws.com/fuzo