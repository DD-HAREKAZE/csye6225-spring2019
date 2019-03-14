#!/bin/bash

echo "Enter your stack name [ENTER]: "
read stack

echo "Enter username (circleci) you want to create:"
read username
# ImageId=ami-0f2b4fc905b0bd1f1
# ImageId=ami-02eac2c0129f6376b

aws cloudformation create-stack --stack-name ${stack} --template-body file://../cloudformation/csye6225-aws-iam-stack.json  --parameters ParameterKey=circleci,ParameterValue=circleci --capabilities='CAPABILITY_NAMED_IAM'
i=1
sp="/-\|"
while true
do
  printf "\b${sp:i++%${#sp}:1}"
done &
trap "kill $!" EXIT
aws cloudformation wait stack-create-complete --stack-name ${stack}
if [ $? -eq 0 ];then
  echo "Stack ${stack} was created successfully!"
else
  echo "Stack ${stack} create failed!"
fi
kill $! && trap " " EXIT

