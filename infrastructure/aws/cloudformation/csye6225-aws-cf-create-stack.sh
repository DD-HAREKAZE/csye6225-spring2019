echo "Enter the stack name to create"
read stackname
echo $stackname
aws cloudformation create-stack --stack-name $stackname --template-body file://create.yaml
