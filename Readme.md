
### Simple project to examine testcontainers for integration testing. 

One simple integration test in the NonReactiveWithReactiveApp 

ExternalApiIntegrationIT

Uses WebFlux stack but launches any request on a separate thread and calls `block` on any WebClient rest call.

So, all you dummies, stop trying to use a full reactive stack if you have no idea what you're doing.

### Also uses SpringBoot version 3.4.1 with 6.2 version of security. 

Shows WebClient and Security configuration for spring security version 6.2.


