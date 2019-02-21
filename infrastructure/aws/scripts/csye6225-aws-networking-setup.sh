<<<<<<< HEAD
vpcinfo=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 |jq -r ".Vpc.VpcId")
subnet1=$(aws ec2 create-subnet --vpc-id $vpcinfo --cidr-block 10.0.1.0/24 |jq -r ".Subnet.SubnetId")
subnet2=$(aws ec2 create-subnet --vpc-id $vpcinfo --cidr-block 10.0.2.0/24 |jq -r ".Subnet.SubnetId")
subnet3=$(aws ec2 create-subnet --vpc-id $vpcinfo --cidr-block 10.0.3.0/24 |jq -r ".Subnet.SubnetId")
gateway=$(aws ec2 create-internet-gateway | jq -r ".InternetGateway.InternetGatewayId")
aws ec2 attach-internet-gateway --internet-gateway-id $gateway --vpc-id $vpcinfo
routetable=$(aws ec2 create-route-table --vpc-id $vpcinfo | jq -r ".RouteTable.RouteTableId")
aws ec2 associate-route-table  --subnet-id $subnet1 --route-table-id $routetable
aws ec2 associate-route-table  --subnet-id $subnet2 --route-table-id $routetable
aws ec2 associate-route-table  --subnet-id $subnet3 --route-table-id $routetable
aws ec2 create-route --route-table-id $routetable --destination-cidr-block 0.0.0.0/0 --gateway-id $gateway
=======
set -e
#Author: Amrith Prabhu
echo "Author: Amrith Prabhu"
echo "prabhu.am@husky.neu.edu"
#Usage: setting up our networking resources such as Virtual Private Cloud (VPC), Internet Gateway, Route Table and Routes

#Arguments: STACK_NAME

STACK_NAME=$1
#Create VPC and get its Id
vpcId=`aws ec2 create-vpc --cidr-block 10.0.0.0/16 --query 'Vpc.VpcId' --output text`
#Tag vpc
aws ec2 create-tags --resources $vpcId --tags Key=Name,Value=$STACK_NAME-csye6225-vpc
echo "Vpc created-> Vpc Id:  "$vpcId

#Create Internet Gateway
gatewayId=`aws ec2 create-internet-gateway --query 'InternetGateway.InternetGatewayId' --output text`
#Tag Internet Gateway
aws ec2 create-tags --resources $gatewayId --tags Key=Name,Value=$STACK_NAME-csye6225-InternetGateway
echo "Internet gateway created-> gateway Id: "$gatewayId

#Attach Internet Gateway to Vpc
aws ec2 attach-internet-gateway --internet-gateway-id $gatewayId --vpc-id $vpcId
echo "Attached Internet gateway: "$gatewayId" to Vpc: "$vpcId

#Create Route Table
routeTableId=`aws ec2 create-route-table --vpc-id $vpcId --query 'RouteTable.RouteTableId' --output text`
#Tag Route Table
aws ec2 create-tags --resources $routeTableId --tags Key=Name,Value=$STACK_NAME-csye6225-public-route-table
echo "Route table created -> route table Id: "$routeTableId

#Create Route
aws ec2 create-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $gatewayId
echo "Route created: in "$routeTableId" target to "$gatewayId
#Job Done
echo "Job is completed"


>>>>>>> f7d41b4045af4e76d2c72b19b49067a6a1714f79


