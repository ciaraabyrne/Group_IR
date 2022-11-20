# Information retrieval Assignment 2 Deadly Spiders


## Connecting to Azure

Using the ip address: 20.234.163.150 and private key included in the email, ssh onto the instance. When prompted login as azureuser. Move to the Group_IR  directory.

## Installation

All required libraries should already be installed, and the project packaged, if any changes are needed, run

```mvn
mvn package
```

## Usage
Currently all values like files to index and output file are hardcoded, to run you only require to package with maven and run with java like so:
```bash
java -cp "target/Group_IR-1.0.jar" deadlySpiders.Main
```
