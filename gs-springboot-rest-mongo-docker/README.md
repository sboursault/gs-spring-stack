# gs-springboot-rest-mongo-docker


Demo for a rest api representing the Arkham Asylum.

featuring:
* a rest api with
  * error handling,
  * forward compatible design (see Inmate.aka),
  * service discovery with HATEOAS principles,
  * a swagger-ui client
* a mongo repository
* integration tests based on spring's mockMvc and an embedded mongodb
* a docker-composed development environment

Yet to come: 
* uri and model versionning
* improved discoverability
* handling date fields
* decouple storage representation from the one exposed
* spring rest doc with swagger ?
(http://www.robwin.eu/documentation-of-a-rest-api-with-swagger-and-asciidoc/, https://spring.io/guides/gs/testing-restdocs/)
* access restrictions
* project versionning and packaging (should include a snapshot dependency)
* handle PATCH and PUT operations
* data import with spring integration
* https dev environment (https://hub.docker.com/r/marvambass/nginx-ssl-secure/)

Resource:
https://spring.io/guides/tutorials/bookmarks/
https://spring.io/guides
https://en.wikipedia.org/wiki/List_of_Batman_Family_adversaries
