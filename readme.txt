I Implemented DFS,BFS,Greedy Best First and MiniMax for Pacman.

Pacman_New_ChengjieZhu/src/main/java/entrants/pacman/chengjiezhu/

DFS: Expand deepest unexpanded node Implementation.
Enqueue nodes on nodes in LIFO (last-in, first-out) order.Nodes used a stack data structure to order nodes.
Average Time complexity: DFS loop through all of a vertices adjacency lists, calling DFS(v) if it's not been visited, meaning that we incur |V| time steps, plus the time incurred to visit adjacent nodes (essentially, these form an edge, and we have a total of |E| edges, hence, O(V+E) time.
Average Space complexity: O (V+E)
Worst case Time complexity: O(bm) (Maximum tree depth = m) Worst case Space complexity: O(bm)

BFS: Expand shallowest unexpanded node. Nodes wait in a queue to be explored. For BFS, fringe is a first-in-first-out (FIFO) queue. New successors go at end of the queue. Do not add parent of a node as a leaf

Average Time complexity: BFS iterate over the |V| nodes, for at most |V| times. Since we have an upper bound of |E| edges in total in the graph, we will check at most |E| edges. At the end, we have O (|V|+|E|). Average Space complexity: O (V+E)

Worst case Time complexity: O (bd+1) (b:Numbers, d:Depth) Worst case Space complexity: O (bd+1)
Greedy Best First:Time complexityO(bm),Space complexityO(bm)

MinMax:Time complexity: O (V+E) Space complexity: O (V+E)
