#!/bin/zsh

KEYCLOAK_RELEASE_DIR=../keycloak-23.0.7

function ctrl_c() {
  cd ..
}

rm -rf target

mvn clean package

echo "Copying jar to keycloak release dir"
cp target/*.jar $KEYCLOAK_RELEASE_DIR/providers/
#
#cd docker
#docker-compose up --build keycloak

trap "ctrl_c" 2
