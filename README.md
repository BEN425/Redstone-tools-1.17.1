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
* `glaterra` for glazed **terracotta**
* `/conbl` for **concrete block**
* `/conpow` for **concrete powder**

#### Obtaining barrels
* `bar [int]`
  * Gives you a barrel of specified signal strength.
  * For example, `/bar 8` gives you a barrel which can produce the signal strength of 8.
  * `int` varies from 0 to 15.

It also supports the **shulker box**.
* `/shulker [int]`

## Tools
* Lazy key

With this key, you can get frequently-used redstone components easily.

Press **Z** and you will get a **redstone dust** and a **repeater**.

While holding a repeater, press **Z** again and you will get a **comparator**.

While holding a comparator, press **Z** again and you will get a **redstone torch**.

* Config key

Press **V**, a menu will show up.

You can set some useful abilities for the creative mode.

Such as **noclip**, **high speed**, **night vision** and **auto redstone placing**.

You can also set the **keybindings** so that you can access these abilities without opening the menu everytime.

## TODO
* Add some screenshots
* Add download links
* Add more tools

## Versions :
Minecraft: 1.17.1

Fabric API: 0.46.1+1.17

Fabric loader: 0.14.3

Yarn mappings: 1.17.1+build0.65
