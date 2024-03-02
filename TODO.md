1. Particle effect when the meshes are destroyed.
2. Sound effects and music.
3. Display a big banner announcing the winner.
4. Indicate a swap that could have been done after the game ends.
5. Refactor all of the UI code relating to visibility, both on the Kotlin and Javascript side
6. Flash the countdown as it nears the end.
7. Meshes, not just spheres
    * Heart (Red)
    * Crescent Moon (Purple)
    * Snowflake (White)
8. Improve meshes:
    * Drop needs to be rounder on the sides - also, it's too big.
    * Rupee... just looks oddly round, no idea why.
8. Fill in the far left and right edges of an iPhone in landscape.
9. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
10. Include a hash for meshes. Cache meshes in local storage on the javascript side.
11. Use a proper logger, not println.
12. Unit tests.
13. Bug where startTurn() gets called twice back-to-back after a match?
14. Improve the PlayerInfoCard positioning (make it left aligned instead of centered?) and add color to it.
    Evenly spread them out based on player count. Rearrange them as more players join.