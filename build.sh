#!/bin/bash

fileSizeMB=100

buildOutputDirectory="BuildOutput"
package="BuildOutput.zip"

rm -R -f $buildOutputDirectory 
rm -f $package

mkdir $buildOutputDirectory

# TODO: SIGN-7676 increase size to 100MB. This is currently set to 1 for fast development
./generate-artifact.sh "$buildOutputDirectory/file" 1

(cd $buildOutputDirectory && zip -r ../$package .)