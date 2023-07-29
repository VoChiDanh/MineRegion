## Information
This plugin will help you mine block in custom region and execute command when block broken by player (Use to give item or something else, for default file, i use mmoitems command to give item)

[Want to example see video ? Click this line](https://github.com/VoChiDanh/MineRegion/assets/86956269/6c163298-f60e-4035-b5e7-891a75da007c)

## Depend
- WorldGuard
- PlaceholderAPI

## Command
/mr reload - Reload file <br>
/mr bypass - Bypass mine region and you can break/build block normal


## Config Explain
```
block:
COBBLESTONE: #Block type
max_number: 70 #Max random number can regenerator,
regen: #MATERIAL;CHANCE - max_number will regenerator random number, which chance is the nearest with number, that block will be regen on the next time
- COAL_ORE;5 #If max number regenerator is 10, next regen block is COAL_ORE
- STONE;30 #If max number regenerator is 20, next regen block is STONE
- COBBLESTONE;60 #If max number regenerator is 65, next regen block is COBBLESTONE
time_regen: 20 #Time to regen from bedrock to new block
replace: BEDROCK #Block will be replaced when COBBLESTONE broken by player. After 20s, bedrock will be replaced by new block
amount: 1 #Amount of item, example 1, 3, 5-10, 10-15,..
command: #You can use placeholder in command, #amount# is total of setting amount above + fortune level (random from "amount" to ("amount" + fortune level + 2))
- "mi give MATERIAL COBBLESTONE %player_name% #amount# 0 100 0 silent"
```


## Note
If you have any question/issue/suggest for new features, you should join https://discord.gg/r5ejaPSjku
<br>
Thanks ~~ !