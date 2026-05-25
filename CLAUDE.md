# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

`mhc.geo` is an Eclipse Java project providing geographic calculation primitives and KML file generation. Its primary application is `StudyLayoutGenerator`, which produces KML and CSV files describing the physical layout of an agricultural herbicide study area near Bryan/College Station, TX.

## Build and Development

This is an Eclipse IDE project with no Maven or Gradle wrapper. The build is managed entirely through Eclipse's Java builder.

- **Build**: Open in Eclipse and use Project > Build (or the auto-build on save). Output goes to `bin/`.
- **Dependency**: Requires the sibling Eclipse project `mhc.util` (referenced as `/mhc.util` in `.classpath`). Both projects must be open in the same Eclipse workspace.
- **Java version**: Java 24 (uses records, text blocks, and `///` Markdown doc comments).
- **Run tests**: In Eclipse, right-click `src/mhc/tests/geo/` > Run As > JUnit Test. Tests use JUnit 5 (Jupiter).
- **Run a single test class**: Right-click the specific test file > Run As > JUnit Test.
- **Generate Javadoc**: Run the Ant script `javadoc.xml` (covers both `mhc.util/src` and `src`); output goes to `doc/`.

## Architecture

All core types are immutable Java **records**:

- **`GeoPoint(lat, lon)`** — Decimal-degree lat/lon; validates ±90°/±180° bounds.
- **`Bearing(degrees)`** — Compass bearing, always normalized to `[0, 360)` on construction (accepts any value including negative).
- **`GeoLine(start, end)`** — Line segment between two `GeoPoint`s; can compute its `length()` and `bearing()`.
- **`GeoRectangle(c0, c1, c2, c3)`** — Four-corner polygon with corners in counterclockwise order `c0→c1→c2→c3→c0`. `c0` is the anchor (SW corner in typical use). Factory method `GeoRectangle.fromCorner(anchor, widthFeet, heightFeet, bearing)` constructs from an anchor and dimensions.

**`Geo`** is a static utility class using a spherical Earth model:
- `Geo.distance(p1, p2)` — Haversine formula, returns **feet**.
- `Geo.bearing(a, b)` — Returns a `Bearing` object.
- `Geo.projectPt(start, bearing, distanceFeet)` — Projects a point along a bearing.
- `Geo.projectPt(start, bearing, overDelta, upDelta)` — Projects *over* (along bearing) then *up* (perpendicular, bearing−90°).

**`KMLGenerator`** produces KML 2.2 output via a `PrintWriter`. Call sequence: `generateHeader` → mix of `generateKMLLine`, `generateKMLRectangle`, `generateLabel`, `generateMarker` → `generateFooter`. KML color values are in AABBGGRR format (KML standard). The `Style` enum names map directly to KML style IDs defined in `KML_Styles`.

**`StudyLayoutGenerator`** is the application entry point (`main`). Layout parameters (plot count, dimensions, buffers, bearing, anchor) are static fields. What gets generated is controlled by adding values to the `genChoices` `EnumSet` in the static initializer—most options are commented out by default. Output files are written to a UNC network path (`\\NAS4DRIVE\Server\WildlifeManagement\...`).

## Code Style

- Enum constants use **UpperCamelCase** (e.g. `RedThin`, `FooBar`), not `SCREAMING_SNAKE_CASE`.
- Enum constants each go on their own line with a trailing `//` to prevent the formatter from collapsing them (e.g. `Red, //`).
- Variable names use **lowerCamelCase**.
- Javadoc comments use `///` Markdown-style (Java 24+), not `/** */` block comments. Use `///` only on classes, methods, and fields — not on enum constants and not inside method bodies.

## Key Conventions

- All distances throughout the library are in **feet**.
- "Horizontal" = direction of `HorizontalBearing` = across columns (left→right); "Vertical" = `HorizontalBearing − 90°` = up rows.
- Plot naming: columns A–D (left to right), rows 01–15 (top to bottom); plot A01 is nearest the NW corner.
- `mhc.io.Out` and `mhc.io.SOut` (from `mhc.util`) are used for debug output; `Out.setDevelopmentMode(true)` must be called to enable trace output.
- Tests use `Out.formatD(ShowOut, ...)` so diagnostic output is gated by the `ShowOut` boolean on each test class.
