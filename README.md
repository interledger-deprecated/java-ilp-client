# java-ilp-client [![CircleCI](https://circleci.com/gh/interledger/java-ilp-client.svg?style=svg)](https://circleci.com/gh/interledger/java-ilp-client)

A sending and receiving client for ILP

Currently implements basic five-bells-ledger REST client. The plan is to implement all client services and then the business logic behind these.

It uses spring-boot to run a command line tool which dumps data to the logs.

Classes in `org.interledger.ilp.ledger.model` will be moved to the java-ilp-core package when they have stabilized.

## Configuration

Edit application.properties or set ENV variables

```
ledger.rest.metaUrl=red.ilpdemo.org //Meta URL of the ledger REST API
```

## Run

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

## Development

To contribute and work on this library clone the code and dependencies to the same folder (the dependencies are not available as packages yet so we use project dependencies).

```
$ git clone https://github.com/interledger/java-crypto-conditions.git
$ git clone https://github.com/interledger/java-ilp-core.git
$ git clone https://github.com/interledger/java-ilp-client.git
```

Before submitting a pull request ensure that your build passes with no Checkstyle errors.
