### Version 1.4.1 (Apr 28, 2016)

* Performance improvements with the use of flatmap when converting Response types
* Added a strict dependency on the Basic Client class for all HTTP classes
* Fixes and other improvements


### Version 1.4.0 (Apr 13, 2016)

* Added support for the Play JSON library - See `PlayHTTP`


### Version 1.3.0 (Apr 8, 2016)

* Added the Cirrus object for shorthanding request and getting the response body directly
* Added support for making HEAD requests in BasicHTTP
* Allowed the use of custom clients in HTTP requests
* Removed the use of the "!" operator in favour of the "send" method
* Fixed response parsing for non 2xx and 3xx response codes


### Version 1.2.0 (Apr 5, 2016)

* Added support for the Argonaut JSON library - See `ArgonautHTTP`
* Added support for posting forms in BasicHTTP
* Added fluent builder syntax for HTTPVerbs
* Added support for default headers on the Basic Client
* Added support for manually tweaking the underlying HTTPURLConnection of the Basic Client
* Added a builder for the Basic Client class
* Fixed a bug in creating Request query parameters
* Fixed a bug where only GET and POST request where made in SprayHTTP


### Version 1.1.0 (Mar 14, 2016)

* Added support for the Spray JSON library - See `SprayHTTP`


### Version 1.0.0 (Mar 13, 2016)

* Added support for making basic HTTP requests - See `BasicHTTP`
