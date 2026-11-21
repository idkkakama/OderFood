---
name: Kitchen Dispatch & Operations OS
colors:
  surface: '#151312'
  surface-dim: '#151312'
  surface-bright: '#3c3837'
  surface-container-lowest: '#100e0d'
  surface-container-low: '#1d1b1a'
  surface-container: '#221f1e'
  surface-container-high: '#2c2928'
  surface-container-highest: '#373433'
  on-surface: '#e8e1df'
  on-surface-variant: '#e2bfb2'
  inverse-surface: '#e8e1df'
  inverse-on-surface: '#33302e'
  outline: '#a98a7e'
  outline-variant: '#5a4138'
  surface-tint: '#ffb599'
  primary: '#ffb599'
  on-primary: '#5a1c00'
  primary-container: '#f66018'
  on-primary-container: '#4f1700'
  inverse-primary: '#a73a00'
  secondary: '#7bd0ff'
  on-secondary: '#00354a'
  secondary-container: '#00a6e0'
  on-secondary-container: '#00374d'
  tertiary: '#4ae176'
  on-tertiary: '#003915'
  tertiary-container: '#00a74b'
  on-tertiary-container: '#003111'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffdbce'
  primary-fixed-dim: '#ffb599'
  on-primary-fixed: '#370e00'
  on-primary-fixed-variant: '#7f2b00'
  secondary-fixed: '#c4e7ff'
  secondary-fixed-dim: '#7bd0ff'
  on-secondary-fixed: '#001e2c'
  on-secondary-fixed-variant: '#004c69'
  tertiary-fixed: '#6bff8f'
  tertiary-fixed-dim: '#4ae176'
  on-tertiary-fixed: '#002109'
  on-tertiary-fixed-variant: '#005321'
  background: '#151312'
  on-background: '#e8e1df'
  surface-variant: '#373433'
typography:
  headline-xl:
    fontFamily: Space Grotesk
    fontSize: 3rem
    fontWeight: '700'
    lineHeight: 3.5rem
    letterSpacing: -0.03em
  headline-xl-mobile:
    fontFamily: Space Grotesk
    fontSize: 2rem
    fontWeight: '700'
    lineHeight: 2.5rem
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Space Grotesk
    fontSize: 2.25rem
    fontWeight: '700'
    lineHeight: 2.75rem
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Space Grotesk
    fontSize: 1.625rem
    fontWeight: '700'
    lineHeight: 2rem
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Space Grotesk
    fontSize: 1.5rem
    fontWeight: '600'
    lineHeight: 2rem
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Space Grotesk
    fontSize: 1.25rem
    fontWeight: '600'
    lineHeight: 1.75rem
  body-lg:
    fontFamily: Geist
    fontSize: 1.125rem
    fontWeight: '400'
    lineHeight: 1.625rem
  body-md:
    fontFamily: Geist
    fontSize: 0.9375rem
    fontWeight: '400'
    lineHeight: 1.375rem
  body-sm:
    fontFamily: Geist
    fontSize: 0.8125rem
    fontWeight: '400'
    lineHeight: 1.125rem
  label-lg:
    fontFamily: Space Grotesk
    fontSize: 0.9375rem
    fontWeight: '600'
    lineHeight: 1.25rem
    letterSpacing: 0.04em
  label-md:
    fontFamily: Space Grotesk
    fontSize: 0.8125rem
    fontWeight: '600'
    lineHeight: 1.125rem
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Space Grotesk
    fontSize: 0.6875rem
    fontWeight: '700'
    lineHeight: 0.875rem
    letterSpacing: 0.08em
  timer-display:
    fontFamily: Space Grotesk
    fontSize: 1.75rem
    fontWeight: '700'
    lineHeight: 2rem
    letterSpacing: -0.01em
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-kds: 0.75rem
  margin: 1.5rem
  margin-mobile: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system delivers a high-velocity, mission-critical operations environment engineered for high-throughput restaurant workflows. Across kitchen display systems (KDS), floor expediting tablets, and administrative terminals, visibility under extreme operational pressure is paramount.

The design identity fuses Industrial Utilitarianism with Cyber-Physical KDS ergonomics:
- **Tone & Demeanor:** Urgent, laser-focused, authoritative, and strictly utilitarian. It eliminates decorative noise in favor of stark contrast and instant data consumption.
- **Visual Stance:** Deep OLED dark-mode foundations counteract greasy environments, harsh fluorescent kitchen lighting, and heat glares while cutting visual fatigue across double shifts.
- **Micro-Interactions & Feedback:** Tactile, snap-action feedback states with high-contrast active perimeters ensure zero ambiguous touches under wet or gloved handling.

## Colors

The palette is tuned specifically for deep-contrast OLED kitchen panels and handheld service monitors. It provides instantaneous distinction between order states across the service floor.

### Functional Color Matrix
- **Primary (`#EA580C`):** High-heat dispatch accent. Drives critical calls-to-action, timer priority states, and primary routing headers.
- **Secondary (`#38BDF8`):** Active preparation / In-transit status. Indicates orders on fire or line stations currently processing ingredients.
- **Tertiary (`#22C55E`):** Order bump, completed plate, ready-for-expedite, and vacant table indicators.
- **Critical Urgency (`#EF4444`):** Voided tickets, 86'd inventory alerts, and tickets crossing SLA breach limits (>15 mins).
- **Surface & Background Architecture:** Built on `#0C0A09` (Stone Black Base) with segmented surface containers graded from `#1C1917` (Surface Elevation 1) to `#292524` (Surface Elevation 2).
- **Text & High Legibility Tiers:** Primary glyphs utilize `#FAFAF9` (98% luminance) over pure dark grounds; secondary metadata rests on `#A8A29E` (65% luminance) to preserve visual hierarchy across glancing distances.

## Typography

Typography prioritizes rapid legibility from a 2 to 3-meter distance. 

- **Headlines & Timers (`Space Grotesk`):** Highly structured, technical letterforms with distinctive geometric cuts that prevent numeric ambiguity (crucial for distinguishing table numbers, ticket counts, and elapsed countdowns).
- **Body & Order Itemization (`Geist`):** Delivers clean, neutral rendering for dense order modifier lists, special dietary tags (e.g., "NO GLUTEN / ALLERGY"), and seat allocations.
- **Tabular Figures & Metrics:** All timers, quantities, and check amounts strictly implement monospaced tabular numbers (`font-variant-numeric: tabular-nums`) to prevent layout shift during countdown state changes.

## Layout & Spacing

The operational interface uses a compact, density-driven fluid grid system built to maximize on-screen ticket volume without visual collisions.

### Grid & Viewport Behavior
- **KDS Kitchen Monitors (Wall & Rail Mounts):** Fluid multi-column horizontal lanes (4, 5, or 6 lanes based on 1080p/4K resolution) spanning with fixed-width column tracks (`gutter-kds: 0.75rem`). Cards reflow horizontally or pin across priority headers.
- **Server Handhelds (Mobile Viewports):** Single-column stacked ticket view with sticky top-level filter tabs and edge-anchored action bars. Outer margins sit at `margin-mobile: 1rem`.
- **Manager & Admin Dashboard (Desktop):** Standard 12-column dynamic responsive grid with `margin: 1.5rem` and `gutter: 1rem`.

### Touch-Target Standards
All touch targets must enforce a minimum dimension of `48px` × `48px` (expanding to `56px` on line-cook bump bars) to accommodate rapid, imperfect interactions during service peaks.

## Elevation & Depth

In a grease- and steam-prone visual environment, soft ambient drop shadows wash out and reduce visibility. This design system discards soft diffusion in favor of high-contrast perimeter outlines and structured tonal surfaces.

### Surface Hierarchy
- **Base Substrate (Floor):** `#0C0A09` — Foundation layer, non-interactive canvas.
- **Station Container (Tier 1):** `#1C1917` with a structural border: `1px solid #292524`. Houses ticket arrays, lane docks, and table matrices.
- **Active Order Ticket (Tier 2):** `#292524` with high-contrast boundaries. Urgency states apply a persistent, glowing perimeter stroke:
  - *Standard:* `1px solid #44403C`
  - *Firing/Active:* `1.5px solid #EA580C`
  - *Breaching SLA:* `2px solid #EF4444` with a subtle static warning ring (`box-shadow: 0 0 0 1px #EF4444`).
- **Floating Modals & Expedite Flyouts (Tier 3):** `#1C1917` anchored over a tinted backdrop curtain (`rgba(12, 10, 9, 0.85)`). Modals are enclosed in crisp, hard-line borders (`1px solid #78716C`).

## Shapes

The design system adopts a crisp, industrial form factor (`roundedness: 1`). Radii are tightly contained to maximize horizontal screen real estate and convey an instrument-grade aesthetic.

- **Base Components (Inputs, Chips, Table Nodes):** `0.25rem` (`rounded-sm`).
- **Order Cards & Panel Containers:** `0.5rem` (`rounded-lg`).
- **Full Action Modals:** `0.75rem` (`rounded-xl`).
- **Pill Badges (Table Numbers, Rush Tags):** Fully rounded capsules strictly reserved for time badges and status indicators to differentiate them from square action buttons.

## Components

### Buttons & Action Bars
- **Bump Button (Primary Line Control):** Full-width card footer button, `48px` to `56px` height. Default surface `#EA580C`, high-contrast text `#FAFAF9`, `font-family: Space Grotesk`, bold uppercase. Pressed state shifts instantly to `#C2410C` without spring delays.
- **Secondary Operations:** Outlined `#44403C` on `#1C1917` surface. Hover/Focus reveals `#FAFAF9` label with `#57534E` border.

### Order Ticket Cards (KDS Core)
- **Header:** Contains ticket ID (`headline-sm`), table/seat marker, and elapsed tabular timer (`timer-display`). Top edge is tagged with a `4px` indicator strip (Orange = In Progress, Blue = Expedited, Red = Delayed).
- **Body:** Order items rendered in `body-md` (`#FAFAF9`). Modifiers, substitutions, and allergy alerts rendered in indented `body-sm` highlighted with high-contrast orange or red callouts (`#FDBA74` / `#FCA5A5`). Completed line items show a strikethrough with dimmed opacity (`40%`).

### Status Badges & Chips
- Compact, high-density elements with minimal vertical padding (`0.25rem 0.5rem`).
- **Ready State:** `#14532D` background with `#4ADE80` text and `1px solid #22C55E`.
- **In-Prep State:** `#0C4A6E` background with `#38BDF8` text and `1px solid #0284C7`.
- **Delayed / Caution State:** `#7C2D12` background with `#FDBA74` text and `1px solid #EA580C`.

### Table Matrix Nodes (Floor Plan View)
- Squircular grid modules (`rounded-sm`) mapped with floor coordinates.
- Unoccupied: Dark charcoal surface (`#1C1917`) with dashed `#44403C` borders.
- Seated: Solid `#292524` surface with real-time course progress rings and server attribution.

### Input Fields & Keypads
- Numerical PIN & Quantity Keypads: Giant touch cells (minimum `64px` height) with `#292524` fill and active border highlights.
- Text Fields: Flat dark background (`#1C1917`), `1px solid #44403C`, shifting to `#EA580C` when active.