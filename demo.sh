#!/usr/bin/env sh
set -eu
cd "$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
exec java -jar dist/minimarket-lp2.jar --spring.profiles.active=demo
