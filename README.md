# mcSchem-for-redstone

A tool that generates Redstone component towers to encode numbers directly into Minecraft `.schem` files.

---

## 🛠️ Features

* **Multiple Encoding Methods (Binary):**
  * Barrels (container inventory signal)
  * Repeaters (present or not)
  * Levers (on/off states)
* **Custom Base Encoding:** Supports bases **2 through 16** (available with barrels only).
* **Batch Towers:** Encode multiple numbers at once. Towers are automatically separated by 1 block.
* **Flexible Input:** Encode individual numbers manually or generate values using mathematical functions.
* **Fixed-Point Precision:** Operates on fixed-point numbers. Define your custom format or specify a maximum value, and the program will automatically calculate the optimal fixed-point format.
* **Compatibility:** Designed for **Minecraft 1.21**. Schematics may not work as intended on older versions.
* **WorldEdit Required:** Requires WorldEdit to load, place, and rotate the generated schematic files.

---

## 📥 Download & Installation

1. Download or clone the `main` directory.
2. Run `launcher.bat`.
   * *Note: If Java 21 is not installed on your system, the script will automatically download and set it up for you.*

---

## 🚀 How to Use

1. Launch the application via `launcher.bat`.
2. Follow the interactive CLI prompts in the terminal window.
3. Once completed, your `.schem` file will be generated in the output directory.
4. Move or copy the generated `.schem` file to your Minecraft server/client folder:
   .minecraft/config/worldedit/schematics/
5. In Minecraft, load and place your schematic using WorldEdit commands:
```text
//schem load <FILE_NAME>
//paste
```
## 🧮 Math & Function Engine

When choosing to encode via functions instead of raw numbers, you can use built-in operators and mathematical functions.

### Available Operators
`+`, `-`, `*`, `/`, `^` *(exponentiation)*

### Variables & Constants
* `x` or `-x` *(evaluates over a range)*
* `pi`
* `e`

### Priority of Operations
* **No operator precedence:** Operators are evaluated strictly from left to right.
* Use **parentheses** to define execution order.
* **Example:** Do not write `3 + 2 * x`. Write `3 + (2 * x)` instead.

### Built-in Functions

| Category | Functions |
| :--- | :--- |
| **Trigonometric** | `sin`, `cos`, `tan`, `atan` |
| **Hyperbolic** | `sinh`, `cosh`, `tanh`, `atanh` |
| **Exponential & Log** | `exp`, `sqrt`, `log`, `ln` |

---

### Function Syntax

* **`FUNCTION_NAME`**  
  Computes the given function using the variable `x` *(e.g., `sin` evaluates `sin(x)`)*.

* **`FUNCTION_NAME(VALUE)`**  
  Computes the function for a specific `VALUE`. `VALUE` can be a raw number, an expression, or another nested function *(e.g., `sin(cos(x))` or `sqrt(16)`)*.

> ⚠️ **Current Limitation (v1.0):**  
>Always leave a space between arguments and operators. Numbers are parsed as signed doubles. 
