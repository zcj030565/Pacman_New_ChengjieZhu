I Implemented DFS,BFS,Greedy Best First and MiniMax for Pacman.

Pacman_New_ChengjieZhu/src/main/java/entrants/pacman/chengjiezhu/

DFS: Expand deepest unexpanded node Implementation: For DFS, fringe is a first-in-first-out (FIFO) queue.
New successors go at beginning of the queue.
Average Time complexity: DFS loop through all of a vertices adjacency lists, calling DFS(v) if it's not been visited, meaning that we incur |V| time steps, plus the time incurred to visit adjacent nodes (essentially, these form an edge, and we have a total of |E| edges, hence, O(V+E) time.
Average Space complexity: O (V+E)
Worst case Time complexity: O(bm) (Maximum tree depth = m) Worst case Space complexity: O(bm)


Greedy Best First:Time complexityO(bm),Space complexityO(bm)


MinMax:Time complexity: O (V+E) Space complexity: O (V+E)
