# gs-spring-rest-hystrix-exceptions


Demo for a rest api representing the Arkham Asylum.

I includes:
* a swagger ui client
* error handling
* a mongo repository
* tests based on spring's mockMvc
* forward compatible design (see Inmate.aka)

Yet to come: 
* uri and model versionning
* service discovery with HATEAOS principles
* date fields
* decouple storage representation from the one exposed
* spring rest doc with swagger ?
(http://www.robwin.eu/documentation-of-a-rest-api-with-swagger-and-asciidoc/, https://spring.io/guides/gs/testing-restdocs/)
* access restrictions
* project versionning and packaging (should include a snapshot dependency)
* handle PATCH and PUT operations
* docker-composed service to facilite creating the dev environment

Resource:
https://spring.io/guides/tutorials/bookmarks/
https://spring.io/guides
https://en.wikipedia.org/wiki/List_of_Batman_Family_adversaries