1. Make the countdown actually show the time left.
2. Broadcast the current grid whenever a new player joins... maybe even give a new joiner lives?
3. Pause the countdown when a swap starts. Penalize and resume if the swap is invalid.
4. Button to start the game.
5. End the game when all players lose all their lives (or once a winner is determined?)
6. Fix Bolt model.
7. Implement addText, changeText, addCountdown, and changeCountdown in the javascript side.
8. Meshes, not just spheres
    * Heart (Red)
    * Crescent Moon (Purple)
    * Snowflake (White)
    * Rupee (Green)
    * Raindrop (Blue)
9. Fill in the far left and right edges of an iPhone in landscape.
10. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
11. Having both a position setter and a move method appear to be coexisting... poorly. Either fix it or undo it.
    * Theory - two different animations are trying to run on a single object. If the wrong animation "wins" it ends up in the wrong spot.
    * Oddly, making addModel be async reduces the frequency of the issue in Edge? I guess I'll just leave it that way for now, even though there's no other reason to have it be async...
12. Include a hash for meshes. Cache meshes in local storage on the javascript side.
13. Use a proper logger, not println.
14. Unit tests.
15. Bug where startTurn() gets called twice back-to-back after a match?