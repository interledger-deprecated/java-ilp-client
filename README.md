# Java Interledger Client [![join the chat on gitter][gitter-image]][gitter-url] [![circle-ci][circle-image]][circle-url] [![codecov][codecov-image]][codecov-url]

[gitter-url]: https://gitter.im/interledger/java
[gitter-image]: https://badges.gitter.im/interledger/java.svg
[circle-image]: https://circleci.com/gh/interledger/java-ilp-client.svg?style=shield
[circle-url]: https://circleci.com/gh/interledger/java-ilp-client
[codecov-image]: https://codecov.io/gh/interledger/java-ilp-client/branch/master/graph/badge.svg
[codecov-url]: https://codecov.io/gh/interledger/java-ilp-client


A sending and receiving client for ILP

Currently implements basic five-bells-ledger REST client. The plan is to implement all client services and then the business logic behind these.

It uses spring-boot to run a command line tool which dumps data to the logs.

Classes in `org.interledger.ilp.ledger.model` will be moved to the java-ilp-core package when they have stabilized.

## Usage

### Requirements
This project uses Gradle to manage dependencies and other aspects of the build.  To install Gradle, follow the instructions at [https://gradle.org](https://gradle.org/).

### Get the Code
This project depends on [Java Crypto-Conditions](https://github.com/interledger/java-crypto-conditions)
and [Java ILP Core](https://github.com/interledger/java-ilp-core). To contribute to this library, 
clone the following two projects to the same folder (the dependencies are not available as a 
package yet so we use a project dependency).

```bash
$ git clone https://github.com/interledger/java-crypto-conditions.git
$ git clone https://github.com/interledger/java-ilp-core.git
```

In the same directory, check out this project:
```bash
$ git clone https://github.com/interledger/java-ilp-client.git
```

### Build the Project
To build the project, execute the following command from the top-level folder that you cloned the projects to.  For example:

```bash
$ gradle build test
```

#### Checkstyle
The project uses checkstyle to keep code style consistent. To run the style checks:

```bash
$ gradle build check
```

### Configuration

Edit application.properties or set ENV variables

```
ledger.rest.metaUrl=red.ilpdemo.org //Meta URL of the ledger REST API
```

### Run

```
$ gradle bootRun
```

## Letsencrypt

If you aren't running the very latest JDK you may get the following error running the command line tool:

```
sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```

The ledger you are connecting to is using SSL certs from LetsEncrypt and the root certs are not shipped by default in the certstore so you need to add them yourself.

Download the DER files for the root and intermediate certs from: https://letsencrypt.org/certificates/
On OSX the following (or a variation) will add these to the cert store:

```
$ sudo keytool -import -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -noprompt -trustcacerts -alias isrgrootx1 -file ~/Downloads/isrgrootx1.der
$ sudo keytool -import -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -noprompt -trustcacerts -alias letsencryptauthorityx3 -file ~/Downloads/letsencryptauthorityx3.der
``` 

## Contributing
We welcome any and all submissions, whether it's a typo, bug fix, or new feature.

### Making submissions
This project utilizes a Pull Request submission model.  Before submitting a pull request, please
ensure that your build passes with no test or checkstyle failures.