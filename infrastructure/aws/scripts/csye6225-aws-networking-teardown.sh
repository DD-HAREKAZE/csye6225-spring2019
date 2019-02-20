<<<<<<< HEAD
#manual method
echo -n "Please input the id of VPC you want to delete:"
read vpcid
#echo -n "Please input the id of sucerity group you want to delete:"
#read sgid
#aws ec2 delete-security-group --group-id $sgid
echo -n "Please input the id of subnet1 you want to delete:"
read subnetid1
aws ec2 delete-subnet --subnet-id $subnetid1
echo -n "Please input the id of subnet2 you want to delete:"
read subnetid2
aws ec2 delete-subnet --subnet-id $subnetid2
echo -n "Please input the id of subnet3 you want to delete:"
read subnetid3
aws ec2 delete-subnet --subnet-id $subnetid3
echo -n "Please input the id of route table you want to delete:"
read rtbid
aws ec2 delete-route-table --route-table-id $rtbid
echo -n "Please input the id of gateway you want to delete:"
read igwid
aws ec2 detach-internet-gateway --internet-gateway-id $igwid --vpc-id $vpcid
aws ec2 delete-internet-gateway --internet-gateway-id $igwid
aws ec2 delete-vpc --vpc-id $vpcid


=======
set -e
#Author: Amrith Prabhu
echo "Author: Amrith Prabhu"
echo "        prabhu.am@husky.neu.edu"
#Usage: Deleting our networking resources such as Virtual Private Cloud (VPC), Internet Gateway, Route Table and Routes

#Arguments: STACK_NAME
STACK_NAME=$1
#Get a vpc-Id using the name provided
vpcId=`aws ec2 describe-vpcs --filter "Name=tag:Name,Values=${STACK_NAME}-csye6225-vpc" --query 'Vpcs[*].{id:VpcId}' --output text`
#Get a Internet Gateway Id using the name provided
gatewayId=`aws ec2 describe-internet-gateways --filter "Name=tag:Name,Values=${STACK_NAME}-csye6225-InternetGateway" --query 'InternetGateways[*].{id:InternetGatewayId}' --output text`
#Get a route table Id using the name provided
routeTableId=`aws ec2 describe-route-tables --filter "Name=tag:Name,Values=${STACK_NAME}-csye6225-public-route-table" --query 'RouteTables[*].{id:RouteTableId}' --output text`

#Delete the route
aws ec2 delete-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0
echo "Deleting the route..."

#Delete the route table
aws ec2 delete-route-table --route-table-id $routeTableId
echo "Deleting the route table-> route table id: "$routeTableId

#Detach Internet gateway and vpc
aws ec2 detach-internet-gateway --internet-gateway-id $gatewayId --vpc-id $vpcId
echo "Detaching the Internet gateway from vpc..."

#Delete the Internet gateway
aws ec2 delete-internet-gateway --internet-gateway-id $gatewayId
echo "Deleting the Internet gateway-> gateway id: "$gatewayId

#Delete the vpc
aws ec2 delete-vpc --vpc-id $vpcId
echo "Deleting the vpc-> vpc id: "$vpcId

echo "Task Completed!"
>>>>>>> f7d41b4045af4e76d2c72b19b49067a6a1714f79
