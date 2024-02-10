1. Increment both the score of the Player and the Room when matches are made.
2. Change turns when a match is made.
3. When the countdown ends, decrement lives for both the Player and the Room (the game continues when the Room Lives hit zero, but the Room Score is final at that point...)
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

---

I need to fully think out what the flow between dropTiles, detectAndRemoveMatches, doAnyMovesRemain, and newTurn is...

dropTiles -> detectAndRemoveMatches
  yes -> ^dropTiles
  no -> doAnyMovesRemain
    yes -> newTurn
    no -> clear -> ^^dropTiles

As far as how scoring works, check what was done in GameNight...

Countdowns should exist/be exposed as their own class in the engine... not just as a UI element.