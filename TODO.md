1. Start a Countdown when the game starts. Pause when a swap starts. Penalize and resume if the swap is invalid. Adjust when the player changes.
2. When the countdown ends, decrement lives for both the Player and the Room (the game continues when the Room Lives hit zero, but the Room Score is final at that point...)
3. Button to start the game.
4. End the game when all players lose all their lives (or once a winner is determined?) 
5. Fix Bolt model.
6. Implement addText, changeText, addCountdown, and changeCountdown in the javascript side.
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

---

Countdowns should exist/be exposed as their own class in the engine... not just as a UI element.