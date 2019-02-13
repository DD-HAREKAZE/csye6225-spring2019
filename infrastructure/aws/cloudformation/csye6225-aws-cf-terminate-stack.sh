
echo "Enter the stack name to delete"
read stack_name
echo $stack_name
aws cloudformation delete-stack --stack-name $stack_name
#change the stack-name to choose which one to delete
