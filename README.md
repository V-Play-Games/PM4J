# PM4J (Pokémon Masters For Java)
A simple wrapper API written in Java for the RESTful API "PokeMasDB".

## Summary
1. [Introduction](#introduction)
2. [Entities](#entities)
3. [Fetching Data](#fetching-data)
4. [Caches](#caches)
5. [Download](#download)
5. [Dependencies](#dependencies)

## Introduction
PM4J (Pokémon Masters For Java) is a simple wrapper API written in Java to communicate with [PokemasDB](https://www.pokemasdb.com/). It has various Objects & Caches to work with.

## Entities
Each type of data is categorized in terms of Objects or "**Entities**". All the entities have the ability to parse raw JSON data to the type of entity desired & can be stored in a [JSONArray](#json-array).
1. [Move](#move)
2. [Sync Move](#sync-move)
3. [Passives](#passives)
4. [Sync Grid Node](#sync-grid-node)
5. [Stats](#stats)
6. [Stat Range](#stat-range)
7. [Pokemon](#pokemon)
8. [Trainer](#trainer)

### Move
There are two kinds of moves. There are Pokémon Moves which run based on the Move Gauge, using a specific amount of slots from it, and there are Trainer moves which can only be used a set amount of times in a battle. These moves are typically akin to using items and provide a statistical benefit.

**Fields**
Name | Description | Type
---- | ----------- | ----
`name` | The name of this Move. | *String*
`type` | The type of this Move. | *String*
`category` | The category of this Move. | *String*
`minPower` | The minimum power of this Move. | *Integer*
`target` | The target(s) of this Move. | *String*
`accuracy` | The accuracy of this Move. | *String*
`cost` | The amount of gauges this move this moves consumes in order to be used. It is 0 for Quick Moves, Trainer Moves, X Items and some other items (like Move Gauge Boost). | *Integer*
`uses` | The amount of MP (Move Point) this move has. `0` means infinite usage. | *Integer*
`effect` | The additional effect(s) of this move apart from dealing damage. | *String*

### Sync Move
A Sync Move is a powerful move that can be used during battle in Pokémon Masters EX. According to Professor Bellis, sync stones are required for sync pairs to use a sync move, which are known to amplify the power that arises from the bond between Trainer and Pokémon, which she calls "sync strength." As the bond grows, so does the strength of the sync move. Every Trainer in Pasio has a Poryphone with an embedded sync stone to allow all to access sync moves. [Read More.](https://bulbapedia.bulbagarden.net/wiki/Sync_move)

**Fields**
Name | Description | Type
---- | ----------- | ----
`name` | The name of this Sync Move. | *String*
`type` | The type of this Sync Move. | *String*
`category` | The category of this Sync Move. | *String*
`minPower` | The minimum power of this Sync Move. | *Integer*
`target` | The target(s) of this Sync Move. | *String*
`effectTag` | The effect tag of this Sync Move. | *String*
`description` | The additional effect(s) of this Sync Move apart from dealing damage. | *String*


### Passives
A Passive Skill is a game mechanic in Pokémon Masters EX that provides a passive effect in battle, similar to Abilities. Some sync pairs have multiple passive skills, and thus can have up to a maximum of three passive skills. Additional passive skills can be obtained through a sync pair's sync grid or as lucky skills. [Read More.](https://bulbapedia.bulbagarden.net/wiki/Passive_skill)

**Fields**
Name | Description | Type
---- | ----------- | ----
`name` | The name of this Passive Skill. | *String*
`description` | The description of this Passive Skill. | *String*

### Sync Grid Node
A Sync Grid Node is a hexagonal tile on a Sync Pair's Sync Grid. Sync grid is a feature in Pokémon Masters EX that allows the player to strengthen sync pairs in unique ways. The sync grid consists of hexagonal tiles, with each tile unlocking a grid skill. Each grid skill offers different enhancements, such as raising stats, enhancing moves and unlocking passive skills. [Read More.](https://bulbapedia.bulbagarden.net/wiki/Sync_grid)

**Fields**
Name | Description | Type
---- | ----------- | ----
`bonus` | The bonus this Sync Grid Node activates. | *String*
`title` | If the bonus represents a Passive Skill, the title of the Passive Skill this Sync Grid Node activates, else the bonus. | *String*
`description` | If the bonus represents a Passive Skill, the description of the Passive Skill this Sync Grid Node activates, else the bonus. | *String*
`syncOrbCost` | The amount of Sync Pair Specific Sync Orbs this Sync Grid Node requires to be activated. | *Integer*
`energyCost` | The amount of energy this Sync Grid Node requires to be activated. | *Integer*
`reqSyncLevel` | The Sync Move Level this Sync Grid Node requires for the Sync Pair to be at to be activated. | *Integer*
`gridPos` | The position in the Sync Grid of this Sync Grid Node. | *String*
`gridPosX` | The X position in the Sync Grid of this Sync Grid Node. | *Integer*
`gridPosY` | The Y position in the Sync Grid of this Sync Grid Node. | *Integer*

### Stats
Each Sync Pair has a level and six stats: HP, Attack, Defense, Sp. Atk, Sp. Def, and Speed. The stats function similarly to how they do in other Pokémon games. HP represents how much damage a sync pair can take; Attack determines the power of physical moves, which can be guarded against with Defense; Sp. Atk and Sp. Def are similar to Attack and Defense but for special moves; and Speed affects the rate the move gauge fills. [Read More.](https://www.Pokémon.com/us/strategy/Pokémon-masters-ex-level-caps-sync-grids-move-level-evolution-and-more/)

**Fields**
Name | Description | Type
---- | ----------- | ----
`hp` | The HP Stat. | *Integer*
`atk` | The Physical Attack Stat. | *Integer*
`def` | The Physical Defense Stat. | *Integer*
`spAtk` | The Special Attack Stat. | *Integer*
`spDef` | The Special Defense Stat. | *Integer*
`speed` | The Speed Stat. | *Integer*
`bulk` | The Bulk Stat. It is a hidden stat which was used in past versions of Pokémon Masters to determine which ally will be targeted by the opponents. It is usually 0 in Base Stats of the [Stat Range](#stat-range) as its usually not provided | *Integer*

### Stat Range
The Stats of a Sync Pair are different at different levels. Each time a sync pair levels up, their stats increase. The Stat Range represents the range of stats from their Base Stats (at level 1) to their Max Stats (at level 125).

**Fields**
Name | Description | Type
---- | ----------- | ----
`base` | The Base Stats. | *[Stats](#stats)*
`max` | The Max Stats. | *[Stats](#stats)*

### Pokemon
Pokémon are fictional creatures that are central to the Pokémon franchise. Inherent to them are several fantastic powers not demonstrated by most real animals, such as the manipulation of electricity or fire. Pokémon are shown to exist instead of animals in their world, although animals are also seldom seen in older media. Pokémon on Pasio are always tied to a Trainer forming a Sync Pair.

**Fields**
Name | Description | Type
---- | ----------- | ----
`name` | The name of this Pokémon. | *String*
`trainer` | The name of the Trainer of this Pokémon. | *String*
`typing` | The typing of this Pokémon. | *String*
`weakness` | The weakness of this Pokémon. Note that this variable is not available directly but can be obtained from its getter method. | *String Array*
`role` | The role of this Pokémon. | *String*
`rarity` | The rarity of this Pokémon (in no. of Stars). | *Integer*
`gender` | The gender of this Pokémon  | *String*
`otherForms` | The name of other forms of this Pokémon. Note that this variable is not available directly but can be obtained from its getter method. | *String Array*
`moves` | The moves of this Pokémon | *[JSONArray](#json-array)([Move](#move))*
`syncMove` | The Sync Move of this Pokémon | *[Sync Move](#sync-move)*
`passives` | The Passive Skills of this Pokémon | *[JSONArray](#json-array)([Passives](#passives))*
`stats` | The Stats of this Pokémon | *[Stat Range](#stat-range)*
`grid` | The Sync Grid of this Pokémon | *[JSONArray](#json-array)([Sync Grid Node](#sync-grid-node))*

### Trainer
A Trainer is a human being who trains its Pokémon(s) & take part in battles, events etc. Trainers on Pasio are always tied to their Pokémon forming a Sync Pair.

**Fields**
Name | Description | Type
---- | ----------- | ----
`name` | The name of this Trainer. | *String*
`rarity` | The rarity of this Trainer (in no. of Stars). | *Integer*
`img` | The URL which leads to the image of this trainer on PokemasDB.  | *String*
`data` | The URL which leads to the data of this trainer on PokemasDB.  | *String*
`pokemon` | The Array of the names of all the Pokémon this trainer has formed a Sync Pair with. Note that this variable is not available directly but can be obtained from its getter method. | *String Array*
`pokemonData` | The List of Pokémon this Trainer has formed a Sync Pair with. | *[JSONArray](#json-array)([Pokémon](#Pokémon))*

## JSON Array
PM4J provides you with a unique inheritor of an ArrayList. A JSONArray can only contain elements which implement the interface "PasrsableJSONObject", which all the entities implement. It can be set into an "initialized" state by calling its `initialized()` method, after which any of the values in this List cannot be changed but only inquired.

## Fetching Data
To fetch data for a single trainer, follow these steps:-
1. Construct a new [`Connection`](https://github.com/V-Play-Games/PM4J/blob/PM4J-patch/src/main/java/com/vplaygames/PM4J/Connection.java) object.
2. Fetch the data & store it in a String variable.
3. Parse the data.
4. Use the data!
#### Examples
* Fetching Data of a single trainer (including its Pokémon)
```java
try (Connection conn = new Connection()) {
    String data = conn.requestTrainer("Red");
    Trainer t = Trainer.parse(data);
    // rest of the code
} catch (Exception e) {
    e.printStackTrace();
}
```
* Fetching Data of a all trainers (excluding their Pokémon)
```java
try (Connection conn = new Connection()) {
    String data = conn.requestTrainers();
    JSONArray<Trainer> array = JSONArray.parse(data, Constants.EMPTY_TRAINER);
    // rest of the code
} catch (Exception e) {
    e.printStackTrace();
}
```

## Caches
As downloading the data everytime itas needed can be a costly decision. It is always better to cache the data in some form. PM4J provides few classes that will help you to do so.
1. [Trainer Data Cache](#trainer-data-cache)
2. [Pokemon Data Cache](#pokemon-data-cache)
3. [Move Data Cache](#move-data-cache)
4. [Skill Data Cache](#skill-data-cache)
5. [PokemasDB Cache](#pokemasdb-cache)

### Trainer Data Cache
Represents a Cache of all the Data of all the Trainers and their respective Pokémon.

**Example**
```java
TrainerDataCache tdc;
try {
    tdc = TrainerDataCache.getInstance();
} catch (Exception e) {
    e.printStackTrace();
}
if (tdc != null) {
    // use the object
} else {
    System.out.println("Data not initialized yet");
}
```

### Pokemon Data Cache
Represents a Cache of all the Data of all the usable Pokémon in Pokémon Masters who have formed a Sync Pair with a scoutable or usable Trainer.

**Example**
```java
PokemonDataCache pdc;
try {
    pdc = PokemonDataCache.getInstance();
} catch (Exception e) {
    e.printStackTrace();
}
if (pdc != null) {
    // use the object
} else {
    System.out.println("Data not initialized yet");
}
```

### Move Data Cache
Represents a Cache of all the Data of all the usable Moves in Pokémon Masters.

**Example**
```java
MoveDataCache mdc;
try {
    mdc = MoveDataCache.getInstance();
} catch (Exception e) {
    e.printStackTrace();
}
if (mdc != null) {
    // use the object
} else {
    System.out.println("Data not initialized yet");
}
```

### Skill Data Cache
Represents a Cache of all the Data of all the usable Passive Skills in Pokémon Masters, present in either a Sync Pair's default Passives, or present in a Sync Pair's Sync Grid.

**Example**
```java
SkillDataCache sdc;
try {
    sdc = SkillDataCache.getInstance();
} catch (Exception e) {
    e.printStackTrace();
}
if (sdc != null) {
    // use the object
} else {
    System.out.println("Data not initialized yet");
}
```

#### Note
All the Cache classes mentioned above use the [Logger](https://github.com/V-Play-Games/PM4J/blob/PM4J-patch/src/main/java/com/vplaygames/PM4J/Logger.java) to log details of the processes & the caches mentioned so far are Singleton Classes, which means they can be initialized only once. The Singleton Instance is returned by the `getInstance()` and `getInstance(boolean)` methods. The Caches cache the data as a [Cache](https://github.com/V-Play-Games/PM4J/blob/PM4J-patch/src/main/java/com/vplaygames/PM4J/caches/framework/Cache.java) which is an inheritor of HashMap. The classes also provide other details such as downloading and processing time.

### PokemasDB Cache
This cache works a little different from the other caches.
* This class is a Singleton Class but can be forced to re-initialize if the need arises.
* This cache, when initialized, initializes all the other caches because it depends on them for data.
* This cache, if re-initialized, re-initializes all the other caches to refresh data.
Note:- Other caches cannot be re-initialized directly, this is the only way to re-initialize them.
* This cache can run the initialization parallely (in another Thread).
* As there cannot be two types of data with the same name (like a Move having the same name as a Trainer), the Object is directly stored in the Cache without the check for duplicates.
* The cache needs to be initialized before being used, or the [`getInstance()`](https://github.com/V-Play-Games/PM4J/blob/PM4J-patch/src/main/java/com/vplaygames/PM4J/caches/PokemasDBCache.java#L116) method will return null.
* The cache can be initialized by using any of the following methods:-
  1. [`initialize()`](https://github.com/V-Play-Games/PM4J/blob/PM4J-patch/src/main/java/com/vplaygames/PM4J/caches/PokemasDBCache.java#L125)
  2. [`initialize(boolean)`](https://github.com/V-Play-Games/PM4J/blob/PM4J-patch/src/main/java/com/vplaygames/PM4J/caches/PokemasDBCache.java#L134)
  3. [`initialize(boolean, boolean)`](https://github.com/V-Play-Games/PM4J/blob/PM4J-patch/src/main/java/com/vplaygames/PM4J/caches/PokemasDBCache.java#L146)
* The cache can be re-initialized by using the [`forceReinitialized(boolean, boolean)`](https://github.com/V-Play-Games/PM4J/blob/PM4J-patch/src/main/java/com/vplaygames/PM4J/caches/PokemasDBCache.java#L116)

**Example**
```java
try {
  PokemasDBCache.initialize();
} catch (Exception e) {
  e.printStackTrace();
}
PokemasDBCache pmdc = PokemasDBCache.getInstance();
if (pmdc != null) {
  // use the object
} else {
  System.out.println("Data not initialized yet");
}
```

## Download
Latest Stable Version: **1.0.0**<br>
Be sure to replace the VERSION key below with the version shown above!

### Maven
```xml
<dependency>
    <groupId>com.github.v-play-games</groupId>
    <artifactId>PM4J</artifactId>
    <version>VERSION</version>
</dependency>
```

### Gradle
```gradle
dependencies {
    compile 'com.github.v-play-games:PM4J:VERSION'
}
```

## Dependencies:

This project requires **Java 8+**.<br>
All dependencies are managed automatically by Maven.
 * JSON-Simple
   * Version: **1.1.1**
   * [Github](https://github.com/fangyidong/json-simple)
   * [Website](https://code.google.com/archive/p/json-simple/)
 * OkHttp
   * Version: **3.13.0**
   * [Github](https://github.com/square/okhttp)
   * [Website](https://square.github.io/okhttp/)
