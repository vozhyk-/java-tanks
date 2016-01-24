#java-tanks

Java implementation of [ncursed-tanks](https://github.com/AwesomePatrol/ncursed-tanks).

##Build

##User Guide

###run server

###run client

####Main menu

In main menu player should connect to game server. By default IP is set up to `127.0.0.1`. Hostname
may be used instead of IP address. To connect, one should press return. If connection is successful
`New game` and `Random game` buttons will be enabled. `Random game` finds random game available on
the server and joins it, otherwise returns an error. `New game` creates new game on the server and
joins to it.

####Lobby

In lobby player may set number of bots that will be added to game, map width and map type. Pressing
`ready` button sets `Ready` state. When all players in the lobby are ready the game starts.

####Game

When it's player's turn to shoot the buttons are enabled. To choose angle one can use `up` and
`down` arrow keys, buttons or simply by clicking on the map. Power of the shot is set by `left`
and `right` arrow keys or buttons. `shoot` button or `enter` key makes tank shoot. When there is
only one tank left the game ends and [scoreboard](#scoreboard) is displayed.

####Scoreboard

List of all players is displayed with information who won and lost. Here the user can return to
[main menu](#main-menu) using `return` button.

##Protocol

###Commands

Command does not take arguments or return anything unless specified otherwise.

* `'N' NewGame`
    Creates new game on the server.
* `'J' Join`
    Joins previously created game or randomly choosen by server. Returns `joinreply`.
* `'R'  Ready`
    Sets player state to ready.
* `'c'  SetConfig`
    Sets config to given value. Takes `config_item` as argument.
* `'a'  SetAbility`
    Sets chosen ability.
* `'M'  GetMap`
    Asks server to send a map. Returns `struct map_info`.
* `'C'  GetChanges`
    Asks server to send updates. Returns `struct update`
* `'F'  Shoot`
    Sends shot to be processed by server. Takes `struct shot`.
* `'A'  UseAbility`
    Sends ability event to be processed by server. Takes `struct shot`.
* `'m' SendChatMsg`
    Sends chat message to server. Takes string as argument.
* `'i'  GetImpact`
    Asks server to return impact for given values. Used by C bot only. Takes `struct shot`. Returns
    time of imapct `impact_t`.

###Communication

`->` means sending, `<-` denotes receiving


`New game` button:

    -> 'N'
    -> 'J'
    <- `joinreply`

`Random Game` button:

    -> 'J'
    <- `joinreply`

`Ready` button:

    -> 'R'

`Set` button or ComboBox event:

    -> 'c'
    -> `config_item`

`send` button:

    -> 'm'
    -> string

`shoot` button:

    -> 'F'
    -> `struct shot`


####Fetch changes

In java version separate thread is receiving and processing updates every second.

    -> 'C'
    <- `struct update`
    <- `struct update`
    …
    <- `struct update` = `Empty`

where `struct update` is one of the types:
* `ConfigUpdate`
* `EmptyUpdate`
* `LogUpdate`
* `MapUpdate`
* `PlayerUpdate`
* `ShotImpactUpdate`
* `ShotUpdate`
* `TypedUpdateWithArgument`
* `Update`
* `UpdateWithArgument`

