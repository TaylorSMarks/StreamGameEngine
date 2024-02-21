1. End the game when all players lose all their lives (or once a winner is determined?)
2. Button to start the game.
3. Pause the countdown when a swap starts. Penalize and resume if the swap is invalid.
4. Ability to identify yourself by a name. (BABYLON.GUI.InputText)
5. Make it clear whose turn it is, and whether it's your turn or not.
6. Fix Bolt model.
7. Meshes, not just spheres
    * Heart (Red)
    * Crescent Moon (Purple)
    * Snowflake (White)
    * Rupee (Green)
    * Raindrop (Blue)
8. Fill in the far left and right edges of an iPhone in landscape.
9. Also fix the damn issue where it keeps crashing based on the SSE Event streams being resized or whatever.
10. Include a hash for meshes. Cache meshes in local storage on the javascript side.
11. Use a proper logger, not println.
12. Unit tests.
13. Bug where startTurn() gets called twice back-to-back after a match?
14. Improve the PlayerInfoCard positioning (make it left aligned instead of centered?) and add color to it.
    Evenly spread them out based on player count. Rearrange them as more players join.