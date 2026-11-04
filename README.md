<div align="center">

<img src="./.github/assets/logo.png" alt="TachiyoMihon logo" title="TachiyoMihon logo" width="80"/>

# TachiyoMihon

### Mihon, with the Tachiyomi v14 reading habits back
A personal fork of [Mihon](https://github.com/mihonapp/mihon) that follows its stable releases and reverts a handful of changes that broke long-standing Tachiyomi workflows.

[![Release](https://img.shields.io/github/v/release/MD-1909/TachiyoMihon?label=Release&labelColor=06599d&color=043b69)](https://github.com/MD-1909/TachiyoMihon/releases/latest)
[![Upstream sync](https://img.shields.io/github/actions/workflow/status/MD-1909/TachiyoMihon/tachiyomihon-sync.yml?label=upstream%20sync&labelColor=27303D)](https://github.com/MD-1909/TachiyoMihon/actions/workflows/tachiyomihon-sync.yml)
[![Based on Mihon](https://img.shields.io/github/v/release/mihonapp/mihon?label=based%20on%20Mihon&labelColor=27303D&color=0D1117)](https://github.com/mihonapp/mihon/releases/latest)
[![License: Apache-2.0](https://img.shields.io/github/license/mihonapp/mihon?labelColor=27303D&color=0877d2)](/LICENSE)

## Download

Grab the latest APK from [Releases](https://github.com/MD-1909/TachiyoMihon/releases/latest). Most modern phones want the `arm64-v8a` build; the plain `tachiyomihon-v*.apk` is universal.

It installs as a separate app (`app.mihon.tachiyomihon`), so it can live next to Mihon. After the first install, updates are offered and installed from inside the app.

*Requires Android 8.0 or higher.*

## What's different from Mihon

<div align="left">

| Area | Mihon | TachiyoMihon |
| --- | --- | --- |
| **Downloaded only – reader** | The reader only knows about downloaded chapters. Moving past the first/last downloaded chapter shows *"There's no previous chapter"*. | Library and chapter list stay filtered to downloads, but the reader moves into the neighbouring non-downloaded chapter and streams it, like Tachiyomi v14. |
| **Downloaded only – download menu** | *Next 1/5/10/25 unread*, *Unread* and *Bookmarked* only look at downloaded chapters, so they do nothing. | They pick from the chapters hidden by downloaded-only mode, so you can still queue the next chapters. |
| **Unread indicator** | A dot next to every unread chapter (chapter list and Updates). | Removed. Unread chapters are still shown in full brightness, read ones dimmed. |
| **Missing chapters** | Warning in the chapter header, "missing chapters" rows between chapters, a warning in the reader and a forced transition page at every numbering gap, even when the gap only exists because of a filter. | Reader warning and forced transition pages removed. Header warning and gap rows are hidden by default (toggle in *Settings → Library*). |
| **Sliders** | Material 3 Expressive: tall track, bar-shaped thumb, track split around the thumb. | The older look everywhere (reader page slider and settings): thin track with dot ticks and a round thumb. |
| **Updates** | From `mihonapp/mihon`. | From this repo's releases. Versions are `<Mihon version>.<revision>`, e.g. `v0.21.3.2`. |
| **Telemetry** | Firebase crash reporting in GitHub builds. | Not included. |

Unchanged on purpose: *Resume* still jumps to the next **downloaded** unread chapter, and *Settings → Reader → Skip filtered chapters* works as in Mihon.

### How it stays up to date

1. A daily workflow checks for a new stable Mihon release and merges it into the `tachiyomihon` branch.
2. A guard script ([`tachiyomihon-guard.sh`](.github/scripts/tachiyomihon-guard.sh)) fails if the merge brings back any of the reverted behaviour, then unit tests and a release build run.
3. If everything passes, a pull request opens with Mihon's release notes and a list of upstream changes to files this fork customizes. If anything fails, an issue is opened instead.
4. Merging that pull request builds, signs and publishes a release, and the app offers the update.

### Building locally

```sh
./gradlew assembleRelease
```

Requires JDK 21. Add `-Penable-updater` to turn on the in-app updater. To sign with your own key, create a git-ignored `keystore.properties` in the project root with `storeFile`, `storePassword`, `keyAlias` and `keyPassword`.

Issues specific to this fork go [here](https://github.com/MD-1909/TachiyoMihon/issues). Anything that also happens in Mihon belongs [upstream](https://github.com/mihonapp/mihon/issues).

</div>

---

Based on [Mihon](https://github.com/mihonapp/mihon), all credit for the app goes to its contributors. Licensed under [Apache 2.0](/LICENSE), like Mihon. This app hosts no content and has no affiliation with any content provider.

</div>
