# Introduction

### Hey you !

Thank you for wanting to help. Even the smallest things can help this project become better.

Please read the guidelines before contributing, and follow them (or try to) when contributing.

### What you can do to help.

There are many ways to contribute to this project, report bugs, request missing features, suggest enhancements and changes to existing ones. You also can improve the README with useful tips that could help the other users.

You can fork the repository, and [help me solve some issues](https://github.com/aminecmi/Mteo/issues?q=is%3Aissue+is%3Aopen) or [develop new things](https://github.com/aminecmi/Mteo/issues)

# Some rules
### Bug reports/Feature request

* Always search before reporting an issue or asking for a feature to avoid duplicates.
* Include every useful details (app version, phone model, Android version and screenshots when possible).
* Avoid bumping non-fatal issues, or feature requests. I'll try to fix them as soon as possible, and try to prioritize the requests. (You may wan to use the [reactions](https://github.com/blog/2119-add-reactions-to-pull-requests-issues-and-comments) for that)

### Pull requests

* Please ask before starting to work on an issue. I may be working on it, or someone else could be doing so.
* Each pull request should implement **ONE** feature or bugfix. Keep in mind that you can submit as many PR as you want.
* Your code must be simple and clear enough to avoid using comments to explain what it does.
* Follow the used coding style [the android koding style](https://android.github.io/kotlin-guides/style.html) ([some idoms for reference](http://kotlinlang.org/docs/reference/idioms.html)) with more to come.
* Try as much as possible to write a test for your feature, and if you do so, run it, and make it work.
* Always check your changes and discard the ones that are irrelevant to your feature or bugfix.
* Have meaningful commit messages.
* Always reference the issue you are working on in your PR description.
* Be willing to accept criticism on your PRs (as I am on mine).
* Remember that PR review can take time.

# Build the project

You can directly import this project into IntellIJ/Android Studio.

You'll have to:

- Define some parameters either in `~/.gradle/gradle.properties` or as gradle parameters (see the examples)

    - darkSkyApiKey: the api key secret linked to your dark sky account

### Examples:
#### Inside ~/.gradle/gradle.properties

```
darkSkyApiKey="MYKEY"
```

#### As gradle parameters

```
./gradlew .... -P darkSkyApiKey="MYKEY"
```
