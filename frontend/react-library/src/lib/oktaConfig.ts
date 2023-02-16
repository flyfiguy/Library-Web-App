//All information needed to work with third party Okta service

export const oktaConfig = {
    clientId: '0oa8cijs16D8MP3SF5d7',
    issuer: 'https://dev-42693214.okta.com/oauth2/default',
    redirectUri: 'http://localhost:3000/login/callback',
    scopes: ['openid','profile','email'],
    pkce: true,
    disableHttpsCheck: true
}