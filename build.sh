#!/bin/bash

fileSizeMB=100

buildOutputDirectory="BuildOutput"
package="BuildOutput.zip"

rm -R -f $buildOutputDirectory 
rm -f $package

mkdir $buildOutputDirectory

./generate-artifact.sh "$buildOutputDirectory/file" 100

(cd $buildOutputDirectory && zip -r ../$package .)