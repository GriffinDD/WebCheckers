<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/help.css">
</head>

<body>

<h1>
Help Menu
</h1>
<h2> Welcome! </h2>
<p>
Welcome to checkers! The classic checkers game has been around for thousands of years. This game follows the standard american rules for checkers as follows below.
</p>

<img src="../img/genericBoard.png" class="center"/>

<h2> Checkers Rules </h2>
<p>Below gives the basic setup rules for checkers </p>

<h3> Initial Setup </h3>
<ul style="list-style-type:none;">
  <li> The checkerboard is an 8x8 grid of light and dark squares in an alternating pattern. Each player has a "dark" square on the far left and a light square on his far right.</li>
  <li> Pieces will remain on "dark" spaces throughout the game. </li>
  <li> Each player starts with 12 pieces, each initially placed on the nearest 12 dark spaces. </li>
 </ul>
 <h3> Gameplay </h3>


<ul style="list-style-type:none;">
  <li>The red player moves first.</li>
  <li> A move consists of the following:
    <ul style="list-style-type:none;">
      <li><em>Simple move</em>: Single  pieces can move one adjacent square diagonally forward away from the player. A piece can only move to a vacant dark square.</li>
      <li><em>Single jump move</em>: A player captures an opponent's piece by  jumping over it, diagonally, to an adjacent vacant dark square. The opponent's captured piece is removed from the board.  The player can never jump over, even without capturing, one of the player's own pieces. A player cannot  jump the same piece twice.</li>
      <li><em>Multiple jump move</em>: Within one turn, a player can make a multiple jump move  with the same piece  by jumping from vacant dark square to vacant dark square. The player must capture one  of the opponent's pieces with each jump. The player can capture several pieces with a move of several jumps. </li>
    </ul>
  </li>

  <figure>
    <img src="../img/doubleJump.gif" class="centersmall"/>
    <figcaption> Here a double jump is executed. The Red piece would become a king due to reaching the farthest row from the player.</figcaption>
   </figure>

  <li>If a player can make a jump, they must do so. </li>
  <li>If the player can make any jump move (single or multiple), they must choose among those options. In other words, jumps have priority over single moves, yet all jumps are considered equal.</li>
  <li>A player must move, and if there are no valid moves they lose the game.</li>
  <li>When a single piece reaches the row furthest from the player, it becomes a king. A king is represented as a double stack of game pieces or a small crown if digital. </li>
  <li>A king plays just as a normal piece, however it can move towards the player as well. Kings can jump backwards and forwards, and "multiple jump moves" can be a combination of these.</li>
  <li>When all of a player's pieces are gone, they lose the game.</li>

  <figure>
    <img src="../img/multijumpexampleKing.gif" class="centersmall"/>
    <figcaption> Here, a piece is being used to jump multiple pieces. When it hits the last row it becomes a king and then can jump in multiple directions.
     While in the american rules a players turn must end when their piece is kinged upon reaching the final row, this example demonstrates
     the mobility and multiple jump potential of a king.
    </figcaption>
   </figure>

</ul>


<div class="exit">
<form action="" method="GET">
    <button type="submit">Exit</button>
</form>
</div>
</body>

</html>
