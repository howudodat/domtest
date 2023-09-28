# domtest

### How to run 

- Build the project `mvn clean verify`

- Super Development mode :

In one terminal run  `mvn tomcat7:run -pl *-server -am -Denv=dev`

In another terminal run `mvn gwt:codeserver -pl *-client -am`

Or just run ./run in a terminal
