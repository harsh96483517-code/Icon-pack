# Liquid glass: icon pack + daily calendar icon app

Based on the idea of [OverShifted/LiquidGlass](https://github.com/OverShifted/LiquidGlass) (MIT).

The Gradle project lives at the repository root, so GitHub Actions can build it directly.

- `app/` : Android app that switches the calendar icon to today's date every midnight.
- `icon-pack/` : liquid glass icons (`icons/`), `mapping.csv`, and the Python tools
  (`convert.py`, `liquidglass.py`, `make_icon.py`) to convert more icons or make a letter icon
  for a missing app. Includes `calendar_1`..`calendar_31` date icons.
  It ships inside `liquidglass-all.zip`; the build workflow unpacks it and publishes it as an
  artifact, or you can unzip it yourself.
- `.github/workflows/build.yml` : builds the APK and publishes the APK + icon pack as artifacts.

See `WORKFLOW.md` for the steps.
