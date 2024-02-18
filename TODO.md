1. Show players and scores.
2. Countdown, Model, and Ui should all have a common interface (or a common superclass?) that has a visibleTo(player): Boolean method...
   And anytime an instance is initialized or a new player joins, it should be run and emit accordingly.
3. Button to start the game.
4. Pause the countdown when a swap starts. Penalize and resume if the swap is invalid.
5. End the game when all players lose all their lives (or once a winner is determined?)
6. Fix Bolt model.
7. Meshes, not just spheres
    * Heart (Red)
    * Crescent Moon (Purple)
    * Snowflake (White)
    * Rupee (Green)
    * Raindrop (Blue)
8. Fill in the far left and right edges of an iPhone in landscape.
9. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
10. Having both a position setter and a move method appear to be coexisting... poorly. Either fix it or undo it.
    * Theory - two different animations are trying to run on a single object. If the wrong animation "wins" it ends up in the wrong spot.
    * Oddly, making addModel be async reduces the frequency of the issue in Edge? I guess I'll just leave it that way for now, even though there's no other reason to have it be async...
11. Include a hash for meshes. Cache meshes in local storage on the javascript side.
12. Use a proper logger, not println.
13. Unit tests.
14. Bug where startTurn() gets called twice back-to-back after a match?