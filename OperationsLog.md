**2023/05/24 - 25063 - gateway: Add tenant header**
- A user having access to all tenant realms should be created in keycloak and his credentials have to be added as env variable in api-gateway pod and injected to application-config yaml for api-gateway
````
multi-realm:
    username: ${MULTI_REALM_USERNAME}
    password: ${MULTI_REALM_PASSWORD}
````
- Add below properties to cloud-config api-gateway-dev.yaml, sample below
````
client.id: ${CAMUNDA_IDENTITY_SERVICE:camunda-identity-service}
  auth-server-url: ${KEYCLOAK_URL_AUTH:https://keycloak-tsplatform.techsophy.com}/auth
````
- Add the list of tenants registered to cloud-config api-gateway-dev.yaml file, the default techsophy-platform should always be present
````
 tenants:
  registrations:
    - techsophy-platform
````
