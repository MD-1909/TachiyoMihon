#!/usr/bin/env bash
# Fails if an upstream merge silently brought back behaviour TachiyoMihon removes.
set -uo pipefail
cd "$(git rev-parse --show-toplevel)"

failed=0
fail() { echo "GUARD FAILED: $1"; failed=1; }

absent() { # pattern, description, paths...
  local pattern=$1 desc=$2; shift 2
  local hits
  hits=$(grep -rnE --include='*.kt' --include='*.kts' "$pattern" "$@" 2>/dev/null)
  [ -n "$hits" ] && fail "$desc"$'\n'"$hits"
}

present() { # pattern, description, file
  grep -qE "$1" "$3" 2>/dev/null || fail "$2 ($3)"
}

APP=app/src/main/java

absent 'downloadedOnly|filterDownloaded\(' \
  "Reader filters chapters by downloaded-only mode again" "$APP/eu/kanade/tachiyomi/ui/reader"
present 'downloadCandidates' \
  "Download actions no longer bypass downloaded-only mode" "$APP/eu/kanade/tachiyomi/ui/manga/MangaViewModel.kt"

absent 'RoundedFilled\.Circle' "Unread dot indicator is back" "$APP" presentation-core/src

absent 'missing_chapters_warning|ChapterGapWarning' "Reader missing-chapter warning is back" "$APP"
gap_hits=$(grep -rnE --include='*.kt' --exclude='MissingChapters.kt' 'calculateChapterGap' "$APP/eu/kanade/tachiyomi/ui/reader/viewer")
[ -n "$gap_hits" ] && fail "Chapter gaps force transition pages again"$'\n'"$gap_hits"
grep -A2 '"pref_hide_missing_chapter_indicators"' domain/src/main/java/tachiyomi/domain/library/service/LibraryPreferences.kt \
  | grep -q 'true' || fail "Missing chapter indicators are no longer hidden by default"

absent 'SliderDefaults\.(Thumb|Track)' "Expressive slider thumb/track used directly" "$APP" presentation-core/src
for f in $(grep -rlE --include='*.kt' 'import androidx\.compose\.material3\.(Slider|VerticalSlider|RangeSlider)$' "$APP" presentation-core/src); do
  grep -q 'LegacySliderTrack' "$f" || fail "Slider without legacy track in $f"
done

present 'MD-1909/TachiyoMihon' "Updater no longer points at the fork" "$APP/eu/kanade/tachiyomi/data/updater/AppUpdateChecker.kt"
present 'applicationIdSuffix = "\.tachiyomihon"' "Release package id changed" app/build.gradle.kts
present 'tachiyomihonRevision' "Fork revision suffix removed from versionName" app/build.gradle.kts

if [ "$failed" -ne 0 ]; then exit 1; fi
echo "All TachiyoMihon guards passed"
