2023/05/24 -25063 -awgment-api-gateway gateway: Add tenant header
1)multi-realm-username and multi-realm-password should be created in keycloak which has access to all realms
and has to be added as env variable in api-gateway pod
2)Added below env variables in tp-cloud-config api-gateway-dev.yaml as mentioned below

client.id: ${CAMUNDA_IDENTITY_SERVICE:camunda-identity-service}
  auth-server-url: ${KEYCLOAK_URL_AUTH:https://keycloak-tsplatform.techsophy.com}/auth
  multi-realm:
    username: ${MULTI_REALM_USERNAME}
    password: ${MULTI_REALM_PASSWORD}
These above properties were added in tp-cloud-config api-gateway-dev.yaml file
