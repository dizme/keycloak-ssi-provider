#!/bin/zsh
JAR_NAME=ssi-provider

function ctrl_c() {
  cd ..
}

rm -rf target

mvn clean package

# check if keycloak release dir variable exported
if [ -z "$KEYCLOAK_RELEASE_DIR" ]; then
  echo "KEYCLOAK_RELEASE_DIR is not set"
  KEYCLOAK_RELEASE_DIR=./docker/keycloak
fi


echo "Copying jar to keycloak release dir"
cp target/$JAR_NAME.jar $KEYCLOAK_RELEASE_DIR/providers/
#
#cd docker
#docker-compose up --build keycloak

trap "ctrl_c" 2
