# Information retrieval Assignment 2 Deadly Spiders


## Connecting to Azure

Using the ip address: 20.234.163.150 and private key included in the email, ssh onto the instance
```bash
sudo ssh -i <private_key path> azureuser@20.234.163.150
```
Move to the Group_IR  directory.

## Installation

All required libraries should already be installed, and the project packaged, if any changes are needed, run

```mvn
mvn package
```
after they were made.

## Usage
Currently all values like files to index and output file are hardcoded, to run you only require to package with maven and then run with the run script like so:
```bash
./run.sh
```
Alternatively run manually with java like so
```bash
java -Xmx2048M -cp "target/Group_IR-1.0.jar" deadlySpiders.Main
```
This will create the output file in Output/outputs.txt, afterwards you can run trec_eval like so
```bash
./../trec_eval/trec_eval <Qrels_file> Output/outputs.txt
```
