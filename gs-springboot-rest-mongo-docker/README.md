# gs-springboot-rest-mongo-docker


Demo for a rest api representing the Arkham Asylum.

featuring:
* a rest api with
  * error handling,
  * forward compatible design (see Inmate.aka),
  * handling date fields
  * service discovery with HATEOAS principles,
  * a swagger-ui client,
  * internal and external representations defined in separate classes, mapping realized by mapstruct 
* a mongo repository
* integration tests based on spring's mockMvc and an embedded mongodb
* a docker-composed development environment
* simple analytics with redis

Yet to come: 
* uri and model versionning
* improved discoverability
* decouple storage representation from the one exposed
* spring rest doc with swagger ?
(http://www.robwin.eu/documentation-of-a-rest-api-with-swagger-and-asciidoc/, https://spring.io/guides/gs/testing-restdocs/)
* access restrictions
* project versionning and packaging (should include a snapshot dependency)
* handle PATCH and PUT operations, collection with sort and pagination
* data import with spring integration
* https dev environment (https://hub.docker.com/r/marvambass/nginx-ssl-secure/)
* elastic search index
* load balancing, shared session storage and log aggregation
* blue/greeen deployment
* discover the api through the browser
* add a sort of task engine to rerun failed tasks (spring integration ?)
* check visual non regression like this: https://medium.com/friday-people/how-we-do-visual-regression-testing-af63fa8b8eb1

Resource:
https://spring.io/guides/tutorials/bookmarks/
https://spring.io/guides
https://en.wikipedia.org/wiki/List_of_Batman_Family_adversaries
