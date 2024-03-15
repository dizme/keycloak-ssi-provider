#!/bin/zsh

function ctrl_c() {
  cd ..
}

rm -rf target
rm docker/plugins/*.jar

mvn clean package
cp target/*.jar docker/plugins/
cd docker
docker-compose up --build keycloak

trap "ctrl_c" 2
