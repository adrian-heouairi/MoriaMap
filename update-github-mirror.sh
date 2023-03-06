#!/bin/sh

if ! [ "$4" ]; then
    echo "Usage: ${0##*/} <gaufre login> <gaufre password> <GitHub login> <GitHub token>"
    echo "To create a GitHub token, visit https://github.com/settings/tokens"
    exit 0
fi

cd /tmp
rm -rf moriamap.git
git clone --mirror https://"$1":"$2"@gaufre.informatique.univ-paris-diderot.fr/tazouev/moriamap.git
cd moriamap.git
git remote set-url --push origin https://"$3":"$4"@github.com/SkyNalix/MoriaMap.git

while sleep 30; do
  git fetch -p origin
  git push --mirror
done
