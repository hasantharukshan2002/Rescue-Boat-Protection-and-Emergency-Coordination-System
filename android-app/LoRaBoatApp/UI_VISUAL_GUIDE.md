# Android App - UI/UX Visual Guide

## Main Screen (MainActivity)

```
┌─────────────────────────────────────┐
│  LoRa Boat Monitoring System         │  ← Header (Dark Blue)
│  Connected    ●                      │  ← Status indicator
├─────────────────────────────────────┤
│  [ Connect Device        ]           │  ← Connection Button
├─────────────────────────────────────┤
│                                     │
│  ╔═════════════════════════════════╗│
│  ║ Boat Information                ║│  ← Card 1
│  ║ Boat ID:        1               ║│
│  ║ Flip Status:    NORMAL   ●      ║│  (Green indicator)
│  ╚═════════════════════════════════╝│
│                                     │
│  ╔═════════════════════════════════╗│
│  ║ Sensor Data                     ║│  ← Card 2
│  ║ Signal Strength:  75%           ║│
│  ║ ████████░░░░░░░░░░░░  75%       ║│  (Progress bar)
│  ║                                 ║│
│  ║ Battery Level:    85%           ║│
│  ║ ████████░░░░░░░░░░░░  85%       ║│  (Progress bar)
│  ║                                 ║│
│  ║ Temperature:    28.5°C          ║│
│  ╚═════════════════════════════════╝│
│                                     │
│  ╔═════════════════════════════════╗│
│  ║ Location Data                   ║│  ← Card 3
│  ║ Latitude:   40.712776           ║│
│  ║ Longitude: -74.005974           ║│
│  ╚═════════════════════════════════╝│
│                                     │
├─────────────────────────────────────┤
│  Last update: 14:35:22              │  ← Status bar
└─────────────────────────────────────┘
```

## Flip Alert Screen

```
┌─────────────────────────────────────┐
│  LoRa Boat Monitoring System         │
│  Connected    ●                      │
├─────────────────────────────────────┤
│  [ Disconnect Device     ]          │
├─────────────────────────────────────┤
│                                     │
│  ╔═════════════════════════════════╗│
│  ║         ⚠ BOAT FLIPPED!        ║│  ← ALERT (Red background)
│  ║   Immediate action required!    ║│
│  ╚═════════════════════════════════╝│
│                                     │
│  ╔═════════════════════════════════╗│
│  ║ Boat Information                ║│
│  ║ Boat ID:        1               ║│
│  ║ Flip Status:    FLIPPED!  ✕     ║│  (Red indicator)
│  ╚═════════════════════════════════╝│
│                                     │
│  ... more data below ...            │
│                                     │
└─────────────────────────────────────┘
```

## Device Selection Screen (DeviceListActivity)

```
┌─────────────────────────────────────┐
│  Select Device                      │  ← Title
│                                     │
├─────────────────────────────────────┤
│  [ Scan for Devices      ]          │  ← Scan button
├─────────────────────────────────────┤
│                                     │
│  ┌─────────────────────────────┐   │
│  │ HC-05 (00:19:5D:AB:C1)      │   │  ← Device 1 (tappable)
│  └─────────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ LoRa-BT (20:16:D8:50:F0)    │   │  ← Device 2 (tappable)
│  └─────────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ BlueMod (98:D3:31:20:50)    │   │  ← Device 3 (tappable)
│  └─────────────────────────────┘   │
│                                     │
└─────────────────────────────────────┘
```

## Color Scheme

| Color | Usage | Hex Code |
|-------|-------|----------|
| Dark Blue | Header, titles | #0D47A1 |
| Light Blue | Primary accent | #2196F3 |
| Green | Success, normal status | #4CAF50 |
| Red | Alert, flipped status | #F44336 |
| Orange | Warning, disconnected | #FF9800 |
| Gray | Secondary text | #9E9E9E |
| White | Background, text | #FFFFFF |
| Light Gray | Card background | #EEEEEE |

## UI Hierarchy

```
Application (LoRa Boat App)
│
├── MainActivity (Primary Screen)
│   ├── Header
│   │   ├── App Title
│   │   └── Connection Status
│   ├── Connection Button
│   ├── Flip Alert (conditional)
│   ├── ScrollView (content)
│   │   └── LinearLayout
│   │       ├── Boat Information Card
│   │       │   ├── Boat ID display
│   │       │   └── Flip Status indicator
│   │       ├── Sensor Data Card
│   │       │   ├── Signal Strength (with progress bar)
│   │       │   ├── Battery Level (with progress bar)
│   │       │   └── Temperature
│   │       └── Location Data Card
│   │           ├── Latitude
│   │           └── Longitude
│   └── Status Bar
│       └── Last Update Time
│
└── DeviceListActivity (Secondary Screen)
    ├── Title
    ├── Scan Button
    └── Device List
        ├── Device Item 1
        ├── Device Item 2
        └── Device Item N
```

## Data Flow Diagram

```
┌─────────────┐
│   Arduino   │
│  + LoRa     │ ──(LoRa Signal)──> Base Station
│  + Sensors  │
│  + Gyro     │
└──────┬──────┘
       │
       │ (Bluetooth Serial)
       ▼
┌──────────────────────┐
│  Bluetooth Module    │
│  (HC-05/HM-10)       │
└──────┬───────────────┘
       │
       │ (Wireless)
       ▼
┌──────────────────────┐
│   Android Phone      │
│  ┌────────────────┐  │
│  │ Bluetooth Mgr  │  │
│  │ ┌────────────┐ │  │
│  │ │ Read Data  │ │  │
│  │ │ Parse JSON │ │  │
│  │ │ Update UI  │ │  │
│  │ └────────────┘ │  │
│  └────────────────┘  │
│                      │
│  ┌────────────────┐  │
│  │   MainActivity │  │  ─→ Display on Screen
│  │   (UI Thread)  │  │
│  └────────────────┘  │
└──────────────────────┘
```

## Status Indicators

### Connection Status

```
Connected:
  Text: "Connected"
  Color: Green (#4CAF50)
  Location: Top of screen

Disconnected:
  Text: "Disconnected"
  Color: Orange (#FF9800)
  Location: Top of screen
```

### Flip Status

```
Normal:
  Text: "NORMAL"
  Color: Green (#4CAF50)
  Alert: Hidden

Flipped:
  Text: "FLIPPED!"
  Color: Red (#F44336)
  Alert: Full-screen red box with warning icon
```

### Progress Indicators

```
Signal Strength:
  ┌───────────────────────────────┐
  │ Signal Strength:      75%     │
  │ ████████░░░░░░░░░░░░ 75%     │ ← Visual bar
  └───────────────────────────────┘

Battery Level:
  ┌───────────────────────────────┐
  │ Battery Level:        85%     │
  │ ████████░░░░░░░░░░░░ 85%     │ ← Visual bar
  └───────────────────────────────┘
```

## Text Styles

### Headers
- Font Size: 24sp
- Weight: Bold
- Color: Dark Blue (#0D47A1)

### Card Titles
- Font Size: 18sp
- Weight: Bold
- Color: Dark Blue (#0D47A1)

### Labels
- Font Size: 14sp
- Weight: Normal
- Color: Gray (#9E9E9E)

### Values
- Font Size: 14sp
- Weight: Bold
- Color: Black/Primary

### Small Text
- Font Size: 12sp
- Weight: Normal
- Color: Gray (#9E9E9E)

## Button Styles

### Primary Button (Connection)
```
Size: Full Width, 48dp Height
Background: Primary Blue (#2196F3)
Text: "Connect Device" or "Disconnect"
Text Color: White
Corner Radius: 4dp
```

### Secondary Button (Scan)
```
Size: Full Width, 48dp Height
Background: Primary Blue (#2196F3)
Text: "Scan for Devices"
Text Color: White
Corner Radius: 4dp
```

## Card Layout

```
╔═══════════════════════════════════════╗
║ [Card Title]                    24sp  ║  ← Header (Bold, blue)
║ ─────────────────────────────────────  ║
║ Label 1:  Value 1          14sp label ║
║ Label 2:  Value 2          14sp label ║
║ ████████░░░░░░░░░░░░░░░░░  Progress  ║
║ Label 3:  Value 3          14sp label ║
╚═══════════════════════════════════════╝
```

## Responsive Design

### Portrait Mode (Default)
- Full width cards
- Vertical layout
- Scrollable content area
- Buttons at bottom

### Landscape Mode
- Cards optimized for width
- Two-column layout (if space allows)
- Horizontal scrolling disabled
- Responsive text sizing

## Animations

### Smooth Transitions
- Button press: 200ms ripple effect
- Status change: Instant text update
- Alert appearance: Fade in 300ms
- Connection: Smooth color transition

## Accessibility Features

- High contrast colors
- Readable font sizes
- Clear labels for all data
- Status changes clearly visible
- Descriptive text for all actions

## Error States

```
┌─────────────────────────────────────┐
│  [Connection Lost]                  │  ← Toast notification (2-3 seconds)
└─────────────────────────────────────┘

Status text: "Disconnected" (Orange)
Button: Changes to "Connect Device"
Cards: Data frozen at last values
```

## Layout Margins and Padding

| Element | Margin | Padding |
|---------|--------|---------|
| Screen | 16dp | - |
| Cards | 12dp between | 16dp inside |
| Header | 0dp | 16dp |
| Buttons | 0 vertical, 16dp bottom | - |
| Status bar | 0dp | 12dp |

## Typography Scale

```
Display:   24sp (App title)
Heading:   18sp (Section titles)
Subhead:   16sp (Navigation)
Body:      14sp (Content)
Caption:   12sp (Secondary info)
```

## Best Practices Implemented

✅ Material Design 3 compliance
✅ Minimum touch target 48x48dp
✅ High contrast text/background
✅ Readable at arm's length
✅ Color not sole differentiator
✅ Clear visual hierarchy
✅ Consistent spacing
✅ Smooth animations
✅ Error state handling
✅ Responsive layout
