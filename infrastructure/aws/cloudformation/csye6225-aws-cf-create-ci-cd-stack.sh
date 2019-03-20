#!/bin/bash

echo "Enter the stack you would like to create..."
read stackName

echo "enter deploy tag of ec2"
read ec2tag

echo "enter the valid aws account number"
read accno

echo "Enter the valid Username for circleci"
read username

echo "Enter Bucket Name for Code Deployment"
read codedeploybucket

echo "Enter Bucket Name for Attachments"
read attachmentbucket

dir_var=$(pwd)
echo "Current Directory is '$dir_var'"
file_dir_var="file://$dir_var/ci-cd.json"


stackId=$(aws cloudformation create-stack --stack-name $stackName --template-body $file_dir_var --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey="NameTag",ParameterValue=$ec2tag ParameterKey="AccTag",ParameterValue=$accno ParameterKey="circleciusername",ParameterValue=$username ParameterKey="deploybucket",ParameterValue=$codedeploybucket ParameterKey="attachmentbucket",ParameterValue=$attachmentbucket --output text)

echo "Stack ID : '$stackId'"

aws cloudformation wait stack-create-complete --stack-name $stackId
echo $stackId

if [ -z $stackId ]; then
	echo 'Error. Stack creation failed. Please try again...'
		exit 1
	else
		echo "Stack Creation Done. Well Done..."
	fi
