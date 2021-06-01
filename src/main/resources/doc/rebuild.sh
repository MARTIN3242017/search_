sudo docker rm -f $(docker ps -a | grep " search " | awk '{print $1}')
sudo docker rmi -f search:latest
cd /docker/search
sudo chmod 777 search-0.0.1-SNAPSHOT.jar
sudo docker build -t search .
sudo docker run --name=search -d -p 9876:9876 -v /docker/:/docker/ --env spring.profiles.active=test search 
date
echo 'Rebuild Search Success!'