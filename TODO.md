1. Pause the countdown when a swap starts. Penalize and resume if the swap is invalid.
2. Button to start the game.
3. End the game when all players lose all their lives (or once a winner is determined?) 
4. Fix Bolt model.
5. Implement addText, changeText, addCountdown, and changeCountdown in the javascript side.
6. Meshes, not just spheres
    * Heart (Red)
    * Crescent Moon (Purple)
    * Snowflake (White)
    * Rupee (Green)
    * Raindrop (Blue)
7. Fill in the far left and right edges of an iPhone in landscape.
8. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
9. Having both a position setter and a move method appear to be coexisting... poorly. Either fix it or undo it.
   * Theory - two different animations are trying to run on a single object. If the wrong animation "wins" it ends up in the wrong spot.
   * Oddly, making addModel be async reduces the frequency of the issue in Edge? I guess I'll just leave it that way for now, even though there's no other reason to have it be async...
10. Include a hash for meshes. Cache meshes in local storage on the javascript side.
11. Use a proper logger, not println.
12. Unit tests.
13. Bug where startTurn() gets called twice back-to-back after a match?