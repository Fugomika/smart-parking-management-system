#!/bin/bash
cd "$(dirname "$0")"
mkdir -p out

# Cek keberadaan MySQL JDBC connector
if ! ls lib/mysql-connector-j-*.jar 1>/dev/null 2>&1; then
    echo "❌ MySQL Connector/J tidak ditemukan di folder lib/"
    echo "   Download dari: https://dev.mysql.com/downloads/connector/j/"
    echo "   Pilih 'Platform Independent' → letakkan file .jar ke folder lib/"
    exit 1
fi

find src -name "*.java" > sources.txt
javac -encoding UTF-8 -cp "lib/*" -d out @sources.txt
STATUS=$?
rm -f sources.txt

if [ $STATUS -eq 0 ]; then
    echo "✅ Kompilasi berhasil!"
else
    echo "❌ Kompilasi gagal."
    exit 1
fi
