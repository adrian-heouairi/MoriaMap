#!/bin/sh

repo_root=$(git rev-parse --show-toplevel) || exit

cd -- "$repo_root" || exit

if ./gradlew javadoc build test; then
    echo 'No errors found, you can commit after checking SonarQube:'
    echo 'make sure you introduced no bugs, vulnerabilities, or code smells.'
    echo 'Also try to have a code coverage of at least 90 % on your new code.'
else
    echo 'Errors encountered, fix them before committing.'
fi
