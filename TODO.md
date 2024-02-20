1. Show players and scores. Make it clear who you are, who's turn it is, and whether it's your turn or not.
2. Button to start the game.
3. End the game when all players lose all their lives (or once a winner is determined?)
4. Pause the countdown when a swap starts. Penalize and resume if the swap is invalid.
5. Fix Bolt model.
6. Meshes, not just spheres
    * Heart (Red)
    * Crescent Moon (Purple)
    * Snowflake (White)
    * Rupee (Green)
    * Raindrop (Blue)
7. Fill in the far left and right edges of an iPhone in landscape.
8. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
9. Include a hash for meshes. Cache meshes in local storage on the javascript side.
10. Use a proper logger, not println.
11. Unit tests.
12. Bug where startTurn() gets called twice back-to-back after a match?