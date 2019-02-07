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