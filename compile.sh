#!/bin/bash
cd "$(dirname "$0")"
mkdir -p out
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
STATUS=$?
rm -f sources.txt
if [ $STATUS -eq 0 ]; then
    echo "✅ Kompilasi berhasil!"
else
    echo "❌ Kompilasi gagal."
    exit 1
fi
