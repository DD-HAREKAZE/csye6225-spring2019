echo -n "Please input the id of VPC you want to delete:"
read vpcid
aws ec2 delete-vpc --vpc-id $vpcid
