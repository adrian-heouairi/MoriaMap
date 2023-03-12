#!/bin/sh

repo_root=$(git rev-parse --show-toplevel) || exit

cd -- "$repo_root" || exit

if ./gradlew javadoc build test; then
  echo 'No errors found, you can commit'
else
  echo 'Errors encountered, fix them before committing'
fi
