# spring-boot-oauth2-jwt
Access token

Para la peticion de flujo Password: POST -> https://localhost:8445/oauth/tokens y en body "grant_type=password&client_id=client_id&client_secret=secret&username=username_usuario&password=password_usaurio_en_Base64";

Refresh token

Para invocar a refresh token: POST -> https://localhost:8445/oauth/tokens y en body "grant_type=refresh_token&client_id=client_id&client_secret=secret&refresh_token=refresh_token"

Revoke
Logout Para hacer logout: POST-> https://localhost:8445/oauth/tokens/revoke y en el body "token=token&token_type_hint=access_token"

Para las peticiones POST usar 'application/x-www-form-urlencoded'.

Peticiones autorizadar con token

https://localhost:8445/url_llamada y en cabecera añadir 'Authorization': token
