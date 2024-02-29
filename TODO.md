1. Display a big banner announcing the winner.
2. Indicate a swap that could have been done after the game ends.
3. Refactor all of the UI code relating to visibility, both on the Kotlin and Javascript side
4. Flash the countdown as it nears the end.
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
13. Improve the PlayerInfoCard positioning (make it left aligned instead of centered?) and add color to it.
    Evenly spread them out based on player count. Rearrange them as more players join.