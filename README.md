GridSearchComparison
====================

Compares A* and A*+JPS

## Usage

The program reads in a PNG file. One green pixel marks the start, and one red pixel marks the end. Any other colors are treated as impassable terrain.

A* and Jump Point Search are run 40 times by default on the input map, with the start and end position randomly placed.

The program also outputs algorithm stats in a csv file.

## Algorithm Implementation

Algorithm functions are abstracted with various delegates and generics to allow running searches on arbitrary objects. This also means that they are not optimized for time, but are more for research/playing around with variables.

## Project Scope

This code was originally built for a class project. 

I don't recommend implementing it elsewhere in anything more than a fun little project, since the abstractions ended up a mess after "rapid development" (read multiple additions really fast and messy).

## Recommended Changes

- Add additional args readings
- Add additional Heuristics
- Run different maps
- Add addiitional search algorithms
- Add additional models (Tree, etc.)
