1. Particle effect when the meshes are destroyed.
2. Sound effects and music.
3. Display a big banner announcing the winner.
4. Indicate a swap that could have been done after the game ends (glow the ones that should have been swapped.)
5. Start a glow on any tile as its selected, as visual confirmation that the selection registered.
6. Refactor all of the UI code relating to visibility, both on the Kotlin and Javascript side
7. Flash the countdown as it nears the end. Maybe flash the background when a life is lost?
8. Meshes, not just spheres
    * Crescent Moon (Purple)
    * Snowflake (White)
9. Fill in the far left and right edges of an iPhone in landscape.
10. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
11. Include a hash for meshes. Cache meshes in local storage on the javascript side.
12. Use a proper logger, not println.
13. Unit tests.
14. Bug where startTurn() gets called twice back-to-back after a match?
15. Improve the PlayerInfoCard positioning (make it left aligned instead of centered?) and add color to it.
    Evenly spread them out based on player count. Rearrange them as more players join.