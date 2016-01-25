#java-tanks

Java implementation of [ncursed-tanks](https://github.com/AwesomePatrol/ncursed-tanks).


##Build

To build, you have to import the project in Eclipse and clean/rebuild it.

##User Guide

###run server

Start it from the command line with
```
% CLASSPATH=bin java re.neutrino.java_tanks.server.Main
```
or from Eclipse by running `server.Main` (in the latter case, to stop the server you will have to kill it manually if you run anything else in Eclipse afterwards).

###run client

Start it from the command line with
```
CLASSPATH=bin java re.neutrino.java_tanks.client.Main
```
or from Eclipse by running `client.Main`.

####Main menu

In main menu player should connect to game server. By default IP is set up to `127.0.0.1`. A hostname
may be used instead of an IP address. To connect, one should press the return key. If connection is successful
`New game` and `Random game` buttons will be enabled. `Random game` finds a random game available on
the server and joins it, otherwise shows an error message. `New game` creates a new game on the server and
joins it.

####Lobby

In lobby player may set the number of bots that will be added to game, map width and map type. Pressing
the `ready` button sets `Ready` state. When all players in the lobby are ready the game starts.

####Game

When it's the player's turn to shoot the buttons are enabled. To choose angle one can use `up` and
`down` arrow keys, buttons or simply click on the map. Power of the shot is set by `left`
and `right` arrow keys or buttons. `shoot` button or `enter` key makes the tank shoot. When there is
only one tank left the game ends and the [scoreboard](#scoreboard) is displayed.

####Scoreboard

List of all players is displayed with information on who won and lost. Here the user can return to
the [main menu](#main-menu) using the `return` button.


##Protocol

###Commands

Command does not take arguments or return anything unless specified otherwise.

* `'N' NewGame`
    Creates a new game on the server.
* `'J' Join`
    Joins previously created game or randomly choosen by server. Returns `JoinReply`.
* `'R'  Ready`
    Sets player state to ready.
* `'c'  SetConfig`
    Sets a config option to a given value. Takes `ConfigOption` as argument.
* `'a'  SetAbility`
    Selects chosen ability.
* `'M'  GetMap`
    Asks server to send a map. Returns `GameMap`.
* `'C'  GetChanges`
    Asks server to send updates. Returns `list(Update)`
* `'F'  Shoot`
    Sends shot to be processed by server. Takes `Shot`.
* `'A'  UseAbility`
    Sends ability event to be processed by server. Takes `Shot`.
* `'m' SendChatMsg`
    Sends chat message to server. Takes `String` as argument.
* `'i'  GetImpact`
    Asks server to return impact for given values. Used by C bot only. Takes `Shot`. Returns
    time of impact `impactT`.

###Communication

`->` means sending a command, `<-` denotes receiving


`New game` button:

    -> NewGame
    -> Join(nickname)
    <- `JoinReply`

`Random Game` button:

    -> Join(nickname)
    <- `JoinReply`

`Ready` button:

    -> Ready

`Set` button or ComboBox event:

    -> SetConfig(ConfigOption)

`send` button:

    -> SendChatMsg(message)

`shoot` button:

    -> Shoot(Shot)

fetch map:

    -> GetMap
    <- Map


####Fetch changes

In Java version a separate thread is receiving and processing updates every second.

    -> GetChanges
    <- Update
    <- Update
    …
    <- Update (.type == Empty)

where `Update` is one of the types:

* `ConfigUpdate`
    Updates specified item in `Config`. Default values are preset for server and client.
* `EmptyUpdate`
    For sending a list of updates. It is the last update in a queue.
* `LogUpdate`
    Adds line to log/chat.
* `MapUpdate`
    Changes height at a point in previously generated map.
* `PlayerUpdate`
    Adds / removes a player or updates properties of one.
* `ShotImpactUpdate`
    Specifies `impactT` (from which the impact position can be derived) of a shot.
* `ShotUpdate`
    Specifies `Shot`. A shot rendering thread is started.


##Java structure

###Client

Structure of the Java client.

`Main`
* `GUI`
  * `MainMenuPanel`
  * `LobbyPanel`
  * `GamePanel`
  * `EndGamePanel`
  * `changePane(JPanel new_panel)`: Method replacing current JPanel with `new_panel`
* `Net`
  * `Socket`
  * `CommunicationStream`: every send-receive pair is in a block synchronized on it
  * `ChangesThread`: Described in [fetch-changes](#fetch-changes)
    * `fetch_changes()`
  * `joinServer(String nick)`: returns true on success
  * `send_command(Command cmd)`
* `DebugStream`
  * `print(DebugLevel, String title, &optional Object value)`
* `Game`: constructor takes `nickname` to call `joinServer()`; starts `ChangesThread()`; switches to
`LobbyPanel`; enables `Ready` button in it
  * `GameMap` (common to client and server)
    * `Info` (common to client and server)
      * `seed`
      * `length`
      * `height`
      * `Type`
    * `short[]`: array of heights, `y=[x]`
    * `generate()`: fills array with heights generated from function and `Info`
  * `Config`
    * `Item[]`
      * `name`
      * `value`
      * `min`
      * `max`
  * `PlayersList`
    * `delete(PlayerUpdate p)`
    * `update(PlayerUpdate p)`: updates player in `p` if it is already present; adds it to the list otherwise

###Server

Structure of the Java server.

`Main`
* A `ClientThread` for every client
  * Receives commands in a loop
  * `processCommand(cmd)` overloaded methods for command types. Usually call corresponding methods in `Game`
* `GameList`
  * `Game`: contains most of the game logic
    * `clients`
      * `Client`: describes every client; can be a `Bot`
        * `Player` (common to client and server)
          * `State`
            * `Joined`, `Ready`, `Waiting`, ...
	      * `isConnected`
	      * `id`
	      * `nickname`
	      * `hitpoints`
	      * `pos`: a `MapPosition`
	      * `abilityId`
	      * `abilityCooldown`
        * `UpdateQueue` (common to client and server): updates for the client; can be sent and emptied or added to
    * `ServerGameMap`: a `GameMap` that can be changed by impacts
