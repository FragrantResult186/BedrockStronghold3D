﻿# BedrockStronghold3D
This project utilizes the Minecraft Bedrock stronghold [layout simulation](https://github.com/mjtb49/BedrockStrongholdSim) to allow users to view the structure in 3D.

### Bedrock Stronghold Generation Points and Layout
First, Bedrock strongholds have two types of position generation. One is determined by the angle and whether the chunk can generate a village, causing the stronghold to generate under a village (in every world, three strongholds will always generate beneath villages). Additionally, since the layout remains the same as long as the seed and chunk coordinates are identical, it is mostly unaffected by version differences.

Besides 3D visualization, this project also includes a search function that displays the coordinates of the three village strongholds for a given seed.

### References
In addition to the Bedrock stronghold layout simulation code, I had to write code to determine the positions of the three village strongholds. For this, I referenced [the following C++ code](https://github.com/bedrock-dev/MCBEStructureFinder/blob/1.18%2B/structure.cpp#L257C1-L258C1) and ported it to Java.

Additionally, since village locations require biome generation from version 1.18+, I also used [biome-sampler](https://github.com/jellejurre/biome-sampler).

### Purpose
I don't know what this is useful for yet. Originally, I was using a brute-force simulation code for Bedrock strongholds to search for the longest continuous staircase or the most densely packed three strongholds in a seed. I made it 3D just because I had free time.

Also, I'm a beginner, so this code might be terrible in some places, and since I'm Japanese, there might be mistakes in my translations. Please understand!

<img src=https://github.com/FragrantResult186/BedrockStronghold3D/blob/main/image.png width="500"/>

### Credit  
[BedrockStrongholdSim](https://github.com/mjtb49/BedrockStrongholdSim)

[MCBEStructureFinder](https://github.com/bedrock-dev/MCBEStructureFinder/tree/1.18+)

[biome-sampler](https://github.com/jellejurre/biome-sampler)
