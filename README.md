# Liquid glass: icon pack + daily calendar icon app

Based on the idea of [OverShifted/LiquidGlass](https://github.com/OverShifted/LiquidGlass) (MIT).

The Gradle project lives at the repository root, so GitHub Actions can build it directly.

- `app/` : Android app that switches the calendar icon to today's date every midnight.
- `iconpack/` : installable **icon pack APK** (Liquid Glass). All 166 icons as drawables plus
  `res/xml/appfilter.xml` (auto-apply for known apps) and `res/xml/drawable.xml` (so launchers can
  browse every icon). Icons are copied in from `liquidglass-all.zip` by the build workflow.
- `icon-pack/` : liquid glass icons (`icons/`), `mapping.csv`, and the Python tools
  (`convert.py`, `liquidglass.py`, `make_icon.py`) to convert more icons or make a letter icon
  for a missing app. Includes `calendar_1`..`calendar_31` date icons.
  It ships inside `liquidglass-all.zip`; the build workflow unpacks it and publishes it as an
  artifact, or you can unzip it yourself.
- `.github/workflows/build.yml` : builds the APK and publishes the APK + icon pack as artifacts.

See `WORKFLOW.md` for the steps.
