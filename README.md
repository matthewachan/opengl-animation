# COMS W4160 Programming Assignment 1
> OpenGL Introduction

# Overview
For Part 1, I implemented (1) the OBJLoader class, (2) `translate`, `scale`, `rotate` and `reflect` Mesh functions and (3) the model, view and projection transformations.

To run Part 1, use `ant` (since it is the default build target) or `ant run` (if you would like to specify the target).

For the [Creative Scene](#creative-scene), I chose to implement a procedurally generated animation of a tree growing.

To run the creative scene, use `ant anim` as I have created a new ant build target for this program.

## Creative Scene
This was achieved using 3 OBJ files: (1) a plane, (2) a cone and (3) a cloud. The scene starts out with 2 planes, one for the ground and one for the background sky, and 1 cone representing the trunk of the tree.

Over time, new, smaller cones appear on the trunk and slowly elongate over time--giving the appearance of new branches sprouting from the tree.

After all branches have been fully grown, tree leaves appear and the tree is fully grown.

![](etc/anim.gif)

### Details
The creative scene uses 3 new java classes: (1) `engine/Branch`, (2) `game/HelloAnim` and (3) `game/TreeAnim`.

The Branch class extends GameItem to add additional fields such as `center`, `length`, and `maxLength`. These fields track the length and center of the branches in order to more easily perform Mesh transformations to animate the growth of the branches.

HelloAnim is a modified version of HelloGame for rendering the animation.

TreeAnim is a modified version of Main for running the animation.

**Note**: I have also added fields and functions to the Mesh class to modify the position coordinates of the vertex representing the tip of the cone. This allows me to easily "grow" the branch by translating the tip of the Mesh.
