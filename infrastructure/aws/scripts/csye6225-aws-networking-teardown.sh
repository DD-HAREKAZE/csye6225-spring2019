
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
#commit for Yifu
