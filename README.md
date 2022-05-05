# Redstone Tools

This is a small mod that adds some convenient tools for redstone builders.
It is available for Minecraft 1.17.1 Fabric.

## Commands :
#### Obtaining blocks
* `/wool [color]`
  * Gives you a wool of the specified color.
  * For example, `/wool red` gives you a red wool.
  * `color` accepts abbreviations, `/wool w` gives you a white wool.
  * All abbreviations can be found in `WoolCommand.java`

It also supports other colored blocks.
* `/terra` for **terracotta**
* `/glass` for **stained glass**
* `/glaterra` for glazed **terracotta**
* `/conbl` for **concrete block**
* `/conpow` for **concrete powder**

#### Obtaining barrels
* `/bar [int]`
  * Gives you a barrel of specified signal strength.
  * For example, `/bar 8` gives you a barrel which can produce the signal strength of 8.
  * `int` varies from 0 to 15.

It also supports the **shulker box**.
* `/shulker [int]`

#### Redstone
* `/binCount [bits] [signed] [distance] [direction]`
  * Calculate a binary number represented by redstone lamps.
  * `bits` [int] : The bits of the binary number. It can be at most 63.
  * `signed` [boolean] : The number is signed or unsigned. Using 2's complement if it is signed.
  * `distance` [int] : The distance between two redstone lamps.
  * `direction` : The direction of redstone lamps. It can a name, such as down, north, east, d, n, e. It can also be an axis, such as -y, -z, +x.
* `/hexCount [size] [signed] [distance] [direction]`
  * It is similiar to `binCount`, but it calculates a hexxadecimal number.
  * `size` is the digit of the number.

**You have to execute these 2 commands above the MSB block, so they can work properly.**

#### Other commands
* `/killitem`

Removee all item and xp orb entities.

* `/slab`

Gives you a slab.

## Tools
* Lazy key

With this key, you can get frequently-used redstone components easily.

Press **Z** and you will get a **redstone dust** and a **repeater**.

While holding a repeater, press **Z** again and you will get a **comparator**.

While holding a comparator, press **Z** again and you will get a **redstone torch**.

* Config key

Press **V**, a menu will show up.

You can set some useful abilities for the creative mode.

Such as **noclip**, **high speed**, **night vision**, **instant kill** and **auto redstone placing**.

You can also set the **keybindings** so that you can access these abilities without opening the menu everytime.


## Versions
Minecraft: 1.17.1

Fabric API: 0.46.1+1.17

Fabric loader: 0.14.3

## Download

Go to the Curseforge page of this mod :

https://www.curseforge.com/minecraft/mc-mods/bens-redstone-tools
