# Most-Anagrams-Finder
Data Structures Capstone Project

Average execution times for words.txt in seconds:

    BST: 2.342
    RBT: 0.727
    Hash: 0.391

I expect BST to have the fastest performance and Hash to have the slowest
performance.

The results of timing my program's execution does match my expectations. Hash
is the fastest because it looks up key-value pairs in constant time of
theta(1). BST and RBT both have to iterate through the map when looking up
key-value pairs, so their worst-case complexities will always be longer than if
you use Hash. RBT is faster than BST because it is self-balancing. Because RBTs
are always balanced, the worst-case complexity for searching for key-value
pairs is O(lg(n)). However, because BSTs do not self-balance, their worst-case
complexity for searching for key-value pairs is O(n). So, in order from fastest
to slowest in terms of worst-case complexities for searching is Hash, RBT, BST
which matches my timing results.
