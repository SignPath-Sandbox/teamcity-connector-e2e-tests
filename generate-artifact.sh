#!/bin/bash

# Check if the correct number of arguments is provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <filename> <size in Mega Bytes (M)>"
    exit 1
fi

FILENAME=$1
SIZE=$2

BLOCK_SIZE=1M # Copy blocks of 1M

# Generate a file of the specified size
dd if=/dev/urandom of=$FILENAME bs=$BLOCK_SIZE count=$SIZE status=progress iflag=fullblock

echo "File '$FILENAME' of size $SIZE MegaBytes created successfully."