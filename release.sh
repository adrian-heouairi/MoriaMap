#!/bin/bash

# Assumes the artifacts branch exists.

set -e

if ! [ "$2" ]; then
    echo "Usage: ${0##*/} <gaufre token> <release description> [additional files for the zip]..."
    echo "You can create a token in your GitLab settings"
    echo "To make a release: manually merge develop into master, then run this script."
    exit 1
fi

token=$1
description=$2
shift 2

[ "$(which curl)" ] || { echo 'You have to install curl'; exit 1; }

master=$(git rev-parse --show-toplevel)
cd -- "$master"

[ -d .git ] || { echo "Don't run this script in a worktree"; exit 1; }

[ "$(git branch --show-current)" = master ] || { echo "Run the script on branch master"; exit 1; }

git pull --rebase
./gradlew build

version=$(sed -En "s/^version +['\"](.+)['\"]$/\1/p" build.gradle)
[ "$version" ]

artifacts="$master"_worktree
[ -e "$artifacts" ] || git worktree add "$artifacts" artifacts

cd "$artifacts"
git pull --rebase
[ -e artifacts ] || mkdir artifacts
cd artifacts
zip=MoriaMap-v"$version".zip
[ -e "$zip" ] && { echo "Zip already exists"; exit 1; }

files=()
for i; do files+=("$master/$i"); done

zip -j "$zip" "$master/build/libs/MoriaMap-$version.jar" "$master/Instructions.txt" "${files[@]}"
git add "$zip"
git commit -m "Add release zip for v$version"
git push

data='{ "name": "MoriaMap v'$version'", "tag_name": "v'$version'", "description": "'$description'", "ref": "master", "assets": { "links": [{ "name": "Zip archive with jar", "url": "https://gaufre.informatique.univ-paris-diderot.fr/tazouev/moriamap/raw/artifacts/artifacts/'$zip'?inline=false" }] } }'

curl --header 'Content-Type: application/json' --header "PRIVATE-TOKEN: $token" --data "$data" --request POST https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/6168/releases
