1. Hide the Join game button + name input text box once you join.
2. Hide the start game button, the join game button, and the name input text box once the game is started.
3. Hide the start game button prior to joining.
4. Show the appropriate UI elements after the game ends.
5. Flash the countdown as it nears the end.
6. Fix the issue of a new countdown being created when a new game starts.
7. Fix Bolt model.
8. Meshes, not just spheres
    * Heart (Red)
    * Crescent Moon (Purple)
    * Snowflake (White)
    * Rupee (Green)
    * Raindrop (Blue)
9. Fill in the far left and right edges of an iPhone in landscape.
10. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
11. Include a hash for meshes. Cache meshes in local storage on the javascript side.
12. Use a proper logger, not println.
13. Unit tests.
14. Bug where startTurn() gets called twice back-to-back after a match?
15. Improve the PlayerInfoCard positioning (make it left aligned instead of centered?) and add color to it.
    Evenly spread them out based on player count. Rearrange them as more players join.