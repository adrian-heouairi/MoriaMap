#!/bin/bash

# Assumes the artifacts branch exists.

set -e

error() {
    echo "Error: $*"
    exit 1
}

if ! [ "$3" ]; then
    echo "Usage: ${0##*/} <gaufre token> <version> <release description> [additional files for the zip]..."
    echo "Additional file example: build/jacoco/test.exec"
    echo "You can create a token in your GitLab settings"
    echo "To make a release: manually merge develop into master, then run this script."
    exit 1
fi

token=$1
version=${2#v}
description=$3
shift 3

[ "$(which curl)" ] || error 'You have to install curl'

master=$(git rev-parse --show-toplevel)
cd -- "$master"

[ -d .git ] || error "Don't run this script in a worktree"

git fetch

[ "$(git branch --show-current)" = master ] || error "Run the script on branch master"

git pull
./gradlew assemble

#version=$(sed -En "s/^version +['\"](.+)['\"]$/\1/p" build.gradle)
#[ "$version" ]

artifacts="$master"_worktree
[ -e "$artifacts" ] || git worktree add "$artifacts" artifacts

cd "$artifacts"
git checkout artifacts
git pull
[ -e artifacts ] || mkdir artifacts
cd artifacts

zip=MoriaMap-v"$version".zip
if ! [ -e "$zip" ]; then
    files=()
    for i; do files+=("$master/$i"); done

    zip -j "$zip" "$master/build/libs/MoriaMap.jar" "$master/map_data.csv" "$master/timetables.csv" "$master/README.md" "$master/RUN_LINUX.sh" "$master/RUN_WINDOWS.bat" "${files[@]}"
    git add "$zip"
    git commit -m "Add release zip for v$version"
    git push
fi

data='{ "name": "MoriaMap v'$version'", "tag_name": "v'$version'", "description": "'$description'", "ref": "master", "assets": { "links": [{ "name": "Zip archive with jar", "url": "https://gaufre.informatique.univ-paris-diderot.fr/tazouev/moriamap/raw/artifacts/artifacts/'$zip'?inline=false" }] } }'

curl --header 'Content-Type: application/json' --header "PRIVATE-TOKEN: $token" --data "$data" --request POST https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/6168/releases
